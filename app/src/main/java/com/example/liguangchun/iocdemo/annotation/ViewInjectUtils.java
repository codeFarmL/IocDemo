package com.example.liguangchun.iocdemo.annotation;

import android.support.v7.app.AppCompatActivity;
import android.view.View;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by liguangchun on 2018/4/12.
 */

public class ViewInjectUtils {

    private static final String METHOD_SET_CONTENTVIEW = "setContentView";//本身Activity绑定布局的方法

    private static final String METHOD_FIND_VIEW_BY_ID = "findViewById";

    /**
     * 注入ContentV
     *
     * @param activity
     */
    private static void injectContentView(AppCompatActivity activity) {
        Class<? extends AppCompatActivity> mClass = activity.getClass();
        ContentView contentView = mClass.getAnnotation(ContentView.class);
        if (contentView != null) {
            int layoutId = contentView.value();
            try {
                Method method = mClass.getMethod(METHOD_SET_CONTENTVIEW, int.class); //int.class 是一个引用
                method.setAccessible(true);
                method.invoke(activity, layoutId);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * @param activity
     */
    private static void injectViews(AppCompatActivity activity) {
        Class<? extends AppCompatActivity> mClass = activity.getClass();
        //拿到所有变量
        Field[] mField = mClass.getDeclaredFields();
        for (Field field : mField) {
            ViewInject viewInject = field.getAnnotation(ViewInject.class);
            if (viewInject != null) {
                int id = viewInject.value();
                if (id != -1) {
                    try {
                        Method method = mClass.getMethod(METHOD_FIND_VIEW_BY_ID, int.class);
                        field.setAccessible(true);
                        Object view = method.invoke(activity, id);
                        field.set(activity, view);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public static void inject(AppCompatActivity activity) {

        injectContentView(activity);
        injectViews(activity);
        injectEvents(activity);

    }

    /**
     * 注入点击和长按事件
     * @param context
     */
    private static void injectEvents(AppCompatActivity context) {
        Class<?> clazz = context.getClass();
        Method[] methods = clazz.getDeclaredMethods();

        for (Method method : methods) {
            //获取方法上所有的注解
            Annotation[] annnotions = method.getAnnotations();
            for (Annotation annotation : annnotions) {

                //获取注解annotionType onClick OnLongClick
                Class<?> annotionType = annotation.annotationType();

                //获取注解的注解 onClick注解上面的EventBase
                EventBase eventBase = annotionType.getAnnotation(EventBase.class);
                if (eventBase == null) {
                    continue;
                }
                String listenerSetter = eventBase.listenerSetter();

                Class<?> listenerType = eventBase.listenerType();

                String callMethod = eventBase.callBackMethod();

                //方法名 与方法Method的对应关系
                Map<String, Method> methodMap = new HashMap<>();
                methodMap.put(callMethod, method);//eg:key:onClick(自己定义的)======value:MainActivity.onClick(Activity中的)

                try {
                    Method valueMethod = annotionType.getDeclaredMethod("value");
                    int[] viewIds = (int[]) valueMethod.invoke(annotation);

                    for (int viewId : viewIds) {
                        //通过反射拿到View
                        Method findViewById = clazz.getMethod("findViewById", int.class);
                        View view = (View) findViewById.invoke(context, viewId);
                        if (view == null) {
                            continue;
                        }

                        Method setOnClickListener = view.getClass().getMethod(listenerSetter, listenerType);

                        ListenerInvocationHandler handler = new ListenerInvocationHandler(context, methodMap);

                        //proxy 已经实现了listenerType接口
                        Object proxy = Proxy.newProxyInstance(
                                listenerType.getClassLoader(),
                                new Class[]{listenerType}, handler);
                        //接口里的方法有proxy来处理
                        setOnClickListener.invoke(view, proxy);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }

    }
}
