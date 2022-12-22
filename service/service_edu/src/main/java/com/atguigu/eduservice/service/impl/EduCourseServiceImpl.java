package com.atguigu.eduservice.service.impl;

import com.atguigu.eduservice.entity.EduChapter;
import com.atguigu.eduservice.entity.EduCourse;
import com.atguigu.eduservice.entity.EduCourseDescription;
import com.atguigu.eduservice.entity.EduTeacher;
import com.atguigu.eduservice.entity.vo.CourseInfoVo;
import com.atguigu.eduservice.entity.vo.CoursePublishVo;
import com.atguigu.eduservice.entity.vo.CourseQuery;
import com.atguigu.eduservice.mapper.EduCourseDescriptionMapper;
import com.atguigu.eduservice.mapper.EduCourseMapper;
import com.atguigu.eduservice.service.EduChapterService;
import com.atguigu.eduservice.service.EduCourseService;
import com.atguigu.eduservice.service.EduVideoService;
import com.atguigu.servicebase.exceptionhandler.CustomException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
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
public class EduCourseServiceImpl extends ServiceImpl<EduCourseMapper, EduCourse> implements EduCourseService {
    @Resource
    EduCourseMapper courseMapper;

    @Resource
    EduCourseDescriptionMapper descriptionMapper;

    @Resource
    EduVideoService eduVideoService;

    @Resource
    EduChapterService eduChapterService;

    @Override
    public String saveCourseInfo(CourseInfoVo courseInfoVo) {
        //1 向课程表添加课程基本信息
        EduCourse eduCourse = new EduCourse();
        eduCourse.setIsDeleted(0);
        BeanUtils.copyProperties(courseInfoVo,eduCourse);
        int insert = courseMapper.insert(eduCourse);
        if (insert<=0){
            throw new CustomException(20001,"课程信息添加失败");
        }

        //2 向课程简介表添加信息
        EduCourseDescription eduCourseDescription = new EduCourseDescription();
        BeanUtils.copyProperties(courseInfoVo,eduCourseDescription);
        eduCourseDescription.setId(eduCourse.getId());
        descriptionMapper.insert(eduCourseDescription);

        return eduCourse.getId();
    }

    @Override
    public CourseInfoVo getCourseInfo(String courseId) {

        //1查询课程表
        EduCourse eduCourse = courseMapper.selectById(courseId);
        CourseInfoVo courseInfoVo = new CourseInfoVo();
        BeanUtils.copyProperties(eduCourse,courseInfoVo);
        //2查询描述表
        EduCourseDescription eduCourseDescription = descriptionMapper.selectById(courseId);
        courseInfoVo.setDescription(eduCourseDescription.getDescription());

        return courseInfoVo;
    }

    @Override
    public void updateCourseInfo(CourseInfoVo courseInfoVo) {
        //1修改课程表
        EduCourse eduCourse = new EduCourse();
        BeanUtils.copyProperties(courseInfoVo,eduCourse);
        int update = courseMapper.updateById(eduCourse);
        if(update == 0){
            throw new CustomException(20001,"修改课程信息失败");
        }
        //2修改
        EduCourseDescription eduCourseDescription = new EduCourseDescription();
        BeanUtils.copyProperties(courseInfoVo,eduCourseDescription);
        descriptionMapper.updateById(eduCourseDescription);

    }

    @Override
    public CoursePublishVo publishCourseInfo(String id) {
        CoursePublishVo publishCourseInfo = courseMapper.getPublishCourseInfo(id);
        return publishCourseInfo;
    }

    @Override
    public void coursePageQuery(Page<EduCourse> eduCoursePage, CourseQuery courseQuery) {
        //构建条件
        QueryWrapper<EduCourse> queryWrapper = new QueryWrapper<>();
        String title = courseQuery.getTitle();
        String status = courseQuery.getStatus();

        if (!StringUtils.isEmpty(title)) {
            queryWrapper.like("title", title);
        }

        if (!StringUtils.isEmpty(status)) {
            queryWrapper.eq("status", status);
        }

        //排序
        queryWrapper.orderByDesc("gmt_create");
        courseMapper.selectPage(eduCoursePage,queryWrapper);
    }

    //删除课程
    @Override
    public void removeCourse(String courseId) {
        //根据课程id删除小节
        eduVideoService.deleteByCourseId(courseId);

        //根据课程id删除章节
        eduChapterService.deleteByCourseId(courseId);

        //根据课程id删除描述
        descriptionMapper.deleteById(courseId);

        //根据课程id删除课程本身
        int result = courseMapper.deleteById(courseId);

        if (result==0){//失败返回
            throw new CustomException(20001,"删除失败");
        }
    }


}
