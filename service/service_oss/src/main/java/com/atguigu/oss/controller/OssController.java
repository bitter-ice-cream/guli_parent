package com.atguigu.oss.controller;

import com.atguigu.commonutils.R;
import com.atguigu.oss.service.OssService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

/**
 * @author lu
 */
@RestController
@CrossOrigin
@RequestMapping("/eduOss/fileOss")
public class OssController {

    @Resource
    private OssService ossService;

    /**
     * 上传头像的方法
     * @param file
     * @return
     */
    @PostMapping("/upload")
    public R uploadOssFile(@RequestBody MultipartFile file){
        //获取到上传的文件 MultipartFile
        String url = ossService.uploadFileAvatar(file);

        //返回上传到的oss路径
        return R.ok().data("url",url);
    }


}
