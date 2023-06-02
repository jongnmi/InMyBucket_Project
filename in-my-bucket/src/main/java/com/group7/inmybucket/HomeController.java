package com.group7.inmybucket;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.group7.inmybucket.service.FeedService;

@Controller
public class HomeController {
	@Autowired
	FeedService service;
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Model model) {
		
		// 통계자료 보내주기
		model.addAttribute("allBucketCount", service.allBucketCount());
		model.addAttribute("accomplishedBucketCount", service.accomplishedBucketCount());
		model.addAttribute("userExCount", service.userExCount());
		

		return "common/home";
	}
	
}
