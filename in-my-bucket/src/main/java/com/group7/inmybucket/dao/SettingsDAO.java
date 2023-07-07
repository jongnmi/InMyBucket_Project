package com.group7.inmybucket.dao;

public interface SettingsDAO {
	public String getUserEmail(String userid);
	public Integer getProfileVisible(String userid);
	public int profileVisibleUpdate(String userid);
}
