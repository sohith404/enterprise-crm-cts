package com.crm.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
@Aspect
@Component
public class LoggingAspect {
    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);
    @Before("execution(* com.crm.repositories.*.*(..))")
    public void beforeRepositories(JoinPoint joinPoint) {
        loggerInfoBefore(joinPoint);
    }
    @Before("execution(* com.crm.service.*.*(..))")
    public void beforeService(JoinPoint joinPoint) {
        loggerInfoBefore(joinPoint);
    }
    @Before("execution(* com.crm.controller.*.*(..))")
    public void beforeController(JoinPoint joinPoint) {
        loggerInfoBefore(joinPoint);
    }
    private void loggerInfoBefore(JoinPoint joinPoint) {
        logger.info("Executing method: {}.{}", joinPoint.getSignature().getName(), joinPoint.getSignature().getDeclaringTypeName());
    }
    @AfterReturning(pointcut = "execution(* com.crm.repositories.*.*(..))", returning = "result")
    public void afterReturningRepositories(JoinPoint joinPoint, Object result) {
        loggerInfoAfterReturning(joinPoint, result);
    }
    @AfterReturning(pointcut = "execution(* com.crm.service.*.*(..))", returning = "result")
    public void afterReturningService(JoinPoint joinPoint, Object result) {
        loggerInfoAfterReturning(joinPoint, result);
    }
    @AfterReturning(pointcut = "execution(* com.crm.controller.*.*(..))", returning = "result")
    public void afterReturningController(JoinPoint joinPoint, Object result) {
        loggerInfoAfterReturning(joinPoint, result);
    }
    private void loggerInfoAfterReturning(JoinPoint joinPoint, Object result) {
        logger.info("Method executed successfully: {}.{}", joinPoint.getSignature().getName(), joinPoint.getSignature().getDeclaringTypeName());
        if (result != null && result.toString().length() < 1000) {
            logger.info("Method result: {}", result);
        }
    }
    @AfterThrowing(pointcut = "execution(* com.crm.repositories.*.*(..))", throwing = "error")
    public void afterThrowingRepositories(JoinPoint joinPoint, Throwable error) {
        loggerInfoAfterThrowing(joinPoint, error);
    }
    @AfterThrowing(pointcut = "execution(* com.crm.service.*.*(..))", throwing = "error")
    public void afterThrowingService(JoinPoint joinPoint, Throwable error) {
        loggerInfoAfterThrowing(joinPoint, error);
    }
    @AfterThrowing(pointcut = "execution(* com.crm.controller.*.*(..))", throwing = "error")
    public void afterThrowingController(JoinPoint joinPoint, Throwable error) {
        loggerInfoAfterThrowing(joinPoint, error);
    }
    private void loggerInfoAfterThrowing(JoinPoint joinPoint, Throwable error) {
        logger.error("Method execution failed: {}.{}", joinPoint.getSignature().getName(), joinPoint.getSignature().getDeclaringTypeName());
        logger.error("Error: {}", error.getMessage());
    }
}