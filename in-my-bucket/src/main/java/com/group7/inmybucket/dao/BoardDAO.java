package com.group7.inmybucket.dao;

import java.util.List;


import com.group7.inmybucket.dto.BoardDTO;
import com.group7.inmybucket.vo.PagingVO;



public interface BoardDAO {
	// 글등록
	public int boardInsert(BoardDTO dto);
	// 총 레코드 수
	public int totalRecord(PagingVO vo);
	// 해당 페이지 선택
	public List<BoardDTO> pageSelect(PagingVO vo);
	// 글 선택(no)
	public BoardDTO boardSelect(int no);
	// 글 선택(수정)
	public BoardDTO boardEditSelect(int no);
	// 글 수정(DB)
	public int boardUpdate(BoardDTO dto);
	// 조회수 증가
	public void boardHitCount(int no);
	// 삭제
	public int boardDelete(BoardDTO dto);
	// 여러글 한번에 삭제
	public int boardMultiLineDelete(List<Integer> noList);
	
	
	// 삭제
	public int adboardDelete(BoardDTO dto);
	// 여러글 한번에 삭제
	public int adboardMultiLineDelete(List<Integer> noList);
}
