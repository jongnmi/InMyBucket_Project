package com.group7.inmybucket.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.group7.inmybucket.dto.BucketDTO;
import com.group7.inmybucket.dto.GroupBoardDTO;
import com.group7.inmybucket.dto.GroupCommentDTO;
import com.group7.inmybucket.service.GroupBoardService;
import com.group7.inmybucket.service.GroupCommentService;
import com.group7.inmybucket.service.GroupMemberService;

@Controller
public class GroupBoardController {
	@Autowired
	GroupBoardService gService;
	@Autowired
	GroupCommentService cService;
	@Autowired
	GroupMemberService mService;
	
	@GetMapping("/group/main")
	public ModelAndView groupBucketMain(int groupNo, HttpSession session) {
		ModelAndView mav = new ModelAndView();
		
		if(session.getAttribute("logId") == null || !mService.isMember((String) session.getAttribute("logId"), groupNo)) {
			mav.setViewName("/common/alert");
			mav.addObject("msg", "권한이 없는 사용자입니다.");
			mav.addObject("url", "/inmybucket");
			
			return mav;
		}
		
		try {
			String userid = (String) session.getAttribute("logId");
			boolean isAdmin = gService.isAdmin(userid, groupNo);
			
			mav.addObject("isAdmin", isAdmin);
			
			BucketDTO bucket = gService.getBucket(groupNo);
			String nickname = gService.getNickname(bucket.getUserid());
			mav.addObject("bucketInfo", bucket);
			mav.addObject("nickname", nickname);
		}
		catch(DataAccessException e) {
			e.printStackTrace();
		}

		mav.setViewName("/group/GroupMain");
		return mav;
	}

	@GetMapping("/group/noticeBoard")
	public ModelAndView groupNoticeBoard(int groupNo, HttpSession session) {
		ModelAndView mav = new ModelAndView();
		try {
			List<GroupBoardDTO> noticeList = gService.getGroupNotice(groupNo);
			mav.addObject("noticeList", noticeList);
			
			String userid = (String) session.getAttribute("logId");
			boolean isAdmin = gService.isAdmin(userid, groupNo);
			
			mav.addObject("isAdmin", isAdmin);
			
			int noticeCnt = gService.getTotalNoticeCnt(groupNo);
			mav.addObject("noticeCnt", noticeCnt);
			
			BucketDTO bucket = gService.getBucket(groupNo);
			mav.addObject("bucketNo", bucket.getBucket_no());
			
		} catch (DataAccessException e) {
			e.printStackTrace();
		}
		mav.setViewName("/group/noticeBoard");
		return mav;
	}
	
	@GetMapping("/group/boardList")
	public ModelAndView groupBoardList(int groupNo, HttpSession session) {
		ModelAndView mav = new ModelAndView();
		try {
			List<GroupBoardDTO> boardList = gService.getGroupBoard(groupNo);
			mav.addObject("boardList", boardList);
			
			int boardCnt = gService.getTotalBoardCnt(groupNo);
			mav.addObject("boardCnt", boardCnt);
			
			String userid = (String) session.getAttribute("logId");
			boolean isAdmin = gService.isAdmin(userid, groupNo);
			
			mav.addObject("isAdmin", isAdmin);
			
			BucketDTO bucket = gService.getBucket(groupNo);
			mav.addObject("bucketNo", bucket.getBucket_no());
			
		} catch (DataAccessException e) {
			e.printStackTrace();
		}
		mav.setViewName("/group/boardList");
		return mav;
	}
	
	@GetMapping("/group/noticeBoard/view")
	public ModelAndView groupNoticeView(int boardNo, int groupNo, HttpSession session) {
		ModelAndView mav = new ModelAndView();
		
		try {
			GroupBoardDTO board = gService.getBoardByNo(boardNo);
			board.setUsernick(gService.getNickname(board.getUserid()));

			List<GroupCommentDTO> commentList = cService.getGroupComment(boardNo);
			mav.addObject("commentList", commentList);
			
			String userid = (String) session.getAttribute("logId");
			boolean isAdmin = gService.isAdmin(userid, groupNo);
			
			mav.addObject("isAdmin", isAdmin);
			
			mav.addObject("boardInfo", board);
			
			int commentCount = cService.getGroupCommentCnt(boardNo);
			mav.addObject("commentCount", commentCount);
			
			boolean isCommentAuthor = cService.isAuthor(userid, boardNo);
			mav.addObject("isCommentAuthor", isCommentAuthor);
		} catch(DataAccessException e) {
			e.printStackTrace();
		}
		
		mav.setViewName("/group/boardView");
		return mav;
	}
	
	
	@GetMapping("/group/boardList/view")
	public ModelAndView groupBoardView(int boardNo, int groupNo, HttpSession session) {
		ModelAndView mav = new ModelAndView();
		
		try {
			GroupBoardDTO board = gService.getBoardByNo(boardNo);
			board.setUsernick(gService.getNickname(board.getUserid()));
			
			List<GroupCommentDTO> commentList = cService.getGroupComment(boardNo);
			mav.addObject("commentList", commentList);
			
			String userid = (String) session.getAttribute("logId");
			boolean isAdmin = gService.isAdmin(userid, groupNo);
			boolean isAuthor = gService.isAuthor(userid, boardNo);
			
			mav.addObject("isAdmin", isAdmin);
			mav.addObject("isAuthor", isAuthor);
			
			mav.addObject("boardInfo", board);
			
			int commentCount = cService.getGroupCommentCnt(boardNo);
			mav.addObject("commentCount", commentCount);
			
			boolean isCommentAuthor = cService.isAuthor(userid, boardNo);
			mav.addObject("isCommentAuthor", isCommentAuthor);
		} catch(DataAccessException e) {
			e.printStackTrace();
		}
		
		mav.setViewName("/group/boardView");
		return mav;
	}
	
