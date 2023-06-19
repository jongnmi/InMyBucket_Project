package com.group7.inmybucket.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.group7.inmybucket.service.SettingsService;

@RestController
public class SettingsController {
	@Autowired
	SettingsService service;
	
	@RequestMapping(value="/settings")
	public ModelAndView settingsList(HttpSession session) {
		
		ModelAndView mav = new ModelAndView();
		
		// System.out.println((String)session.getAttribute("logName"));

		String logId = (String)session.getAttribute("logId");
		Integer profile_visible = service.getProfileVisible(logId);
		
		mav.addObject("profile_visible", profile_visible);
		mav.setViewName("settings/settings");
		return mav;
	}
	
	@RequestMapping(value="/profileVisible.do", method=RequestMethod.GET, produces="application/text;charset=utf-8")
	public String settingProfileVisible(@RequestParam String logId) {
		String msg = "";
		System.out.println("1 "+logId);
		
		// 프로필 공개/비공개 update
		int result = service.profileVisibleUpdate(logId);
		System.out.println("2 "+result);
		
		if(result==1) {
			msg="success";
		}
		return msg;
	}
}
