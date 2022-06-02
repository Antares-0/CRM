package com.powernode.crm.workbench.service;

import com.powernode.crm.workbench.domain.Transaction;

import java.util.List;
import java.util.Map;

/**
 * @author LXM
 * @create 2022-05-24 15:43
 */
public interface TransactionService {
    int insertCreateTransaction(Transaction transaction);

    List<Transaction> queryTransactionForPageByCondition(Map<String, Object> map);

    int queryCountOfTransactionForPageByCondition(Map<String, Object> map);

    void saveCreateTransaction(Map<String, Object> map);

    Transaction queryTransactionForDetailById(String id);

}
