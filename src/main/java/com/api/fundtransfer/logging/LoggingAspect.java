package com.api.fundtransfer.logging;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

    @Around("execution(* com.api.fundtransfer..*(..))")  // Adjust the package as needed
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();
        Object[] methodArgs = joinPoint.getArgs();

        logger.info("Entering method: {}.{} with arguments {}", className, methodName, methodArgs);

        Object result;
        try {
            result = joinPoint.proceed();
        } catch (Throwable t) {
            logger.error("Exception in method: {}.{}", className, methodName, t);
            throw t;
        }

        logger.info("Exiting method: {}.{} with result {}", className, methodName, result);
        return result;
    }
}
