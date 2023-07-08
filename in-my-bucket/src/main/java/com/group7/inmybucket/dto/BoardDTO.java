package com.group7.inmybucket.dto;

import java.util.List;

public class BoardDTO {
	private int no;
	private String subject;
	private String content;
	
	private String userid;
	private String usernick;
	private String username;
	
	private int hit;
	private int likecnt;
	private String writedate;
	private String ip;
	
	// 여러개의 레코드 한번에 삭제할 때 필요한 레코드 번호
	private List<Integer> noList;
	
	@Override
	public String toString() {
		return "BoardDTO [no=" + no + ", subject=" + subject + ", content=" + content + ", userid=" + userid
				+ ", usernick=" + usernick + ", username=" + username + ", hit=" + hit + ", likecnt=" + likecnt + ", writedate=" + writedate
				+ ", ip=" + ip + "]";
	}

	

	public int getNo() {
		return no;
	}

	public void setNo(int no) {
		this.no = no;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}
//--------------------------------------------------
	public String getUsernick() {
		return usernick;
	}


	public void setUsernick(String username) {
		this.usernick = username;
	}
// -------------------------------------------------
	public String getUsername() {
		return username;
	}


	public void setUsername(String username) {
		this.username = username;
	}
// -------------------------------------------------
	public int getHit() {
		return hit;
	}

	public void setHit(int hit) {
		this.hit = hit;
	}

	public int getLikecnt() {
		return likecnt;
	}

	public void setLikecnt(int likecnt) {
		this.likecnt = likecnt;
	}

	public String getWritedate() {
		return writedate;
	}

	public void setWritedate(String writedate) {
		this.writedate = writedate;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}


	// ---------------------------------------
	public List<Integer> getNoList() {
		return noList;
	}

	public void setNoList(List<Integer> noList) {
		this.noList = noList;
	}
	
	
}
