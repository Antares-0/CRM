package com.powernode.crm.workbench.service;

import com.powernode.crm.workbench.domain.TransactionRemark;

import java.util.List;

/**
 * @author LXM
 * @create 2022-05-26 14:13
 */
public interface TransactionRemarkService {
    List<TransactionRemark> queryTransactionRemarkForDetailByTranId(String tranId);
}
