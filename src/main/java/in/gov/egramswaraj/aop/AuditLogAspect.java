package in.gov.egramswaraj.aop;

import java.time.LocalDateTime;
import java.util.Arrays;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.fasterxml.jackson.databind.ObjectMapper;

import in.gov.egramswaraj.entity.AuditLog;
import in.gov.egramswaraj.service.AuditService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Aspect
@Component
@RequiredArgsConstructor
public class AuditLogAspect {

	private final AuditService auditService;
	private final ObjectMapper objectMapper;

	@Around("execution(* in.gov.egramswaraj.rest..*(..))")
	public Object logAudit(ProceedingJoinPoint joinPoint) throws Throwable {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
				.getRequest();

		AuditLog log = new AuditLog();
		log.setUsername(request.getUserPrincipal() != null ? request.getUserPrincipal().getName() : "user-service");
		Long userId = (Long) SecurityContextHolder.getContext().getAuthentication().getDetails();
		log.setUserId(userId);
		log.setMethod(joinPoint.getSignature().toShortString());
		log.setIpAddress(request.getRemoteAddr());
		log.setAction(request.getMethod() + " " + request.getRequestURI());
		log.setCreatedOn(LocalDateTime.now());

		log.setPayload(sanitizePayload(joinPoint.getArgs()));
		log.setBrowserAgent(request.getHeader("User-Agent"));
		Object result;
		try {
			result = joinPoint.proceed();
			log.setStatusCode(result instanceof ResponseEntity ? ((ResponseEntity<?>) result).getStatusCode().value()
					: HttpStatus.OK.value());
		} catch (Throwable ex) {
			log.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
			log.setException(ex.getClass().getSimpleName() + ": " + ex.getMessage());
			throw ex;
		} finally {
			auditService.saveAuditLog(log);
		}

		return result;
	}

	private String sanitizePayload(Object[] args) {
		try {
	        String json = objectMapper.writeValueAsString(args);
	        return json.replaceAll("(?i)\"(password|newUserPass|oldUserPazz)\"\\s*:\\s*\"[^\"]*\"", "\"$1\":\"****\"");
	    } catch (Exception e) {
	        return Arrays.toString(args);
	    }
	}
}
