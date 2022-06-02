package com.powernode.crm.workbench.service;

import com.powernode.crm.workbench.domain.ActivityRemark;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author LXM
 * @create 2022-05-16 21:32
 */

public interface ActivityRemarkService {

    List<ActivityRemark> queryActivityRemarkForDetailById(String id);

    int insertActivityRemark(ActivityRemark activityRemark);

    int deleteActivityRemark(String id);

    int updateActivityRemark(ActivityRemark activityRemark);

}
