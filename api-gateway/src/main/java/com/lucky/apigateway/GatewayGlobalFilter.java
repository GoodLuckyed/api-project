package com.lucky.apigateway;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lucky.apicommon.common.ErrorCode;
import com.lucky.apicommon.model.dto.RequestParamsField;
import com.lucky.apicommon.model.entity.InterfaceInfo;
import com.lucky.apicommon.model.enums.InterfaceStatusEnum;
import com.lucky.apicommon.model.enums.UserAccountStatusEnum;
import com.lucky.apicommon.model.vo.UserVo;
import com.lucky.apicommon.service.inner.InnerInterfaceInfoService;
import com.lucky.apicommon.service.inner.InnerUserService;
import com.lucky.apigateway.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.CharBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

import static com.lucky.apisdk.utils.SignUtils.getSign;

/**
 * @author lucky
 * @date 2024/6/18
 * @description 网关全局过滤器
 */
@Slf4j
@Component
public class GatewayGlobalFilter implements GlobalFilter, Ordered {

    // 请求白名单
    private final static List<String> IP_WHITE_LIST = Arrays.asList("127.0.0.1");

    // 5分钟过期时间（秒为单位）
    private static final long FIVE_MINUTER = 5L * 60;

    @DubboReference
    private InnerUserService innerUserService;

    @DubboReference
    private InnerInterfaceInfoService innerInterfaceInfoService;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 记录请求日志
        ServerHttpRequest request = exchange.getRequest();
        log.info("请求唯一id：" + request.getId());
        log.info("请求方法："  + request.getMethod());
        log.info("请求路径：" + request.getPath());
        log.info("网关本地地址：" + request.getLocalAddress());
        log.info("请求远程地址：" + request.getRemoteAddress());
        log.info("请求接口的客户端真实ip地址：" + getIp(request));
        log.info("url：" + request.getURI());
        return verifyParameters(exchange,chain);
    }

    /**
     * 验证参数
     * @param exchange
     * @param chain
     * @return
     */
    private Mono<Void> verifyParameters(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        // TODO 设置请求白名单

        HttpHeaders headers = request.getHeaders();
        String body = headers.getFirst("body");
        String accessKey = headers.getFirst("accessKey");
        String timeStamp = headers.getFirst("timeStamp");
        String sign = headers.getFirst("sign");
        // 校验参数是否为空
        if (StringUtils.isAnyBlank(body,accessKey,timeStamp,sign)){
            throw new BusinessException(ErrorCode.FORBIDDEN_ERROR);
        }
        // 防止重放XHR
        long currentTime = System.currentTimeMillis() / 1000 ;
        assert timeStamp != null;
        if (currentTime - Long.parseLong(timeStamp) >= FIVE_MINUTER){
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR,"会话已过期,请重试");
        }
        // 用户鉴权（判断ak、sk是否合法）
        UserVo user = innerUserService.getInvokeUserByAccessKey(accessKey);
        if (user == null){
            throw new BusinessException(ErrorCode.FORBIDDEN_ERROR,"用户不存在");
        }
        if (user.getStatus().equals(UserAccountStatusEnum.BAN.getValue())){
            throw new BusinessException(ErrorCode.OPERATION_ERROR,"该账号已封禁");
        }
        // 校验签名
        if (!getSign(body, user.getSecretKey()).equals(sign)){
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR,"非法请求");
        }
        if (user.getBalance() <= 0){
            throw new BusinessException(ErrorCode.OPERATION_ERROR,"余额不足，请先充值！");
        }
        // 校验接口是否存在
        String method = Objects.requireNonNull(request.getMethod()).toString();
        String uri = request.getURI().toString().trim();
        if (StringUtils.isAnyBlank(uri, method)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        InterfaceInfo interfaceInfo = innerInterfaceInfoService.getInterfaceInfo(uri, method);
        if (interfaceInfo == null){
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR,"接口不存在");
        }
        if (interfaceInfo.getStatus().equals(InterfaceStatusEnum.OFFLINE.getValue())){
            throw new BusinessException(ErrorCode.OPERATION_ERROR,"接口未开启");
        }
        MultiValueMap<String, String> queryParams = request.getQueryParams();
        String requestParams = interfaceInfo.getRequestParams();
        List<RequestParamsField> list = new Gson().fromJson(requestParams, new TypeToken<List<RequestParamsField>>() {
        }.getType());
        if ("POST".equals(method)){
            // 获取缓存中请求体数据
            String requestBody = getPostRequestBody(request);
            log.info("POST请求参数：" + requestBody);
            Map<String,Object> requestBodyMap = new Gson().fromJson(requestBody, new TypeToken<Map<String, Object>>() {
            }.getType());
            // 校验请求参数
            if (StringUtils.isNotBlank(requestParams)){
                for (RequestParamsField requestParamsField : list) {
                    if ("是".equals(requestParamsField.getRequired())){
                        if (StringUtils.isBlank((CharSequence) requestBodyMap.get(requestParamsField.getFieldName())) || !requestBodyMap.containsKey(requestParamsField.getFieldName())){
                            throw new BusinessException(ErrorCode.FORBIDDEN_ERROR,"请求参数有误，" + requestParamsField.getFieldName() + "不能为空,详细参数请参考API文档：https://www.baidu.com");
                        }
                    }
                }
            }
        }else if ("GET".equals(method)){
            log.info("GET请求参数" + queryParams);
            // 校验请求参数
            if (StringUtils.isNotBlank(requestParams)){
                for (RequestParamsField requestParamsField : list) {
                    if ("是".equals(requestParamsField.getRequired())){
                        if (StringUtils.isBlank(queryParams.getFirst(requestParamsField.getFieldName())) || !queryParams.containsKey(requestParamsField.getFieldName())){
                            throw new BusinessException(ErrorCode.FORBIDDEN_ERROR,"请求参数有误，" + requestParamsField.getFieldName() + "不能为空,详细参数请参考API文档：https://www.baidu.com");
                        }
                    }
                }
            }
        }
        return null;
    }

    /**
     * 获取POST请求正文
     * @param request
     * @return
     */
    private String getPostRequestBody(ServerHttpRequest request) {
        // 获取请求体
        final Flux<DataBuffer> requestBody = request.getBody();
        final AtomicReference<String> requestBodyRef = new AtomicReference<>();
        // 订阅缓冲区去消费请求体中的数据
        requestBody.subscribe(dataBuffer -> {
            final CharBuffer charBuffer = StandardCharsets.UTF_8.decode(dataBuffer.asByteBuffer());
            // 一定要使用 DataBufferUtils.release(dataBuffer) 释放掉，否则会出现内存泄露
            // DataBufferUtils.retain(dataBuffer) => DataBufferUtils.release(dataBuffer)
            DataBufferUtils.release(dataBuffer);
            requestBodyRef.set(charBuffer.toString());
        });
        // 获得请求体数据
        return requestBodyRef.get();
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE + 100;
    }

    public static String getIp(ServerHttpRequest request){
        return null;
    }
}
