package com.coder.lee.fund.repository;

import com.coder.lee.fund.entity.TFundEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Description: Function Description
 * Copyright: Copyright (c)
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/2/9 9:13
 *
 * @author coderLee23
 */
@Repository
public interface TFundRepository extends JpaRepository<TFundEntity, Integer> {

    /**
     * 根据fundCode查找
     *
     * @param fundCode 基金代码
     * @return 实体
     */
    TFundEntity findTFundEntityByFundCode(String fundCode);
}
