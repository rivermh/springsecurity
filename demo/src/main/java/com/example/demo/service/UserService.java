package com.example.demo.service;


import java.time.LocalDate;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;

import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	
	// 중복 체크용 아이디, 이메일
	@Transactional(readOnly = true)
	public boolean existsByUsername(String username) {
		return userRepository.existsByUsername(username);
	}
	
	@Transactional(readOnly = true)
	public boolean existsByEmail(String email) {
		return userRepository.existsByEmail(email);
	}	
	
	// 회원가입
	public void register(String username, String password, String email, LocalDate birth) {
		
		//1. username 형식
		if(!username.matches("^[a-zA-Z0-9]{4,20}$")) {
			throw new IllegalStateException("아이디는 4~20자의 영문/숫자만 가능합니다.");
		}
		
		// 2. 비밀번호 형식
	    if (!password.matches("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d!@#$%^&*()_+=-]{8,}$")) {
	        throw new IllegalStateException("비밀번호는 8자 이상, 영문과 숫자를 포함해야 합니다.");
	    }	
	    
	    // 3. 이메일 형식
	    if (!email.matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$")) {
	        throw new IllegalStateException("이메일 형식이 올바르지 않습니다.");
	    }  
		
		// 4. 아이디 중복 체크
		if(userRepository.existsByUsername(username)) {
			throw new IllegalStateException("이미 존재하는 아이디 입니다.");
		}
		
		// 5. 이메일 중복 체크
		if(userRepository.existsByEmail(email)) {
			throw new IllegalStateException("이미 존재하는 이메일입니다.");
		}
		 
		// 6. 비밀번호 암호화
		String encodedPassword = passwordEncoder.encode(password);
		
		// 7. User 생성
		User user = User.builder()
				.username(username)
				.password(encodedPassword)
				.email(email)
				.birth(birth)
				.role(Role.USER) // 기본 권한
				.build();
		
		// 8. 저장
		userRepository.save(user);
	}
	
	// 조회 메서드 
	@Transactional(readOnly = true)
	public User findByUsername(String username) {
		return userRepository.findByUsername(username).orElseThrow(()-> new IllegalArgumentException("사용자 없음"));
	}
}
