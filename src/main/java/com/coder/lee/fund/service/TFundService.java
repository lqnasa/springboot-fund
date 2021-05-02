package com.coder.lee.fund.service;

import com.coder.lee.fund.entity.TFundEntity;

/**
 * Description: Function Description
 * Copyright: Copyright (c)
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/2/9 9:20
 *
 * @author coderLee23
 */
public interface TFundService {

    /**
     * 保存
     *
     * @param tFundEntity 实体
     */
    void saveFund(TFundEntity tFundEntity);

    /**
     * 根据fundCode查找
     *
     * @param fundCode 基金代码
     * @return 实体
     */
    TFundEntity findByFundCode(String fundCode);

}
