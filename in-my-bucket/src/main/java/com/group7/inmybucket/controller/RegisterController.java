package com.group7.inmybucket.controller;

import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.group7.inmybucket.dto.ProfileDTO;
import com.group7.inmybucket.service.MyPageService;
import com.group7.inmybucket.service.RegisterServiceImpl;

import com.group7.inmybucket.vo.RegisterVO;







@Controller
public class RegisterController {
	@Autowired
	RegisterServiceImpl service;
	
	@Autowired
	JavaMailSenderImpl mailSender;
	
	@Autowired
	MyPageService servicee;
	
	//嚥≪뮄�젃占쎌뵥占쎈쨲
	@GetMapping("/loginForm")
	public String login() {
		return "register/loginForm";
	}
	//嚥≪뮄�젃占쎌뵥(DB)
	@PostMapping("/loginOk")
	public ModelAndView loginOk(String userid, String username, String userpwd, String usernick, String permission, String email, HttpServletRequest request, HttpSession session) {
		//Session 揶쏆빘猿� 占쎈섯占쎈선占쎌궎疫뀐옙
		
		
		
		RegisterVO vo = service.loginOk(userid, userpwd , usernick, permission, email, username);
		//vo->null占쎌뵥 野껋럩�뒭 占쎄퐨占쎄문占쎌쟿�굜遺얜굡揶쏉옙 占쎈씨占쎈뼄. - 嚥≪뮄�젃占쎌뵥占쎈뼄占쎈솭
		//	   null占쎌뵠 占쎈툡占쎈빒野껋럩�뒭 占쎄퐨占쎄문占쎌쟿�굜遺얜굡 占쎌뿳占쎈뼄. - 嚥≪뮄�젃占쎌뵥 占쎄쉐�⑨옙
		ProfileDTO dto = servicee.getProfile(userid);
		
		ModelAndView mav = new ModelAndView();
		if(vo != null ) {//嚥≪뮄�젃占쎌뵥
			session.setAttribute("logId", vo.getUserid() );
			session.setAttribute("logName", vo.getUsername() );
			session.setAttribute("logNick", vo.getUsernick());
			session.setAttribute("logPermission", vo.getPermission());
			session.setAttribute("logPwd", vo.getUserpwd());
			session.setAttribute("logEmail", vo.getEmail());
			session.setAttribute("logStatus", "Y");
			session.setAttribute("filename", dto.getFilename());
			
			String per = (String)session.getAttribute("logPermission");
			String perr = (String)session.getAttribute("filename");
			System.out.println(per);
			System.out.println(perr);
			if(per.equals("9")) {
				mav.setViewName("redirect:/adminindex");
				return mav;
			}
			
			mav.setViewName("redirect:/");
		}else {//嚥≪뮄�젃占쎌뵥 占쎈뼄占쎈솭
			mav.setViewName("redirect:loginForm");
		}
		return mav;
	}
	
	//嚥≪뮄�젃占쎈툡占쎌뜍 - 占쎄쉭占쎈�∽옙�젫椰꾬옙
		@RequestMapping("/logout")
		public ModelAndView logout(HttpSession session) {
			session.invalidate();
			ModelAndView mav = new ModelAndView();
			mav.setViewName("redirect:/");
			return mav;
		}
	
	//占쎌돳占쎌뜚揶쏉옙占쎌뿯 占쎈쨲
	@GetMapping("/join")
	public String join() {
		return "register/join";
	}
	//占쎈툡占쎌뵠占쎈탵 餓λ쵎�궗野껓옙占쎄텢
	@GetMapping("/idCheck")
	public String idCheck(String userid, Model model) {
	//鈺곌퀬�돳
	//占쎈툡占쎌뵠占쎈탵占쎌벥 揶쏉옙占쎈땾 �뤃�뗫릭疫뀐옙 - 0, 1
	int result = service.idCheckCount(userid);
			
	
	model.addAttribute("userid", userid);
	model.addAttribute("result", result);
			
		return "register/idCheck";
	}
	
