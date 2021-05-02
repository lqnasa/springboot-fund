package com.coder.lee.fund.repository.elastic;

import com.coder.lee.fund.entity.TFundArchivesStockEntity;
import com.coder.lee.fund.entity.elastic.EFundArchivesStockEntity;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Description: Function Description
 * Copyright: Copyright (c)
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/2/9 9:16
 *
 * @author coderLee23
 */
//@Repository
public interface EFundArchivesStockEntityRepository extends ElasticsearchRepository<EFundArchivesStockEntity, Integer> {

}
