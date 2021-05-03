package com.coder.lee.fund.spider;

import com.coder.lee.fund.entity.THolderNumLatestEntity;
import com.coder.lee.fund.entity.TInstitutionResearchEntity;
import com.coder.lee.fund.service.THolderNumLatestService;
import com.jayway.jsonpath.JsonPath;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * Description: Function Description
 * Copyright: Copyright (c)
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/5/3 0:30
 *
 * @author coderLee23
 */
@Order(1002)
@Component
public class THolderNumLatestSpider implements CommandLineRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(THolderNumLatestSpider.class);

    private static final String HOLDER_NUM_LATEST_URL = "http://dcfm.eastmoney.com/em_mutisvcexpandinterface/api/js/get?st=NoticeDate&sr=-1&ps=50&p=%s&type=HOLDERNUMLATEST&sty=list&js={\"pages\":(tp),\"data\":(x)}&token=70f12f2f4f091e459a279469fe49eca5&filter=(HolderNumChangeRate<-10)(RangeChangeRate=)";
    private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.116 Safari/537.36";
    private static final String REFERRER_URL = "http://data.eastmoney.com/";

    private static final String HOLDER_NUM_LATEST_URL_DOWNLOAD = "HOLDER_NUM_LATEST_URL_DOWNLOAD";

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    @Autowired
    private HashOperations<String, String, Object> hashOperations;

    @Autowired
    private THolderNumLatestService tHolderNumLatestService;

    @Override
    public void run(String... args) throws Exception {
        Integer totalPage = getTotalPage();
        LOGGER.info("total page :{}", totalPage);
        for (int i = 0; i < totalPage; i++) {
            saveTHolderNumLatestList(i + 1);
            // 防止爬取太快，避免反爬等问题
            waitTime();
        }
    }

    private Integer getTotalPage() {
        String pageNoUrl = String.format(HOLDER_NUM_LATEST_URL, 1);
        try {
            Connection.Response response = Jsoup.connect(pageNoUrl).ignoreContentType(true).followRedirects(true).referrer(REFERRER_URL)
                    .userAgent(USER_AGENT)
                    .execute();
            String jsonStr = response.body();

            return JsonPath.read(jsonStr, "$.pages");
        } catch (IOException e) {
            LOGGER.error("获取页码数出错", e);
        }
        return 0;
    }

    private void saveTHolderNumLatestList(int pageNo) {
        String pageNoUrl = String.format(HOLDER_NUM_LATEST_URL, pageNo);
        LOGGER.info("pageNoUrl:{}", pageNoUrl);
        Object object = hashOperations.get(HOLDER_NUM_LATEST_URL_DOWNLOAD, pageNoUrl);
        if (Objects.nonNull(object)) {
            LOGGER.info("pageNoUrl:{}已经爬取", pageNoUrl);
            return;
        }

        try {
            Connection.Response response = Jsoup.connect(pageNoUrl).ignoreContentType(true).followRedirects(true).referrer(REFERRER_URL)
                    .userAgent(USER_AGENT)
                    .execute();
            String jsonStr = response.body();
            List<Map<String, Object>> dataList = JsonPath.read(jsonStr, "$.data");


            List<THolderNumLatestEntity> tHolderNumLatestEntityList = dataList.stream().map(map -> {
                THolderNumLatestEntity tHolderNumLatestEntity = new THolderNumLatestEntity();
                tHolderNumLatestEntity.setSecurityCode((String) map.get("SecurityCode"));
                tHolderNumLatestEntity.setSecurityName((String) map.get("SecurityName"));
                tHolderNumLatestEntity.setHolderNum(((Double) map.get("HolderNum")).intValue());
                tHolderNumLatestEntity.setPreviousHolderNum(((Double) map.get("PreviousHolderNum")).intValue());
                tHolderNumLatestEntity.setHolderNumChange(((Double) map.get("HolderNumChange")).intValue());
                tHolderNumLatestEntity.setHolderNumChangeRate((Double) map.get("HolderNumChangeRate"));
                tHolderNumLatestEntity.setRangeChangeRate(Objects.isNull(map.get("RangeChangeRate")) || "-".equals(map.get("RangeChangeRate")) ? 0D : (Double) map.get("RangeChangeRate"));
                String endDateStr = (String) map.get("EndDate");
                String previousEndDateStr = (String) map.get("PreviousEndDate");
                String noticeDateStr = (String) map.get("NoticeDate");
                LocalDate endDate = LocalDate.parse(endDateStr, DATE_TIME_FORMATTER);
                LocalDate previousEndDate = LocalDate.parse(previousEndDateStr, DATE_TIME_FORMATTER);
                LocalDate noticeDate = LocalDate.parse(noticeDateStr, DATE_TIME_FORMATTER);
                tHolderNumLatestEntity.setEndDate(endDate);
                tHolderNumLatestEntity.setPreviousEndDate(previousEndDate);
                tHolderNumLatestEntity.setNoticeDate(noticeDate);
                return tHolderNumLatestEntity;

            }).collect(Collectors.toList());

            tHolderNumLatestService.saveTHolderNumLatestList(tHolderNumLatestEntityList);

            hashOperations.put(HOLDER_NUM_LATEST_URL_DOWNLOAD, pageNoUrl, true);
        } catch (Exception e) {
            LOGGER.error("获取THolderNumLatestList出错", e);
        }
    }

    private void waitTime() {
        Random random = new Random(47);
        long sleepTime = random.nextInt(1000) + 1000L;
        try {
            Thread.sleep(sleepTime);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

}
