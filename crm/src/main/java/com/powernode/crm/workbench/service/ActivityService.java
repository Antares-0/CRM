package com.powernode.crm.workbench.service;

import com.powernode.crm.workbench.domain.Activity;

import java.util.List;
import java.util.Map;

/**
 * @author LXM
 * @create 2022-05-05 21:46
 */
public interface ActivityService {
    public int saveCreateActivity(Activity activity);

    public List<Activity> queryActivityByConditionForPage(Map<String, Object> map);

    public int queryCountOfActivityByCondition(Map<String, Object> map);

    public int deleteActivityByIds(String[] ids);

    public Activity queryActivityById(String id);

    public int updateActivityById(Activity activity);

    public List<Activity> queryAllActivity();

    public List<Activity> queryActivityByIds(String[] ids);

    public int saveActivityByList(List<Activity> activities);

    public Activity queryActivityForDetailsById(String id);

    public List<Activity> queryActivityForDetailByClueId(String id);

    public List<Activity> selectActivityForDetailByNameClueId(Map<String, Object> map);

    public List<Activity> queryActivityForDetailByIds(String[] ids);

    public List<Activity> queryActivityForConvertByNameClueId(Map<String, Object> map);

    public List<Activity> queryActivityForTransactionByName(String name);

}
