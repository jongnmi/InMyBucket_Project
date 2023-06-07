package com.group7.inmybucket.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.group7.inmybucket.dto.BucketDTO;
import com.group7.inmybucket.dto.ImageFileDTO;
import com.group7.inmybucket.dto.ReportDTO;
import com.group7.inmybucket.service.BucketService;
import com.group7.inmybucket.service.FeedService;
import com.group7.inmybucket.service.ReportService;
import com.group7.inmybucket.vo.FilteringVO;
import com.group7.inmybucket.vo.LikeVO;

@RestController
public class BucketListController {
	
	@Autowired
	BucketService service;
	
	@Autowired
	ReportService service2;
	
	@Autowired
	FeedService Fservice;
	
	
	// 버킷리스트 등록
	@GetMapping("/bucketWrite")
	public ModelAndView bucketWrite() {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("bucket/bucketWrite");
		return mav;
	}
	
	// 버킷리스트 등록(DB)
	@PostMapping(value="/bucketWriteOk", produces="application/text;charset=UTF-8")
	public ModelAndView bucketWriteOk(HttpServletRequest request, BucketDTO dto) {
		
		dto.setUserid((String)request.getSession().getAttribute("logId"));
		
		// 파일 업로드
		MultipartHttpServletRequest mr = (MultipartHttpServletRequest)request;
		
		// mr에서 MultipartFile 객체 얻어오기
		List<MultipartFile> files = mr.getFiles("filename");
		
		// 파일을 서버에 업로드할 위치의 절대주소
		String path = request.getSession().getServletContext().getRealPath("/uploadfile");
		System.out.println("path->"+path);
		
		// 업로드 시작 -> 같은 파일이 존재할 때 파일명을 만들어줘야함.
		List<ImageFileDTO> filelist = new ArrayList<ImageFileDTO>(); // 업로드한 파일명들을 담을 컬렉션
		
		if(files!=null) { // 업로드 파일이 있을때
			
			for(int i=0; i<files.size(); i++) { // 업로드한 파일의 수만큼 반복수행
				MultipartFile mf = files.get(i);
				
				String orgFilename = mf.getOriginalFilename(); // 클라이언트가 업로드한 원래파일명을 구한다.
				System.out.println(orgFilename);
				if(orgFilename != null && !orgFilename.equals("")) { // 원래 파일명이 존재하면 rename을 수행
					// 파일객체가 있는지 확인후 같은 파일이 있으면 파일명을 변경한다.
					File f = new File(path, orgFilename);
					
					if(f.exists()) { // file이 있으면 true
						for(int renameNum=1;;renameNum++) {
							// 파일명과 확장자를 나눈다.
							int point = orgFilename.lastIndexOf("."); // 마지막 . 의 위치 구하기
							String orgFile = orgFilename.substring(0, point); // 확장자를 뺀 파일명
							String orgExt = orgFilename.substring(point+1); //확장자
							
							String newFilename = orgFile + "(" + renameNum + ")." + orgExt; // 새로 만들어진 파일명
							
							f = new File(path, newFilename);
							if(!f.exists()) { // 새로만들 파일이 존재하지 않으면 반복문 중지
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
					
					ImageFileDTO ifDTO = new ImageFileDTO();
					ifDTO.setFilename(orgFilename);
					filelist.add(ifDTO);
				}
			}
		}
		ModelAndView mav = new ModelAndView();
		try {
			// 이미지파일 제외한 정보 insert하기
			int result = service.bucketInsert(dto);
			System.out.println(dto.toString());
			if(dto.getIsgroup()==1) {
				int groupResult = service.groupMemberInsert(dto);
			}
			
			// 원글의 시퀀스의 번호를 파일명이 있는 dto에 추가
			for(ImageFileDTO ifDTO:filelist) {
				ifDTO.setBucket_no(dto.getBucket_no());
			}
			
			int fileResult = service.imageFileInsert(filelist);
			mav.setViewName("redirect:/feed"); // 정상구현
		}catch(Exception e) {
			e.printStackTrace();
			// 파일 삭제
			for(ImageFileDTO iDTO:filelist) {
				fileDelete(path, iDTO.getFilename());
			}
			// 삭제하고 글쓰기로 이동(history)
			service.bucketDelete(dto.getBucket_no());
			service.imageFileDelete(dto.getBucket_no());
			
			mav.addObject("msg", "버킷리스트 등록에 실패하였습니다.");
			mav.setViewName("bucket/bucketResult");
		}
		return mav;
	}
	// 업로드 파일 삭제
	public void fileDelete(String path, String filename) {
		File f = new File(path, filename);
		f.delete();
	}
	
	// 버킷리스트 목록
	@RequestMapping("/bucketList")
	public ModelAndView bucketList() {
		ModelAndView mav = new ModelAndView();
		
		List<BucketDTO> list = service.bucketAllSelect();
		
		mav.addObject("list", list);
		mav.setViewName("bucket/bucketList");
		
		return mav;
	}
	
	// 버킷리스트 보기
	@GetMapping("bucket/bucketView/{bucket_no}")
	public ModelAndView bucketView(@PathVariable("bucket_no") int bucket_no,HttpSession session) {
		
		// no와 같은 레코드 선택
		BucketDTO dto = service.bucketSelect(bucket_no);
		// no에 해당하는 첨부파일을 선택
		List<ImageFileDTO> filelist = service.imageFileSelect(bucket_no);
		
		// 좋아요
		LikeVO vo = new LikeVO();
		String logId = (String)session.getAttribute("logId");
		vo.setUserid(logId);
		vo.setBucket_no(bucket_no);
		System.out.println(vo.toString());
		
		// 로그인 했으면 dto에 like_no 넣어주기
		if( vo.getUserid() != null ) {
			Integer like_no = service.isLike(vo);
			System.out.println("like_no->"+like_no);
			dto.setLike_no(like_no);
		}
		ModelAndView mav = new ModelAndView();
		
		// 그룹 버킷이면 멤버 정보 불러오기
		boolean isMember = false;
		if(dto.getIsgroup()==1) {
			// 멤버 리스트
			List<String> memberList = service.groupMemberNick(bucket_no);
			mav.addObject("memberList", memberList);
			// 멤버인지 아닌지
			isMember = memberList.contains(session.getAttribute("logNick"));
			System.out.println(isMember);
		}
		
		mav.addObject("dto", dto);
		mav.addObject("filelist", filelist);
		mav.addObject("isMember", isMember);
		mav.addObject("comment_count", service.commentCount(bucket_no)); // 댓글 수
		
		mav.setViewName("bucket/bucketView");
		return mav;
	}
	
	// 좋아요
	@ResponseBody
	@RequestMapping(value="/saveLike.do", method=RequestMethod.GET)
	public BucketDTO saveLike(@RequestParam("bucket_no") int bucket_no, HttpSession session) {
		
		LikeVO vo = new LikeVO();
		
		vo.setBucket_no(bucket_no);
		vo.setUserid((String)session.getAttribute("logId"));
		
		// like테이블에 insert
		int result = service.bucketSaveLike(vo);
		// insert 성공하면
		if(result==1) {
			service.bucketLikeUp(vo); // 좋아요수 +1
			System.out.println("saveLike-> userid="+vo.getUserid()+", bucket_no= "+ vo.getBucket_no());
		}
		
		BucketDTO bDto = new BucketDTO();
		bDto.setBucket_no(bucket_no);
		bDto.setLike_count(service.bucketLikeCount(vo));
		
		return bDto;
	}
	
	// 좋아요 취소
	@ResponseBody
	@RequestMapping(value="/removeLike.do", method=RequestMethod.GET)
	public BucketDTO removeLike(@RequestParam("bucket_no") int bucket_no, HttpSession session) {
		
		LikeVO vo = new LikeVO();
		
		vo.setBucket_no(bucket_no);
		vo.setUserid((String)session.getAttribute("logId"));
		
		
		// like테이블에서 delete
		int result = service.bucketRemoveLike(vo);
		// delete 성공하면
		if(result==1) {
			service.bucketLikeDown(vo); // 좋아요수 -1
			System.out.println("removeLike -> bucket_no="+vo.getBucket_no()+", userid="+vo.getUserid());
		}
		
		BucketDTO bDto = new BucketDTO();
		bDto.setBucket_no(bucket_no);
		bDto.setLike_count(service.bucketLikeCount(vo));
		
		return bDto;
	}
	
	// 그룹 참여 요청
	@ResponseBody
	@RequestMapping(value="/groupRequest.do", method=RequestMethod.POST, produces="application/text;charset=utf-8")
	public String groupRequest(@RequestParam("bucket_no") int bucket_no, HttpSession session) {
		
		String msg ="---그룹 버킷리스트 참여 요청중---";
		// 요청기록 확인
		int isRequest = service.isGroupRequest(bucket_no, (String)session.getAttribute("logId"));
		// 기록 없으면 그룹 큐에 insert
		if(isRequest<1) {
			int result = service.groupQueueInsert(bucket_no,(String)session.getAttribute("logId"));
			if(result==1) {
				msg = "참여 요청이 등록되었습니다. \n그룹장의 수락을 기다려주세요.";
			}
		}else {// 기록 있으면
			msg = "이미 참여 요청이 등록되어있습니다. \n그룹장의 수락을 기다려주세요.";
		}
		return msg;
	}
	

	// 버킷리스트 수정폼
	@GetMapping("/bucket/bucketEdit/{bucket_no}")
	public ModelAndView bucketEdit(@PathVariable("bucket_no") int bucket_no, HttpSession session) {
		ModelAndView mav = new ModelAndView();
		
		// 기존글 가져오기
		BucketDTO dto = service.bucketEditSelect(bucket_no, (String)session.getAttribute("logId"));
		
		if(dto==null) { // 글을 선택못하면 글내용보기로 이동
			mav.setViewName("redirect:bucketView/"+ bucket_no);
		}else {
			// 이미지
			List<ImageFileDTO> filelist = service.imageFileSelect(bucket_no);
			
			mav.addObject("dto", dto);
			mav.addObject("filelist", filelist);
			mav.addObject("fileCount", filelist.size());
			
			// 수정폼으로 이동
			mav.setViewName("bucket/bucketEdit");
		}
		
		return mav;
	}
	// 버킷리스트 수정(DB)
	@PostMapping("/bucket/bucketEditOk")
	public ModelAndView bucketEditOk(BucketDTO dto, HttpSession session, HttpServletRequest request) {
		ModelAndView mav = new ModelAndView();
		String path = session.getServletContext().getRealPath("/uploadfile");
		
		dto.setUserid((String)session.getAttribute("logId")); // 로그인 아이디
		
		// DB와 삭제파일 정리 - 남은 이미지들
		List<String> finalImageList = service.imageFilenameList(dto.getBucket_no()); // DB파일
		
		// 새로 업로드된 파일 업로드
		MultipartHttpServletRequest mr = (MultipartHttpServletRequest) request;
		
		List<MultipartFile> mfList = mr.getFiles("filename");
		
		// 새로 업로드된 이미지명을 보관할 컬렉션
		List<String> newImageList = new ArrayList<String>();
		
		
		//======================================
		if(mfList!=null) { // 업로드 파일이 있을때
			// 새로 업로드한 MultipartFile 개수만큼
			for(MultipartFile mf : mfList) {
				
				String filename = mf.getOriginalFilename(); // 업로드한 원래 파일명
				
				if(filename!=null && !filename.equals("")) { // 파일명이 있으면
					File f = new File(path, filename);
					if(f.exists()) { // 파일이 있으면
						
						for(int filenum=1; ; filenum++) {
							int p = filename.lastIndexOf("."); // 마지막 . 위치
							String filenameNoExt = filename.substring(0, p); // 파일명
							String ext = filename.substring(p+1); // 확장자
							
							String newFile = filenameNoExt + " (" + filenum + ")." + ext;
							f = new File(path, newFile);
							if(!f.exists()) {
								filename = newFile;
								break;
							}
	
						} // for
					} // if
					// 업로드
					try {
						mf.transferTo(new File(path, filename));
					}catch(Exception e) {
						e.printStackTrace();
					}
					
					// 새로 업로드한 파일명을 보관
					newImageList.add(filename);
					
				}// if
			}// for
			
		}// if
		try {
			// 정상구현
			// 1. 원글업데이트
			System.out.println(dto.toString());
			int result = service.bucketEditUpdate(dto);
			// 2. DB파일 - 삭제파일 + 새로 업데이트된 파일 => 레코드 업데이트
			
			if(dto.getDelimage()!=null) {
			// DB파일 - 삭제파일
			for(int i=0; i<dto.getDelimage().size(); i++) {
				finalImageList.remove(dto.getDelimage().get(i));
			}
			// DB파일 + 새로업로드
			finalImageList.addAll(newImageList);
			
			System.out.println("finalImageList->"+finalImageList.toString());
			service.imageFileDelete(dto.getBucket_no());
			
			List<ImageFileDTO> imageDTOList = new ArrayList<ImageFileDTO>();
			for(String iname: finalImageList) {
				ImageFileDTO iDTO = new ImageFileDTO();
				iDTO.setBucket_no(dto.getBucket_no());
				iDTO.setFilename(iname);
				imageDTOList.add(iDTO);
			}
			
			int iResult = service.imageFileEditInsert(imageDTOList);
			}
			// 3. 업로드 폴더에서 파일 삭제
			if(dto.getDelimage()!=null) { // 삭제된 이미지가 있으면
				for(int i=0; i<dto.getDelimage().size(); i++) {
					fileDelete(path, dto.getDelimage().get(i));
				}
				
			}
			
			
			
			// 4. 글내용보기로 이동
			mav.setViewName("redirect:bucketView/"+dto.getBucket_no());
			
		}catch(Exception e) {
			
			// 실패시
			
			// 1. 새로 업로드된 파일 삭제
			for(int i=0; i<newImageList.size(); i++) {
				fileDelete(path, newImageList.get(i));
			}
			e.printStackTrace();
			// 2. 수정페이지로 다시 보내기
			mav.addObject("msg", "버킷리스트 수정에 실패하였습니다.");
			mav.setViewName("bucket/bucketResult");
		}
		return mav;
	}
	
	// 버킷리스트 삭제
	@GetMapping("/bucket/bucketDelete")
	public ModelAndView bucketDelete(int bucket_no, HttpSession session) {
		String path = session.getServletContext().getRealPath("/uploadfile");
		
		ModelAndView mav = new ModelAndView();
		
		// DB에 있는 이미지 목록 가져오기
		List<String> imagenameList = service.imageFilenameList(bucket_no);
		
		// 파일명이 있는 레코드 삭제
		int imageResult = service.imageFileDelete(bucket_no);
		
		// 원글 삭제
		int result = service.bucketEditDelete(bucket_no, (String)session.getAttribute("logId"));
		
		if(result>0) {
			// 파일삭제
			for(String iname : imagenameList) {
				fileDelete(path, iname);
			}
			mav.setViewName("redirect:/feed");
		}else {
			mav.setViewName("redirect:/bucketView/"+ bucket_no);
		}
		return mav;
	}
	
	// 버킷리스트 신고 폼
	@GetMapping("/bucket/bucketReport/{bucket_no}")
	public ModelAndView bucketReport(@PathVariable("bucket_no") int bucket_no, HttpSession session) {
		ModelAndView mav = new ModelAndView();
		
		// 원글에서 정보 가져오기 
		BucketDTO dto = service.bucketReportSelect(bucket_no, (String)session.getAttribute("logId"));
		
		if(dto==null) {
			
			mav.setViewName("redirect:bucketView/"+ bucket_no);
			
		}else {

			mav.addObject("dto", dto);
			
			mav.setViewName("bucket/bucketReport");
		}
		
		return mav;
	}
	
	// 버킷리스트 신고 등록(DB)
	@PostMapping("/bucket/bucketReportOk")
	public ModelAndView bucketReportOk(ReportDTO dto, HttpServletRequest request){
		dto.setReport_userid((String)request.getSession().getAttribute("logId"));
		ModelAndView mav = new ModelAndView();
		System.out.println(dto.toString());
		try {
			
			int result = service2.bucketReportInsert(dto);
			mav.setViewName("redirect:/feed");
			
		}catch(Exception e) {
			
			e.printStackTrace();
			
			mav.addObject("msg", "신고 실패하였습니다.");
			mav.setViewName("bucket/bucketResult");
		}
		
		return mav;
	}
	
	// 버킷리스트 신고 목록
	@RequestMapping("/bucketReportList")
	public ModelAndView bucketReportList() {
		ModelAndView mav = new ModelAndView();
		// 통계자료 보내주기
		mav.addObject("allBucketCount", Fservice.allBucketCount());
		mav.addObject("accomplishedBucketCount", Fservice.accomplishedBucketCount());
		mav.addObject("userExCount", Fservice.userExCount());
			
		List<ReportDTO> list = service2.bucketReportAllSelect();
			
		mav.addObject("list", list);
		mav.setViewName("admin/report/bucketReportList");
			
		return mav;
	}
	
	// 버킷리스트 신고 보기
	@GetMapping("bucket/bucketReportView/{report_no}")
	public ModelAndView bucketReportView(@PathVariable("report_no") int report_no) {
			
		// no와 같은 레코드 선택
		ReportDTO dto = service2.bucketReportSelect(report_no);
		
		ModelAndView mav = new ModelAndView();
		mav.addObject("dto", dto);
		mav.setViewName("admin/report/bucketReportView");
		
		System.out.println(dto.toString());
		
		return mav;
	}
	
	// 버킷리스트 신고 진행상황 수정(DB)
	@PostMapping("bucket/bucketReportEditOk")
	public ModelAndView bucketReportEditOk(ReportDTO dto) {
		ModelAndView mav = new ModelAndView();
		
		try {
			System.out.println(dto.toString());
			int result = service2.bucketReportEditUpdate(dto);
			System.out.println(dto.toString());
			mav.setViewName("redirect:/bucketReportList");
			
		}catch(Exception e) {
			
			e.printStackTrace();
			
			mav.addObject("msg", "수정 실패하였습니다.");
			mav.setViewName("bucket/bucketResult");
		}
		
		return mav;
	}
	
	// 신고 글 삭제
	@GetMapping("/bucket/bucketReportDelete")
	public ModelAndView bucketReportDelete(int report_no) {
		ModelAndView mav = new ModelAndView();
		
		try {
			
			int result = service2.bucketReportDelete(report_no);
			
			mav.setViewName("redirect:/admin/report/bucketReportList");
			
		}catch(Exception e) {
			
			e.printStackTrace();
			
			mav.addObject("msg", "삭제 실패하였습니다.");
			mav.setViewName("bucket/bucketResult");
			
		}
		
		return mav;
	}	
}
