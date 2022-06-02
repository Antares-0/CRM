package com.powernode.crm.settings.service.impl;

import com.powernode.crm.settings.domain.DictionaryValue;
import com.powernode.crm.settings.mapper.DictionaryValueMapper;
import com.powernode.crm.settings.service.DicValueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author LXM
 * @create 2022-05-20 9:24
 */
@Service("dicValueService")
public class DicValueServiceImpl implements DicValueService {

    @Autowired
    private DictionaryValueMapper dictionaryValueMapper;

    @Override
    public List<DictionaryValue> queryAllDicValue(String typeCode) {
        return dictionaryValueMapper.selectDicValueByTypeCode(typeCode);
    }
}
