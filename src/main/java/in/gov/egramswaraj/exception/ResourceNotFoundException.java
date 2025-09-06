package in.gov.egramswaraj.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResourceNotFoundException extends RuntimeException{

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final String resource;
    private final HttpStatus status;

    public ResourceNotFoundException(String message, String resource){
        super(message);
        this.resource = resource;
        this.status = HttpStatus.NOT_FOUND;
    }


}