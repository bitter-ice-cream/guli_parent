package com.atguigu.eduservice.service;

import com.atguigu.eduservice.entity.EduSubject;
import com.atguigu.eduservice.entity.subject.OneSubject;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 * 课程科目 服务类
 * </p>
 *
 * @author testjava
 * @since 2022-09-13
 */
public interface EduSubjectService extends IService<EduSubject> {

    /**
     * 添加课程分类
     * @param file
     * @param subjectService
     */
    void saveSubject(MultipartFile file,EduSubjectService subjectService);

    /**
     * 课程分类列表 树形
     * @return 一级分类列表
     */
    List<OneSubject> getAllOneTwoSubject();
}
