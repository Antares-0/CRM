package com.powernode.crm.workbench.service;

import com.powernode.crm.workbench.domain.Clue;
import com.powernode.crm.workbench.domain.ClueRemark;

import java.util.List;
import java.util.Map;

/**
 * @author LXM
 * @create 2022-05-18 9:56
 */
public interface ClueService {

    int insertNewClue(Clue clue);

    List<Clue> queryAllClueByConditionForPage(Map<String, Object> map);

    int queryAllClueByConditionForPageForCount(Map<String, Object> map);

    int updateClueById(Clue clue);

    Clue queryClueById(String id);

    void saveConvert(Map<String, Object> map);

    int deleteClueByArray(String[] ids);

}
