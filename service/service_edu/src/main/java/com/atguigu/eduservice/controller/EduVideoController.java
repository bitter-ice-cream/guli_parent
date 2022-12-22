package com.atguigu.eduservice.controller;


import com.atguigu.commonutils.R;
import com.atguigu.eduservice.client.VodClient;
import com.atguigu.eduservice.entity.EduChapter;
import com.atguigu.eduservice.entity.EduCourse;
import com.atguigu.eduservice.entity.EduVideo;
import com.atguigu.eduservice.service.EduVideoService;
import com.atguigu.servicebase.exceptionhandler.CustomException;
import com.sun.xml.bind.v2.TODO;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * <p>
 * 课程视频 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2022-10-06
 */
@RestController
@RequestMapping("/eduService/eduVideo")
@CrossOrigin
public class EduVideoController {

    @Resource
    private EduVideoService eduVideoService;

    @Resource
    private VodClient vodClient;

    //添加小节
    @PostMapping("addVideo")
    public R addVideo(@RequestBody EduVideo eduVideo){
        eduVideoService.save(eduVideo);
        return R.ok();
    }

    //删除小节
    @DeleteMapping("{videoId}")
    public R deleteVideo(@PathVariable String videoId){
        //根据小节id获取视频id，调用方法实现视频删除
        EduVideo video = eduVideoService.getById(videoId);
        //根据视频id实现删除
        if (!StringUtils.isEmpty(video.getVideoSourceId())){
            R result = vodClient.removeAlyVideo(video.getVideoSourceId());
            if (result.getCode()==20001){
                throw new CustomException(20001,"删除视频失败，熔断器");
            }

        }
        eduVideoService.removeById(videoId);

        return R.ok();
    }

    //根据小节id查询
    @GetMapping("getVideoInfo/{videoId}")
    public R getVideoInfo(@PathVariable String videoId){
        EduVideo eduVideo = eduVideoService.getById(videoId);
        return R.ok().data("video",eduVideo);
    }

    //修改小节
    @PostMapping ("updateVideo")
    public R updateVideo(@RequestBody EduVideo video){
        eduVideoService.updateById(video);
        return R.ok();
    }

}

