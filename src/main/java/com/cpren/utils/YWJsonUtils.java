package com.cpren.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * json工具类
 *
 * @author cdxu@iyunwen.com on 2018/9/28
 */
@Component
public class YWJsonUtils {

    private static ObjectMapper mapper;

    /**
     * 禁止实例化
     */
    private YWJsonUtils() {

    }

    public static ObjectMapper getMapper() {
        return mapper;
    }

    @Autowired
    public void setMapper(ObjectMapper mapper) {
        YWJsonUtils.mapper = mapper;
    }
}
