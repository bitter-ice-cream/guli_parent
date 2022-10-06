package com.atguigu.oss.service;

import com.atguigu.commonutils.R;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author lu
 */
public interface OssService {

    /**
     * 上传的文件
     * @param file
     * @return 上传的阿里云url文件地址
     */
    String uploadFileAvatar(MultipartFile file);


}
