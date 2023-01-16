package com.myshop.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration		// 스프링에서 설정 클래스로 사용하겠다.
@EnableWebSecurity	// SpringSecurityFilterChain이 자동으로 포함됨
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Override	// http요청에 대한 보안을 설정 : 페이지 권한설정, 로그인 페이지 설정, 로그아웃 메소드 등에 대한 설정
	protected void configure(HttpSecurity http) throws Exception {
		
	}
	
	@Bean		// @Configuration 얘랑 주로 같이 사용하고 싱글톤이 보장된다.
	public PasswordEncoder passwordEncoder() { // 비밀번호 암호화를 위해서 사용하는 빈(Bean)
		return new BCryptPasswordEncoder();
	}
	
	
}
