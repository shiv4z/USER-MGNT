package in.gov.egramswaraj.utility;

public class JwtClaimKeys {
	
	private JwtClaimKeys(){
		throw new IllegalStateException("JwtClaimKeys class");
	}

    public static final String USER_NAME = "userName";
    public static final String ROLE = "role";
    public static final String USER_ID = "userId";
    public static final String STATE_CODE = "stateCode";
    public static final String DISTRICT = "district";
    public static final String SUB_DISTRICT = "subDistrict";
    public static final String ENTITY_TYPE_ID = "entityTypeId";
    public static final String ENTITY_CODE = "entityCode";
    public static final String YEAR = "year";
}
