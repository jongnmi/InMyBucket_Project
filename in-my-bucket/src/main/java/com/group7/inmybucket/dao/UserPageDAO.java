package com.group7.inmybucket.dao;

import java.util.List;

import com.group7.inmybucket.dto.FeedDTO;
import com.group7.inmybucket.dto.ProfileDTO;
import com.group7.inmybucket.vo.UserDataVO;
import com.group7.inmybucket.vo.UserFeedVO;

public interface UserPageDAO {
	// 프로필
	public ProfileDTO getProfile(String userid);
	public String getUsernick(String userid);
	// 데이터
	public UserDataVO getUserData(String userid);
	public int getProfileVisible(String userid);
	// 피드
	public int feedCount(UserFeedVO vo); // 보여지는 유저피드 수
	public List<FeedDTO> listUser(UserFeedVO vo); // 유저피드
}
