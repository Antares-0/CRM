package com.powernode.crm.workbench.service;

import com.powernode.crm.workbench.domain.Clue;
import com.powernode.crm.workbench.domain.ClueActivityRelation;

import java.util.List;

/**
 * @author LXM
 * @create 2022-05-23 10:30
 */
public interface ClueActivityRelationService {

    int saveCreateClueActivityRelationByList(List<ClueActivityRelation> list);

    int deleteCueActivityByClueIdActivityId(ClueActivityRelation clueActivityRelation);
}
