package com.coder.lee.fund.spider;

import com.coder.lee.fund.entity.TFundArchivesStockEntity;
import com.coder.lee.fund.entity.TFundEntity;
import com.coder.lee.fund.service.TFundArchivesStockService;
import com.coder.lee.fund.service.TFundService;
import com.jayway.jsonpath.JsonPath;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Description: Function Description
 * Copyright: Copyright (c)
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/2/9 1:29
 *
 * @author coderLee23
 */
//@Order(1000)
//@Component
public class FundRankingSpider implements CommandLineRunner {

    private static final String GP_RANK_HANDLER_URL = "http://fund.eastmoney.com/data/rankhandler.aspx?op=ph&dt=kf&ft=gp&rs=&gs=0&sc=6yzf&st=desc&sd=2020-06-30&ed=2021-06-30&qdii=&tabSubtype=,,,,,&pi=1&pn=10000&dx=1&v=0.69104189822889";
    private static final String HH_RANK_HANDLER_URL = "http://fund.eastmoney.com/data/rankhandler.aspx?op=ph&dt=kf&ft=hh&rs=&gs=0&sc=6yzf&st=desc&sd=2020-06-30&ed=2021-06-30&qdii=&tabSubtype=,,,,,&pi=1&pn=10000&dx=1&v=0.69104189822889";
    private static final String FUND_RANKING_URL = "http://fund.eastmoney.com/data/fundranking.html";
    private static final String FUND_YEARS_URL = "http://fundf10.eastmoney.com/FundArchivesDatas.aspx?type=jjcc&code=%s&topline=10&year=&month=&rt=0.16801913405800972";
    private static final String FUND_F20_URL = "http://fundf10.eastmoney.com/FundArchivesDatas.aspx?type=jjcc&code=%s&topline=20&year=%s&month=&rt=0.20032375982039508";
    private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.116 Safari/537.36";
    private static final String BLANK_SIGN = "---";


    @Autowired
    private TFundArchivesStockService tFundArchivesStockService;

    @Autowired
    private TFundService tFundService;

//    @Autowired
//    private EFundArchivesStockEntityService eFundArchivesStockEntityService;
//
//    @Autowired
//    private EFundEntityService eFundEntityService;


    @Override
    public void run(String... args) throws Exception {
        Stream.of(GP_RANK_HANDLER_URL, HH_RANK_HANDLER_URL).forEach(this::getFundByType);
    }

