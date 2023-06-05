package com.group7.inmybucket.controller;

import java.util.List;


import javax.inject.Inject;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.group7.inmybucket.dao.RegisterDAO;
import com.group7.inmybucket.service.BoardService;
import com.group7.inmybucket.service.FeedService;
import com.group7.inmybucket.dto.BoardDTO;
import com.group7.inmybucket.vo.PagingVO;
import com.group7.inmybucket.vo.RegisterVO;



@Controller 
public class AdminBoardController {
	@Autowired
	FeedService Fservice;
	@Inject
	BoardService service;
	
	//관리차 페이지
	@RequestMapping("/adminindex")
	public String adminindex(Model model) {
		
		// 통계자료 보내주기
		model.addAttribute("allBucketCount", Fservice.allBucketCount());
		model.addAttribute("accomplishedBucketCount", Fservice.accomplishedBucketCount());
		model.addAttribute("userExCount", Fservice.userExCount());
		
		System.out.println("피드 내용");
		
		return "admin/adminindex";
	}

	
	

	
	// 게시판 목록
	@RequestMapping("/adminboard") // /board/boardList
	public ModelAndView adminboard(PagingVO vo) {
		
		ModelAndView mav = new ModelAndView();
		// 통계자료 보내주기
		mav.addObject("allBucketCount", Fservice.allBucketCount());
		mav.addObject("accomplishedBucketCount", Fservice.accomplishedBucketCount());
		mav.addObject("userExCount", Fservice.userExCount());
		
		// 총 레코드 수를 구하여
		vo.setTotalRecord(service.totalRecord(vo));

		System.out.println("keyword@ : " + vo.getSearchKey());
		System.out.println("page@ : " + vo.toString());
		
		if(vo.getSearchKey() == null)
			vo.setSearchKey("no");
		
		// DB조회
		// 해당페이지 레코드 선택하기
		mav.addObject("list", service.pageSelect(vo));
		
		mav.addObject("vo", vo); // 뷰페이지로 페이지정보 세팅
		mav.setViewName("admin/board/adminboard");
		return mav;
	}
	// 글내용보기
	@RequestMapping("/adboardView")
	public ModelAndView adboardView(int no, PagingVO vo) {
		
		// 조회수 증가
		service.boardHitCount(no);
	
		BoardDTO dto = service.boardSelect(no);
		
		ModelAndView mav = new ModelAndView();
		
		mav.addObject("dto", dto); // 선택한 레코드
		mav.addObject("vo", vo); // 페이지번호, 검색키, 검색어
		
		mav.setViewName("admin/board/adboardView");
		
		return mav;
	}
	
	
	
	// 삭제
	@GetMapping("/boardDel")
	public ModelAndView boardDel(BoardDTO dto, PagingVO vo, HttpSession session) {

		
		int result = service.boardDelete(dto);
		
		ModelAndView mav = new ModelAndView();
		
		mav.addObject("nowPage", vo.getNowPage());
		if(vo.getSearchWord()!=null) { // 검색정보 있을 때
			mav.addObject("searchKey", vo.getSearchKey());
			mav.addObject("searchWord", vo.getSearchWord());
		}
		if(result>0) { // 삭제 성공시 리스트로 이동
			mav.setViewName("redirect:admin/board/adminboard");
		} else { // 삭제 실패시 글내용보기로 이동
			mav.addObject("no", dto.getNo());
			mav.setViewName("redirect:admin/board/adminboard");
		}
		
		return mav;
	}
	// 여러개의 글을 한번에 지우기
	@PostMapping("/boardMultiDel")
	public ModelAndView boardMultiDel(BoardDTO dto, PagingVO vo) {
		System.out.println("test");
		int result = service.boardMultiLineDelete(dto.getNoList()); // 삭제
		
		ModelAndView mav = new ModelAndView();
		mav.addObject("nowPage", vo.getNowPage());
		if(vo.getSearchWord()!=null && !vo.getSearchWord().equals("")) {
			mav.addObject("searchKey", vo.getSearchKey());
			mav.addObject("searchWord", vo.getSearchWord());
		}
		mav.setViewName("redirect:admin/board/adminboard");
		return mav;
	}
	

	 

}
