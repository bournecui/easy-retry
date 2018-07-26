package me.com.github.bournecui.easyretry;

import static com.github.bournecui.easyretry.EasyRetryBuilder.newBuilder;

import java.sql.SQLException;

import org.junit.Assert;
import org.junit.Test;

import com.github.bournecui.easyretry.EasyCallable;
import com.github.bournecui.easyretry.EasyRetry;
import com.github.bournecui.easyretry.EasyRunnable;
import com.github.bournecui.easyretry.ResultPredicate;
import com.github.bournecui.easyretry.TimeoutException;

/**
 * Unit test for simple App.
 */
public class EasyRetryTest {

    @Test
    public void testEastRunnabel() {
        final int[] attempt = {0};

        newBuilder().build().run(() -> attempt[0]++);
        Assert.assertSame(attempt[0], 1);
    }

    @Test
    public void testEasyCallable() {
        final int[] attempt = {0};

        Integer result = newBuilder().build().call(() -> ++attempt[0]);
        Assert.assertSame(result, 1);
    }

    @Test
    public void testResultPredicate() {
        final int[] attempt = {0};

        Integer result = newBuilder().maxAttempts(5).build().call(() -> ++attempt[0], result1 -> result1 == 2);
        Assert.assertSame(result, 2);
    }

    @Test
    public void testMaxAttempt() {
        final int[] attempt = {0};

        Integer result = newBuilder()
                .maxAttempts(5)
                .build()
                .call(() -> {
                    if (++attempt[0] < 5) {
                        throw new IllegalStateException(attempt[0] + " is not expected!");
                    }
                    return attempt[0];
                });

        Assert.assertSame(result, 5);
        Assert.assertSame(attempt[0], 5);
    }

    @Test
    public void testExceptionNoneIncludeAndExclude() {
        final int[] attempt = {0};

        Integer result = newBuilder()
                .maxAttempts(3)
                .build()
                .call(() -> {
                    if (++attempt[0] < 3) {
                        throw new IllegalStateException(attempt[0] + " is not expected!");
                    }
                    return attempt[0];
                });

        Assert.assertSame(result, 3);
        Assert.assertSame(attempt[0], 3);
    }

    @Test
    public void testExceptionInclude() {
        final int[] attempt = {0};

        Integer result = newBuilder()
                .maxAttempts(3)
                .includeExceptions(IllegalStateException.class)
                .build()
                .call(() -> {
                    if (++attempt[0] < 3) {
                        throw new IllegalStateException(attempt[0] + " is not expected!");
                    }
                    return attempt[0];
                });

        Assert.assertSame(result, 3);
        Assert.assertSame(attempt[0], 3);
    }

    @Test(expected = IllegalStateException.class)
    public void testExceptionInclude2() {
        final int[] attempt = {0};

        Integer result = newBuilder()
                .maxAttempts(3)
                .includeExceptions(IllegalArgumentException.class)
                .build()
                .call(() -> {
                    if (++attempt[0] < 3) {
                        throw new IllegalStateException(attempt[0] + " is not expected!");
                    }
                    return attempt[0];
                });

        Assert.assertSame(result, 3);
        Assert.assertSame(attempt[0], 3);
    }

    @Test
    public void testExceptionParentInclude() {
        final int[] attempt = {0};

        Integer result = newBuilder()
                .maxAttempts(3)
                .includeExceptions(Exception.class)
                .build()
                .call(() -> {
                    if (++attempt[0] < 3) {
                        throw new IllegalStateException(attempt[0] + " is not expected!");
                    }
                    return attempt[0];
                });

        Assert.assertSame(result, 3);
        Assert.assertSame(attempt[0], 3);
    }

    @Test(expected = IllegalStateException.class)
    public void testExceptionExclude() {
        final int[] attempt = {0};

        Integer result = newBuilder()
                .maxAttempts(3)
                .excludeExceptions(IllegalStateException.class)
                .build()
                .call(() -> {
                    if (++attempt[0] < 3) {
                        throw new IllegalStateException(attempt[0] + " is not expected!");
                    }
                    return attempt[0];
                });

        Assert.assertSame(result, 3);
        Assert.assertSame(attempt[0], 3);
    }

    @Test
    public void testExceptionExclude2() {
        final int[] attempt = {0};

        Integer result = newBuilder()
                .maxAttempts(3)
                .excludeExceptions(IllegalArgumentException.class)
                .build()
                .call(() -> {
                    if (++attempt[0] < 3) {
                        throw new IllegalStateException(attempt[0] + " is not expected!");
                    }
                    return attempt[0];
                });

        Assert.assertSame(result, 3);
        Assert.assertSame(attempt[0], 3);
    }

    @Test(expected = IllegalStateException.class)
    public void testExceptionParentExclude() {
        final int[] attempt = {0};

        Integer result = newBuilder()
                .maxAttempts(3)
                .excludeExceptions(Exception.class)
                .build()
                .call(() -> {
                    if (++attempt[0] < 3) {
                        throw new IllegalStateException(attempt[0] + " is not expected!");
                    }
                    return attempt[0];
                });

        Assert.assertSame(result, 3);
        Assert.assertSame(attempt[0], 3);
    }