	@RequestMapping(value="/joinOk", method=RequestMethod.POST)
	public ModelAndView joinOk(RegisterVO vo) {
		System.out.println(vo.toString());
		
		ModelAndView mav = new ModelAndView();
		//占쎌돳占쎌뜚揶쏉옙占쎌뿯
		
		int result = service.registerInsert(vo);
		service.registerprofile(vo);
		
		
		if(result>0) {//占쎌돳占쎌뜚揶쏉옙占쎌뿯 占쎄쉐�⑤벊�뻻 - 嚥≪뮄�젃占쎌뵥占쎈쨲占쎌몵嚥∽옙 占쎌뵠占쎈짗
			
			mav.setViewName("redirect:loginForm");
		}else{//占쎈뼄占쎈솭占쎈뻻
			mav.addObject("msg","회원등록실패하였습니다.");
			mav.setViewName("register/joinOkResult");
		}
		return mav;
	}
	//占쎌돳占쎌뜚占쎌젟癰귨옙 占쎈땾占쎌젟占쎈쨲 - session 嚥≪뮄�젃占쎌뵥 占쎈툡占쎌뵠占쎈탵占쎈퓠 占쎈퉸占쎈뼣占쎈릭占쎈뮉 占쎌돳占쎌뜚占쎌젟癰귨옙 select占쎈릭占쎈연 �뀎怨좊읂占쎌뵠筌욑옙 占쎌뵠占쎈짗
	@GetMapping("/joinEdit")
	public ModelAndView joinEdit(HttpSession session) {
		RegisterVO vo = service.registerEdit((String)session.getAttribute("logId"));
			
		ModelAndView mav = new ModelAndView();
		mav.addObject("vo", vo);
		mav.setViewName("register/joinEdit");
		return mav;
		}
	//占쎌돳占쎌뜚占쎌젟癰귨옙 占쎈땾占쎌젟(DB) - form占쎌벥 占쎄땀占쎌뒠�⑨옙 session占쎌벥 嚥≪뮄�젃占쎌뵥 占쎈툡占쎌뵠占쎈탵�몴占� �뤃�뗫릭占쎈연 占쎌돳占쎌뜚占쎌젟癰귣�占쏙옙 占쎈땾占쎌젟占쎈립占쎈뼄.
	@PostMapping("/joinEditOk")
	public ModelAndView joinEditOk(RegisterVO vo, HttpSession session) {
		vo.setUserid((String)session.getAttribute("logId"));
			
		int cnt = service.registerEditOk(vo);
			
		ModelAndView mav = new ModelAndView();
		if(cnt>0){// 占쎈땾占쎌젟占쎄쉐�⑤벊�뻻 -> db占쎈퓠占쎄퐣 占쎈땾占쎌젟占쎈쭆 占쎄땀占쎌뒠占쎌뱽 癰귣똻肉т틠�눊��
			mav.setViewName("redirect:/");
		}else{// 占쎈땾占쎌젟占쎈뼄占쎈솭占쎈뻻 -> 占쎌뵠占쎌읈占쎈읂占쎌뵠筌욑옙 (占쎈르�뵳占�)
			mav.addObject("msg","회원정보수정 실패하였습니다.");
			mav.setViewName("register/joinOkResult");
		}
		return mav;
	}
	//占쎈툡占쎌뵠占쎈탵 筌≪뼐由�
	@RequestMapping(value="idSearchForm", method=RequestMethod.GET)
	public String idSearchForm(HttpServletRequest request, Model model, RegisterVO searchVO) {
		return "register/idSearchForm";
	}
	//占쎈툡占쎌뵠占쎈탵占쎌젟癰귣똾�넇占쎌뵥
	@RequestMapping(value = "idSearchOk")
	public String search_result_id(HttpServletRequest request, Model model,
	    @RequestParam(required = true, value = "username") String username, 
	    @RequestParam(required = true, value = "tel") String tel,
	    RegisterVO searchVO) {
	 
	 
	try {
	    
	    searchVO.setUsername(username);
	    searchVO.setTel(tel);
	    RegisterVO memberSearch = service.memberIdSearch(searchVO);
	    
	    model.addAttribute("searchVO", memberSearch);
	 
	} catch (Exception e) {
	    System.out.println(e.toString());
	    model.addAttribute("msg", "오류가 발생되었습니다.");
	}
	 
	return "register/idSearchOk";
	}

	
	///占쎌뵠筌롫뗄�뵬/////////////////////////////////
	@GetMapping("/psSearchForm")
	public String psSearchForm() {
		return "register/psSearchForm";
		}
	@PostMapping("/psSearchEmailSend")
	@ResponseBody
	public String idSearchEmailSend(RegisterVO vo) {
			
		//占쎌뵠�뵳袁㏓궢 占쎌뵠筌롫뗄�뵬占쎌뵠 占쎌뵬燁살꼹釉�占쎈뮉 占쎌돳占쎌뜚占쎌벥 �뜮袁⑨옙甕곕뜇�깈
		String userpwd = service.psSearch(vo.getUsername(), vo.getEmail());
			
		if(userpwd==null || userpwd.equals("")) {//�뜮袁⑥쓰 占쎈씨占쎌몵筌롳옙 鈺곕똻�삺占쎈릭筌욑옙 占쎈륫占쎈뮉 占쎌젟癰귨옙
			return "N";
		}else {
			
		//�뜮袁⑥쓰 占쎌뿳占쎌몵筌롳옙
		//DB鈺곌퀬�돳占쎈립 �뜮袁⑨옙甕곕뜇�깈�몴占� 占쎌뵠筌롫뗄�뵬嚥∽옙 癰귣�沅→�⑨옙 筌롫뗄�뵬癰귣�源됵옙�뼄占쎈뮉 占쎌젟癰귣�占쏙옙 占쎈르占쎌젻餓ο옙占쎈뼄.
			String emailSubject = "비밀번호 찾기 결과";
			String emailContent = "<div style='background:pink; margin:50px; padding:50px; border:2px solid gray; font-size:2em; text-align:center'>";
			emailContent += "검색한 비밀번호 입니다.<br/>";
			emailContent += "비밀번호 : "+userpwd;
			emailContent += "</div>";
			try {
				//mimeMessage -> mimeMessageHelper
				MimeMessage message = mailSender.createMimeMessage();
				MimeMessageHelper messageHelper = new MimeMessageHelper(message, true, "UTF-8");
					
				//癰귣�沅∽옙�뮉 筌롫뗄�뵬雅뚯눘�꺖
				messageHelper.setFrom("gprhf123@naver.com");
				messageHelper.setTo(vo.getEmail());
				messageHelper.setSubject(emailSubject);
				messageHelper.setText("text/html; charset=UTF-8", emailContent);
					
				mailSender.send(message);//癰귣�沅→묾占�
				return "Y";
			}catch(Exception e) {
				e.printStackTrace();
				return "N";
			}
		}
	}
	@RequestMapping(value="register/removeForm", method=RequestMethod.GET)
	public ModelAndView removeForm(HttpSession session) {
		RegisterVO vo = service.removeForm((String)session.getAttribute("logId"), (String)session.getAttribute("logPwd"));
		ModelAndView mav = new ModelAndView();
		mav.addObject("vo", vo);
		mav.setViewName("register/removeForm");
		return mav;
	}
	
