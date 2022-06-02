package com.powernode.crm.workbench.service;

import com.powernode.crm.workbench.domain.Customer;

import java.util.List;

/**
 * @author LXM
 * @create 2022-05-25 15:54
 */
public interface CustomerService {

    List<String> queryAllCustomerName(String name);

    int saveCustomer(Customer customer);

}
