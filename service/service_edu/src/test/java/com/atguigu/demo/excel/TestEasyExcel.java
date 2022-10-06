package com.atguigu.demo.excel;

import com.alibaba.excel.EasyExcel;

import java.util.ArrayList;
import java.util.List;

public class TestEasyExcel {

    public static void main(String[] args) {
        //实现Excel写的操作
        //1设置写入文件夹地址和Excel文件名称
//        String fileName = "D:\\write.xlsx";
//
//        //2调用easyExcel里面的方法实现写操作
//        EasyExcel.write(fileName,DemoData.class).sheet("学生列表").doWrite(getData());


        //实现EasyExcel的读操作
        String fileName = "D:\\write.xlsx";
        EasyExcel.read(fileName,DemoData.class,new ExcelListener()).sheet().doRead();

    }

    private static List<DemoData> getData(){
        List<DemoData> list = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            DemoData data = new DemoData();
            data.setSno(i);
            data.setSname("kkk"+i);
            list.add(data);
        }

        return list;
    }


}
