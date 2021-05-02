package com.coder.lee.fund.entity;

import javax.persistence.*;
import java.time.LocalDate;

/**
 * Description: Function Description
 * Copyright: Copyright (c)
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/5/3 0:15
 *
 * @author coderLee23
 */
@Entity
@Table(name = "t_holder_num_latest", schema = "public", catalog = "postgres")
public class THolderNumLatestEntity {

    /**
     * SecurityCode: "600226",
     * SecurityName: "*ST瀚叶",
     * LatestPrice: 2.7,
     * PriceChangeRate: 5.06,
     * HolderNum: 43765,
     * PreviousHolderNum: 56388,
     * HolderNumChange: -12623,
     * HolderNumChangeRate: -22.386,
     * RangeChangeRate: 47.7,
     * EndDate: "2021-03-31T00:00:00",
     * PreviousEndDate: "2020-12-31T00:00:00",
     * HolderAvgCapitalisation: 183744.141170113,
     * HolderAvgStockQuantity: 71495.77,
     * TotalCapitalisation: 8041562338.31,
     * CapitalStock: 3129012583,
     * NoticeDate: "2021-04-30T00:00:00",
     * ClosePrice: "2.57",
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private int id;

    private String securityCode;

    private String securityName;

    private Integer holderNum;

    private Integer previousHolderNum;

    private Integer holderNumChange;

    private Double holderNumChangeRate;

    private Double rangeChangeRate;

    private LocalDate endDate;

    private LocalDate previousEndDate;

    private LocalDate noticeDate;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSecurityCode() {
        return securityCode;
    }

    public void setSecurityCode(String securityCode) {
        this.securityCode = securityCode;
    }

    public String getSecurityName() {
        return securityName;
    }

    public void setSecurityName(String securityName) {
        this.securityName = securityName;
    }

    public Integer getHolderNum() {
        return holderNum;
    }

    public void setHolderNum(Integer holderNum) {
        this.holderNum = holderNum;
    }

    public Integer getPreviousHolderNum() {
        return previousHolderNum;
    }

    public void setPreviousHolderNum(Integer previousHolderNum) {
        this.previousHolderNum = previousHolderNum;
    }

    public Integer getHolderNumChange() {
        return holderNumChange;
    }

    public void setHolderNumChange(Integer holderNumChange) {
        this.holderNumChange = holderNumChange;
    }

    public Double getHolderNumChangeRate() {
        return holderNumChangeRate;
    }

    public void setHolderNumChangeRate(Double holderNumChangeRate) {
        this.holderNumChangeRate = holderNumChangeRate;
    }

    public Double getRangeChangeRate() {
        return rangeChangeRate;
    }

    public void setRangeChangeRate(Double rangeChangeRate) {
        this.rangeChangeRate = rangeChangeRate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public LocalDate getPreviousEndDate() {
        return previousEndDate;
    }

    public void setPreviousEndDate(LocalDate previousEndDate) {
        this.previousEndDate = previousEndDate;
    }

    public LocalDate getNoticeDate() {
        return noticeDate;
    }

    public void setNoticeDate(LocalDate noticeDate) {
        this.noticeDate = noticeDate;
    }
}
