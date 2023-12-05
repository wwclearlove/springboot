package com.glasssix.dubbo.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.google.common.collect.Lists;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * @program: java-nwpu
 * @description:
 * @author: wenjiang
 * @create: 2020-04-26 13:56
 **/
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {

    public static List<String> excludeClasses = Lists.newArrayList();

    @Override
    public void insertFill(MetaObject metaObject) {

        Date date = new Date();
        if (metaObject.hasGetter("createdTime")) {
            setFieldValByName("createdTime", date, metaObject);
        }
        if (metaObject.hasGetter("modifyTime")) {
            setFieldValByName("modifyTime", date, metaObject);
        }
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        boolean exclude = isExclude(metaObject);
        if (exclude) {
            return;
        }
        Date date = new Date();
        if (metaObject.hasGetter("modifyTime")) {
            setFieldValByName("modifyTime", date, metaObject);
        }
    }

    public boolean isExclude(MetaObject metaObject) {
        if (excludeClasses != null && excludeClasses.size() > 0) {
            Object originalObject = metaObject.getOriginalObject();
            for (String cls : excludeClasses) {
                try {
                    Class<?> clazz = Class.forName(cls);
                    if (originalObject.getClass().isInstance(clazz.newInstance())) {
                        return true;
                    }
                } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

}
