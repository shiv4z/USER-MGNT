package in.gov.egramswaraj.utility;

import java.util.HashMap;
import java.util.Map;

public class ApplicationUtility {
	
	private ApplicationUtility() {
		throw new IllegalStateException("ApplicationUtility class");
	}
	private static final ThreadLocal<Map<String, Object>> requestAttributes = new ThreadLocal<>();

	public static void setAttribute(String key, Object value) {
		Map<String, Object> attributes = requestAttributes.get();
		if (attributes == null) {
			attributes = new HashMap<>();
			requestAttributes.set(attributes);
		}
		attributes.put(key, value);
	}

	public static Object getAttribute(String key) {
		Map<String, Object> attributes = requestAttributes.get();
		return attributes != null ? attributes.get(key) : null;
	}

	public static void clear() {
		requestAttributes.remove();
	}
}