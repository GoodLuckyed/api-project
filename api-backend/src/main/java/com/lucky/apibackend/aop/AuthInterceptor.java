package com.lucky.apibackend.aop;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.lucky.apibackend.annontation.AuthCheck;
import com.lucky.apibackend.common.ErrorCode;
import com.lucky.apibackend.exception.BusinessException;
import com.lucky.apibackend.model.vo.UserVo;
import com.lucky.apibackend.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author lucky
 * @date 2024/4/18
 * @description 权限校验AOP
 */

@Aspect
@Component
public class AuthInterceptor {

    @Resource
    private UserService userService;

    @Around("@annotation(authCheck)")
    public Object doInterceptor(ProceedingJoinPoint joinPoint,AuthCheck authCheck) throws Throwable {
        List<String> anyRole = Arrays.stream(authCheck.anyRole()).filter(StringUtils::isNotBlank).collect(Collectors.toList());
        String mustRole = authCheck.mustRole();
        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
        //当前登用户
        UserVo loginUser = userService.getLoginUser(request);
        //拥有任意权限即通过
        if (CollectionUtils.isNotEmpty(anyRole)){
            //获取当前登录用户的角色
            String userRole = loginUser.getUserRole();
            if (!anyRole.contains(userRole)){
                throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
            }
        }
        //必须有所有权限才通过
        if (StringUtils.isNotBlank(mustRole)){
            //获取当前登录用户的角色
            String userRole = loginUser.getUserRole();
            if (!mustRole.equals(userRole)){
                throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
            }
        }
        //通过权限校验，放行
        return joinPoint.proceed();
    }
}
