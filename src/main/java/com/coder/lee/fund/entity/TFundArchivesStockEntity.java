package com.coder.lee.fund.entity;

import javax.persistence.*;
import java.util.Objects;

/**
 * Description: Function Description
 * Copyright: Copyright (c)
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/2/9 9:11
 *
 * @author coderLee23
 */
@Entity
@Table(name = "t_fund_archives_stock", schema = "public", catalog = "postgres")
public class TFundArchivesStockEntity {

    private Integer id;
    private String stockName;
    private String stockCode;
    private Double netWorthRatio;
    private Double shareholding;
    private Double holdMoney;
    private String fundCode;
    private String upUntilDate;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Basic
    @Column(name = "stock_name")
    public String getStockName() {
        return stockName;
    }

    public void setStockName(String stockName) {
        this.stockName = stockName;
    }

    @Basic
    @Column(name = "stock_code")
    public String getStockCode() {
        return stockCode;
    }

    public void setStockCode(String stockCode) {
        this.stockCode = stockCode;
    }

    @Basic
    @Column(name = "net_worth_ratio")
    public Double getNetWorthRatio() {
        return netWorthRatio;
    }

    public void setNetWorthRatio(Double netWorthRatio) {
        this.netWorthRatio = netWorthRatio;
    }

    @Basic
    @Column(name = "shareholding")
    public Double getShareholding() {
        return shareholding;
    }

    public void setShareholding(Double shareholding) {
        this.shareholding = shareholding;
    }

    @Basic
    @Column(name = "hold_money")
    public Double getHoldMoney() {
        return holdMoney;
    }

    public void setHoldMoney(Double holdMoney) {
        this.holdMoney = holdMoney;
    }

    @Basic
    @Column(name = "fund_code")
    public String getFundCode() {
        return fundCode;
    }

    public void setFundCode(String fundCode) {
        this.fundCode = fundCode;
    }

    @Basic
    @Column(name = "up_until_date")
    public String getUpUntilDate() {
        return upUntilDate;
    }

    public void setUpUntilDate(String upUntilDate) {
        this.upUntilDate = upUntilDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TFundArchivesStockEntity that = (TFundArchivesStockEntity) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(stockName, that.stockName) &&
                Objects.equals(stockCode, that.stockCode) &&
                Objects.equals(netWorthRatio, that.netWorthRatio) &&
                Objects.equals(shareholding, that.shareholding) &&
                Objects.equals(holdMoney, that.holdMoney) &&
                Objects.equals(fundCode, that.fundCode) &&
                Objects.equals(upUntilDate, that.upUntilDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, stockName, stockCode, netWorthRatio, shareholding, holdMoney, fundCode, upUntilDate);
    }

    @Override
    public String toString() {
        return "TFundArchivesStockEntity{" +
                "id=" + id +
                ", stockName='" + stockName + '\'' +
                ", stockCode='" + stockCode + '\'' +
                ", netWorthRatio=" + netWorthRatio +
                ", shareholding=" + shareholding +
                ", holdMoney=" + holdMoney +
                ", fundCode='" + fundCode + '\'' +
                ", upUntilDate='" + upUntilDate + '\'' +
                '}';
    }
}
