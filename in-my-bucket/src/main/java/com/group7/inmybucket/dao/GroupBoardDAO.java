package com.group7.inmybucket.dao;

import java.util.List;

import com.group7.inmybucket.dto.BucketDTO;
import com.group7.inmybucket.dto.GroupBoardDTO;

public interface GroupBoardDAO {
	/** 그룹 버킷 정보 가져오기 */
	public BucketDTO getBucket(int groupNo);
	/** 그룹 내 공지사항 제외 게시글 가져오기 */
	public List<GroupBoardDTO> getGroupBoard(int groupNo);
	/** 그룹 내 공지사항 제목, 번호 가져오기 */
	public List<GroupBoardDTO> getGroupNotice(int groupNo);
	/** 그룹 내 게시판 글 작성 */
	public int postGroupBoard(GroupBoardDTO dto);
	/** 그룹 내 게시판 글 수정 */
	public int updateGroupBoard(GroupBoardDTO dto);
	/** 그룹 내 게시판 글 삭제 */
	public int deleteGroupBoard(int boardNo, int groupNo);
	/** 작성자 닉네임 가져오기 */
	public String getNickname(String userid);
	/** 게시글 번호로 게시글 가져오기 */
	public GroupBoardDTO getBoardByNo(int boardNo);
	/** 공지사항 수 가져오기 */
	public int getTotalNoticeCnt(int groupNo);
	/** 게시글 수 가져오기 */
	public int getTotalBoardCnt(int groupNo);
	/** 관리자인지 확인 */
	public boolean isAdmin(String userid, int groupNo);
	/** 글 작성자인지 확인 */
	public boolean isAuthor(String userid, int boardNo);
}
