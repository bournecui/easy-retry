package com.github.bournecui.easyretry;

/**
 * Created by cuilei05 on 2018/7/23.
 */
@FunctionalInterface
public interface EasyCallable<T> extends EasyJob {
    T call();
}
