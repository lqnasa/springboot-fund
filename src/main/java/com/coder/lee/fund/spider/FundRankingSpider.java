package com.coder.lee.fund.spider;

import com.coder.lee.fund.entity.TFundArchivesStockEntity;
import com.coder.lee.fund.entity.TFundEntity;
import com.coder.lee.fund.entity.elastic.EFundArchivesStockEntity;
import com.coder.lee.fund.entity.elastic.EFundEntity;
import com.coder.lee.fund.service.TFundArchivesStockEntityService;
import com.coder.lee.fund.service.TFundEntityService;
import com.coder.lee.fund.service.elastic.EFundArchivesStockEntityService;
import com.coder.lee.fund.service.elastic.EFundEntityService;
import com.jayway.jsonpath.JsonPath;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

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

    private static final String RANK_HANDLER_URL = "http://fund.eastmoney.com/data/rankhandler.aspx?op=ph&dt=kf&ft=gp&rs=&gs=0&sc=6yzf&st=desc&sd=2020-02-09&ed=2021-04-29&qdii=&tabSubtype=,,,,,&pi=1&pn=10000&dx=1&v=0.69104189822889";
    private static final String FUND_RANKING_URL = "http://fund.eastmoney.com/data/fundranking.html";
    private static final String FUND_YEARS_URL = "http://fundf10.eastmoney.com/FundArchivesDatas.aspx?type=jjcc&code=%s&topline=10&year=&month=&rt=0.16801913405800972";
    private static final String FUND_F20_URL = "http://fundf10.eastmoney.com/FundArchivesDatas.aspx?type=jjcc&code=%s&topline=20&year=%s&month=&rt=0.20032375982039508";
    private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.116 Safari/537.36";
    private static final String BLANK_SIGN = "---";


    @Autowired
    private TFundArchivesStockEntityService tFundArchivesStockEntityService;

    @Autowired
    private TFundEntityService tFundEntityService;

