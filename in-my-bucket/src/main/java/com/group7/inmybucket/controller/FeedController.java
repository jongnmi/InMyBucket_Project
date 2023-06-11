package com.group7.inmybucket.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.group7.inmybucket.dto.FeedDTO;
import com.group7.inmybucket.dto.ImageFileDTO;
import com.group7.inmybucket.service.FeedService;
import com.group7.inmybucket.vo.FilteringVO;

@RestController
public class FeedController {
	@Autowired
	FeedService service;
	
	@RequestMapping(value="/feed")
	public ModelAndView feedList(FilteringVO vo, HttpServletRequest request) {
		ModelAndView mav = new ModelAndView();
		
		// 보내져온 pageNum이 있으면 pageNum 바꿔주기
		String strPageNum = request.getParameter("pageNum");
		if(strPageNum != null) {
			vo.setPageNum(Integer.parseInt(strPageNum));
		}
		
		// 필터링 내용 넘겨주기
		String strCategory = request.getParameter("category");
		String strListFilter = request.getParameter("listFilter");
		String strListSort = request.getParameter("listSort");
		String strSearchWord = request.getParameter("searchWord");
		vo.setCategory(strCategory);
		vo.setListFilter(strListFilter);
		vo.setListSort(strListSort);
		vo.setSearchWord(strSearchWord);
		
		// 보여지는 총 피드 수
		vo.setFeedCount(service.feedCount(vo));
		System.out.println("feedCount="+vo.getFeedCount());
		// 검색된 유저 수
		vo.setUserCount(service.userCount(vo));
		if(strSearchWord!=null && strSearchWord!="") System.out.println("userCount="+vo.getUserCount());
		
		System.out.println(service.listAll(vo));
		
		mav.addObject("userList", service.userList(vo));
		mav.addObject("list", service.listAll(vo));
		mav.addObject("vo",vo);
		System.out.println("userList="+service.userList(vo));
		
		mav.setViewName("bucket/bucketFeed");
		return mav;
	}
	
}