    private void getFundByType(String fundUrl) {
        try {
            Connection.Response response = Jsoup.connect(fundUrl).ignoreContentType(true).followRedirects(true).referrer(FUND_RANKING_URL)
                    .userAgent(USER_AGENT)
                    .execute();
            String body = response.body();
            body = body.replaceAll("var rankData = (.*);", "$1");
            List<String> datas = JsonPath.read(body, "$.datas");
            for (String data : datas) {
                String fundCode = data.split(",")[0];
                TFundEntity tFundEntity = tFundService.findByFundCode(fundCode);
                if (tFundEntity != null) {
                    continue;
                }
                getFundCodeAndSave(data);
                List<Integer> fundYears = findFundYears(fundCode);
                for (Integer fundYear : fundYears) {
                    saveFundF20(fundCode, fundYear);
                    waitTime();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void getFundCodeAndSave(String data) {
        String fundCode = data.split(",")[0];
        String fundName = data.split(",")[1];
        String oneYear = data.split(",")[10];
        String twoYears = data.split(",")[11];
        String threeYears = data.split(",")[12];
        String thisYear = data.split(",")[13];
        String establishment = data.split(",")[14];

        TFundEntity tFundEntity = new TFundEntity();
        tFundEntity.setFundCode(fundCode);
        tFundEntity.setFundName(fundName);
        tFundEntity.setOneYear(oneYear);
        tFundEntity.setTwoYears(twoYears);
        tFundEntity.setThreeYears(threeYears);
        tFundEntity.setThisYear(thisYear);
        tFundEntity.setEstablishment(establishment);

        tFundService.saveFund(tFundEntity);
        // 保存到es中
//        EFundEntity eFundEntity = new EFundEntity();
//        BeanUtils.copyProperties(tFundEntity, eFundEntity);
//        eFundEntityService.saveFund(eFundEntity);

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


    private List<Integer> findFundYears(String fundCode) throws IOException {
        String fundYearsUrl = String.format(FUND_YEARS_URL, fundCode);
        Connection.Response fundYearRes = Jsoup.connect(fundYearsUrl).ignoreContentType(true).followRedirects(true).referrer(String.format("http://fundf10.eastmoney.com/ccmx_%s.html", fundCode))
                .userAgent(USER_AGENT)
                .execute();
        String fundF20Body = fundYearRes.body();
        fundF20Body = fundF20Body.replaceAll("var apidata=(.*);", "$1");
        return JsonPath.read(fundF20Body, "$.arryear[*]");
    }

    private void saveFundF20(String fundCode, Integer year) throws IOException {
        String fundF10Url = String.format(FUND_F20_URL, fundCode, year);
        Connection.Response fundF20Res = Jsoup.connect(fundF10Url).ignoreContentType(true).followRedirects(true).referrer(String.format("http://fundf10.eastmoney.com/ccmx_%s.html", fundCode))
                .userAgent(USER_AGENT)
                .execute();
        String fundF20Body = fundF20Res.body();
        fundF20Body = fundF20Body.replaceAll("var apidata=(.*);", "$1");
        String content = JsonPath.read(fundF20Body, "$.content");
        Document doc = Jsoup.parse(content);
        Elements elements = doc.select(".box");

        List<TFundArchivesStockEntity> tFundArchivesStockEntityList = elements.stream().flatMap(element -> {
            String upUntilDate = element.selectFirst("h4 font").text();
            Elements tableTbodyTrEls = element.select("table tbody tr");
            return tableTbodyTrEls.stream().map(trElement -> {
                String stockCode = trElement.selectFirst("td:eq(1)").text();
                String stockName = trElement.selectFirst("td:eq(2)").text();

                int tdSize = trElement.select("td").size();
                String netWorthRatio = tdSize <= 7 ? trElement.selectFirst("td:eq(4)").text() : trElement.selectFirst("td:eq(6)").text();
                String shareholding = tdSize <= 7 ? trElement.selectFirst("td:eq(5)").text() : trElement.selectFirst("td:eq(7)").text();
                String holdMoney = tdSize <= 7 ? trElement.selectFirst("td:eq(6)").text() : trElement.selectFirst("td:eq(8)").text();

                TFundArchivesStockEntity tFundArchivesStockEntity = new TFundArchivesStockEntity();
                tFundArchivesStockEntity.setFundCode(fundCode);
                tFundArchivesStockEntity.setStockCode(stockCode);
                tFundArchivesStockEntity.setStockName(stockName);
                if (!netWorthRatio.contains(BLANK_SIGN)) {
                    tFundArchivesStockEntity.setNetWorthRatio(Double.parseDouble(netWorthRatio.replace("%", "")));
                } else {
                    tFundArchivesStockEntity.setNetWorthRatio(0D);
                }

                if (!shareholding.contains(BLANK_SIGN)) {
                    tFundArchivesStockEntity.setShareholding(Double.parseDouble(shareholding.replace(",", "")));
                } else {
                    tFundArchivesStockEntity.setShareholding(0D);
                }

                if (!holdMoney.contains(BLANK_SIGN)) {
                    tFundArchivesStockEntity.setHoldMoney(Double.parseDouble(holdMoney.replace(",", "")));

                } else {
                    tFundArchivesStockEntity.setHoldMoney(0D);
                }

                tFundArchivesStockEntity.setUpUntilDate(upUntilDate);
                return tFundArchivesStockEntity;
            });

        }).collect(Collectors.toList());

        tFundArchivesStockService.saveFundArchivesStockList(tFundArchivesStockEntityList);
//        List<EFundArchivesStockEntity> eFundArchivesStockEntityList = tFundArchivesStockEntityList.stream().map(tFundArchivesStockEntity -> {
//            EFundArchivesStockEntity eFundArchivesStockEntity = new EFundArchivesStockEntity();
//            BeanUtils.copyProperties(tFundArchivesStockEntity, eFundArchivesStockEntity);
//            return eFundArchivesStockEntity;
//        }).collect(Collectors.toList());
//        eFundArchivesStockEntityService.saveFundArchivesStockList(eFundArchivesStockEntityList);
    }

}
