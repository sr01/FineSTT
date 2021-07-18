package com.rosi.masts.test

import org.junit.rules.MethodRule
import org.junit.runners.model.FrameworkMethod
import org.junit.runners.model.Statement
import org.mockito.kotlin.timeout
import org.mockito.verification.VerificationMode

@Retention(AnnotationRetention.RUNTIME)
annotation class Retry(val retryCount: Int)

class RetryRule : MethodRule {
    override fun apply(base: Statement, method: FrameworkMethod, target: Any): Statement {
        return object : Statement() {
            override fun evaluate() {
                var caughtThrowable: Throwable? = null

                try {
                    base.evaluate()
                } catch (t: Throwable) {
                    val retry = method.getAnnotation(Retry::class.java)
                    if (retry != null) {
                        for (i in 1 until retry.retryCount) {
                            try {
                                base.evaluate()
                                return
                            } catch (t: Throwable) {
                                caughtThrowable = t
                                println(method.name + ": run " + (i + 1) + " failed")
                            }
                        }

                        println(method.name + ": giving up after " + retry.retryCount + " failures")
                        throw caughtThrowable!!

                    } else {
                        throw t
                    }
                }
            }
        }
    }
}

@Retention(AnnotationRetention.RUNTIME)
annotation class RetryUntilTimeout(val intervalMillis: Long = 500, val timeoutMillis: Long = 5000)

class RetryUntilTimeoutRule : MethodRule {
    override fun apply(base: Statement, method: FrameworkMethod, target: Any): Statement {
        return object : Statement() {
            override fun evaluate() {
                var caughtThrowable: Throwable? = null
                val startTime = System.currentTimeMillis()

                try {
                    base.evaluate()
                } catch (t: Throwable) {
                    val retry = method.getAnnotation(RetryUntilTimeout::class.java)
                    if (retry != null) {
                        var i = 0
                        while (System.currentTimeMillis() - startTime < retry.timeoutMillis) {
                            try {
                                Thread.sleep(retry.intervalMillis)
                                base.evaluate()
                                return
                            } catch (t: Throwable) {
                                caughtThrowable = t
                                i++
                            }
                        }

                        println(method.name + ": giving up after " + retry.timeoutMillis + " ms")
                        throw caughtThrowable!!

                    } else {
                        throw t
                    }
                }
            }
        }
    }
}

fun repeatUntilPass(timeoutMillis: Long = 1000, intervalMillis: Long = 100, function: () -> Unit) {
    var caughtThrowable: Throwable? = null
    val startTime = System.currentTimeMillis()

    try {
        function()
    } catch (t: Throwable) {
        while (System.currentTimeMillis() - startTime < timeoutMillis) {
            try {
                Thread.sleep(intervalMillis)
                function()
                return
            } catch (t: Throwable) {
                caughtThrowable = t
            }
        }
        throw caughtThrowable!!
    }
}

const val TEST_TIMEOUT = 100L

fun onceWithTimeout(millis: Long = TEST_TIMEOUT): VerificationMode = timeout(millis).times(1)