//    @Autowired
//    private EFundArchivesStockEntityService eFundArchivesStockEntityService;
//
//    @Autowired
//    private EFundEntityService eFundEntityService;


    @Override
    public void run(String... args) throws Exception {
        try {
            Connection.Response response = Jsoup.connect(RANK_HANDLER_URL).ignoreContentType(true).followRedirects(true).referrer(FUND_RANKING_URL)
                    .userAgent(USER_AGENT)
                    .execute();
            String body = response.body();
            body = body.replaceAll("var rankData = (.*);", "$1");
            List<String> datas = JsonPath.read(body, "$.datas");
            for (String data : datas) {
                String fundCode = data.split(",")[0];
                TFundEntity tFundEntity = tFundEntityService.findByFundCode(fundCode);
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

        tFundEntityService.saveFund(tFundEntity);
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
        String fundF10Body = fundYearRes.body();
        fundF10Body = fundF10Body.replaceAll("var apidata=(.*);", "$1");
        return JsonPath.read(fundF10Body, "$.arryear[*]");
    }

    private void saveFundF20(String fundCode, Integer year) throws IOException {
        String fundF10Url = String.format(FUND_F20_URL, fundCode, year);
        Connection.Response fundF10Res = Jsoup.connect(fundF10Url).ignoreContentType(true).followRedirects(true).referrer(String.format("http://fundf10.eastmoney.com/ccmx_%s.html", fundCode))
                .userAgent(USER_AGENT)
                .execute();
        String fundF10Body = fundF10Res.body();
        fundF10Body = fundF10Body.replaceAll("var apidata=(.*);", "$1");
        String content = JsonPath.read(fundF10Body, "$.content");
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

        tFundArchivesStockEntityService.saveFundArchivesStockList(tFundArchivesStockEntityList);
//        List<EFundArchivesStockEntity> eFundArchivesStockEntityList = tFundArchivesStockEntityList.stream().map(tFundArchivesStockEntity -> {
//            EFundArchivesStockEntity eFundArchivesStockEntity = new EFundArchivesStockEntity();
//            BeanUtils.copyProperties(tFundArchivesStockEntity, eFundArchivesStockEntity);
//            return eFundArchivesStockEntity;
//        }).collect(Collectors.toList());
//        eFundArchivesStockEntityService.saveFundArchivesStockList(eFundArchivesStockEntityList);
    }

    public static void main(String[] args) {
        String str = "{ content:\"<div class='box'><div class='boxitem w790'><h4 class='t'><label class='left'><a href='http://fund.eastmoney.com/000594.html'>大摩进取优选股票</a>&nbsp;&nbsp;2021年1季度股票投资明细</label><label class='right lab2 xq505'>&nbsp;&nbsp;&nbsp;&nbsp;来源：天天基金&nbsp;&nbsp;&nbsp;&nbsp;截止至：<font class='px12'>2021-03-31</font></label></h4><div class='space0'></div><table class='w782 comm tzxq'><thead><tr><th class='first'>序号</th><th>股票代码</th><th>股票名称</th><th>最新价</th><th>涨跌幅</th><th class='xglj'>相关资讯</th><th>占净值<br />比例</th><th class='cgs'>持股数<br />（万股）</th><th class='last ccs'>持仓市值<br />（万元）</th></tr></thead><tbody><tr><td>1</td><td><a href='//quote.eastmoney.com/sh601233.html'>601233</a></td><td class='tol'><a href='//quote.eastmoney.com/sh601233.html'>桐昆股份</a></td><td class='tor'><span id='dq601233'></span></td><td class='tor'><span id='zd601233'></span></td><td class='xglj'><a href='ccbdxq_000594_601233.html' class='red'>变动详情</a><a href='//fund.eastmoney.com/ba/topic,601233.html' >股吧</a><a href='//quote.eastmoney.com/sh601233.html' >行情</a><a href='//emweb.eastmoney.com/PC_HSF10/OperationsRequired/Index?type=web&code=sh601233' style='display:none'>档案</a></td><td class='tor'>8.91%</td><td class='tor'>1,056.82</td><td class='tor'>21,823.32</td></tr><tr><td>2</td><td><a href='//quote.eastmoney.com/sz002597.html'>002597</a></td><td class='tol'><a href='//quote.eastmoney.com/sz002597.html'>金禾实业</a></td><td class='tor'><span id='dq002597'></span></td><td class='tor'><span id='zd002597'></span></td><td class='xglj'><a href='ccbdxq_000594_002597.html' class='red'>变动详情</a><a href='//fund.eastmoney.com/ba/topic,002597.html' >股吧</a><a href='//quote.eastmoney.com/sz002597.html' >行情</a><a href='//emweb.eastmoney.com/PC_HSF10/OperationsRequired/Index?type=web&code=sz002597' style='display:none'>档案</a></td><td class='tor'>7.42%</td><td class='tor'>461.38</td><td class='tor'>18,178.40</td></tr><tr><td>3</td><td><a href='//quote.eastmoney.com/sz002064.html'>002064</a></td><td class='tol'><a href='//quote.eastmoney.com/sz002064.html'>华峰化学</a></td><td class='tor'><span id='dq002064'></span></td><td class='tor'><span id='zd002064'></span></td><td class='xglj'><a href='ccbdxq_000594_002064.html' class='red'>变动详情</a><a href='//fund.eastmoney.com/ba/topic,002064.html' >股吧</a><a href='//quote.eastmoney.com/sz002064.html' >行情</a><a href='//emweb.eastmoney.com/PC_HSF10/OperationsRequired/Index?type=web&code=sz002064' style='display:none'>档案</a></td><td class='tor'>5.98%</td><td class='tor'>1,251.43</td><td class='tor'>14,641.77</td></tr><tr><td>4</td><td><a href='//quote.eastmoney.com/sh601966.html'>601966</a></td><td class='tol'><a href='//quote.eastmoney.com/sh601966.html'>玲珑轮胎</a></td><td class='tor'><span id='dq601966'></span></td><td class='tor'><span id='zd601966'></span></td><td class='xglj'><a href='ccbdxq_000594_601966.html' class='red'>变动详情</a><a href='//fund.eastmoney.com/ba/topic,601966.html' >股吧</a><a href='//quote.eastmoney.com/sh601966.html' >行情</a><a href='//emweb.eastmoney.com/PC_HSF10/OperationsRequired/Index?type=web&code=sh601966' style='display:none'>档案</a></td><td class='tor'>5.54%</td><td class='tor'>289.92</td><td class='tor'>13,568.27</td></tr><tr><td>5</td><td><a href='//quote.eastmoney.com/sh600409.html'>600409</a></td><td class='tol'><a href='//quote.eastmoney.com/sh600409.html'>三友化工</a></td><td class='tor'><span id='dq600409'></span></td><td class='tor'><span id='zd600409'></span></td><td class='xglj'><a href='ccbdxq_000594_600409.html' class='red'>变动详情</a><a href='//fund.eastmoney.com/ba/topic,600409.html' >股吧</a><a href='//quote.eastmoney.com/sh600409.html' >行情</a><a href='//emweb.eastmoney.com/PC_HSF10/OperationsRequired/Index?type=web&code=sh600409' style='display:none'>档案</a></td><td class='tor'>5.46%</td><td class='tor'>1,235.62</td><td class='tor'>13,381.74</td></tr><tr><td>6</td><td><a href='//quote.eastmoney.com/sh600346.html'>600346</a></td><td class='tol'><a href='//quote.eastmoney.com/sh600346.html'>恒力石化</a></td><td class='tor'><span id='dq600346'></span></td><td class='tor'><span id='zd600346'></span></td><td class='xglj'><a href='ccbdxq_000594_600346.html' class='red'>变动详情</a><a href='//fund.eastmoney.com/ba/topic,600346.html' >股吧</a><a href='//quote.eastmoney.com/sh600346.html' >行情</a><a href='//emweb.eastmoney.com/PC_HSF10/OperationsRequired/Index?type=web&code=sh600346' style='display:none'>档案</a></td><td class='tor'>5.44%</td><td class='tor'>454.53</td><td class='tor'>13,331.41</td></tr><tr><td>7</td><td><a href='//quote.eastmoney.com/sz002493.html'>002493</a></td><td class='tol'><a href='//quote.eastmoney.com/sz002493.html'>荣盛石化</a></td><td class='tor'><span id='dq002493'></span></td><td class='tor'><span id='zd002493'></span></td><td class='xglj'><a href='ccbdxq_000594_002493.html' class='red'>变动详情</a><a href='//fund.eastmoney.com/ba/topic,002493.html' >股吧</a><a href='//quote.eastmoney.com/sz002493.html' >行情</a><a href='//emweb.eastmoney.com/PC_HSF10/OperationsRequired/Index?type=web&code=sz002493' style='display:none'>档案</a></td><td class='tor'>5.25%</td><td class='tor'>466.92</td><td class='tor'>12,858.92</td></tr><tr><td>8</td><td><a href='//quote.eastmoney.com/sh603612.html'>603612</a></td><td class='tol'><a href='//quote.eastmoney.com/sh603612.html'>索通发展</a></td><td class='tor'><span id='dq603612'></span></td><td class='tor'><span id='zd603612'></span></td><td class='xglj'><a href='ccbdxq_000594_603612.html' class='red'>变动详情</a><a href='//fund.eastmoney.com/ba/topic,603612.html' >股吧</a><a href='//quote.eastmoney.com/sh603612.html' >行情</a><a href='//emweb.eastmoney.com/PC_HSF10/OperationsRequired/Index?type=web&code=sh603612' style='display:none'>档案</a></td><td class='tor'>5.20%</td><td class='tor'>777.74</td><td class='tor'>12,747.13</td></tr><tr><td>9</td><td><a href='//quote.eastmoney.com/sz000100.html'>000100</a></td><td class='tol'><a href='//quote.eastmoney.com/sz000100.html'>TCL科技</a></td><td class='tor'><span id='dq000100'></span></td><td class='tor'><span id='zd000100'></span></td><td class='xglj'><a href='ccbdxq_000594_000100.html' class='red'>变动详情</a><a href='//fund.eastmoney.com/ba/topic,000100.html' >股吧</a><a href='//quote.eastmoney.com/sz000100.html' >行情</a><a href='//emweb.eastmoney.com/PC_HSF10/OperationsRequired/Index?type=web&code=sz000100' style='display:none'>档案</a></td><td class='tor'>5.07%</td><td class='tor'>1,329.33</td><td class='tor'>12,415.92</td></tr><tr><td>10</td><td><a href='//quote.eastmoney.com/sh600219.html'>600219</a></td><td class='tol'><a href='//quote.eastmoney.com/sh600219.html'>南山铝业</a></td><td class='tor'><span id='dq600219'></span></td><td class='tor'><span id='zd600219'></span></td><td class='xglj'><a href='ccbdxq_000594_600219.html' class='red'>变动详情</a><a href='//fund.eastmoney.com/ba/topic,600219.html' >股吧</a><a href='//quote.eastmoney.com/sh600219.html' >行情</a><a href='//emweb.eastmoney.com/PC_HSF10/OperationsRequired/Index?type=web&code=sh600219' style='display:none'>档案</a></td><td class='tor'>4.94%</td><td class='tor'>3,504.97</td><td class='tor'>12,092.15</td></tr></tbody></table><div class='hide' id='gpdmList'>1.601233,0.002597,0.002064,1.601966,1.600409,1.600346,0.002493,1.603612,0.000100,1.600219,</div><div class='tfoot'><font class='px12'><a style='cursor:pointer;' onclick='LoadMore(this,3,LoadStockPos)'>显示全部持仓明细>></a></font></div></div></div></div><div style='padding:5px 10px'>注：加*号代表进入上市公司的十大流通股东却没有进入单只基金前十大重仓股的个股。</div>\",arryear:[2021,2020,2019,2018,2017,2016,2015,2014],curyear:2021}";
        List<Integer> arryear = JsonPath.read(str, "$.arryear[*]");
        for (Integer s : arryear) {
            System.out.println(s);
        }
    }
}
