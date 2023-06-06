package com.group7.inmybucket.controller;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

import javax.inject.Inject;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.group7.inmybucket.dao.RegisterDAO;
import com.group7.inmybucket.service.CommentService;
import com.group7.inmybucket.service.FeedService;
import com.group7.inmybucket.service.RegisterServiece;
import com.group7.inmybucket.dto.BoardDTO;
import com.group7.inmybucket.dto.CommentDTO;
import com.group7.inmybucket.vo.PagingVO;
import com.group7.inmybucket.vo.RegisterVO;

 

@Controller
/* @RequestMapping("/admin") */
public class AdminUserController {
	
	private static final Logger logger = LoggerFactory.getLogger(AdminUserController.class);
	
	@Inject
	RegisterServiece service;
	
	@Inject
	CommentService coservice;
	
	@Autowired
	FeedService Fservice;
	
	//////////////////////////////////////////////////////////////////
	
	/*
	 * public String commentlist(Model model) { List<CommentDTO> commnetlist =
	 * coservice.commentListSelect(no); return "admin/comment/commentlist"; }
	 */
	
	
	
	/////////////////////////////////////////////////////////////////
 


	// 여러개의 글을 한번에 지우기
	@PostMapping("/adminMultiDel") 
	public ModelAndView adminMultiDel(RegisterVO rvo, PagingVO vo) {
		 
		int result = service.userMultiLineDelete(rvo.getNoList()); // 삭제
		 
		ModelAndView mav = new ModelAndView(); 


		mav.setViewName("redirect:userlist");
 
		return mav;
	}
	
	
    @GetMapping("/userlist")
    public ModelAndView userlist(Model model,PagingVO vo){
    	

		ModelAndView mav = new ModelAndView();
		 
		// 통계자료 보내주기
		mav.addObject("allBucketCount", Fservice.allBucketCount());
		mav.addObject("accomplishedBucketCount", Fservice.accomplishedBucketCount());
		mav.addObject("userExCount", Fservice.userExCount());

		// 총 레코드 수를 구하여
		vo.setTotalRecord(service.totalRecord(vo));
 
        
		//CDATA에 Page정보를 전송해서 결과가져오기
        List<RegisterVO> userlist = service.userlist(vo);
          
        
        //번호순으로 정렬
        //Collections.sort(userlist, new MyObjectComparator());
         
        
        
        model.addAttribute("list",userlist);
         
		 
		mav.addObject("vo", vo); // 뷰페이지로 페이지정보 세팅
		mav.setViewName("admin/user/userlist");
        return mav;
    }
	
	 
	/*
	 * // 글내용보기
	 * 
	 * @GetMapping("/adboardView") public ModelAndView adboardView(int no, PagingVO
	 * vo) {
	 * 
	 * // 조회수 증가 service.boardHitCount(no);
	 * 
	 * BoardDTO dto = service.boardSelect(no);
	 * 
	 * ModelAndView mav = new ModelAndView();
	 * 
	 * mav.addObject("dto", dto); // 선택한 레코드 mav.addObject("vo", vo); // 페이지번호, 검색키,
	 * 검색어
	 * 
	 * mav.setViewName("board/boardView");
	 * 
	 * return mav; }
	 */
	
	// 삭제
	@GetMapping("/aduserDel")
	public ModelAndView aduserDel(RegisterVO rvo, PagingVO vo, HttpSession session) {
		
//		dto.setUserid((String)session.getAttribute("logId"));
		
		int result = service.userDelete(rvo);
		
		ModelAndView mav = new ModelAndView();
		
		mav.addObject("nowPage", vo.getNowPage());
		if(vo.getSearchWord()!=null) { // 검색정보 있을 때
			mav.addObject("searchKey", vo.getSearchKey());
			mav.addObject("searchWord", vo.getSearchWord());
		}
		if(result>0) { // 삭제 성공시 리스트로 이동
			mav.setViewName("admin/adminindex");
		} else { // 삭제 실패시 글내용보기로 이동
			mav.addObject("no", rvo.getUserid());
			mav.setViewName("redirect:adminindex");
		}
		
		return mav;
	}
	// 여러개의 글을 한번에 지우기
	@PostMapping("/aduserMultiDel")
	public ModelAndView aduserMultiDel(RegisterVO rvo, PagingVO vo) {
		//int result = service.userMultiLineDelete(rvo.getNoList()); // 삭제
		
		ModelAndView mav = new ModelAndView();
		mav.addObject("nowPage", vo.getNowPage());
		if(vo.getSearchWord()!=null && !vo.getSearchWord().equals("")) {
			mav.addObject("searchKey", vo.getSearchKey());
			mav.addObject("searchWord", vo.getSearchWord());
		}
		mav.setViewName("redirect:admin/adminindex");
		return mav;
	}

 
	 
 
	 

}

