package com.group7.inmybucket.controller;

import java.util.List;



import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.group7.inmybucket.service.CommentService;
import com.group7.inmybucket.service.FeedService;
import com.group7.inmybucket.dto.CommentDTO;
import com.group7.inmybucket.vo.PagingVO;
import com.group7.inmybucket.vo.RegisterVO;


@RestController
public class CommentController {
	
	@Autowired
	CommentService service;
	
	@Autowired
	FeedService Fservice;
	
	// 여러개의 글을 한번에 지우기
	@RequestMapping("/CommentMultiDel") 
	public ModelAndView CommentMultiDel(CommentDTO cdt, PagingVO vo) {
		  
		int result = service.CommentMultiDel(cdt.getNoList()); // 삭제

		ModelAndView mav = new ModelAndView();
		mav.addObject("nowPage", vo.getNowPage());
		if(vo.getSearchWord()!=null && !vo.getSearchWord().equals("")) {
			mav.addObject("searchKey", vo.getSearchKey());
			mav.addObject("searchWord", vo.getSearchWord());
		}
 
		mav.setViewName("redirect:commentListAll");
 
		return mav;
	}
	
	@PostMapping("/commentSend")
	public String commentSend(CommentDTO dto, HttpServletRequest request) {
		dto.setIp(request.getRemoteAddr()); // ip
		dto.setUserid((String)request.getSession().getAttribute("logId")); //
		
		int result = service.commentInsert(dto);
		
		return result+"";
	}
	
	// 댓글목록
	@GetMapping("/commentList")
	public List<CommentDTO> commentList(int no) {
		System.out.println(no + "");
		
		
		List<CommentDTO> cdt = service.commentListSelect(no);
 
		
		return cdt;
		
	}
	
	// 댓글수정
	@PostMapping("/commentEdit")
	public String commentEdit(CommentDTO dto, HttpSession session) {
		dto.setUserid((String)session.getAttribute("logId"));
		int result = service.commentUpdate(dto);
		return String.valueOf(result);
	}
	// 댓글삭제
	@GetMapping("/commentDelete")
	public String commentDelete(int c_no, HttpSession session) {
		String userid = (String)session.getAttribute("logId");
		return String.valueOf(service.commentDelete(c_no, userid));
	}

 
	
	//////////////////////////////////////////////
	
	@GetMapping("/commentListAll")
	public ModelAndView commentListAll(PagingVO vo) {
		
		ModelAndView mav = new ModelAndView();
		
		// 통계자료 보내주기
		mav.addObject("allBucketCount", Fservice.allBucketCount());
		mav.addObject("accomplishedBucketCount", Fservice.accomplishedBucketCount());
		mav.addObject("userExCount", Fservice.userExCount());

		System.out.println("key : " + vo.getSearchKey());
		System.out.println("word : " + vo.getSearchWord());
		System.out.println("vo : " + vo.toString());
		 
		
		// 총 레코드 수를 구하여
		vo.setTotalRecord(service.totalRecord(vo));
 
		
		List<CommentDTO> cls = service.commentListSelectAll(vo);
 

		mav.addObject("list", cls); // 뷰페이지로 페이지정보 세팅
		mav.addObject("vo", vo); // 뷰페이지로 페이지정보 세팅
		mav.setViewName("admin/comment/commentList");
		return mav;

	}
}
