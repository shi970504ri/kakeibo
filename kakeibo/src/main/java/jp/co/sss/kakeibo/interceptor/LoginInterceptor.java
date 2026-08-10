package jp.co.sss.kakeibo.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class LoginInterceptor implements HandlerInterceptor {
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		HttpSession session = request.getSession(false);
		Object loginUser = (session != null) ? session.getAttribute("userId") : null;
		if (loginUser == null) {
			String requestURI = request.getRequestURI();
			if (requestURI.contains("/api/")) {
				response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
				return false;
			}
			response.sendRedirect(request.getContextPath() + "/user/login");
			return false;
		}
		return true;
	}
}