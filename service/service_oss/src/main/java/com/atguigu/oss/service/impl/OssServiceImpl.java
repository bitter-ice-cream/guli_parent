package com.atguigu.oss.service.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.atguigu.oss.service.OssService;
import com.atguigu.oss.utils.ConstantPropertiesUtils;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

/**
 * @author lu
 */
@Service
public class OssServiceImpl implements OssService {
    @Override
    public String uploadFileAvatar(MultipartFile file) {
        String endpoint = ConstantPropertiesUtils.END_POINT;
        String accessKeyId = ConstantPropertiesUtils.ACCESS_KEY_ID;
        String accessKeySecret = ConstantPropertiesUtils.ACCESS_KEY_SECRET;
        String bucketName = ConstantPropertiesUtils.BUCKET_NAME;

        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        try {
            //获取文件名称
            String objectName = file.getOriginalFilename();

            //在文件名称中添加上随机的值
            String uuid = UUID.randomUUID().toString().replaceAll("-","");
            objectName = uuid + objectName;

            InputStream inputStream = file.getInputStream();
            // 调用oss实现上传
            //把文件按照日期进行分类 2019/05/12/01.jpg
            String datePath = new DateTime().toString("yyyy/MM/dd");
            objectName = datePath + "/" + objectName;

            ossClient.putObject(bucketName, objectName, inputStream);
            ossClient.shutdown();
            //需要把上传到阿里云oss的路径手动拼接出来
            String url = "https://" + bucketName + "." + endpoint + "/" + objectName;
            return url;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }


    }
}
