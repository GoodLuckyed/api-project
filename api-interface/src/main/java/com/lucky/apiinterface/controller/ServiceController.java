package com.lucky.apiinterface.controller;

import cn.hutool.json.JSONUtil;
import com.lucky.apisdk.exception.ApiException;
import com.lucky.apisdk.model.params.NameParams;
import com.lucky.apisdk.model.params.RandomWallpaperParams;
import com.lucky.apisdk.model.response.NameResponse;
import com.lucky.apisdk.model.response.RandomWallpaperResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.lucky.apiinterface.utils.RequestUtils.buildUrl;
import static com.lucky.apiinterface.utils.RequestUtils.get;

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
     * 随机获取毒鸡汤
     * @return
     */
    @GetMapping("/poisonousChickenSoup")
    public String getPoisonousChickenSoup() {
        return get("https://api.btstu.cn/yan/api.php?charset=utf-8&encode=json");
    }

    /**
     * 随机获取壁纸
     * @param randomWallpaperParams
     * @return
     */
    @GetMapping("/randomWallpaper")
    public RandomWallpaperResponse getRandomWallpaper(RandomWallpaperParams randomWallpaperParams) throws ApiException {
        String baseUrl = "https://api.btstu.cn/sjbz/api.php";
        String url = buildUrl(baseUrl,randomWallpaperParams);
        if (StringUtils.isAllBlank(randomWallpaperParams.getLx(),randomWallpaperParams.getMethod())){
            url = url + "?format=json";
        }else {
            url = url + "?format=json";
        }
        return JSONUtil.toBean(get(url),RandomWallpaperResponse.class);
    }

    /**
     * 随机获取笑话
     * @return
     */
    @GetMapping("/joke")
    public String getJoke(){
        return get("https://api.vvhan.com/api/text/joke");
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
