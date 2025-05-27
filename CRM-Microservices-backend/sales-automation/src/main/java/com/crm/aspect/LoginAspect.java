package com.crm.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

@Component
@Aspect
@Slf4j
public class LoginAspect {

    @Pointcut("execution(* com.crm.service..*(..))")
    public void serviceMethods() {
    }


    @Pointcut("execution(* com.crm.controller..*(..))")
    public void controllerMethods() {
    }


    @Pointcut("execution(* com.crm.repository..*(..))")
    public void repositoryMethods() {
    }

    /**
     * Logs information before the execution of a service method.
     *
     * @param joinPoint The JoinPoint object containing method execution details.
     */
    @Before("serviceMethods()")
    public void logBeforeServiceMethodExecution(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        Object[] methodArgs = joinPoint.getArgs();
        log.info("Service Method {} is called with arguments: {}", methodName, methodArgs);
    }

    /**
     * Logs information after the successful execution of a service method.
     *
     * @param joinPoint The JoinPoint object containing method execution details.
     */
    @AfterReturning(pointcut = "serviceMethods()")
    public void logAfterServiceMethodExecution(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        log.info("Service Method {} has returned.", methodName);
    }

    /**
     * Logs information after a service method throws an exception.
     *
     * @param joinPoint The JoinPoint object containing method execution details.
     * @param exception The exception thrown by the service method.
     */
    @AfterThrowing(pointcut = "serviceMethods()", throwing = "exception")
    public void logAfterServiceMethodThrowing(JoinPoint joinPoint, Throwable exception) {
        String methodName = joinPoint.getSignature().getName();
        log.info("Service Method {} has thrown an Exception -> {}", methodName, exception.getMessage());
    }

    /**
     * Logs information before the execution of a controller method.
     *
     * @param joinPoint The JoinPoint object containing method execution details.
     */
    @Before("controllerMethods()")
    public void logBeforeControllerMethodExecution(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        Object[] methodArgs = joinPoint.getArgs();
        log.info("Controller Method {} is called with arguments: {}", methodName, methodArgs);
    }

    /**
     * Logs information after the successful execution of a controller method.
     *
     * @param joinPoint The JoinPoint object containing method execution details.
     * @param result The result returned by the controller method.
     */
    @AfterReturning(pointcut = "controllerMethods()", returning = "result")
    public void logAfterControllerMethodExecution(JoinPoint joinPoint, Object result) {
        String methodName = joinPoint.getSignature().getName();
        log.info("Controller Method {} has returned.", methodName);
    }

    /**
     * Logs information after a controller method throws an exception.
     *
     * @param joinPoint The JoinPoint object containing method execution details.
     * @param exception The exception thrown by the controller method.
     */
    @AfterThrowing(pointcut = "controllerMethods()", throwing = "exception")
    public void logAfterControllerMethodThrowing(JoinPoint joinPoint, Throwable exception) {
        String methodName = joinPoint.getSignature().getName();
        log.info("Controller Method {} has thrown an Exception -> {}", methodName, exception.getMessage());
    }

    /**
     * Logs information before the execution of a repository method.
     *
     * @param joinPoint The JoinPoint object containing method execution details.
     */
    @Before("repositoryMethods()")
    public void logBeforeRepositoryMethodExecution(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        Object[] methodArgs = joinPoint.getArgs();
        log.info("Repository Method {} is called with arguments: {}", methodName, methodArgs);
    }

    /**
     * Logs information after the successful execution of a repository method.
     *
     * @param joinPoint The JoinPoint object containing method execution details.
     * @param result The result returned by the repository method.
     */
    @AfterReturning(pointcut = "repositoryMethods()", returning = "result")
    public void logAfterRepositoryMethodExecution(JoinPoint joinPoint, Object result) {
        String methodName = joinPoint.getSignature().getName();
        log.info("Repository Method {} has returned.", methodName);
    }

    /**
     * Logs information after a repository method throws an exception.
     *
     * @param joinPoint The JoinPoint object containing method execution details.
     * @param exception The exception thrown by the repository method.
     */
    @AfterThrowing(pointcut = "repositoryMethods()", throwing = "exception")
    public void logAfterRepositoryMethodThrowing(JoinPoint joinPoint, Throwable exception) {
        String methodName = joinPoint.getSignature().getName();
        log.info("Repository Method {} has thrown an Exception -> {}", methodName, exception.getMessage());
    }
}


