package com.github.bournecui.easyretry;

/**
 * Created by cuilei05 on 2018/7/23.
 */
@FunctionalInterface
public interface EasyRunnable extends EasyJob {
    void run();
}
