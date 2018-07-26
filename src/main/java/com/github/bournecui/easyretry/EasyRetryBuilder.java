package com.github.bournecui.easyretry;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import com.github.bournecui.easyretry.util.CollectionUtils;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class EasyRetryBuilder {

    /**
     * Job will be retried when these exceptions occur.
     */
    private Set<Class<? extends Throwable>> includeExceptions;

    /**
     * Job will not be retried when these exceptions occur. Exceptions will be thrown.
     */
    private Set<Class<? extends Throwable>> excludeExceptions;

    /**
     * Max attempt, default value is 3.
     */
    private int maxAttempts;

    /**
     * Max wait time, in millisecond, default with no limit.
     */
    private long maxWaitTime;

    /**
     * The backoff in millisecond, default value is 0.
     */
    private long backoff;

    private EasyRetryBuilder() {
        includeExceptions = new HashSet<>();
        excludeExceptions = new HashSet<>();
        maxAttempts = 3;
        backoff = 0L;
        maxWaitTime = 0L;
    }

    public static EasyRetryBuilder newBuilder() {
        return new EasyRetryBuilder();
    }

    public EasyRetry build() {
        Collection commons = CollectionUtils.intersection(includeExceptions, excludeExceptions);
        if (CollectionUtils.isNotEmpty(commons)) {
            throw new IllegalStateException(commons + " are both in included and excluded exceptions!");
        }

        EasyRetry easyRetry = new EasyRetry();

        easyRetry.setIncludeExceptions(Collections.unmodifiableList(new ArrayList<>(includeExceptions)));
        easyRetry.setExcludeExceptions(Collections.unmodifiableList(new ArrayList<>(excludeExceptions)));

        if (backoff > 0) {
            easyRetry.setBackoff(backoff);
        }

        if (maxAttempts > 0) {
            easyRetry.setMaxAttempts(maxAttempts);
        }

        if (maxWaitTime > 0) {
            easyRetry.setMaxWaitTime(maxWaitTime);
        }
        return easyRetry;
    }

    public EasyRetryBuilder includeExceptions(Class<? extends Throwable>... includes) {
        this.includeExceptions.addAll(Arrays.asList(includes));
        return this;
    }

    public EasyRetryBuilder excludeExceptions(Class<? extends Throwable>... excludes) {
        this.excludeExceptions.addAll(Arrays.asList(excludes));
        return this;
    }

    public EasyRetryBuilder maxWaitTime(Long maxWaitTime) {
        this.maxWaitTime = maxWaitTime;
        return this;
    }

    public EasyRetryBuilder maxAttempts(int maxAttempts) {
        this.maxAttempts = maxAttempts;
        return this;
    }

    public EasyRetryBuilder backoff(long backoff) {
        this.backoff = backoff;
        return this;
    }
}