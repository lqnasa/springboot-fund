package com.coder.lee.fund.entity;

import javax.persistence.*;
import java.time.LocalDate;

/**
 * Description: 机构调研
 * Copyright: Copyright (c)
 * Company: Ruijie Co.; Ltd.
 * Create Time: 2021/5/2 9:46
 *
 * @author coderLee23
 */
@Entity
@Table(name = "t_institution_research", schema = "public", catalog = "postgres")
public class TInstitutionResearchEntity {

    // CompanyCode;
//    CompanyName;
//    OrgCode;
//    OrgName;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private int id;
    /**
     * 机构调研数量
     */
    private Integer orgSum;
    /**
     * 股票代码
     */
    private String sCode;
    /**
     * 股票名称
     */
    private String sName;
    /**
     * 公告时间
     */
    private LocalDate noticeDate;
    /**
     * 调研时间
     */
    private LocalDate startDate;
//    private String EndDate;
//    private String Place;
//    private String Description;
//    private String Orgtype;
//    private String OrgtypeName;
//    private String Personnel;
//    private String Licostaff;
//    private String Maincontent;
//    private String ChangePercent;
//    private String Close;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getOrgSum() {
        return orgSum;
    }

    public void setOrgSum(Integer orgSum) {
        this.orgSum = orgSum;
    }

    public String getsCode() {
        return sCode;
    }

    public void setsCode(String sCode) {
        this.sCode = sCode;
    }

    public String getsName() {
        return sName;
    }

    public void setsName(String sName) {
        this.sName = sName;
    }

    public LocalDate getNoticeDate() {
        return noticeDate;
    }

    public void setNoticeDate(LocalDate noticeDate) {
        this.noticeDate = noticeDate;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

}
