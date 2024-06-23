package com.lucky.apibackend.service.impl;

import cn.hutool.core.lang.UUID;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lucky.apibackend.common.BaseResponse;
import com.lucky.apibackend.common.ErrorCode;
import com.lucky.apibackend.constant.UserConstant;
import com.lucky.apibackend.exception.BusinessException;
import com.lucky.apibackend.model.dto.user.UserLoginRequest;
import com.lucky.apibackend.model.dto.user.UserRegisterRequest;
import com.lucky.apibackend.model.entity.User;
import com.lucky.apibackend.model.enums.UserAccountStatusEnum;
import com.lucky.apibackend.model.vo.UserVo;
import com.lucky.apibackend.service.UserService;
import com.lucky.apibackend.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author ccc
 * @description 针对表【user(用户)】的数据库操作Service实现
 * @createDate 2024-04-16 22:47:51
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Resource
    private UserMapper userMapper;

    /**
     * 用户注册
     *
     * @param userRegisterRequest
     * @return
     */
    @Override
    public long userRegister(UserRegisterRequest userRegisterRequest) {
        String userName = userRegisterRequest.getUserName();
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        String invitationCode = userRegisterRequest.getInvitationCode();

        //校验
        if (StringUtils.isAllBlank(userName, userAccount, userPassword, checkPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数为空");
        }
        if (userName.length() > 40) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "昵称过长");
        }
        if (userAccount.length() < 4) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号过短");
        }
        if (userPassword.length() < 8 || checkPassword.length() < 8) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "密码过短");
        }

        //账号不包含特殊字符
        String validPattern = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Matcher matcher = Pattern.compile(validPattern).matcher(userAccount);
        if (matcher.find()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号包含特殊字符");
        }
        //密码和校验密码一致
        if (!userPassword.equals(checkPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "两次密码不一致");
        }

        //TODO 加锁
        //账户不能重复
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUserAccount, userAccount);
        Long count = userMapper.selectCount(queryWrapper);
        if (count > 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号已存在");
        }
        User invitationUser = null;
        if (StringUtils.isNotBlank(invitationCode)){
            LambdaQueryWrapper<User> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
            userLambdaQueryWrapper.eq(User::getInvitationCode,invitationCode);
            //可能会有邀请码相同的用户，查出的不只一个
            invitationUser = this.getOne(userLambdaQueryWrapper);
            if (invitationUser == null){
                throw new BusinessException(ErrorCode.OPERATION_ERROR, "邀请码不存在");
            }
        }
        //密码加密
        String encryptPassword = DigestUtils.md5DigestAsHex((UserConstant.SALT + userPassword).getBytes());

        //插入数据，保存到数据库
        User user = new User();
        user.setUserName(userName);
        user.setUserAccount(userAccount);
        user.setUserPassword(encryptPassword);
        user.setAccessKey("111");
        user.setSecretKey("222");
        //todo 给邀请和被邀请的用户的账户各添加30积分
        user.setInvitationCode(generateRandomString(8));
        boolean save = this.save(user);
        if (!save){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "注册失败");
        }
        return user.getId();
    }

    /**
     * 生成随机字符串
     * @param length
     * @return
     */
    private String generateRandomString(int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder sb = new StringBuilder(length);
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(characters.length());
            char randomChar = characters.charAt(index);
            sb.append(randomChar);
        }
        return sb.toString();
    }

    /**
     * 用户登录
     * @param userLoginRequest
     * @param request
     * @return
     */
    @Override
    public String userLogin(UserLoginRequest userLoginRequest, HttpServletRequest request) {
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();

        //校验
        if (StringUtils.isAllBlank(userAccount, userPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数为空");
        }
        if(userAccount.length() < 4){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号过短");
        }
        if(userPassword.length() < 8){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "密码过短");
        }
        //账号不包含特殊字符
        String validPattern = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Matcher matcher = Pattern.compile(validPattern).matcher(userAccount);
        if (matcher.find()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号包含特殊字符");
        }
        //密码加密
        String encryptPassword = DigestUtils.md5DigestAsHex((UserConstant.SALT + userPassword).getBytes());
        //查询用户是否存在
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUserAccount, userAccount);
        queryWrapper.eq(User::getUserPassword, encryptPassword);
        User user = userMapper.selectOne(queryWrapper);
        if (user == null){
            log.info("user login failed,UserAccount cannot match userPassword");
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "用户不存在或账户密码错误");
        }
        //判断用户状态
        if (user.getStatus().equals(UserAccountStatusEnum.BAN.getValue())){
            log.info("user login failed,UserAccount is banned");
            throw new BusinessException(ErrorCode.PROHIBITED, "账号已封禁");
        }
        //用户信息脱敏
        UserVo userVo = new UserVo();
        BeanUtils.copyProperties(user, userVo);
        //记录登录态
        String token = UUID.randomUUID().toString(true);
        request.getSession().setAttribute(UserConstant.USER_LOGIN_STATE + token, userVo);
        return token;
    }

    /**
     * 用户注销
     * @param request
     * @return
     */
    @Override
    public boolean userLogout(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (StringUtils.isBlank(token)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数为空");
        }
        if (request.getSession().getAttribute(UserConstant.USER_LOGIN_STATE + token) == null){
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR, "用户未登录");
        }
        //清除登录态
        request.getSession().removeAttribute(UserConstant.USER_LOGIN_STATE + token);
        return true;
    }

    /**
     * 获取当前登录用户
     * @param request
     * @return
     */
    @Override
    public UserVo getLoginUser(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        Object userObj = request.getSession().getAttribute(UserConstant.USER_LOGIN_STATE + token);
        UserVo loginUser = (UserVo) userObj;
        if (loginUser == null || loginUser.getId() == null){
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR, "用户未登录");
        }
        if (loginUser.getStatus().equals(UserAccountStatusEnum.BAN.getValue())){
            throw new BusinessException(ErrorCode.PROHIBITED, "账号已封禁");
        }
        return loginUser;
    }

    /**
     * 是游客
     * @param request
     * @return
     */
    @Override
    public UserVo isTourist(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        Object userObj = request.getSession().getAttribute(UserConstant.USER_LOGIN_STATE + token);
        UserVo loginUser = (UserVo) userObj;
        if (loginUser == null || loginUser.getId() <= 0){
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR,"未登录");
        }
        return loginUser;
    }

    @Override
    public boolean isAdmin(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        Object userObj = request.getSession().getAttribute(UserConstant.USER_LOGIN_STATE + token);
        UserVo loginUser = (UserVo) userObj;
        if (loginUser == null || loginUser.getId() <= 0){
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        return loginUser != null && loginUser.getUserRole().equals(UserConstant.ADMIN_ROLE);
    }

    /**
     * 更新用户积分（扣除）
     * @param userId
     * @param reduceScore
     * @return
     */
    @Override
    public boolean reduceWalletBalance(Long userId, Integer reduceScore) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getId, userId);
        User user = this.getOne(queryWrapper);
        user.setBalance(user.getBalance() - reduceScore);
        return this.updateById(user);
    }
}





