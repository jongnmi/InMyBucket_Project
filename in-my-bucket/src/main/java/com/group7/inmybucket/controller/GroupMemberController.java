package com.group7.inmybucket.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import com.group7.inmybucket.dto.UserDTO;
import com.group7.inmybucket.service.GroupMemberService;

@Controller
public class GroupMemberController {
	@Autowired
	GroupMemberService service;
	
	@GetMapping("/group/member/manage")
	public ModelAndView getMembers(int groupNo) {
		ModelAndView mav = new ModelAndView();
		try {
			List<UserDTO> memberList = service.getMembers(groupNo);
			List<String> queueList = service.getQueueMembers(groupNo);

			mav.addObject("memberList", memberList);
			mav.addObject("queueList", queueList);
			mav.addObject("groupNo", groupNo);
		} catch(DataAccessException e) {
			e.printStackTrace();
		}
		mav.setViewName("/group/memberManage");
		
		return mav;
	}
	
	@GetMapping("/group/member/manage/approve")
	public ModelAndView addGroupMember(String userid, int groupNo) {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("/common/alert");
		
		try {
			if(service.isMember(userid, groupNo))
				mav.addObject("msg", "이미 존재하는 사용자입니다.");
			
			else if(!service.inQueue(userid, groupNo))
				mav.addObject("msg", "승인 목록에 존재하지 않는 사용자입니다.");
			
			else {
				service.addGroupMember(userid, groupNo);
				service.deleteInQueue(userid, groupNo);
				mav.addObject("msg", "승인되었습니다.");
			}
		} catch(DataAccessException e) {
			mav.addObject("msg", "오류가 발생하였습니다.");
			e.printStackTrace();
		}
		mav.addObject("url", "/inmybucket/group/member/manage?groupNo=" + groupNo);
		return mav;
	}
	
	@GetMapping("/group/member/manage/denied")
	public ModelAndView groupMemeberManage(String userid, int groupNo) {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("/common/alert");
		
		try {
			if(service.isMember(userid, groupNo))
				mav.addObject("msg", "이미 존재하는 사용자입니다.");
			
			else if(!service.inQueue(userid, groupNo))
				mav.addObject("msg", "승인 목록에 존재하지 않는 사용자입니다.");
			
			else {
				service.deleteInQueue(userid, groupNo);
				mav.addObject("msg", "승인 거부하였습니다.");
			}
			
		} catch(DataAccessException e) {
			e.printStackTrace();
		}
		mav.addObject("url", "/inmybucket/group/member/manage?groupNo=" + groupNo);
		
		return mav;
	}
	
	@GetMapping("/group/member/manage/kick")
	public ModelAndView groupMemberKick(String userid, int groupNo) {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("/common/alert");
		
		try {
			if(!service.isMember(userid, groupNo))
				mav.addObject("msg", "존재하지 않는 사용자입니다.");
			else {
				service.deleteGroupMember(userid, groupNo);
				mav.addObject("msg", "추방하였습니다.");
			}
		} catch(DataAccessException e) {
			e.printStackTrace();
		}
		
		mav.addObject("url", "/inmybucket/group/member/manage?groupNo=" + groupNo);
		
		return mav;
	}
	
	@GetMapping("/group/member/manage/leave")
	public ModelAndView groupMemberLeave(HttpSession session, int groupNo) {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("/common/alert");
		
		try {
			String userid = (String) session.getAttribute("logId");
			service.deleteGroupMember(userid, groupNo);
			mav.addObject("msg", "탈퇴가 완료되었습니다.");
		} catch(DataAccessException e) {
			mav.addObject("msg", "오류가 발생하였습니다.");
			e.printStackTrace();
		}
		
		mav.addObject("url", "/inmybucket");
		return mav;
	}
	
}
