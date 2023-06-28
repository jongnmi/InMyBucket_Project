package com.group7.inmybucket.dao;

import java.util.List;

import com.group7.inmybucket.dto.BucketDTO;
import com.group7.inmybucket.dto.GroupBoardDTO;
import com.group7.inmybucket.dto.GroupCommentDTO;
import com.group7.inmybucket.dto.UserDTO;

public interface GroupCommentDAO {
	/**
	 * 그룹 게시판 댓글 가져오기
	 * group_board_no: 그룹 게시판의 게시글 번호
	 *  */
	public List<GroupCommentDTO> getGroupComment(int boardNo);
	/** 그룹 게시판 댓글 작성 */
	public int postGroupComment(GroupCommentDTO dto);
	/** 그룹 게시판 댓글 수정 */
	public int updateGroupComment(GroupCommentDTO dto);
	/** 그룹 게시판 댓글 삭제 */
	public int deleteGroupComment(int commentNo);
	/** 그룹 게시판 댓글 수 가져오기 */
	public int getGroupCommentCnt(int boardNo);
	/** 댓글 작성자인지 확인 */
	public boolean isAuthor(String userid, int boardNo);
}
