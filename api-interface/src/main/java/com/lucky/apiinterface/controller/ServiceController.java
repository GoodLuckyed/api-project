package com.lucky.apiinterface.controller;

import cn.hutool.json.JSONUtil;
import static com.lucky.apiinterface.utils.RequestUtils.get;
import com.lucky.apisdk.model.params.NameParams;
import com.lucky.apisdk.model.response.NameResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author lucky
 * @date 2024/4/25
 * @description 模拟接口控制器
 */

@RestController
@RequestMapping("/")
public class ServiceController {

    /**
     * 获取输入的名称
     */
    @GetMapping("/name")
    public NameResponse getName(NameParams nameParams){
        return JSONUtil.toBean(JSONUtil.toJsonStr(nameParams),NameResponse.class);
    }

    /**
     * 随机获取土味情话
     * @return
     */
    @GetMapping("/loveTalk")
    public String randomLoveTalk() {
        return get("https://api.vvhan.com/api/text/love");
    }

    /**
     * 随机获取文案
     * @return
     */
    @GetMapping("/copyWriting")
    public String getCopyWriting(){
        return get("https://api.vvhan.com/api/ian/rand");
    }
}
