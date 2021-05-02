package com.coder.lee.fund.service;

import com.coder.lee.fund.entity.TFundArchivesStockEntity;
import org.springframework.stereotype.Controller;

import java.util.List;

/**
 * Description: Function Description
 * Copyright: Copyright (c)
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/2/9 9:19
 *
 * @author coderLee23
 */
public interface TFundArchivesStockEntityService {

    /**
     * 保存
     *
     * @param tFundArchivesStockEntity 实体
     */
    void saveFundArchivesStock(TFundArchivesStockEntity tFundArchivesStockEntity);

    /**
     * 保存list
     *
     * @param tFundArchivesStockEntityList 实体list
     */
    void saveFundArchivesStockList(List<TFundArchivesStockEntity> tFundArchivesStockEntityList);
}
