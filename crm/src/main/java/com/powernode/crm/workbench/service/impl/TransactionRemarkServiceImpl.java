package com.powernode.crm.workbench.service.impl;

import com.powernode.crm.workbench.domain.TransactionRemark;
import com.powernode.crm.workbench.mapper.TransactionRemarkMapper;
import com.powernode.crm.workbench.service.TransactionRemarkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author LXM
 * @create 2022-05-26 14:14
 */
@Service("transactionRemarkService")
public class TransactionRemarkServiceImpl implements TransactionRemarkService {

    @Autowired
    private TransactionRemarkMapper transactionRemarkMapper;

    @Override
    public List<TransactionRemark> queryTransactionRemarkForDetailByTranId(String tranId) {
        return transactionRemarkMapper.selectTransactionForDetailById(tranId);
    }
}
