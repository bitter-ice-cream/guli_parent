package com.atguigu.eduservice.service.impl;

import com.atguigu.eduservice.entity.EduChapter;
import com.atguigu.eduservice.entity.EduVideo;
import com.atguigu.eduservice.entity.chapter.ChapterVo;
import com.atguigu.eduservice.entity.chapter.VideoVo;
import com.atguigu.eduservice.mapper.EduChapterMapper;
import com.atguigu.eduservice.mapper.EduVideoMapper;
import com.atguigu.eduservice.service.EduChapterService;
import com.atguigu.servicebase.exceptionhandler.CustomException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2022-10-06
 */
@Service
public class EduChapterServiceImpl extends ServiceImpl<EduChapterMapper, EduChapter> implements EduChapterService {
    @Resource
    EduChapterMapper chapterMapper;

    @Resource
    EduVideoMapper eduVideoMapper;


    //课程大纲列表，根据课程id进行查询
    @Override
    public List<ChapterVo> getChapterVideoByCourseId(String courseId) {
        //1根据课程id查询课程里面的所有章节
        QueryWrapper<EduChapter> chapterQueryWrapper = new QueryWrapper<>();
        chapterQueryWrapper.eq("course_id",courseId);
        List<EduChapter> eduChapters = chapterMapper.selectList(chapterQueryWrapper);

        //2根据课程id查询课程里面所有的小节
        QueryWrapper<EduVideo> videoQueryWrapper = new QueryWrapper<>();
        videoQueryWrapper.eq("course_id",courseId);
        List<EduVideo> eduVideos = eduVideoMapper.selectList(videoQueryWrapper);

        //3遍历查询list集合进行封装
        List<ChapterVo> finalList = new ArrayList<>();
        for (int i = 0; i < eduChapters.size(); i++) {
            ChapterVo chapterVo = new ChapterVo();
            BeanUtils.copyProperties(eduChapters.get(i),chapterVo);
            //4遍历查询小节list进行封装
            List<VideoVo> children = new ArrayList<>();
            for (int j = 0; j < eduVideos.size(); j++) {

                if (eduVideos.get(j).getChapterId().equals(chapterVo.getId())){
                    VideoVo video = new VideoVo();
                    BeanUtils.copyProperties(eduVideos.get(j),video);
                    children.add(video);
                }
            }
            chapterVo.setChildren(children);
            finalList.add(chapterVo);
        }

        return finalList;
    }

    //删除章节的方法
    @Override
    public boolean deleteChapter(String chapterId) {
        QueryWrapper<EduVideo> wrapper = new QueryWrapper<>();
        wrapper.eq("chapter_id",chapterId);

        int count = eduVideoMapper.selectCount(wrapper);
        if (count > 0) {//查询出小节，不进行删除
            throw new CustomException(20001,"存在小节,不能删除");
        }else {
            //删除小节
            int result = chapterMapper.deleteById(chapterId);
            return result>0;

        }


    }

    @Override
    public void deleteByCourseId(String courseId) {
        QueryWrapper<EduChapter> wrapper=new QueryWrapper<>();
        wrapper.eq("course_id",courseId);
        baseMapper.delete(wrapper);

    }
}
