package in.gov.egramswaraj.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

    @Pointcut("execution(* in.gov.egramswaraj.serviceImpl..*(..)) || execution(* in.gov.egramswaraj.rest..*(..))")
    public void serviceAndControllerMethods() {}

    @Before("serviceAndControllerMethods()")
    public void logMethodEntry(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        logger.info("Entering {}.{} with arguments: {}",
                signature.getDeclaringTypeName(),
                signature.getName(),
                joinPoint.getArgs());
    }

    @AfterReturning(value = "serviceAndControllerMethods()", returning = "result")
    public void logMethodExit(JoinPoint joinPoint, Object result) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        logger.info("Exiting {}.{} with return value: {}",
                signature.getDeclaringTypeName(),
                signature.getName(),
                result);
    }

    @AfterThrowing(value = "serviceAndControllerMethods()", throwing = "ex")
    public void logExceptions(JoinPoint joinPoint, Exception ex) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        logger.error("Exception in {}.{} with cause: {}",
                signature.getDeclaringTypeName(),
                signature.getName(),
                ex.getMessage(), ex);
    }
}