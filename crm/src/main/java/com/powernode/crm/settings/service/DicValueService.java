package com.powernode.crm.settings.service;

import com.powernode.crm.settings.domain.DictionaryValue;

import java.util.List;

/**
 * @author LXM
 * @create 2022-05-20 9:23
 */
public interface DicValueService {

    List<DictionaryValue> queryAllDicValue(String typeCode);
}
