package com.coder.lee.fund.service.elastic.impl;

import com.coder.lee.fund.entity.elastic.EFundEntity;
import com.coder.lee.fund.repository.elastic.EFundEntityRepository;
import com.coder.lee.fund.service.elastic.EFundEntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Description: Function Description
 * Copyright: Copyright (c)
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/2/9 9:31
 *
 * @author coderLee23
 */
public class EFundEntityServiceImpl implements EFundEntityService {

    @Autowired
    private EFundEntityRepository eFundEntityRepository;

    @Override
    public void saveFund(EFundEntity eFundEntity) {
        eFundEntityRepository.save(eFundEntity);
    }

    @Override
    public EFundEntity findByFundCode(String fundCode) {
        return eFundEntityRepository.findTFundEntityByFundCode(fundCode);
    }
}
