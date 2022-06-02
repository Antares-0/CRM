package com.powernode.crm.workbench.service;

import com.powernode.crm.workbench.domain.TransactionHistory;

import java.util.List;

/**
 * @author LXM
 * @create 2022-05-26 13:50
 */
public interface TransactionHistoryService {
    List<TransactionHistory> queryAllTranHistoryForDetail(String tranId);
}
