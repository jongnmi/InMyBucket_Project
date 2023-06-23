package com.group7.inmybucket.dao;

import java.util.List;


import com.group7.inmybucket.dto.BoardDTO;
import com.group7.inmybucket.dto.CommentDTO;
import com.group7.inmybucket.vo.PagingVO;




public interface CommentDAO {
	// 댓글등록
	public int commentInsert(CommentDTO dto);
	// 댓글목록
	public List<CommentDTO> commentListSelect(int no);
	// 댓글수정
	public int commentUpdate(CommentDTO dto);
	// 댓글삭제
	public int commentDelete(int c_no, String userid);

	// 댓글 여러개삭제
	public int CommentMultiDel(List<Integer> noList);
	
	
	public List<CommentDTO> commentListSelectAll(PagingVO vo);
	// 총 레코드 수
	public int totalRecord(PagingVO vo);
	// 해당 페이지 선택
	public List<BoardDTO> pageSelect(PagingVO vo);
	
}
