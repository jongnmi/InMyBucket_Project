package com.group7.inmybucket.controller;

import java.nio.charset.Charset;



import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.group7.inmybucket.service.BoardService;
import com.group7.inmybucket.dto.BoardDTO;
import com.group7.inmybucket.vo.PagingVO;



// Controller : 酉곕떒�뼱瑜� �궗�슜�븷 �닔 �뾾�떎. �뒪�겕由쏀듃媛� �븘�슂�븯硫� jsp�뙆�씪�쓣 �깮�꽦�븯�뿬 援ы쁽�븳�떎.
// RestController : �봽濡좏듃 �뼵�뼱瑜� 諛깆뿏�뱶�뿉�꽌 湲곗닠�븷 �닔 �엳�뒗 湲곕뒫�쓣 �젣怨듯븳�떎.
//					諛섑솚�삎�쓣 String�쑝濡� �븯硫� 酉고럹�씠吏� �뙆�씪紐낆씠 �븘�땲�씪 而⑦뀗痢� �궡�슜�쑝濡� 泥섎━�븳�떎. -> ModelAndView濡� 由ы꽩�빐�빞�븿
@RestController
@RequestMapping("/board")
public class BoardController {
	@Autowired
	BoardService service;
	// 寃뚯떆�뙋 紐⑸줉
	@GetMapping("boardList") // /board/boardList
	public ModelAndView boardList(PagingVO vo) {
		
		ModelAndView mav = new ModelAndView();
 
		
		// 珥� �젅肄붾뱶 �닔瑜� 援ы븯�뿬
		vo.setTotalRecord(service.totalRecord(vo));
 

		if(vo.getSearchKey() == null)
			vo.setSearchKey("no"); 
		 
 

		// DB議고쉶
		// �빐�떦�럹�씠吏� �젅肄붾뱶 �꽑�깮�븯湲�
		mav.addObject("list", service.pageSelect(vo));
		
		mav.addObject("vo", vo); // 酉고럹�씠吏�濡� �럹�씠吏��젙蹂� �꽭�똿
		mav.setViewName("board/boardList");
		return mav;
	}
	// 湲��벐湲고뤌
	@GetMapping("/boardWrite")
	public ModelAndView boardWrite(HttpServletRequest request, HttpSession session) {
		ModelAndView mav = new ModelAndView(); 
		 
		String logined = session.getAttribute("logStatus").toString();
		
		System.out.println("로그인 상태 : " + logined);
		
		//濡쒓렇�씤 �븞�뻽�쓣寃쎌슦�뒗 濡쒓렇�씤�럹�씠吏�濡�
		if(!logined.equals("Y")) mav.setViewName("/register/loginForm");
		else  mav.setViewName("board/boardWrite");
		
		
		return mav;
	}
	
	
	// 湲��벐湲�(DB�벑濡�)
	@PostMapping("/boardWriteOk")
	//						form		ip(request), 湲��벖�씠(session)
	public ResponseEntity<String> boardWriteOk(BoardDTO dto, HttpServletRequest request) {
		
		String ip = request.getRemoteAddr();
		String userid = (String)request.getSession().getAttribute("logId");
		String username = (String)request.getSession().getAttribute("logNick");
		String subject = request.getParameter("subject");
		String content = request.getParameter("content");

		dto.setUsername(username);   
		dto.setSubject(subject);    
		dto.setContent(content);
		
		dto.setIp(ip); // ip		 		
		dto.setUserid(userid); // 濡쒓렇�씤�븳 �븘�씠�뵒 援ы븯湲�
		
		System.out.println("작성한 사용자 : " + username);

		// 湲��벑濡앹떆 �떎�뙣�븯硫� �삁�쇅 諛쒖깮
		String htmlTag = "<script>";
 
		try {
			int result = service.boardInsert(dto);
			htmlTag += "location.href='boardList';"; 
		
		}catch(Exception e) {
			htmlTag += "alert('글이 등록되지 않았습니다.');"; 
			htmlTag += "history.back();";
		}
		htmlTag += "</script>";
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(new MediaType("text", "html", Charset.forName("UTF-8")));
		headers.add("Content-Type", "text/html; charset=UTF-8");
		
		return new ResponseEntity<String>(htmlTag, headers, HttpStatus.OK);
	}
	// 湲��궡�슜蹂닿린
	@GetMapping("/boardView")
	public ModelAndView boardView(int no, PagingVO vo) {
 
		//議고쉶�닔利앷�
		service.boardHitCount(no);  

 		BoardDTO dto = service.boardSelect(no);
 		
 		ModelAndView mav = new ModelAndView();
 		
 		mav.addObject("dto", dto); // �꽑�깮�븳 �젅肄붾뱶
 		mav.addObject("vo", vo); // �럹�씠吏�踰덊샇, 寃��깋�궎, 寃��깋�뼱
 		
 		mav.setViewName("board/boardView");
		return mav;
		 
	}
	// �닔�젙�뤌
	@GetMapping("/boardEdit")
	public ModelAndView boardEdit(int no, PagingVO vo) {
		BoardDTO dto = service.boardEditSelect(no);
		
		// "	" '		'
		String subject = dto.getSubject().replaceAll("\"", "&quot;"); // " -> \" �젣�뼱臾몄옄
		subject.replaceAll("'", "&#39");
		dto.setSubject(subject);
		
		ModelAndView mav = new ModelAndView();
		mav.addObject("dto", dto);
		mav.addObject("vo", vo);
		
		mav.setViewName("board/boardEdit");
		
		return mav;
	}
 
	
	
