package com.coder.lee.fund.service.impl;

import com.coder.lee.fund.entity.TFundArchivesStockEntity;
import com.coder.lee.fund.repository.TFundArchivesStockEntityRepository;
import com.coder.lee.fund.service.TFundArchivesStockEntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Description: Function Description
 * Copyright: Copyright (c)
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/2/9 9:32
 *
 * @author coderLee23
 */
@Service
public class TFundArchivesStockEntityServiceImpl implements TFundArchivesStockEntityService {

    @Autowired
    private TFundArchivesStockEntityRepository tFundArchivesStockEntityRepository;

    @Override
    public void saveFundArchivesStock(TFundArchivesStockEntity tFundArchivesStockEntity) {
        tFundArchivesStockEntityRepository.save(tFundArchivesStockEntity);
    }

    @Override
    public void saveFundArchivesStockList(List<TFundArchivesStockEntity> tFundArchivesStockEntityList) {
        tFundArchivesStockEntityRepository.saveAll(tFundArchivesStockEntityList);
    }
}
