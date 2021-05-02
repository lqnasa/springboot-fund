package com.coder.lee.fund.entity.elastic;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

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
@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(indexName = "e_fund_index")
public class EFundEntity {

    /**
     * FieldType，可以是text、long、short、date、integer等
     * 　　text：存储数据时候，会自动分词，并生成索引
     * 　　keyword：存储数据时候，不会分词建立索引
     * 　　analyzer:分词器名称
     */
    @Id
    private int id;
    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    private String fundName;
    @Field(type = FieldType.Text)
    private String fundCode;
    @Field(type = FieldType.Keyword)
    private String oneYear;
    @Field(type = FieldType.Keyword)
    private String twoYears;
    @Field(type = FieldType.Keyword)
    private String threeYears;
    @Field(type = FieldType.Keyword)
    private String thisYear;
    @Field(type = FieldType.Keyword)
    private String establishment;
}
