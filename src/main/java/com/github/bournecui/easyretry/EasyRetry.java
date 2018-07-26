package com.github.bournecui.easyretry;

import java.util.List;

import com.github.bournecui.easyretry.util.CollectionUtils;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by cuilei05 on 2018/7/20.
 */
@Getter
@Slf4j
public class EasyRetry {

    private List<Class<? extends Throwable>> includeExceptions;
    private List<Class<? extends Throwable>> excludeExceptions;

    private Integer maxAttempts;
    private Long maxWaitTime;
    private Long backoff;

    protected EasyRetry() {

    }

    public void run(EasyRunnable easyRunnable) {
        executeUninterruptable(easyRunnable, null);
    }

    public <T> T call(EasyCallable<T> easyCallable) {
        return (T) executeUninterruptable(easyCallable, null);
    }

    public <T> T call(EasyCallable<T> easyCallable, ResultPredicate<T> resultPredicate) {
        return (T) executeUninterruptable(easyCallable, resultPredicate);
    }

    public void runInterruptable(EasyRunnable easyRunnable) throws InterruptedException {
        execute(easyRunnable, null);
    }

    public <T> T callInterruptable(EasyCallable<T> easyCallable) throws InterruptedException {
        return (T) execute(easyCallable, null);
    }

    public <T> T callInterruptable(EasyCallable<T> easyCallable, ResultPredicate<T> resultPredicate)
            throws InterruptedException {
        return (T) execute(easyCallable, resultPredicate);
    }

    private Object executeUninterruptable(EasyJob easyJob, ResultPredicate resultPredicate) {
        try {
            return execute(easyJob, resultPredicate);
        } catch (InterruptedException e) {
            log.warn("Ingore InterruptedException");
        }
        return null;
    }

    private Object execute(EasyJob easyJob, ResultPredicate resultPredicate)
            throws InterruptedException {
        long start = System.currentTimeMillis();
        Object result = null;
        int attempt = 0;
        while (++attempt <= maxAttempts) {
            log.debug("attempt:{}", attempt);
            try {
                result = doExecute(easyJob);
                /*
                 *  For the result, under these two circumstances retry will not be continued.
                 *      1. There's any ResultPredicate set, thus you didn't care the result.
                 *      2. The ResultPredicate.test() methond return false.
                 */

                if (easyJob instanceof EasyRunnable || resultPredicate == null) {
                    log.debug("Job finished after {}/{} attempts.", attempt, maxAttempts);
                    return result;
                }

                if (resultPredicate.test(result)) {
                    log.debug("Job finished after {}/{} attempts.", attempt, maxAttempts);
                    return result;
                }

                log.debug("ResultPredicate return false, retry will be continued! ");
            } catch (Throwable e) {
                String message = String.format("Exception occurs during the %d/%d attempt.", attempt, maxAttempts);

                /*
                 *  Under these two circumstances the exception will be thrown:
                 *      1. Max-attempts is reached.
                 *      2. The exception is set to ignore.
                 */
                if (attempt == maxAttempts || !retryOnException(e)) {
                    log.error(message, e);
                    throw e;
                }
                log.warn(message, e);
            }

            long end = System.currentTimeMillis();

            if (maxWaitTime != null && maxWaitTime > 0 && (end - start) >= maxWaitTime) {
                throw new TimeoutException(
                        String.format("It has reach the max wait time after %d/%d attemps, retry will not"
                                + " be continued!", attempt, maxAttempts));
            }

            if (backoff != null && backoff > 0) {
                log.debug("Sleep, backoff:{}", backoff);
                Thread.sleep(this.backoff);
            }
        }
        return result;
    }

    private Object doExecute(EasyJob easyJob) {
        if (easyJob instanceof EasyCallable) {
            return ((EasyCallable) easyJob).call();
        } else if (easyJob instanceof EasyRunnable) {
            ((EasyRunnable) easyJob).run();
        }
        return null;
    }

    private boolean retryOnException(Throwable e) {

        boolean includeFilter = CollectionUtils.isNotEmpty(includeExceptions);
        boolean excludeFilter = CollectionUtils.isNotEmpty(excludeExceptions);

        if (!includeFilter && !excludeFilter) {
            return true;
        }

        if (includeFilter && !excludeFilter) {
            for (Class<? extends Throwable> includeException : includeExceptions) {
                if (includeException.isInstance(e)) {
                    log.debug("Parent exception {} is included, retry on exception {}", includeException, e.getClass());
                    return true;
                }
            }
            return false;
        }

        if (!includeFilter && excludeFilter) {
            for (Class<? extends Throwable> excludeException : excludeExceptions) {
                if (excludeException.isInstance(e)) {
                    log.debug("Parent exception {} is excluded, not retry on exception {}", excludeException,
                            e.getClass());
                    return false;
                }
            }
            return true;
        }

        return retryOnException(e.getClass());
    }

    private boolean retryOnException(Class<? extends Throwable> eClass) {
        if (excludeExceptions.contains(eClass)) {// 被排除了
            log.debug("{} is excluded, not retry.", eClass);
            return false;
        } else if (includeExceptions.contains(eClass)) { // 被包含了
            log.debug("{} is included, retry.", eClass);
            return true;
        } else if (eClass.equals(Throwable.class)) { // 即没有被排除也没有被包含
            log.debug("Exception not included, not retry.");
            return false;
        } else {
            return retryOnException((Class<? extends Throwable>) eClass.getSuperclass());
        }
    }

    protected void setIncludeExceptions(List<Class<? extends Throwable>> includeExceptions) {
        this.includeExceptions = includeExceptions;
    }

    protected void setExcludeExceptions(List<Class<? extends Throwable>> excludeExceptions) {
        this.excludeExceptions = excludeExceptions;
    }

    protected void setMaxAttempts(Integer maxAttempts) {
        this.maxAttempts = maxAttempts;
    }

    protected void setMaxWaitTime(Long maxWaitTime) {
        this.maxWaitTime = maxWaitTime;
    }

    protected void setBackoff(Long backoff) {
        this.backoff = backoff;
    }
}