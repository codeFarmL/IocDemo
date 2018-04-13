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
@EventBase(listenerSetter = "setOnLongClickListener",
        listenerType = View.OnLongClickListener.class,callBackMethod = "onLongClick")
public @interface OnLongClick {
    int[] value() default -1;
}