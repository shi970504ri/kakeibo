package jp.co.sss.kakeibo.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class LoginInterceptor implements HandlerInterceptor {
	@Override
	public boolean preHandle(
		HttpServletRequest request,
		HttpServletResponse response,
		Object handler) throws Exception {
		HttpSession session = request.getSession();
		String requestUri = request.getRequestURI();
		String modalParam = request.getParameter("modal");
		if ((requestUri.endsWith("/another/terms") || requestUri.endsWith("/another/privacy") || requestUri.endsWith("/another/disclaimer")) && "true".equals(modalParam)) {
			return true;
		}
		if (session.getAttribute("user") == null) {
			response.sendRedirect(request.getContextPath() + "/user/login");
			return false;
		}
		return true;
	}
}