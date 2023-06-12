package com.group7.inmybucket.controller;

import java.util.Date;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import com.group7.inmybucket.dto.GroupCommentDTO;
import com.group7.inmybucket.service.GroupCommentService;

@Controller
public class GroupCommentController {
	@Autowired
	GroupCommentService service;
	
	@PostMapping("/group/board/comment/post")
	public ModelAndView postComment(GroupCommentDTO dto, int boardNo, int groupNo, HttpSession session) {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("/common/alert");
		mav.addObject("url", "/inmybucket/group/boardList/view?boardNo=" + boardNo + "&groupNo=" + groupNo);
		
		try {
			dto.setUserid((String) session.getAttribute("logId"));
			dto.setUsernick((String) session.getAttribute("logNick"));
			service.postGroupComment(dto);
			mav.addObject("msg", "등록이 완료되었습니다.");
		} catch(DataAccessException e) {
			e.printStackTrace();
			mav.addObject("msg", "오류가 발생하였습니다.");
		}
		
		return mav;
	}
	
	@PostMapping("/group/board/comment/update")
	public ModelAndView updateComment(GroupCommentDTO dto, int groupNo) {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("/common/alert");
		mav.addObject("url", "/inmybucket/group/boardList/view?boardNo=" + dto.getGroup_board_no() + "&groupNo=" + groupNo);
		System.out.println(dto.toString());
		try {
			service.updateGroupComment(dto);
			mav.addObject("msg", "수정되었습니다.");
		} catch(DataAccessException e) {
			mav.addObject("msg", "오류가 발생하였습니다.");
			e.printStackTrace();
		}
		
		return mav;
	}
	
	@GetMapping("/group/board/comment/delete")
	public ModelAndView deleteComment(GroupCommentDTO dto, int boardNo, int commentNo, int groupNo, HttpSession session) {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("/common/alert");
		mav.addObject("url", "/inmybucket/group/boardList/view?boardNo=" + boardNo + "&groupNo=" + groupNo);
		
		try {
			service.deleteGroupComment(commentNo);
			mav.addObject("msg", "삭제가 완료되었습니다.");			
		} catch(DataAccessException e) {
			mav.addObject("msg", "오류가 발생하였습니다.");
			e.printStackTrace();
		}
		
		return mav;
	}
}
