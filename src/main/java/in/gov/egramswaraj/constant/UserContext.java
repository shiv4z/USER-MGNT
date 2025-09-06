package in.gov.egramswaraj.constant;

import java.util.Objects;

import org.springframework.stereotype.Component;

import in.gov.egramswaraj.utility.ApplicationUtility;
import in.gov.egramswaraj.utility.JwtClaimKeys;

@Component
public class UserContext {
	
	public Long getUserId() {
        return Long.parseLong(Objects.requireNonNull(ApplicationUtility.getAttribute(JwtClaimKeys.USER_ID)).toString());
    }

    public Long getStateCode() {
        return Long.parseLong(Objects.requireNonNull(ApplicationUtility.getAttribute(JwtClaimKeys.STATE_CODE)).toString());
    }

    public Long getEntityCode() {
        return Long.parseLong(Objects.requireNonNull(ApplicationUtility.getAttribute(JwtClaimKeys.ENTITY_CODE)).toString());
    }

    public String getRole() {
        return Objects.requireNonNull(ApplicationUtility.getAttribute(JwtClaimKeys.ROLE)).toString();
    }

    public String getFinYear() {
        return Objects.requireNonNull(ApplicationUtility.getAttribute(JwtClaimKeys.YEAR)).toString();
    }

    public String getDistrict() {
        return Objects.requireNonNull(ApplicationUtility.getAttribute(JwtClaimKeys.DISTRICT)).toString();
    }

    public String getSubDistrict() {
        return Objects.requireNonNull(ApplicationUtility.getAttribute(JwtClaimKeys.SUB_DISTRICT)).toString();
    }

    public Long getEntityTypeId() {
        return Long.parseLong(Objects.requireNonNull(ApplicationUtility.getAttribute(JwtClaimKeys.ENTITY_TYPE_ID)).toString());
    }

    public Long getStartYear() {
        return Long.parseLong(Objects.requireNonNull(ApplicationUtility.getAttribute(JwtClaimKeys.YEAR)).toString().substring(5,9));
    }

    public Long getEndYear() {
        return Long.parseLong(Objects.requireNonNull(ApplicationUtility.getAttribute(JwtClaimKeys.YEAR)).toString().substring(5,9));
    }

	
    
    
}

