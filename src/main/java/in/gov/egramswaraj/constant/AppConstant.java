package in.gov.egramswaraj.constant;

public class AppConstant {
	
	private AppConstant() {
	    throw new IllegalStateException("AppConstant class");
	  }

	public static final String USER_STATUS = "user status updated successfully";
	public static final String USER_DELETION = "user deleted successfully";
	public static final String ROLE_STATUS = "role status updated successfully";
	public static final String ROLE_DEACTIVATION = "role de-activated successfully";
	public static final String GROUP_STATUS = "group status updated sucessfully.";
	public static final String USER_GROUP_ADDAED = "users successfully mapped to the group";
	public static final String ROLE_GROUP_ADDAED = "roles successfully mapped to the group";
	public static final String PRIVILEGE_ROLE_MAPPING = "privileges successfully mapped to role";
	public static final String INVALID_PRIVILEGE = "some privileges IDs are invalid";
	public static final String BAD_REQUEST_MSG = "either userId or roleId must be provided.";
	public static final String APPLICATION_STATUS = "application status changed successfully.";
	public static final String APPLICATION_MAPPING_STATUS = "application mapped to users successfully.";
	public static final String USER_NOT_FOUND = "user not found with ID: ";
	public static final String ROLE_NOT_FOUND = "role not found with ID: ";
	public static final String GROUP_NOT_FOUND = "group not found with ID: ";
	public static final String PRIVILEGE_NOT_FOUND = "privilege not found with ID: ";
	public static final String APPLICATION_NOT_FOUND = "application not found with ID: ";
	public static final String PRIVILEGE_STATUS = "privilege updated successfully.";
	public static final String OLD_PASSWORD_MATCH = "new password cannot be the same as the last three passwords.";
	public static final String USER_ALREADY_EXIST = "username already exists";
	public static final String ROLE_ALREADY_EXIST = "role already exists";
	public static final String PRIVILEGE_ALREADY_EXIST = "privilege already exists";
	public static final String GROUP_ALREADY_EXIST = "privilege already exists";
	public static final String APPLICATION_NAME_ALREADY_EXIST = "application name already exist";
	public static final String APPLICATION_URL_ALREADY_EXIST = "application url already exist";
	public static final Integer USER_PASSWORD_VALIDATION = 3;
	public static final String ROLE_DELETION = "role deleted successfully";
	public static final String GROUP_DELETION = "group deleted successfully";
	public static final String ROLE_MAPPED = "roles mapped to the users";
	public static final String USER_MAPPED = "users mapped to the role";
	public static final String PRIVILEGE_DELETION = "privilege deleted successfully";
	public static final String PRIVILEGE_NAME_ALREADY_EXIST = "privilege name already exist";
	public static final String PRIVILEGE_URL_ALREADY_EXIST = "privilege url already exist";
	public static final String APPLICATION_DELETION = "application deleted successfully";
	public static final String SOME_USERS_NOT_FOUND = "some users are not found";
	public static final String SOME_ROLES_NOT_FOUND = "some roles are not found";
	public static final String PASSWORD_UPDATED = "password updated successfully.";
	public static final String CREDENTIAL_NOT_FOUND = "credential not found with ID: ";
	public static final String INCORRECT_OLD_PASSWORD = "old password is incorrect.";
	public static final String HIERARCHY_UPDATED = "Hierarchy updated successfully.";
	public static final String PROFILE_NOT_FOUND = "User profile not found with ID: ";
	public static final String PROFILE_ALREADY_EXIST = "user profile already exist with id: ";
	
	

}
