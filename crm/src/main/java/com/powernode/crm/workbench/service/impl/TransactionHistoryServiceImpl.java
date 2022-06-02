package com.powernode.crm.workbench.service.impl;

import com.powernode.crm.workbench.domain.TransactionHistory;
import com.powernode.crm.workbench.mapper.TransactionHistoryMapper;
import com.powernode.crm.workbench.service.TransactionHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author LXM
 * @create 2022-05-26 13:50
 */

@Service("transactionHistoryService")
public class TransactionHistoryServiceImpl implements TransactionHistoryService {
    @Autowired
    private TransactionHistoryMapper transactionHistoryMapper;

    @Override
    public List<TransactionHistory> queryAllTranHistoryForDetail(String tranId) {
        return transactionHistoryMapper.selectTransactionHistoryByTranId(tranId);
    }
}
