package com.example.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.entity.Post;
import com.example.demo.entity.User;
import com.example.demo.repository.PostRepository;
import com.example.demo.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class PostService {

	private final PostRepository postRepository;
	private final UserRepository userRepository;
	
	// 글 작성
	public Long write(String username, String title, String content) {
		User user = userRepository.findByUsername(username)
				.orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
	Post post = Post.create(user, title, content);
	postRepository.save(post);
	
	return post.getId();
	}
	
	// 글 목록
	@Transactional(readOnly = true)
	public List<Post> findAll(){
		return postRepository.findAllByOrderByCreatedAtDesc();
	}
	
	//글 상세
	public Post findById(Long postId) {
	    return postRepository.findWithUserById(postId)
	        .orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다."));
	}
	
	//글 수정
	public void update(Long PostId, String username, String title, String content) {
		Post post = findById(PostId);
		
		if(!post.getUser().getUsername().equals(username)) {
			throw new SecurityException("수정 권한이 없습니다.");
		}
		
		post.update(title, content);
	}
	
	//글 삭제
	public void delete(Long postId, String username) {
		Post post = findById(postId);
		
		if(!post.getUser().getUsername().equals(username)) {
			throw new SecurityException("삭제 권한이 없습니다.");
		}
		
		postRepository.delete(post);
	}
}
