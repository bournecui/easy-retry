package com.github.bournecui.easyretry;

/**
 * To test if the result is expected.
 * If not, the job will be retied.
 * Created by cuilei05 on 2018/7/23.
 */
public interface ResultPredicate<T> {

    /**
     * To test if the result is expected.
     *
     * @param result The result of the job.
     *
     * @return If the result is expected.
     */
    boolean test(T result);
}
