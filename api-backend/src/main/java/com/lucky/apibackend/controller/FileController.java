package com.lucky.apibackend.controller;

import cn.hutool.core.io.FileUtil;
import com.lucky.apibackend.config.CosClientConfig;
import com.lucky.apibackend.model.enums.FileUploadBizEnum;
import com.lucky.apibackend.model.enums.ImageStatusEnum;
import com.lucky.apibackend.model.file.UploadFileRequest;
import com.lucky.apibackend.model.vo.ImageVo;
import com.lucky.apibackend.model.vo.UserVo;
import com.lucky.apibackend.service.UserService;
import com.lucky.apicommon.common.BaseResponse;
import com.lucky.apicommon.common.ErrorCode;
import com.lucky.apicommon.common.ResultUtils;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.model.PutObjectRequest;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

/**
 * @author lucky
 * @date 2024/7/4
 * @description 文件接口
 */

@RestController
@RequestMapping("/file")
@Slf4j
public class FileController {

    // 图片大小2M
    private static final long TWO_M = 2 * 1024 * 1024L;

    @Resource
    private UserService userService;
    @Resource
    private CosClientConfig cosClientConfig;
    @Resource
    private COSClient cosClient;

    /**
     * 上传文件
     * @param multipartFile
     * @param uploadFileRequest
     * @param request
     * @return
     */
    @ApiOperation(value = "上传文件")
    @PostMapping(value = "/upload")
    public BaseResponse<ImageVo> uploadFile(@RequestPart("file") MultipartFile multipartFile,
                                            UploadFileRequest uploadFileRequest, HttpServletRequest request) {
        // 获取业务类型
        String biz = uploadFileRequest.getBiz();
        FileUploadBizEnum fileUploadBizEnum = FileUploadBizEnum.getEnumByValue(biz);
        ImageVo imageVo = new ImageVo();
        if (fileUploadBizEnum == null){
            return uploadError(imageVo,multipartFile,"上传失败,请重试.");
        }
        String result = validFile(multipartFile,fileUploadBizEnum);
        if (!"success".equals(result)){
            return uploadError(imageVo,multipartFile,result);
        }
        // 获取当前登录用户
        UserVo loginUser = userService.getLoginUser(request);
        // 上传到cos的文件目录：根据业务、用户来划分 例如：user_avatar/用户id/xxx.jpg
        String uuid = RandomStringUtils.randomAlphanumeric(8);
        String filename = uuid + "-" + multipartFile.getOriginalFilename();
        String filepath = String.format("/%s/%s/%s", fileUploadBizEnum.getValue(), loginUser.getId(), filename);
        File file = null;
        try {
            file = File.createTempFile(filepath,null);
            multipartFile.transferTo(file);
            PutObjectRequest putObjectRequest = new PutObjectRequest(cosClientConfig.getBucket(), filepath, file);
            cosClient.putObject(putObjectRequest);
            // 拼接上传后返回的文件路径
            String url = "https://" + cosClientConfig.getBucket() + ".cos." + cosClientConfig.getRegion() + ".myqcloud.com" + filepath;
            imageVo.setUid(RandomStringUtils.randomAlphanumeric(8));
            imageVo.setName(multipartFile.getOriginalFilename());
            imageVo.setStatus(ImageStatusEnum.SUCCESS.getValue());
            imageVo.setUrl(url);
            return ResultUtils.success(imageVo);
        } catch (IOException e) {
            log.error("file upload error, filepath = " + filepath, e);
            return uploadError(imageVo,multipartFile,"上传失败,请重试.");
        } finally {
            if (file != null){
                // 删除临时文件
                boolean delete = file.delete();
                if (!delete){
                    log.error("file delete error, filepath = {}",filepath);
                }
            }
        }
    }

    /**
     * 校验文件
     * @param multipartFile
     * @param fileUploadBizEnum
     * @return
     */
    private String validFile(MultipartFile multipartFile, FileUploadBizEnum fileUploadBizEnum) {
        // 获取文件大小
        long fileSize = multipartFile.getSize();
        // 获取文件后缀
        String fileSuffix = FileUtil.getSuffix(multipartFile.getOriginalFilename());
        if (FileUploadBizEnum.USER_AVATAR.equals(fileUploadBizEnum)){
            if (fileSize > TWO_M) {
                return "上传失败,文件大小不能超过2M";
            }
            if (!Arrays.asList("jpg", "jpeg", "png","webp","svg","jiff").contains(fileSuffix)){
                return "上传失败,文件格式不正确";
            }
        }
        if (FileUploadBizEnum.INTERFACE_AVATAR.equals(fileUploadBizEnum)){
            if (fileSize > TWO_M) {
                return "上传失败,文件大小不能超过2M";
            }
            if (!Arrays.asList("jpg", "jpeg", "png","webp","svg","jiff").contains(fileSuffix)){
                return "上传失败,文件格式不正确";
            }
        }
        return "success";
    }

    /**
     * 上传错误
     * @param imageVo
     * @param multipartFile
     * @param errorMessage
     * @return
     */
    private BaseResponse<ImageVo> uploadError(ImageVo imageVo, MultipartFile multipartFile, String errorMessage) {
        imageVo.setUid(RandomStringUtils.randomAlphanumeric(8));
        imageVo.setName(multipartFile.getOriginalFilename());
        imageVo.setStatus(ImageStatusEnum.ERROR.getValue());
        return ResultUtils.error(imageVo, ErrorCode.OPERATION_ERROR,errorMessage);
    }
}
