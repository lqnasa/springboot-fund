package com.coder.lee.fund.service.impl;

import com.coder.lee.fund.entity.TFundEntity;
import com.coder.lee.fund.repository.TFundRepository;
import com.coder.lee.fund.service.TFundService;
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
public class TFundServiceImpl implements TFundService {

    @Autowired
    private TFundRepository tFundRepository;

    @Override
    public void saveFund(TFundEntity tFundEntity) {
        tFundRepository.save(tFundEntity);
    }

    @Override
    public TFundEntity findByFundCode(String fundCode) {
        return tFundRepository.findTFundEntityByFundCode(fundCode);
    }
}