	@GetMapping("/group/boardWrite")
	public ModelAndView groupBoardEdit(HttpSession session, int groupNo) {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("/group/boardEdit");
		mav.addObject("isNotice", 0);
		mav.addObject("groupNo", groupNo);
		
		boolean isAdmin = gService.isAdmin((String) session.getAttribute("logId"), groupNo);
		mav.addObject("isAdmin", isAdmin);
		
		return mav;
	}
	
	@GetMapping("/group/noticeWrite")
	public ModelAndView groupNoticeEdit(HttpSession session, int groupNo) {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("/group/boardEdit");
		mav.addObject("isNotice", 1);
		mav.addObject("groupNo", groupNo);
		
		boolean isAdmin = gService.isAdmin((String) session.getAttribute("logId"), groupNo);
		mav.addObject("isAdmin", isAdmin);
		
		return mav;
	}

	@PostMapping("/group/postBoard")
	public ModelAndView groupBoardPost(GroupBoardDTO dto, int groupNo, HttpSession session) {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("/common/alert");
		try {
			dto.setUserid((String) session.getAttribute("logId"));
			dto.setUsernick((String) session.getAttribute("logNick"));
			dto.setGroup_no(groupNo);
			
			gService.postGroupBoard(dto);
			mav.addObject("msg", "등록이 완료되었습니다.");
			mav.addObject("url", "/inmybucket/group/main?groupNo=" + dto.getGroup_no());
		} catch (DataAccessException e) {
			e.printStackTrace();
			mav.addObject("msg", "오류가 발생하였습니다.");
			mav.addObject("url", "/inmybucket/group/noticeWrite?groupNo=123");
		}
		return mav;
	}
	
	@GetMapping("/group/updateBoard")
	public ModelAndView groupBoardUpdateView(HttpSession session, int boardNo, int groupNo) {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("/group/boardUpdate");
		try {
			GroupBoardDTO board = gService.getBoardByNo(boardNo);
			mav.addObject("board", board);
			mav.addObject("groupNo", groupNo);
			mav.addObject("isnotice", board.isIsnotice());
		} catch (DataAccessException e) {
			e.printStackTrace();
			mav.addObject("msg", "오류가 발생하였습니다.");
			mav.addObject("url", "history.back()");
		}
		
		return mav;
	}

	@PostMapping("/group/updateBoard")
	public ModelAndView groupBoardUpdate(GroupBoardDTO dto, HttpSession session, int boardNo, int groupNo) {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("/common/alert");
		try {
			System.out.println(dto);
			dto.setGroup_board_no(boardNo);
			dto.setGroup_no(groupNo);
			gService.updateGroupBoard(dto);
			mav.addObject("msg", "수정이 완료되었습니다.");
			mav.addObject("url", "/inmybucket/group/main?groupNo=" + groupNo);
		} catch (DataAccessException e) {
			e.printStackTrace();
			mav.addObject("msg", "오류가 발생하였습니다.");
			mav.addObject("url", "/inmybucket/group/updateBoard?boardNo="+ boardNo +"&groupNo=" + groupNo);
		}
		
		return mav;
	}

	@GetMapping("/group/deleteBoard")
	public ModelAndView groupBoardDelete(HttpSession session, int boardNo, int groupNo) {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("/common/alert");
		try {
			gService.deleteGroupBoard(boardNo, groupNo);
			mav.addObject("msg", "삭제가 완료되었습니다.");
			mav.addObject("url", "/inmybucket/group/main?groupNo=" + groupNo);
		} catch (DataAccessException e) {
			e.printStackTrace();
			mav.addObject("msg", "오류가 발생하였습니다.");
			mav.addObject("url", "/inmybucket/group/updateBoard?boardNo="+ boardNo +"&groupNo=" + groupNo);
		}
		
		return mav;
	}
	
}
