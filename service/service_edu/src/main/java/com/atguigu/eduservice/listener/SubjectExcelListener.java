package com.atguigu.eduservice.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.atguigu.eduservice.entity.EduSubject;
import com.atguigu.eduservice.entity.excel.SubjectData;
import com.atguigu.eduservice.service.EduSubjectService;
import com.atguigu.servicebase.exceptionhandler.CustomException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

/**
 * Excel监听器
 * @author lu
 */
public class SubjectExcelListener extends AnalysisEventListener<SubjectData> {

    //SubjectExcelListener不能交给spring进行管理，需要自己new，不能注入其他对象
    //不能实现数据库操作


    public EduSubjectService subjectService;
    public SubjectExcelListener(){}
    public SubjectExcelListener(EduSubjectService subjectService){
        this.subjectService = subjectService;
    }


    @Override
    public void invoke(SubjectData subjectData, AnalysisContext analysisContext) {
        if(subjectData == null){
            throw new CustomException(20001,"文件数据为空");
        }
        //一行一行读取，每次读取两个值，第一个值一级分类，第二个值二级分类
        //判断一级分类是否城府
        EduSubject existOneSubject = this.existOneSubject(subjectService, subjectData.getOneSubjectName());
        //没有相同的一级分类,进行添加
        if(existOneSubject == null){
            existOneSubject = new EduSubject();
            existOneSubject.setParentId("0");
            existOneSubject.setTitle(subjectData.getOneSubjectName());
            subjectService.save(existOneSubject);

        }
        //获取一级分类的id值，作为二级分类的parentid
        String pid = existOneSubject.getId();
        //添加二级分类,判断是否重复
        EduSubject existTwoSubject = this.existTwoSubject(subjectService, subjectData.getTwoSubjectName(), pid);
        if(existTwoSubject == null){
            existTwoSubject = new EduSubject();
            existTwoSubject.setParentId(pid);
            existTwoSubject.setTitle(subjectData.getTwoSubjectName());
            subjectService.save(existTwoSubject);

        }


    }

    /**
     * 判断一级分类不能重复添加
     * @param subjectService
     * @param name
     * @return
     */
    private EduSubject existOneSubject(EduSubjectService subjectService,String name) {
        QueryWrapper<EduSubject> wrapper = new QueryWrapper<>();
        wrapper.eq("title",name);
        wrapper.eq("parent_id","0");
        return subjectService.getOne(wrapper);
    }


    /**
     * 判断二级分类不能重复添加
     * @param subjectService
     * @param name
     * @return
     */
    private EduSubject existTwoSubject(EduSubjectService subjectService,String name,String pid) {
        QueryWrapper<EduSubject> wrapper = new QueryWrapper<>();
        wrapper.eq("title",name);
        wrapper.eq("parent_id",pid);
        return subjectService.getOne(wrapper);
    }


    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }
}
