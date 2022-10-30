package com.spring.customer.condition;


import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.util.ClassUtils;

import java.io.IOException;
import java.util.Properties;

@Slf4j
public class PropertiesOnClass implements Condition {

    @Override
    public boolean matches(ConditionContext conditionContext, AnnotatedTypeMetadata annotatedTypeMetadata) {
        //所有的标注的@Component注解的类都会走这个实现,只有判断是自己定义的注解的时候才需要特殊处理
        if (annotatedTypeMetadata.isAnnotated(PropertiesAnnotation.class.getName())) {
            //获得PropertiesAnnotation.class注解上的所有信息,就是key，value格式的信息,key是注解的属性,value是注解属性的值
            AnnotationAttributes annotationAttributes = AnnotationAttributes.fromMap(annotatedTypeMetadata.getAnnotationAttributes(PropertiesAnnotation.class.getName()));
            try {
                Properties properties = PropertiesLoaderUtils.loadAllProperties("application.properties", ClassUtils.getDefaultClassLoader());
                //获得PropertiesAnnotation这个注解,属性为name的所有值
                String[] values = annotationAttributes.getStringArray("name");
                for (String val : values) {
                    if (properties.getProperty(val).equals("true")) {
                        return true;
                    }
                }
            } catch (IOException e) {
                log.error("加载文件异常", e);
            }
        }
        return false;
    }


}
