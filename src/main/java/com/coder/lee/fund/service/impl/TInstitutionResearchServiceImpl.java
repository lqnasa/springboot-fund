package com.coder.lee.fund.service.impl;

import com.coder.lee.fund.entity.TInstitutionResearchEntity;
import com.coder.lee.fund.repository.TInstitutionResearchRepository;
import com.coder.lee.fund.service.TInstitutionResearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Description: Function Description
 * Copyright: Copyright (c)
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/5/2 10:07
 *
 * @author coderLee23
 */
@Service
public class TInstitutionResearchServiceImpl implements TInstitutionResearchService {

    @Autowired
    private TInstitutionResearchRepository tInstitutionResearchRepository;


    @Override
    public void saveTInstitutionResearchList(List<TInstitutionResearchEntity> tInstitutionResearchEntityList) {
        tInstitutionResearchRepository.saveAll(tInstitutionResearchEntityList);
    }
}
