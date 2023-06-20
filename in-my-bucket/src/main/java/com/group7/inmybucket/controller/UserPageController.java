package com.group7.inmybucket.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.group7.inmybucket.dto.ProfileDTO;
import com.group7.inmybucket.service.UserPageService;
import com.group7.inmybucket.vo.FilteringVO;
import com.group7.inmybucket.vo.UserDataVO;
import com.group7.inmybucket.vo.UserFeedVO;

@Controller
public class UserPageController {
	@Autowired
	UserPageService service;

	@RequestMapping("/userpage")
	public ModelAndView userpage(@RequestParam(name="userid") String userid, FilteringVO vo, HttpServletRequest request) {
		ModelAndView mav = new ModelAndView();
		
		System.out.println("userid="+userid);
		
		// 피드
		// 보내져온 pageNum이 있으면 pageNum 바꿔주기
		String strPageNum = request.getParameter("pageNum");
		if(strPageNum != null) {
			vo.setPageNum(Integer.parseInt(strPageNum));
		}
		// 필터링 내용 넘겨주기
		String strListFilter = request.getParameter("listFilter");
		String strListSort = request.getParameter("listSort");
		
		vo.setListFilter(strListFilter);
		vo.setListSort(strListSort);
		
		System.out.println(vo.getListFilter()+", "+vo.getListSort());
		
		UserFeedVO userFeedVo = new UserFeedVO();
		userFeedVo.setVo(vo);
		userFeedVo.setUserid(userid);
		
		// 보여지는 피드 수
		vo.setFeedCount(service.feedCount(userFeedVo));
		
		// 뿌려주는 피드 리스트
		mav.addObject("list", service.listUser(userFeedVo));
		mav.addObject("vo",vo);
		
		System.out.println("vo="+vo.toString()+", userid="+userid);
		// 피드
		
		ProfileDTO profile = service.getProfile(userid);
		String usernick = service.getUsernick(userid);
		UserDataVO userData = service.getUserData(userid);
		String logId = (String)request.getSession().getAttribute("logId"); // 세션 로그아이디
		
		System.out.println("logId="+logId);
		
		mav.addObject("logId", logId); // 로그인안했으면 null
		mav.addObject("userData", userData);
		mav.addObject("usernick", usernick);
		mav.addObject("profile", profile);
		
		
		mav.setViewName("profile/userpage");

		return mav;
	}
	
	@ResponseBody
	@RequestMapping("/userpageVisibleCheck.do")
	public int userpageVisibleCheck(@RequestParam("userid") String userid) {
		
		int visible = service.getProfileVisible(userid);
		
		return visible;
	}

}
