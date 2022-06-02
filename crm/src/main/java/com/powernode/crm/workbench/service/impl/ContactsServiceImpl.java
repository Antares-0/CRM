package com.powernode.crm.workbench.service.impl;

import com.powernode.crm.workbench.domain.Contacts;
import com.powernode.crm.workbench.mapper.ContactsMapper;
import com.powernode.crm.workbench.service.ContactsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author LXM
 * @create 2022-05-24 16:07
 */
@Service("contactsService")
public class ContactsServiceImpl implements ContactsService {

    @Autowired
    private ContactsMapper contactsMapper;

    @Override
    public List<Contacts> queryAllContactsByName(String name) {
        return contactsMapper.selectContactsForTransactionByName(name);
    }
}
