package com.group7.inmybucket.dao;

import java.util.List;

import com.group7.inmybucket.dto.BucketDTO;
import com.group7.inmybucket.dto.GroupBoardDTO;
import com.group7.inmybucket.dto.GroupCommentDTO;
import com.group7.inmybucket.dto.UserDTO;

public interface GroupMemberDAO {
	/** 그룹에 멤버 추가*/
	public int addGroupMember(String userid, int groupNo);
	/** 그룹 멤버인지 확인 */
	public boolean isMember(String userid, int groupNo);
	/** 승인 요청 목록 불러오기 */
	public List<String> getQueueMembers(int groupNo);
	/** 승인 요청 목록에 있는지 판단 */
	public boolean inQueue(String userid, int groupNo);
	/** 승인 요청 목록 사용자 삭제 */
	public int deleteInQueue(String userid, int groupNo);
	/** 그룹에 속한 멤버들 가져오기 */
	public List<UserDTO> getMembers(int groupNo);
	/** 그룹에 속한 멤버 수 가져오기 */
	public int getMemberCount(int groupNo);
	/** 
	 * 그룹 탈퇴 
	 * userid: 유저 id
	 * group_no: 그룹 번호
	 * */
	public int deleteGroupMember(String userid, int groupNo);
	
}
