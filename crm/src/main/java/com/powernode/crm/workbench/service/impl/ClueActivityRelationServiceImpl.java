package com.powernode.crm.workbench.service.impl;

import com.powernode.crm.workbench.domain.ClueActivityRelation;
import com.powernode.crm.workbench.mapper.ClueActivityRelationMapper;
import com.powernode.crm.workbench.service.ClueActivityRelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author LXM
 * @create 2022-05-23 10:30
 */
@Service("clueActivityRelationService")
public class ClueActivityRelationServiceImpl implements ClueActivityRelationService {

    @Autowired
    private ClueActivityRelationMapper mapper;

    @Override
    public int saveCreateClueActivityRelationByList(List<ClueActivityRelation> list) {
        return mapper.insertClueActivityRelationByList(list);
    }

    @Override
    public int deleteCueActivityByClueIdActivityId(ClueActivityRelation clueActivityRelation) {
        return mapper.deleteClueActivityRelationByClueIdActivityId(clueActivityRelation);
    }
}
