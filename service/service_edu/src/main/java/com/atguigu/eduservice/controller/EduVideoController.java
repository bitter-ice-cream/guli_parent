package com.atguigu.eduservice.controller;


import com.atguigu.commonutils.R;
import com.atguigu.eduservice.entity.EduChapter;
import com.atguigu.eduservice.entity.EduCourse;
import com.atguigu.eduservice.entity.EduVideo;
import com.atguigu.eduservice.service.EduVideoService;
import com.sun.xml.bind.v2.TODO;
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
    EduVideoService eduVideoService;

    //添加小节
    @PostMapping("addVideo")
    public R addVideo(@RequestBody EduVideo eduVideo){
        eduVideoService.save(eduVideo);
        return R.ok();
    }

    //删除小节
    // TODO: 2022/10/23 后续完善，删除小节需要把视频删除
    @DeleteMapping("{videoId}")
    public R deleteVideo(@PathVariable String videoId){
        boolean b = eduVideoService.removeById(videoId);
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

