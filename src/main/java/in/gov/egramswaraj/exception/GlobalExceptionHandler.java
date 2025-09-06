package in.gov.egramswaraj.exception;

import java.io.FileNotFoundException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.apache.coyote.BadRequestException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(NoResourceFoundException.class)
	public ResponseEntity<Object> handleNoResourceFoundException(NoResourceFoundException ex,
			HttpServletRequest request) {
		return buildErrorResponse(HttpStatus.NOT_FOUND, "Resource Not Found", ex.getMessage(), request.getRequestURI());
	}

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<Object> handleResourceNotFound(ResourceNotFoundException ex, HttpServletRequest request) {
		return buildErrorResponse(HttpStatus.NOT_FOUND, "Not Found", ex.getMessage(), request.getRequestURI());
	}

	@ExceptionHandler(NoHandlerFoundException.class)
	public ResponseEntity<Object> handleNoHandlerFound(NoHandlerFoundException ex, HttpServletRequest request) {
		return buildErrorResponse(HttpStatus.NOT_FOUND, "Not Found", ex.getMessage(), request.getRequestURI());
	}

	@ExceptionHandler(NullPointerException.class)
	public ResponseEntity<Object> handleNullPointer(NullPointerException ex, HttpServletRequest request) {
		return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Null pointer encountered error", ex.getMessage(),
				request.getRequestURI());
	}

	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<Object> handleIllegalArgument(IllegalArgumentException ex, HttpServletRequest request) {
		return buildErrorResponse(HttpStatus.BAD_REQUEST, "Illegal argument error", ex.getMessage(),
				request.getRequestURI());
	}

	@ExceptionHandler(IllegalStateException.class)
	public ResponseEntity<Object> handleIllegalState(BindException ex, HttpServletRequest request) {
		Map<String, String> errors = buildError(ex);
		return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Illegal state error", errors,
				request.getRequestURI());
	}

	@ExceptionHandler(ArithmeticException.class)
	public ResponseEntity<Object> handleArithmetic(BindException ex, HttpServletRequest request) {
		Map<String, String> errors = buildError(ex);
		return buildErrorResponse(HttpStatus.BAD_REQUEST, "Arithmetic error", errors, request.getRequestURI());
	}

	@ExceptionHandler(IndexOutOfBoundsException.class)
	public ResponseEntity<Object> handleIndexOutOfBounds(BindException ex, HttpServletRequest request) {
		Map<String, String> errors = buildError(ex);
		return buildErrorResponse(HttpStatus.BAD_REQUEST, "Index out of bounds error", errors, request.getRequestURI());
	}

	@ExceptionHandler(NumberFormatException.class)
	public ResponseEntity<Object> handleNumberFormat(BindException ex, HttpServletRequest request) {
		Map<String, String> errors = buildError(ex);
		return buildErrorResponse(HttpStatus.BAD_REQUEST, "Number format error", errors, request.getRequestURI());
	}

	@ExceptionHandler(UnsupportedOperationException.class)
	public ResponseEntity<Object> handleUnsupported(BindException ex, HttpServletRequest request) {
		Map<String, String> errors = buildError(ex);
		return buildErrorResponse(HttpStatus.BAD_REQUEST, "Unsupported operation error", errors,
				request.getRequestURI());
	}

	@ExceptionHandler(ArrayIndexOutOfBoundsException.class)
	public ResponseEntity<Object> handleArrayIndex(BindException ex, HttpServletRequest request) {
		Map<String, String> errors = buildError(ex);
		return buildErrorResponse(HttpStatus.BAD_REQUEST, "Array index out of bounds error", errors,
				request.getRequestURI());
	}

	@ExceptionHandler(ClassCastException.class)
	public ResponseEntity<Object> handleClassCast(ClassCastException ex, HttpServletRequest request) {
		return buildErrorResponse(HttpStatus.BAD_REQUEST, "Class cast error", ex.getMessage(), request.getRequestURI());
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Object> handleValidationException(MethodArgumentNotValidException ex,
			HttpServletRequest request) {
		Map<String, String> errors = buildError(ex);
		return buildErrorResponse(HttpStatus.BAD_REQUEST, "Validation Error", errors, request.getRequestURI());
	}

	@ExceptionHandler(BindException.class)
	public ResponseEntity<Object> handleBindException(BindException ex, HttpServletRequest request) {
		Map<String, String> errors = buildError(ex);
		return buildErrorResponse(HttpStatus.BAD_REQUEST, "Binding Error", errors, request.getRequestURI());
	}

	@ExceptionHandler(FileNotFoundException.class)
	public ResponseEntity<Object> handleFileNotFoundException(FileNotFoundException ex, HttpServletRequest request) {
		return buildErrorResponse(HttpStatus.NOT_FOUND, "File Not Found", ex.getMessage(), request.getRequestURI());
	}

	@ExceptionHandler(RuntimeException.class)
	public ResponseEntity<Object> handleRuntime(RuntimeException ex, HttpServletRequest request) {
		return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "RuntimeException", ex.getMessage(),
				request.getRequestURI());
	}

	@ExceptionHandler(EntityNotFoundException.class)
	public ResponseEntity<Object> handleEntityNotFound(EntityNotFoundException ex, HttpServletRequest request) {
		return buildErrorResponse(HttpStatus.NOT_FOUND, "Resource Not Found", ex.getMessage(), request.getRequestURI());
	}

	@ExceptionHandler(BadRequestException.class)
	public ResponseEntity<Object> handleBadRequestException(BadRequestException ex, HttpServletRequest request) {
		return buildErrorResponse(HttpStatus.BAD_REQUEST, "Invalid payload request", ex.getMessage(),
				request.getRequestURI());
	}

	@ExceptionHandler(DataIntegrityViolationException.class)
	public ResponseEntity<Object> handleDataIntegrityViolation(DataIntegrityViolationException ex,
			HttpServletRequest request) {
		String rootCauseMessage = Optional.ofNullable(ex.getRootCause()).map(Throwable::getMessage)
				.orElse("No detailed cause available");
		return buildErrorResponse(HttpStatus.CONFLICT, "Constraint violation error", rootCauseMessage,
				request.getRequestURI());
	}

	@ExceptionHandler(SomeUsersNotFoundException.class)
	public ResponseEntity<Object> handleSomeUsersNotFound(SomeUsersNotFoundException ex, HttpServletRequest request) {
		return buildErrorResponse(HttpStatus.NOT_FOUND, "Some user IDs were not found", ex.getMessage(),
				request.getRequestURI());
	}

	@ExceptionHandler(InvalidJwtException.class)
	public ResponseEntity<Object> handleInvalidJwtException(InvalidJwtException ex, HttpServletRequest request) {
		return buildErrorResponse(HttpStatus.UNAUTHORIZED, "JWT Authentication failed", ex.getMessage(),
				request.getRequestURI());
	}

	@ExceptionHandler(PropertyReferenceException.class)
	public ResponseEntity<Object> handlePropertyReferenceException(PropertyReferenceException ex,
			HttpServletRequest request) {
		return buildErrorResponse(HttpStatus.BAD_REQUEST, "Invalid Property Reference", ex.getMessage(),
				request.getRequestURI());
	}

	@ExceptionHandler(InvalidDataAccessApiUsageException.class)
	public ResponseEntity<Object> handleInvalidQuery(InvalidDataAccessApiUsageException ex,
			HttpServletRequest request) {
		return buildErrorResponse(HttpStatus.BAD_REQUEST, "Query Error", ex.getMessage(), request.getRequestURI());
	}

	@ExceptionHandler(JpaSystemException.class)
	public ResponseEntity<?> handleJpaSystem(JpaSystemException ex, HttpServletRequest request) {
		return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "JPA error ", ex.getMessage(),
				request.getRequestURI());
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<Object> handleGenericException(Exception ex, HttpServletRequest request) {
		return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", ex.getMessage(),
				request.getRequestURI());
	}

	private ResponseEntity<Object> buildErrorResponse(HttpStatus status, String error, Object message, String path) {
		Map<String, Object> response = new HashMap<>();
		response.put("timestamp", LocalDateTime.now());
		response.put("status", status.value());
		response.put("error", error);
		response.put("message", message);
		response.put("path", path);

		return new ResponseEntity<>(response, status);
	}

	private Map<String, String> buildError(BindException ex) {
		Map<String, String> errors = new HashMap<>();
		for (FieldError error : ex.getBindingResult().getFieldErrors()) {
			errors.put(error.getField(), error.getDefaultMessage());
		}
		return errors;
	}

}
