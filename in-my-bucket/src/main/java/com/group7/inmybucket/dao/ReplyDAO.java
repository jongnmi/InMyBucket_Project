package com.group7.inmybucket.dao;

import java.util.List;

import com.group7.inmybucket.dto.ReplyDTO;

public interface ReplyDAO {
	// 댓글등록
	public int replyInsert(ReplyDTO dto);
	// 댓글목록
	public List<ReplyDTO> replyListSelect(int bucket_no);
	// 댓글수정
	public int replyUpdate(ReplyDTO dto);
	// 댓글삭제 
	public int replyDelete(int comment_no, String userid);
}
