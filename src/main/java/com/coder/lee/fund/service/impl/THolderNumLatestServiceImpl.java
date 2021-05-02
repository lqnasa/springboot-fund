package com.coder.lee.fund.service.impl;

import com.coder.lee.fund.entity.THolderNumLatestEntity;
import com.coder.lee.fund.repository.THolderNumLatestRepository;
import com.coder.lee.fund.service.THolderNumLatestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Description: Function Description
 * Copyright: Copyright (c)
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/5/3 0:27
 *
 * @author coderLee23
 */
@Service
public class THolderNumLatestServiceImpl implements THolderNumLatestService {

    @Autowired
    private THolderNumLatestRepository tHolderNumLatestRepository;

    @Override
    public void saveTHolderNumLatestList(List<THolderNumLatestEntity> tHolderNumLatestEntityList) {
        tHolderNumLatestRepository.saveAll(tHolderNumLatestEntityList);
    }
}
