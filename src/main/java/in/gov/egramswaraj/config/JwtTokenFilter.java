package in.gov.egramswaraj.config;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

import in.gov.egramswaraj.exception.InvalidJwtException;
import in.gov.egramswaraj.utility.ApplicationUtility;
import in.gov.egramswaraj.utility.JwtClaimKeys;
import in.gov.egramswaraj.utility.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtTokenFilter extends OncePerRequestFilter {

	private final JwtUtil jwtUtil;

	public JwtTokenFilter(JwtUtil jwtUtil) {
		this.jwtUtil = jwtUtil;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		final String authHeader = request.getHeader("Authorization");

		try {
			if (authHeader != null && authHeader.startsWith("Bearer ")) {
				String token = authHeader.substring(7);
				Claims claims = jwtUtil.getClaimsFromToken(token);

				UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
						claims.getSubject(), null, List.of(new SimpleGrantedAuthority("ROLE_" + claims.get("role"))));

				authentication.setDetails(claims.get("userId", Long.class));
				SecurityContextHolder.getContext().setAuthentication(authentication);

				ApplicationUtility.setAttribute(JwtClaimKeys.USER_NAME,
						Optional.ofNullable(claims.getSubject()).orElse("UNKNOWN"));

				ApplicationUtility.setAttribute(JwtClaimKeys.ROLE,
						Optional.ofNullable(claims.get(JwtClaimKeys.ROLE)).orElse("UNKNOWN"));

				ApplicationUtility.setAttribute(JwtClaimKeys.USER_ID,
						Optional.ofNullable(claims.get(JwtClaimKeys.USER_ID)).orElse("0"));

				ApplicationUtility.setAttribute(JwtClaimKeys.STATE_CODE,
						Optional.ofNullable(claims.get(JwtClaimKeys.STATE_CODE)).orElse("0"));

				ApplicationUtility.setAttribute(JwtClaimKeys.DISTRICT,
						Optional.ofNullable(claims.get(JwtClaimKeys.DISTRICT)).orElse("0"));

				ApplicationUtility.setAttribute(JwtClaimKeys.SUB_DISTRICT,
						Optional.ofNullable(claims.get(JwtClaimKeys.SUB_DISTRICT)).orElse("0"));

				ApplicationUtility.setAttribute(JwtClaimKeys.ENTITY_TYPE_ID,
						Optional.ofNullable(claims.get(JwtClaimKeys.ENTITY_TYPE_ID)).orElse("0"));

				ApplicationUtility.setAttribute(JwtClaimKeys.ENTITY_CODE,
						Optional.ofNullable(claims.get(JwtClaimKeys.ENTITY_CODE)).orElse("0"));

				ApplicationUtility.setAttribute(JwtClaimKeys.YEAR,
						Optional.ofNullable(claims.get(JwtClaimKeys.YEAR)).orElse("2025"));

			}

			filterChain.doFilter(request, response);

		} catch (InvalidJwtException ex) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			response.setContentType("application/json");
			response.getWriter()
					.write(new ObjectMapper().writeValueAsString(
							Map.of("timestamp", LocalDateTime.now().toString(), "status", 401, "error", "Unauthorized",
									"message", ex.getMessage(), "path", request.getRequestURI())));
		} finally {

			ApplicationUtility.clear();
		}
	}
}