package in.gov.egramswaraj.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "audit_log", schema = "user_mgnt")
public class AuditLog {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String action;

	private Long userId;

	@Column(nullable = false)
	private String username;

	@Column(nullable = false)
	private String method;

	@Column(columnDefinition = "TEXT")
	private String payload;

	private Integer statusCode;
	
	@Column(columnDefinition = "TEXT")
	private String exception;

	@Column(length = 45)
	private String ipAddress;

	@Column(name = "browser_agent")
	private String browserAgent;
	
	@CreationTimestamp
	private LocalDateTime createdOn;

}
