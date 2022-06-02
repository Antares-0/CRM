package com.powernode.crm.workbench.service.impl;

import com.powernode.crm.workbench.domain.Customer;
import com.powernode.crm.workbench.mapper.CustomerMapper;
import com.powernode.crm.workbench.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CyclicBarrier;

/**
 * @author LXM
 * @create 2022-05-25 15:54
 */
@Service("customerService")
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerMapper customerMapper;

    @Override
    public List<String> queryAllCustomerName(String name) {
        return customerMapper.selectAllCustomerName(name);
    }

    @Override
    public int saveCustomer(Customer customer) {
        return customerMapper.insertCustomer(customer);
    }
}
