package com.myshop.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.myshop.entity.Member;
import com.myshop.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service					// service 클래스의 역할
@Transactional
@RequiredArgsConstructor
public class MemberService {

	private final MemberRepository memberRepository;	// 의존성 주입
	
	public Member saveMember(Member member) {
		
		return memberRepository.save(member);			// member 테이브렝 insert
	}
	
	// email 중복체크
	private void vaildateDuplicateMember(Member member) {
		
		Member findMember = memberRepository.findByEmail(member.getEmail());
		if (findMember != null) {
			throw new IllegalStateException("이미 가입된 회원입니다.");
		}
	}
}
