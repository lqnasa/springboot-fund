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
@Table(name = "t_fund", schema = "public", catalog = "postgres")
public class TFundEntity {

    private int id;
    private String fundName;
    private String fundCode;
    private String oneYear;
    private String twoYears;
    private String threeYears;
    private String thisYear;
    private String establishment;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "fund_name")
    public String getFundName() {
        return fundName;
    }

    public void setFundName(String fundName) {
        this.fundName = fundName;
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
    @Column(name = "one_year")
    public String getOneYear() {
        return oneYear;
    }

    public void setOneYear(String oneYear) {
        this.oneYear = oneYear;
    }

    @Basic
    @Column(name = "two_years")
    public String getTwoYears() {
        return twoYears;
    }

    public void setTwoYears(String twoYears) {
        this.twoYears = twoYears;
    }

    @Basic
    @Column(name = "three_years")
    public String getThreeYears() {
        return threeYears;
    }

    public void setThreeYears(String threeYears) {
        this.threeYears = threeYears;
    }

    @Basic
    @Column(name = "this_year")
    public String getThisYear() {
        return thisYear;
    }

    public void setThisYear(String thisYear) {
        this.thisYear = thisYear;
    }

    @Basic
    @Column(name = "establishment")
    public String getEstablishment() {
        return establishment;
    }

    public void setEstablishment(String establishment) {
        this.establishment = establishment;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TFundEntity that = (TFundEntity) o;
        return id == that.id &&
                Objects.equals(fundName, that.fundName) &&
                Objects.equals(fundCode, that.fundCode) &&
                Objects.equals(oneYear, that.oneYear) &&
                Objects.equals(twoYears, that.twoYears) &&
                Objects.equals(threeYears, that.threeYears) &&
                Objects.equals(thisYear, that.thisYear) &&
                Objects.equals(establishment, that.establishment);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, fundName, fundCode, oneYear, twoYears, threeYears, thisYear, establishment);
    }
}
