package com.coder.lee.fund.service;

import com.coder.lee.fund.entity.THolderNumLatestEntity;

import java.util.List;

/**
 * Description: Function Description
 * Copyright: Copyright (c)
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/5/3 0:26
 *
 * @author coderLee23
 */
public interface THolderNumLatestService {

    /**
     * 保存
     *
     * @param tHolderNumLatestEntityList 实体
     */
    void saveTHolderNumLatestList(List<THolderNumLatestEntity> tHolderNumLatestEntityList);
}
