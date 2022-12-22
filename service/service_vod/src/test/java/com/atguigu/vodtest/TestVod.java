package com.atguigu.vodtest;

import com.aliyun.vod.upload.impl.UploadVideoImpl;
import com.aliyun.vod.upload.req.UploadVideoRequest;
import com.aliyun.vod.upload.resp.UploadVideoResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.vod.model.v20170321.GetPlayInfoRequest;
import com.aliyuncs.vod.model.v20170321.GetPlayInfoResponse;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthRequest;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthResponse;

import java.util.List;

public class TestVod {
    public static void main(String[] args) throws ClientException {
        String videoId = "20447f268db8498b97344a47430af1b9";
        String keyId = "LTAI5tL7tbe6SC8n2pNXuZiy";
        String keySecret = "a6OyzyAHeFygwE1EN6uvVVRf82ctxo";
        String title = "testUpload";
        String fileName = "C:/Users/lu/Desktop/尚硅谷资料/在线教育--谷粒学苑/项目资料/1-阿里云上传测试视频/6 - What If I Want to Move Faster.mp4";
        getPlayAuth(videoId,keyId,keySecret);
        getPlayUrl(videoId,keyId,keySecret);

        testUploadVideo(keyId, keySecret,title,fileName);
    }
    //根据视频id获取播放凭证
    public static void getPlayAuth(String videoId,String keyId,String keySecret) throws ClientException {
        //1根据视频id获取地址
        //创建初始化对象
        DefaultAcsClient client = InitObject.initVodClient(keyId, keySecret);
        //2创建获取视频凭证的requests和response
        GetVideoPlayAuthRequest request = new GetVideoPlayAuthRequest();
        GetVideoPlayAuthResponse response = new GetVideoPlayAuthResponse();
        request.setVideoId(videoId);
        GetVideoPlayAuthResponse acsResponse = client.getAcsResponse(request);

        System.out.println("playAuth:"+acsResponse.getPlayAuth());
    }


    //根据视频id获取视频播放地址
    public static void getPlayUrl(String videoId, String keyId,String keySecret)throws ClientException {
        //1根据视频id获取地址
        //创建初始化对象
        DefaultAcsClient client = InitObject.initVodClient(keyId, keySecret);

        //2创建获取视频的requests和response
        GetPlayInfoRequest request = new GetPlayInfoRequest();
        GetPlayInfoResponse response = new GetPlayInfoResponse();

        //3向request对象中设置视频id
        request.setVideoId(videoId);

        //4调用初始化对象中的方法，传递request，获取数据
        GetPlayInfoResponse acsResponse = client.getAcsResponse(request);

        List<GetPlayInfoResponse.PlayInfo> playInfoList = acsResponse.getPlayInfoList();
        //播放地址
        for (GetPlayInfoResponse.PlayInfo playInfo : playInfoList) {
            System.out.print("PlayInfo.PlayURL = " + playInfo.getPlayURL() + "\n");
        }
        //Base信息
        System.out.print("VideoBase.Title = " + acsResponse.getVideoBase().getTitle() + "\n");
    }

    /**
     * 本地文件上传
     * @param title 上传后的文件名
     * @param fileName 本地文件的路径和名称
     */
    private static void testUploadVideo(String accessKeyId, String accessKeySecret, String title, String fileName) {
        UploadVideoRequest request = new UploadVideoRequest(accessKeyId, accessKeySecret, title, fileName);
        /* 可指定分片上传时每个分片的大小，默认为2M字节 */
        request.setPartSize(2 * 1024 * 1024L);
        /* 可指定分片上传时的并发线程数，默认为1，(注：该配置会占用服务器CPU资源，需根据服务器情况指定）*/
        request.setTaskNum(1);

        UploadVideoImpl uploader = new UploadVideoImpl();
        UploadVideoResponse response = uploader.uploadVideo(request);
        System.out.print("RequestId=" + response.getRequestId() + "\n");  //请求视频点播服务的请求ID
        if (response.isSuccess()) {
            System.out.print("VideoId=" + response.getVideoId() + "\n");
        } else {
            /* 如果设置回调URL无效，不影响视频上传，可以返回VideoId同时会返回错误码。其他情况上传失败时，VideoId为空，此时需要根据返回错误码分析具体错误原因 */
            System.out.print("VideoId=" + response.getVideoId() + "\n");
            System.out.print("ErrorCode=" + response.getCode() + "\n");
            System.out.print("ErrorMessage=" + response.getMessage() + "\n");
        }
    }
}
