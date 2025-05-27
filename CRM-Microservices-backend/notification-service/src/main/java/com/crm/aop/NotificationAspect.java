package com.crm.aop;
 
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
* Aspect class for logging method execution in the CRM application.
* This class uses AspectJ annotations to intercept method calls in repositories, services, and controllers,
* providing logging for method entry, successful execution, and exceptions.
*/

@Aspect
@Component
public class NotificationAspect {
 
	private static final Logger logger = LoggerFactory.getLogger(NotificationAspect.class);

	/**
     * Logs method execution before service methods are invoked.
     *
     * @param joinPoint The JoinPoint representing the intercepted method call.
     */

	@Before("execution(* com.crm.service.*.*(..))")
	public void beforeService(JoinPoint joinPoint) {
		loggerInfoBefore(joinPoint);
	}

	 /**
     * Logs method execution before controller methods are invoked.
     *
     * @param joinPoint The JoinPoint representing the intercepted method call.
     */

	@Before("execution(* com.crm.controller.*.*(..))")
	public void beforeController(JoinPoint joinPoint) {
		loggerInfoBefore(joinPoint);
	}

	/**
     * Logs method execution details before the method is invoked.
     *
     * @param joinPoint The JoinPoint representing the intercepted method call.
     */

	private void loggerInfoBefore(JoinPoint joinPoint) {
		logger.info("Executing method: {}.{}", joinPoint.getSignature().getName(),
				joinPoint.getSignature().getDeclaringTypeName());
	}


	/**
     * Logs method execution details after service methods successfully return.
     *
     * @param joinPoint The JoinPoint representing the intercepted method call.
     * @param result    The result returned by the method.
     */

	@AfterReturning(pointcut = "execution(* com.crm.service.*.*(..))", returning = "result")
	public void afterReturningService(JoinPoint joinPoint, Object result) {
		loggerInfoAfterReturning(joinPoint, result);
	}

	/**
     * Logs method execution details after controller methods successfully return.
     *
     * @param joinPoint The JoinPoint representing the intercepted method call.
     * @param result    The result returned by the method.
     */

	@AfterReturning(pointcut = "execution(* com.crm.controller.*.*(..))", returning = "result")
	public void afterReturningController(JoinPoint joinPoint, Object result) {
		loggerInfoAfterReturning(joinPoint, result);
	}

	/**
     * Logs method execution details after successful method execution.
     *
     * @param joinPoint The JoinPoint representing the intercepted method call.
     * @param result    The result returned by the method.
     */

	private void loggerInfoAfterReturning(JoinPoint joinPoint, Object result) {
		logger.info("Method executed successfully: {}.{}", joinPoint.getSignature().getName(),
				joinPoint.getSignature().getDeclaringTypeName());
		if (result != null && result.toString().length() < 1000) {
			logger.info("Method result: {}", result);
		}
	}

	
	/**
     * Logs method execution details after service methods throw an exception.
     *
     * @param joinPoint The JoinPoint representing the intercepted method call.
     * @param error     The exception thrown by the method.
     */
	
	@AfterThrowing(pointcut = "execution(* com.crm.service.*.*(..))", throwing = "error")
	public void afterThrowingService(JoinPoint joinPoint, Throwable error) {
		loggerInfoAfterThrowing(joinPoint, error);
	}

	/**
     * Logs method execution details after controller methods throw an exception.
     *
     * @param joinPoint The JoinPoint representing the intercepted method call.
     * @param error     The exception thrown by the method.
     */

	@AfterThrowing(pointcut = "execution(* com.crm.controller.*.*(..))", throwing = "error")
	public void afterThrowingController(JoinPoint joinPoint, Throwable error) {
		loggerInfoAfterThrowing(joinPoint, error);
	}

	/**
     * Logs method execution details after an exception is thrown.
     *
     * @param joinPoint The JoinPoint representing the intercepted method call.
     * @param error     The exception thrown by the method.
     */

	private void loggerInfoAfterThrowing(JoinPoint joinPoint, Throwable error) {
		logger.error("Method execution failed: {}.{}", joinPoint.getSignature().getName(),
				joinPoint.getSignature().getDeclaringTypeName());
		logger.error("Error: {}", error.getMessage());
	}
 
}

 