	@RequestMapping(value="register/remove", method=RequestMethod.POST)
	public ModelAndView removeMember(HttpSession session, RegisterVO vo) {
			ModelAndView mav = new ModelAndView();
			
				 
				 int result = service.remove(vo);
				 if(result>0) {
				 session.invalidate();
				 mav.setViewName("redirect:/");
				 }else {
					mav.addObject("msg","회원탈퇴에 실패하였습니다.");
					mav.setViewName("register/joinOkResult");
				 }
				
			return mav;
		}
	@PostMapping("/kakaoOk")
	public String kakaoOk(HttpServletRequest request, String userid, String userpwd, String usernick, String email, String permission, HttpSession session) {
		
		System.out.println(request.getParameter("kakaoemail"));
		System.out.println(request.getParameter("kakaoname"));
		
		// kakaoemail占쎌뱽 kakaoid占쎈퓠 占쏙옙占쎌삢
		String kakaoemail = request.getParameter("kakaoemail");

		RegisterVO userTo = new RegisterVO();

		// kakaoid�몴占� to占쎌벥 id嚥∽옙 占쎄쉭占쎈샒
		userTo.setEmail(kakaoemail);

		// 燁삳똻萸낉옙�궎�④쑴�젟占쎌몵嚥∽옙 嚥≪뮄�젃占쎌뵥占쎈립 占쎌읅占쎌뵠 占쎌뿳占쎈뮉筌욑옙 占쎈씨占쎈뮉筌욑옙 
		int result_lookup = service.kakaoOk(userTo);

		if (result_lookup == 0) { // 占쎌돳占쎌뜚占쎌뵠 占쎈툡占쎈빒野껋럩�뒭 (燁삳똻萸낉옙�궎 �④쑴�젟占쎌몵嚥∽옙 筌ｌ꼷�벉 獄쎻뫖揆占쎈립 野껋럩�뒭) 燁삳똻萸낉옙�궎 占쎌돳占쎌뜚占쎌젟癰귨옙 占쎄퐬占쎌젟 筌≪럩�몵嚥∽옙 占쎌뵠占쎈짗
		    System.out.println("카카오 회원 정보 설정");

		   
		    request.setAttribute("kakaoname",request.getParameter("kakaoname"));
		    
		    request.setAttribute("kakaoemail",request.getParameter("kakaoemail"));

		    // 占쎌돳占쎌뜚揶쏉옙占쎌뿯筌≪럩�몵嚥∽옙 占쎌뵠占쎈짗
		    return "register/join";
		} else { // 占쎌뵠沃섓옙 燁삳똻萸낉옙�궎嚥∽옙 嚥≪뮄�젃占쎌뵥占쎈립 占쎌읅占쎌뵠 占쎌뿳占쎌뱽 占쎈르 (筌ㅼ뮇�겧 1占쎌돳 嚥≪뮄�젃占쎌뵥占쎈르 占쎌돳占쎌뜚揶쏉옙占쎌뿯占쎈쭆 占쎄맒占쎄묶) 嚥≪뮄�젃占쎌뵥筌≪럩�몵嚥∽옙 癰귣�沅▽빳占�
			userTo = service.kaka(userTo);
			session.setAttribute("logId", userTo.getUserid() );
			session.setAttribute("logName", userTo.getUsername() );
			session.setAttribute("logNick", userTo.getUsernick());
			session.setAttribute("logPermission", userTo.getPermission());
			session.setAttribute("logPwd", userTo.getUserpwd());
			session.setAttribute("logEmail", userTo.getEmail());
			
			session.setAttribute("logStatus", "Y");
		}
			return "redirect:/";
		
	}
}

	
