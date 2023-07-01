package com.group7.inmybucket.dao;


import java.util.List;

import com.group7.inmybucket.vo.PagingVO;
import com.group7.inmybucket.vo.RegisterVO;





public interface RegisterDAO {
	//아이디 중복검사 - 아이디의 갯수를 구한다.
	public int idCheckCount(String userid);
	//회원가입
	public int registerInsert(RegisterVO vo);
	//로그인
	public RegisterVO loginOk(String userid, String userpwd, String username, String usernick, String permission, String email);
	//회원정보 수정 폼
	public RegisterVO registerEdit(String userid);
	//회원정보 수정 DB update
	public int registerEditOk(RegisterVO vo); 
	//아이디찾기
	RegisterVO memberIdSearch(RegisterVO searchVO);
	
	//비번변경
	int passwordUpdate(RegisterVO searchVO);
	//비번 메일
	public String psSearch(String username, String email);
	public RegisterVO removeForm(String userid, String userpwd);
	//회원탈퇴
	public int remove(RegisterVO vo);
	public int registerprofile(RegisterVO vo);
	public int kakaoOk(RegisterVO userTO);
	public RegisterVO kaka(RegisterVO userTo);
	
	
	//관리자 - 회원보여주기
	public List<RegisterVO> userlist(PagingVO vo);
	// 총 레코드 수
	public int totalRecord(PagingVO vo);
	// 해당 페이지 선택
	public List<RegisterVO> pageSelect(PagingVO vo);
	// 유저 선택(no)
	public RegisterVO userSelect(int no);
	// 삭제
	public int userDelete(RegisterVO vo);
	// 여러글 한번에 삭제
	public int userMultiLineDelete(List<String> noList);
}
