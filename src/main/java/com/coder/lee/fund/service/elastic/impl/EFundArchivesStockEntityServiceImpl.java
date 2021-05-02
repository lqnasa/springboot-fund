package com.coder.lee.fund.service.elastic.impl;

import com.coder.lee.fund.entity.elastic.EFundArchivesStockEntity;
import com.coder.lee.fund.repository.elastic.EFundArchivesStockEntityRepository;
import com.coder.lee.fund.service.elastic.EFundArchivesStockEntityService;
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
public class EFundArchivesStockEntityServiceImpl implements EFundArchivesStockEntityService {

    @Autowired
    private EFundArchivesStockEntityRepository eFundArchivesStockEntityRepository;

    @Override
    public void saveFundArchivesStock(EFundArchivesStockEntity eFundArchivesStockEntity) {
        eFundArchivesStockEntityRepository.save(eFundArchivesStockEntity);
    }

    @Override
    public void saveFundArchivesStockList(List<EFundArchivesStockEntity> eFundArchivesStockEntityList) {
        eFundArchivesStockEntityRepository.saveAll(eFundArchivesStockEntityList);
    }
}
