package com.lucky.apibackend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lucky.apibackend.model.entity.User;
import com.lucky.apibackend.service.UserService;
import com.lucky.apibackend.mapper.UserMapper;
import org.springframework.stereotype.Service;

/**
* @author ccc
* @description 针对表【user(用户)】的数据库操作Service实现
* @createDate 2024-04-16 22:47:51
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{

}




