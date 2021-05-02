package com.coder.lee.fund.service.elastic;

import com.coder.lee.fund.entity.elastic.EFundArchivesStockEntity;

import java.util.List;

/**
 * Description: Function Description
 * Copyright: Copyright (c)
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/2/9 9:19
 *
 * @author coderLee23
 */
public interface EFundArchivesStockEntityService {

    /**
     * 保存
     *
     * @param eFundArchivesStockEntity 实体
     */
    void saveFundArchivesStock(EFundArchivesStockEntity eFundArchivesStockEntity);

    /**
     * 保存list
     *
     * @param eFundArchivesStockEntityList 实体list
     */
    void saveFundArchivesStockList(List<EFundArchivesStockEntity> eFundArchivesStockEntityList);
}
