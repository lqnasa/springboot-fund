package com.coder.lee.fund.service;

import com.coder.lee.fund.entity.TInstitutionResearchEntity;

import java.util.List;

/**
 * Description: Function Description
 * Copyright: Copyright (c)
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/5/2 10:05
 *
 * @author coderLee23
 */
public interface TInstitutionResearchService {

    /**
     * 保存
     *
     * @param tInstitutionResearchEntityList 实体
     */
    void saveTInstitutionResearchList(List<TInstitutionResearchEntity> tInstitutionResearchEntityList);
}
