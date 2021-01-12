package com.lucheng.mydubbo_3.spi.annotation;

import java.lang.annotation.*;

/**
 * spi注解
 * 标示该接口是一个扩展类接口
 * @author lucheng28
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface SPI {

    //默认扩展点的全限定名
    String value() default "";
}
