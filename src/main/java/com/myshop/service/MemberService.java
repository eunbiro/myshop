package com.myshop.service;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.myshop.entity.Member;
import com.myshop.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service					// service 클래스의 역할
@Transactional				// 서비스클래스에서 로직을 처리하다 오류 발생 시 로직을 수행하기 이전으로 돌려줌(DB의 rollback 같은 느낌) 
@RequiredArgsConstructor	// 의존성 주입 시 final @NotNull 키워드가 붙을 때 @Autiwire 대신 사용하며 생성자를 자동생성 
public class MemberService implements UserDetailsService {	// UserDetailsService : 로그인 시 request에서 넘어온 사용자 정보를 받음

	private final MemberRepository memberRepository;	// 의존성 주입
	
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		Member member = memberRepository.findByEmail(email);
		
		if (member == null) {
			throw new UsernameNotFoundException(email);
		}
		
		// userDetails의 객체를 반환
		return User.builder()
				.username(member.getEmail())
				.password(member.getPassword())
				.roles(member.getRole().toString())
				.build();
	}
	
	public Member saveMember(Member member) {
		
		vaildateDuplicateMember(member);
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
