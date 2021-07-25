package com.coder.lee.fund.spider;

import com.coder.lee.fund.entity.TInstitutionResearchEntity;
import com.coder.lee.fund.service.TInstitutionResearchService;
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
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * Description: Function Description
 * Copyright: Copyright (c)
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/5/2 10:08
 *
 * @author coderLee23
 */
@Order(1001)
@Component
public class InstitutionResearchSpider implements CommandLineRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(InstitutionResearchSpider.class);

    private static final String RESEARCH_URL = "http://datainterface3.eastmoney.com/EM_DataCenter_V3/api/JGDYHZ/GetJGDYMX?tkn=eastmoney&secuCode=&sortfield=3&sortdirec=&pageNum=%s&pageSize=50&cfg=jgdyhz&p=%s&pageNo=%s&_=1619919179390";
    private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.116 Safari/537.36";
    private static final String REFERRER_URL = "http://data.eastmoney.com/";

    private static final String INSTITUTION_RESEARCH_URL_DOWNLOAD = "INSTITUTION_RESEARCH_URL_DOWNLOAD";

    @Autowired
    private HashOperations<String, String, Object> hashOperations;

    @Autowired
    private TInstitutionResearchService tInstitutionResearchService;

    @Override
    public void run(String... args) throws Exception {
        Integer totalPage = getTotalPage();
        LOGGER.info("total page :{}", totalPage);
        for (int i = totalPage; i > 0; i--) {
            String pageNoUrl = String.format(RESEARCH_URL, i, i, i);
            LOGGER.info("pageNoUrl:{}", pageNoUrl);
            Object object = hashOperations.get(INSTITUTION_RESEARCH_URL_DOWNLOAD, pageNoUrl);
            if (Objects.nonNull(object)) {
                LOGGER.info("pageNoUrl:{}已经爬取，增量爬取方式，认为后续的都已经爬过。", pageNoUrl);
                return;
            }
            saveInstitutionResearchList(pageNoUrl);
            // 防止爬取太快，避免反爬等问题
            waitTime();
        }

    }

    private Integer getTotalPage() {
        String pageNoUrl = String.format(RESEARCH_URL, 1, 1, 1);
        try {
            Connection.Response response = Jsoup.connect(pageNoUrl).ignoreContentType(true).followRedirects(true).referrer(REFERRER_URL)
                    .userAgent(USER_AGENT)
                    .execute();
            String jsonStr = response.body();

            return JsonPath.read(jsonStr, "$.Data[0].TotalPage");
        } catch (IOException e) {
            LOGGER.error("获取页码数出错", e);
        }
        return 0;
    }

    private void saveInstitutionResearchList(String pageNoUrl) {
        try {
            Connection.Response response = Jsoup.connect(pageNoUrl).ignoreContentType(true).followRedirects(true).referrer(REFERRER_URL)
                    .userAgent(USER_AGENT)
                    .execute();
            String jsonStr = response.body();
            List<String> dataList = JsonPath.read(jsonStr, "$.Data[0].Data");
            List<TInstitutionResearchEntity> tInstitutionResearchEntityList = dataList.stream().map(str -> {
                String[] split = str.split("\\|");
                TInstitutionResearchEntity tInstitutionResearchEntity = new TInstitutionResearchEntity();
                tInstitutionResearchEntity.setsCode(split[5]);
                tInstitutionResearchEntity.setsName(split[6]);
                tInstitutionResearchEntity.setOrgSum(Integer.valueOf(split[4]));
                tInstitutionResearchEntity.setStartDate(LocalDate.parse(split[8]));
                tInstitutionResearchEntity.setNoticeDate(LocalDate.parse(split[7]));
                return tInstitutionResearchEntity;
            }).collect(Collectors.toList());

            tInstitutionResearchService.saveTInstitutionResearchList(tInstitutionResearchEntityList);

            hashOperations.put(INSTITUTION_RESEARCH_URL_DOWNLOAD, pageNoUrl, true);
        } catch (Exception e) {
            LOGGER.error("获取InstitutionResearchList出错", e);
        }
    }

    private void waitTime() {
        Random random = new Random(47);
        long sleepTime = random.nextInt(1000) + 500L;
        try {
            Thread.sleep(sleepTime);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

}
