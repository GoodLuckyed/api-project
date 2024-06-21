package com.lucky.apigateway.exception;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lucky.apicommon.common.BaseResponse;
import com.lucky.apicommon.common.ResultUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebExceptionHandler;
import reactor.core.publisher.Mono;

/**
 * @author lucky
 * @date 2024/6/18
 * @description 网关层全局异常处理
 */
@Slf4j
@Order(-1)
@Configuration
public class GlobalExceptionHandler implements WebExceptionHandler {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        ServerHttpResponse response = exchange.getResponse();
        //对于已经committed(提交)的response，就不能再使用这个response向缓冲区写任何东西
        if (response.isCommitted()) {
            return Mono.error(ex);
        }

        // header set 设置返回值类型为json
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        // 设置响应码
        if (ex instanceof ResponseStatusException) {
            response.setStatusCode(((ResponseStatusException) ex).getStatus());
        }

        return response
                .writeWith(Mono.fromSupplier(() -> {
                    DataBufferFactory bufferFactory = response.bufferFactory();
                    try {
                        BaseResponse<String> error = ResultUtils.error(HttpStatus.FORBIDDEN.value(), ex.getMessage());
                        log.info("组装错误代码：{}", JSON.toJSONString(error));
                        return bufferFactory.wrap(objectMapper.writeValueAsBytes(error));
                    } catch (Exception e) {
                        log.warn("Error writing response", ex);
                        return bufferFactory.wrap(new byte[0]);
                    }
                }));
    }
}
