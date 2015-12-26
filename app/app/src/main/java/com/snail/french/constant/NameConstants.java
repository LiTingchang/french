package com.snail.french.constant;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by litingchang on 15-12-26.
 */
public class NameConstants {

    private static Map<String, String> nameMap;

    static {
        nameMap = new HashMap<>();
        nameMap.put("G", "语法");
        nameMap.put("L", "听力");
        nameMap.put("R", "阅读");
        nameMap.put("C", "作文");
        nameMap.put("W", "词汇");
        nameMap.put("1", "图片题");
        nameMap.put("2", "电话留言题");
        nameMap.put("3", "公共场所题");
        nameMap.put("4", "广播题");
        nameMap.put("5", "意见调查题");
        nameMap.put("6", "长听力");
        nameMap.put("7", "辨音题");
        nameMap.put("9", "普通语法题");
        nameMap.put("10", "改错题");
        nameMap.put("11", "短阅读");
        nameMap.put("12", "长阅读");
        nameMap.put("13", "完形填空");
        nameMap.put("14", "排序题");
        nameMap.put("15", "同义句");
        nameMap.put("16", "图表题");
        nameMap.put("17", "交际词汇题");
        nameMap.put("18", "近义词题");
        nameMap.put("40", "听写");
        nameMap.put("41", "短听力");
        nameMap.put("42", "长听力");
        nameMap.put("43", "近义词");
        nameMap.put("44", "反义词");
        nameMap.put("45", "语法选择题");
        nameMap.put("46", "完形填空");
        nameMap.put("47", "时态填空");
        nameMap.put("50", "阅读");
        nameMap.put("60", "作文");
    }

    public static String getName(String key) {
        if(nameMap.containsKey(key)) {
            return nameMap.get(key);
        }
        return key;
    }
}
