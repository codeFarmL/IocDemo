package com.example.liguangchun.iocdemo.annotation;

import android.view.View;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by liguangchun on 2018/4/12.
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@EventBase(listenerSetter = "setOnClickListener",
        listenerType = View.OnClickListener.class,
        callBackMethod = "onClick")
public @interface OnClick
{
    /**
     * 进行设置点击事件
     * @return
     */
    int[] value();
}
