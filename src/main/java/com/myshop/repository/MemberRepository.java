package com.myshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.myshop.entity.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {

	Member findByEmail(String email);	// 회원가입할 때 중복 회원이 있는지 확인
}
