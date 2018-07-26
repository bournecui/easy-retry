#easy-retry

## About _easy-retry_
`easy-retry` provides retry support for Java applications.

_Features_

- flexible. You can retry your job with below policies:
    - Exception occurs, also you can config which exceptions will be ngnored.
    - UnExpected result.
    - Retry with max attemts or max wait time.
- easy-to-use
    - See the _Quick Start_ part and try it by yourself.
- lightweight
    - Except the slf4j and logback jars, there is no any other third party jars involved.

## Quick Start
1. Add `easy-retry` to your application.
```
    <dependency>
        <groupId>com.github.bournecui</groupId>
        <artifactId>easy-retry</artifactId>
        <version>0.0.1</version>
    </dependency>
```
2. Use `EasyRetryBuilder` to build an instance of `EasyRetry`, it is thread safe so you can share an instance in your application.
```
EasyRetry easyRetry = EasyRetryBuilder.newBuilder()
                 .maxAttempts(10)
                 .includeExceptions(IllegalStateException.class)
                 .build();
```
3. Use `EasyCallable`(with return value) or `EasyRunnable`(with no return value) to encapsulates your job, and use `easyRetry` to run it.
```
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
```
For the job with a return value (encapsulated by `EasyCallable`), you can use `ResultPredicate` to judge if the result is expected, if not it will be retired.
```
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
```
Your code will be more brief if you use lambda expression and static import for `com.github.bournecui.easyretry.EasyRetryBuilder.newBuilder`.
```
     newBuilder()
          .maxAttempts(10)
          .includeExceptions(IllegalStateException.class)
          .build()
          .call(() -> (int) (Math.random() * 10), result -> result % 3 != 0);
```