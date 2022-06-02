package com.powernode.crm.workbench.service;

import com.powernode.crm.workbench.domain.Contacts;

import java.util.List;

/**
 * @author LXM
 * @create 2022-05-24 16:06
 */
public interface ContactsService {
    List<Contacts> queryAllContactsByName(String name);
}
