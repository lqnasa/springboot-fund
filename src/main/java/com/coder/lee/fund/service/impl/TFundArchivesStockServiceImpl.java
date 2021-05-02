package com.coder.lee.fund.service.impl;

import com.coder.lee.fund.entity.TFundArchivesStockEntity;
import com.coder.lee.fund.repository.TFundArchivesStockRepository;
import com.coder.lee.fund.service.TFundArchivesStockService;
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
public class TFundArchivesStockServiceImpl implements TFundArchivesStockService {

    @Autowired
    private TFundArchivesStockRepository tFundArchivesStockRepository;

    @Override
    public void saveFundArchivesStock(TFundArchivesStockEntity tFundArchivesStockEntity) {
        tFundArchivesStockRepository.save(tFundArchivesStockEntity);
    }

    @Override
    public void saveFundArchivesStockList(List<TFundArchivesStockEntity> tFundArchivesStockEntityList) {
        tFundArchivesStockRepository.saveAll(tFundArchivesStockEntityList);
    }
}
