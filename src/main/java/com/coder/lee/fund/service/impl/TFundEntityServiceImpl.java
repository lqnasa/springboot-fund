package com.coder.lee.fund.service.impl;

import com.coder.lee.fund.entity.TFundEntity;
import com.coder.lee.fund.repository.TFundEntityRepository;
import com.coder.lee.fund.service.TFundEntityService;
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
@Service
public class TFundEntityServiceImpl implements TFundEntityService {

    @Autowired
    private TFundEntityRepository tFundEntityRepository;

    @Override
    public void saveFund(TFundEntity tFundEntity) {
        tFundEntityRepository.save(tFundEntity);
    }

    @Override
    public TFundEntity findByFundCode(String fundCode) {
        return tFundEntityRepository.findTFundEntityByFundCode(fundCode);
    }
}
