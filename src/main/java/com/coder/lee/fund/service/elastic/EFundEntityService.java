package com.coder.lee.fund.service.elastic;

import com.coder.lee.fund.entity.elastic.EFundEntity;

/**
 * Description: Function Description
 * Copyright: Copyright (c)
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/2/9 9:20
 *
 * @author coderLee23
 */
public interface EFundEntityService {

    /**
     * 保存
     *
     * @param eFundEntity 实体
     */
    void saveFund(EFundEntity eFundEntity);

    /**
     * 根据fundCode查找
     *
     * @param fundCode 基金代码
     * @return 实体
     */
    EFundEntity findByFundCode(String fundCode);

}
