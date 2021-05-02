package com.coder.lee.fund.repository.elastic;

import com.coder.lee.fund.entity.TFundEntity;
import com.coder.lee.fund.entity.elastic.EFundEntity;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Description: Function Description
 * Copyright: Copyright (c)
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/2/9 9:13
 *
 * @author coderLee23
 */
//@Repository
public interface EFundEntityRepository extends ElasticsearchRepository<EFundEntity, Integer> {

    /**
     * 根据fundCode查找
     *
     * @param fundCode 基金代码
     * @return 实体
     */
    EFundEntity findTFundEntityByFundCode(String fundCode);
}
