package com.group7.inmybucket;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.lang.Nullable;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/*
interceptor泥섎━�븷 �겢�옒�뒪
諛섎뱶�떆 HandlerInterceptorAdapter瑜� �긽�냽諛쏆븘 留뚮뱾�뼱�빞�븳�떎.

*/
public class LoginInterceptor extends HandlerInterceptorAdapter {
	//而⑦듃濡ㅻ윭媛� �샇異쒕릺湲� �쟾�뿉 �떎�뻾�맂�떎.
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception{
		//濡쒓렇�씤 �쑀臾대�� �솗�씤�븯怨� 濡쒓렇�씤 �맂 寃쎌슦 �샇異쒗븳 留ㅽ븨二쇱냼濡� �씠�룞�븯怨�
		//濡쒓렇�씤�씠 �븞�맂 寃쎌슦 濡쒓렇�씤 �뤌�쑝濡� �떎�뻾�씠 �씠�룞�릺�룄濡앺븳�떎.
		
		HttpSession session = request.getSession();
		
		String logId = (String)session.getAttribute("logId");
		String logStatus = (String)session.getAttribute("logStatus"); // null, "Y"
		String permission = (String)session.getAttribute("logPermission");
		
		if(logStatus==null || !logStatus.equals("Y")) {//濡쒓렇�씤�씠 �븞�맂寃쎌슦 -> 媛��뜕湲� 硫덉텛怨� 濡쒓렇�씤�쑝濡� 蹂대궦�떎. 
			response.sendRedirect(request.getContextPath()+"/loginForm");
			return false;
		} /*
			 * else if(logStatus.equals("Y") && permission.equals("9")) {
			 * response.sendRedirect(request.getContextPath()+"/adminindex"); return false;
			 * }
			 */
		//諛섑솚�삎�씠 false�씠硫� 留ㅽ븨�쓣 蹂�寃쏀븯怨�
		//諛섑솚�삎�씠 true�씠硫� 留ㅽ븨�쓣 吏��냽�븳�떎.
		return true;
	}
	//而⑦듃濡ㅻ윭媛� �떎�뻾�썑 View濡� �씠�룞�븯湲곗쟾�뿉 �떎�뻾�릺�뒗 硫붿냼�뱶
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			@Nullable ModelAndView mav) throws Exception{
		
	}
	//而⑦듃濡ㅻ윭媛� �떎�뻾�썑 �샇異쒕릺�뒗 硫붿냼�뱶
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
			@Nullable Exception ex) throws Exception{
		
	}
}