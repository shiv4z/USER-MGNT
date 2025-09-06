package in.gov.egramswaraj.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ExceptionLoggingAspect {

	private static final Logger logger = LoggerFactory.getLogger(ExceptionLoggingAspect.class);

	@AfterThrowing(pointcut = "within(@org.springframework.web.bind.annotation.RestControllerAdvice *)", throwing = "ex")
	public void logAfterThrowing(JoinPoint joinPoint, Throwable ex) {
		logger.error("Exception in {}.{}() with cause = {} and message = {}",
				joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName(),
				ex.getCause() != null ? ex.getCause() : "NULL", ex.getMessage(), ex);
	}
}
