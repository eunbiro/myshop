package com.myshop.entity;

import static org.junit.jupiter.api.Assertions.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import com.myshop.dto.MemberFormDto;
import com.myshop.repository.CartRepository;
import com.myshop.repository.MemberRepository;

@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
class CartTest {

	@Autowired
	CartRepository cartRepository;
	
	@Autowired
	MemberRepository memberRepository;
	
	@Autowired
	PasswordEncoder passwordEncoder;
	
	@PersistenceContext
	EntityManager em;
	
	public Member createMember() {
		MemberFormDto member = new MemberFormDto();
		member.setName("홍길동");
		member.setEmail("test@email.com");
		member.setAddress("서울시 마포구 합정동");
		member.setPassword("1234");
		
		return Member.createMember(member, passwordEncoder);
	}
	
	@Test
	@DisplayName("장바구니 회원 엔티티 매핑 조회 테스트")
	public void findCartAndMemberTest() {
		Member member = createMember();
		memberRepository.save(member);	// memberRepository에 insert / save : 저장하고자 하는 데이터가 있으면 update로 수정해주고 없으면 create로 추가해준다.
		
		Cart cart = new Cart();
		cart.setMember(member);			// Cart에서 만들어놓은 Member컬럼에 위에서 생성한 member객체를 넣어줌
		cartRepository.save(cart);		// cart에서 cart_id는 자동생성이니까 member정보만 가져와서 cartRepository에 insert
										// 위에서 만든 홍길동의 정보가 해당 cart에 입력된다.
		
		em.flush();						// 트랜잭션이 끝날 때 데이터 베이스에 반영
		em.clear();						// 영속성 컨텍스트를 비워준다. >> 실제 데이터베이스에서 장바구니 엔티티를 가지고 올 때 회원 엔티티도 가지고 오는지 확인하기 위해.
		
		Cart saveCart = cartRepository.findById(cart.getId())
									  .orElseThrow(EntityNotFoundException::new);
		
		assertEquals(saveCart.getMember().getId(), member.getId());
	}

}
