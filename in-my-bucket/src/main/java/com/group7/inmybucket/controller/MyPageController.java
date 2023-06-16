package com.group7.inmybucket.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.group7.inmybucket.dto.ImageFileDTO;
import com.group7.inmybucket.dto.ProfileDTO;
import com.group7.inmybucket.service.MyPageService;
import com.group7.inmybucket.vo.FilteringVO;
import com.group7.inmybucket.vo.UserDataVO;
import com.group7.inmybucket.vo.UserFeedVO;

@Controller
public class MyPageController {
	@Autowired
	MyPageService service;

	@RequestMapping("/mypage")
	public ModelAndView mypage(@RequestParam(name="userid") String userid,String bucketList, FilteringVO vo, HttpServletRequest request, ProfileDTO dto) {
		ModelAndView mav = new ModelAndView();
		
		// 피드
		// 보내져온 pageNum이 있으면 pageNum 바꿔주기
		String strPageNum = request.getParameter("pageNum");
		if(strPageNum != null) {
			vo.setPageNum(Integer.parseInt(strPageNum));
		}
		// 필터링 내용 넘겨주기
		String strListFilter = request.getParameter("listFilter");
		String strListSort = request.getParameter("listSort");
		
		if(strListFilter==null) strListFilter="";
		if(strListSort==null) strListSort="";
		
		vo.setListFilter(strListFilter);
		vo.setListSort(strListSort);
		System.out.println(vo.getListFilter()+", "+vo.getListSort());
		
		UserFeedVO userFeedVo = new UserFeedVO();
		userFeedVo.setVo(vo);
		userFeedVo.setUserid(userid);
		
		// 보여지는 피드 수
		vo.setFeedCount(service.feedCount(userFeedVo));
		
		// 뿌려주는 피드 리스트
		if(bucketList==null) bucketList="mine";
		if(bucketList.equals("mine")) { // 내가 작성한 피드
			mav.addObject("list", service.listMine(userFeedVo));
		} else if(bucketList.equals("like")) { // 내가 좋아요한 피드
			mav.addObject("list", service.listLike(userFeedVo));
		}
		
		mav.addObject("vo",vo);
		mav.addObject("bucketList",bucketList);
		
		// 피드
		ProfileDTO profileData = service.getProfile(userid);
		String usernick = service.getUsernick(userid);
		UserDataVO userData = service.getUserData(userid);
		dto.setUserid(userid);
		System.out.println("dto-> "+dto.toString());
		System.out.println("profileData-> "+profileData);
		
		mav.addObject("userData", userData);
		mav.addObject("usernick", usernick);
		mav.addObject("profileData", profileData);
		mav.setViewName("profile/mypage");

		return mav;
	}

	@PostMapping("/mypage/profileUpdate.do")
	public ModelAndView profileUpdate(HttpServletRequest req, ProfileDTO dto, String nickname) {
		dto.setUserid((String)req.getSession().getAttribute("logId"));
	
		// 파일 업로드
		MultipartHttpServletRequest mr = (MultipartHttpServletRequest) req;
		
		// mr에서 MultipartFile 객체 얻어오기
		List<MultipartFile> files = mr.getFiles("file");
		
		// 파일을 서버에 업로드할 위치의 절대주소
		String path = req.getSession().getServletContext().getRealPath("/uploadfile");
		System.out.println("path->"+path);
		
		// 업로드 시작 -> 같은 파일이 존재할 때 파일명을 만들어줘야함
		List<ProfileDTO> filelist = new ArrayList<ProfileDTO>(); // 업로드할 파일명들을 담을 컬렉션
		
		if(files!=null) { // 업로드 파일이 있을때
			
			for(int i=0; i<files.size(); i++) { // 업로드한 파일의 수만큼 반복수행
				MultipartFile mf = files.get(i);
				
				String orgFilename = mf.getOriginalFilename();
				System.out.println(orgFilename);
				if(orgFilename != null && !orgFilename.equals("")) { // 원래 파일명이 존재하면 rename을 수행
					// 파일 객체가 있는지 확인후 같은 파일이 있으면 파일명을 변경한다.
					File f = new File(path, orgFilename);
					
					if(f.exists()) { // file이 있으면 true
						for(int renameNum=1;;renameNum++) {
							// 파일명과 확장자를 나눈다.
							int point = orgFilename.lastIndexOf(".");
							String orgFile = orgFilename.substring(0, point);
							String orgExt = orgFilename.substring(point+1);
							
							String newFilename = orgFile + "(" + renameNum + ")." + orgExt;
							
							f = new File(path, newFilename);
							if(!f.exists()) {
								orgFilename = newFilename;
								break;
							}
						}
					}
					try {
						mf.transferTo(new File(path, orgFilename));
					}catch(Exception e) {
						e.printStackTrace();
					}
					System.out.println(orgFilename);
					
					dto.setFilename(orgFilename);
					req.getSession().setAttribute("filename", orgFilename); // 세션 업데이트
					filelist.add(dto);
					System.out.println(dto.toString());
				}
			}
		}
		ModelAndView mav = new ModelAndView();
		
		
		mav.setViewName("common/alert");
		mav.addObject("url", "/inmybucket/mypage?userid=" + dto.getUserid());
		
		if(nickname.equals("")) {
			mav.addObject("msg", "닉네임을 설정해주세요.");
			return mav;
		}
		
		else {
			try {
			if(dto.getFilename() == null) {
				dto.setFilename("");
			}else {
				int fileResult = service.profileImageUpdate(filelist);
				
			}
			dto.setProfile_visible(true);
			int userUpdate = service.updateMypageUser(dto);
			int nicknameUpdate = service.updateMypageNickname(dto.getUserid(), nickname);
			if(userUpdate == 0 || nicknameUpdate == 0) {
				mav.addObject("msg", "오류가 발생하였습니다.");
				return mav;
			}
			mav.addObject("msg", "수정이 완료되었습니다.");
			}catch(Exception e) {
			e.printStackTrace();
		}
		}
		
		return mav;
	}

}
