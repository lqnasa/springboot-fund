package com.coder.lee.fund;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.elasticsearch.ReactiveElasticsearchRestClientAutoConfiguration;

/**
 * Description: SpringbootFund
 * Copyright: Copyright (c)
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/2/9 1:29
 *
 * @author coderLee23
 */
@SpringBootApplication(exclude = {ReactiveElasticsearchRestClientAutoConfiguration.class})
public class SpringBootFundApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootFundApplication.class, args);
    }

}
