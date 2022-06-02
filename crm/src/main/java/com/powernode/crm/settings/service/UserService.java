package com.powernode.crm.settings.service;

import com.powernode.crm.settings.domain.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author LXM
 * @create 2022-04-25 9:48
 */

public interface UserService {
    User queryUserByLoginActAndPwd(Map<String, Object> map);

    List<User> queryAllUsers();

}
