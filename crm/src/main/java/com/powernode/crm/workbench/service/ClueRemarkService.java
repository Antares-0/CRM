package com.powernode.crm.workbench.service;

import com.powernode.crm.workbench.domain.ClueRemark;

import java.util.List;

/**
 * @author LXM
 * @create 2022-05-21 14:31
 */
public interface ClueRemarkService {

    List<ClueRemark> queryClueRemarkById(String id);

    int insertClueRemark(ClueRemark clueRemark);
}
