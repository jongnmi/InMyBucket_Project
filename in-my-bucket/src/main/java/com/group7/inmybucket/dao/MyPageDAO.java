package com.group7.inmybucket.dao;

import java.util.List;


import com.group7.inmybucket.dto.FeedDTO;
import com.group7.inmybucket.dto.ProfileDTO;
import com.group7.inmybucket.vo.UserDataVO;
import com.group7.inmybucket.vo.UserFeedVO;

public interface MyPageDAO {
	// 프로필
	public ProfileDTO getProfile(String userid);
	public String getUsernick(String userid);
	public int updateMypageUser(ProfileDTO dto);
	public int updateMypageNickname(String userid, String nickname);
	public int profileImageUpdate(List<ProfileDTO> filelist);
	// 데이터
	public UserDataVO getUserData(String userid);
	// 피드
	public int feedCount(UserFeedVO vo); // 보여지는 피드 수
	public List<FeedDTO> listMine(UserFeedVO vo); // 작성한 피드
	public List<FeedDTO> listLike(UserFeedVO vo); // 좋아요한 피드
}