	// �닔�젙�벑濡�(DB update)
	@PostMapping("/boardEditOk")
	public ResponseEntity<String> boardEditOk(BoardDTO dto, PagingVO vo, HttpSession session) { // 湲� �젙蹂�, �럹�씠吏��젙蹂�, 濡쒓렇�씤�젙蹂� 
		// no�젅肄붾뱶踰덊샇, 濡쒓렇�씤 �븘�씠�뵒媛� 媛숈쓣 �븣 �뾽�뜲�씠�듃
		dto.setUserid((String)session.getAttribute("logId"));
		String bodyTag = "<script>";
		try {
			service.boardUpdate(dto);
			bodyTag += "location.href='boardView?no="+dto.getNo()+"&nowPage="+vo.getNowPage();
			if(vo.getSearchWord()!=null) {
				bodyTag += "&searchKey="+vo.getSearchKey()+"&searchWord="+vo.getSearchWord();
			}
			bodyTag += "';";
		}catch(Exception e) { 
			// �닔�젙�떎�뙣
			e.printStackTrace();
			bodyTag += "alert('게시판 글 수정에 실패하였습니다.');";
			bodyTag += "history.back();";
		}
		bodyTag += "</script>";
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(new MediaType("text", "html", Charset.forName("UTF-8")));
		headers.add("Content-Type", "text/html; charset=UTF-8");
		
		ResponseEntity<String> entity = new ResponseEntity<String>(bodyTag, headers, HttpStatus.OK);
		
		return entity;
	}
	// �궘�젣
	@GetMapping("/boardDel")
	public ModelAndView boardDel(BoardDTO dto, PagingVO vo, HttpSession session) {
		dto.setUserid((String)session.getAttribute("logId"));
		
		int result = service.boardDelete(dto);
		
		ModelAndView mav = new ModelAndView();
		
		mav.addObject("nowPage", vo.getNowPage());
		if(vo.getSearchWord()!=null) { // 寃��깋�젙蹂� �엳�쓣 �븣
			mav.addObject("searchKey", vo.getSearchKey());
			mav.addObject("searchWord", vo.getSearchWord());
		}
		if(result>0) { // �궘�젣 �꽦怨듭떆 由ъ뒪�듃濡� �씠�룞
			mav.setViewName("redirect:boardList");
		} else { // �궘�젣 �떎�뙣�떆 湲��궡�슜蹂닿린濡� �씠�룞
			mav.addObject("no", dto.getNo());
			mav.setViewName("redirect:boardView");
		}
		
		return mav;
	}
	// �뿬�윭媛쒖쓽 湲��쓣 �븳踰덉뿉 吏��슦湲�
	@PostMapping("/boardMultiDel")
	public ModelAndView boardMultiDel(BoardDTO dto, PagingVO vo) {
		
		System.out.println("test : " + dto.getNoList().toString());
		
		int result = service.boardMultiLineDelete(dto.getNoList()); // �궘�젣
		
		ModelAndView mav = new ModelAndView();
		mav.addObject("nowPage", vo.getNowPage());
		if(vo.getSearchWord()!=null && !vo.getSearchWord().equals("")) {
			mav.addObject("searchKey", vo.getSearchKey());
			mav.addObject("searchWord", vo.getSearchWord());
		}
		mav.setViewName("redirect:boardList");
		return mav;
	}
}
