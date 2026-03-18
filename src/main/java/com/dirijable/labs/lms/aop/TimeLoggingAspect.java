package com.dirijable.labs.lms.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class TimeLoggingAspect {

    @Pointcut("execution(public * com.dirijable.labs.lms.service..*.*(..))")
    public void serviceMethod() {
    }

    @Around("serviceMethod()")
    public Object timeLogging(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        try {
            return joinPoint.proceed();
        } finally {
            long endTime = System.currentTimeMillis();
            log.debug("time of method work [{}]: {} ms",
                    joinPoint.getSignature().toShortString(), (endTime - startTime));
        }
    }
}
