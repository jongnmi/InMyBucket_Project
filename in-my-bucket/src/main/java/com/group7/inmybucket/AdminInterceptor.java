package com.group7.inmybucket;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.lang.Nullable;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/*
interceptor처리할 클래스
반드시 HandlerInterceptorAdapter를 상속받아 만들어야한다.

*/
public class AdminInterceptor extends HandlerInterceptorAdapter {
	//컨트롤러가 호출되기 전에 실행된다.
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception{
		//로그인 유무를 확인하고 로그인 된 경우 호출한 매핑주소로 이동하고
		//로그인이 안된 경우 로그인 폼으로 실행이 이동되도록한다.
		
		HttpSession session = request.getSession();
		
		String logId = (String)session.getAttribute("logId");
		String logStatus = (String)session.getAttribute("logStatus");
		String permission = (String)session.getAttribute("logPermission");
		
		if(logStatus==null || !permission.equals("9")) {
			response.sendRedirect(request.getContextPath()+"/");
			return false;
		} 
		//반환형이 false이면 매핑을 변경하고
		//반환형이 true이면 매핑을 지속한다.
		return true;
	}
	//컨트롤러가 실행후 View로 이동하기전에 실행되는 메소드
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			@Nullable ModelAndView mav) throws Exception{
		
	}
	//컨트롤러가 실행후 호출되는 메소드
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
			@Nullable Exception ex) throws Exception{
		
	}
}
