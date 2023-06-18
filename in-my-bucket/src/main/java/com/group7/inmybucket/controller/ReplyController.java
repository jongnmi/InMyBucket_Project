package com.group7.inmybucket.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.group7.inmybucket.dto.ReplyDTO;
import com.group7.inmybucket.service.ReplyService;

@RestController
public class ReplyController {
	@Autowired
	ReplyService service;
	
	// 댓글 등록
	@PostMapping("/replySend")
	public String replySend(ReplyDTO dto, HttpServletRequest request) {
		dto.setUserid((String)request.getSession().getAttribute("logId"));
		
		int result = service.replyInsert(dto);
		
		return result+"";
	}
	
	// 댓글 목록
	@GetMapping("/replyList")
	public List<ReplyDTO> replyList(int bucket_no){
		return service.replyListSelect(bucket_no);
	}
	
	// 댓글 수정
	@PostMapping("/replyEdit")
	public String replyEdit(ReplyDTO dto, HttpSession session) {
		dto.setUserid((String)session.getAttribute("logId"));
		int result = service.replyUpdate(dto);
		return String.valueOf(result);
	}
	
	// 댓글 삭제
	@GetMapping("/replyDelete")
	public String replyDelete(int comment_no, HttpSession session) {
		String userid = (String)session.getAttribute("logId");
		return String.valueOf(service.replyDelete(comment_no, userid));
	}
}

