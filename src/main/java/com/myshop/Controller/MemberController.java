package com.myshop.Controller;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.myshop.dto.MemberFormDto;
import com.myshop.entity.Member;
import com.myshop.service.MemberService;

import lombok.RequiredArgsConstructor;

@RequestMapping("/members")
@Controller
@RequiredArgsConstructor
public class MemberController {

	private final MemberService memberService;
	private final PasswordEncoder passwordEncoder;
	
	// 회원가입 화면
	@GetMapping(value = "/new")
	public String memberForm(Model model) {
		
		model.addAttribute("memberFormDto", new MemberFormDto());
		return "member/memberForm";
	}
	
	// 회원가입 버튼을 눌렀을 때 작동하는 메소드
	@PostMapping(value = "/new")
	public String memberForm(MemberFormDto memberFormDto) {
		
		Member member = Member.createMember(memberFormDto, passwordEncoder);
		memberService.saveMember(member);
		return "redirect:/";	// 기본경로 localhost
	}
}
