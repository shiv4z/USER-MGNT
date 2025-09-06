package in.gov.egramswaraj.response;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationResponse {
	private Long id;
    private String applicationName;
	private String applicationUrl;
	private boolean isActive;
	private LocalDateTime createdOn;
}