    @Test(expected = IllegalStateException.class)
    public void testExceptionParentIncludeSelfExclude() {
        final int[] attempt = {0};

        Integer result = newBuilder()
                .maxAttempts(3)
                .includeExceptions(Exception.class)
                .excludeExceptions(IllegalStateException.class)
                .build()
                .call(() -> {
                    if (++attempt[0] < 3) {
                        throw new IllegalStateException(attempt[0] + " is not expected!");
                    }
                    return attempt[0];
                });

        Assert.assertSame(result, 3);
        Assert.assertSame(attempt[0], 3);
    }

    @Test
    public void testExceptionParentExcludeSelfInClude() {
        final int[] attempt = {0};

        Integer result = newBuilder()
                .maxAttempts(3)
                .includeExceptions(IllegalStateException.class)
                .excludeExceptions(Exception.class)
                .build()
                .call(() -> {
                    if (++attempt[0] < 3) {
                        throw new IllegalStateException(attempt[0] + " is not expected!");
                    }
                    return attempt[0];
                });

        Assert.assertSame(result, 3);
        Assert.assertSame(attempt[0], 3);
    }

    @Test(expected = IllegalStateException.class)
    public void testExceptionParentExcludeGranpaInClude() {
        final int[] attempt = {0};

        Integer result = newBuilder()
                .maxAttempts(3)
                .includeExceptions(Exception.class)
                .excludeExceptions(RuntimeException.class)
                .build()
                .call(() -> {
                    if (++attempt[0] < 3) {
                        throw new IllegalStateException(attempt[0] + " is not expected!");
                    }
                    return attempt[0];
                });

        Assert.assertSame(result, 3);
        Assert.assertSame(attempt[0], 3);
    }

    @Test
    public void testExceptionParentIncludeGranpaExClude() {
        final int[] attempt = {0};

        Integer result = newBuilder()
                .maxAttempts(3)
                .includeExceptions(RuntimeException.class)
                .excludeExceptions(Exception.class)
                .build()
                .call(() -> {
                    if (++attempt[0] < 3) {
                        throw new IllegalStateException(attempt[0] + " is not expected!");
                    }
                    return attempt[0];
                });

        Assert.assertSame(result, 3);
        Assert.assertSame(attempt[0], 3);
    }

    @Test(expected = IllegalStateException.class)
    public void testBothIncludeAndExclude() {
        newBuilder()
                .maxAttempts(3)
                .includeExceptions(IllegalStateException.class)
                .excludeExceptions(IllegalStateException.class)
                .build();
    }

    @Test(expected = IllegalStateException.class)
    public void testNotIncludeNorExclude() {
        final int[] attempt = {0};

        Integer result = newBuilder()
                .maxAttempts(3)
                .includeExceptions(SQLException.class)
                .excludeExceptions(IllegalArgumentException.class)
                .build()
                .call(() -> {
                    if (++attempt[0] < 3) {
                        throw new IllegalStateException(attempt[0] + " is not expected!");
                    }
                    return attempt[0];
                });

        Assert.assertSame(result, 3);
        Assert.assertSame(attempt[0], 3);
    }

    @Test(expected = TimeoutException.class)
    public void testBackoffAndTimeOut() {
        final int[] attempt = {0};

        Integer result = newBuilder()
                .maxAttempts(5)
                .maxWaitTime(3000l)
                .backoff(2000)
                .build()
                .call(() -> {
                    if (++attempt[0] < 4) {
                        throw new IllegalStateException(attempt[0] + " is not expected!");
                    }
                    return attempt[0];
                });

        Assert.assertSame(result, 3);
        Assert.assertSame(attempt[0], 3);
    }

    @Test
    public void testUseage() {

        newBuilder()
                .maxAttempts(10)
                .includeExceptions(IllegalStateException.class)
                .build()
                .call(() -> (int) (Math.random() * 10), result -> result % 3 != 0);

        EasyRetry easyRetry = newBuilder()
                .maxAttempts(10)
                .includeExceptions(IllegalStateException.class)
                .build();

        Integer res1 = easyRetry.call(new EasyCallable<Integer>() {
            @Override
            public Integer call() {
                int aInt = (int) (Math.random() * 10);
                if (aInt % 3 != 0) {
                    throw new IllegalStateException(aInt + " is not expected!");
                }
                return aInt;
            }
        });

        System.out.println("res1:" + res1);
        Integer res2 = easyRetry.call(new EasyCallable<Integer>() {
            @Override
            public Integer call() {
                return (int) (Math.random() * 10);
            }
        }, new ResultPredicate<Integer>() {
            @Override
            public boolean test(Integer result) {
                return result % 3 != 0;
            }
        });
        System.out.println("res2:" + res2);

        easyRetry.run(new EasyRunnable() {
            @Override
            public void run() {

            }
        });
    }
}
