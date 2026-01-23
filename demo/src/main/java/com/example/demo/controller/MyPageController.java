package com.example.demo.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.demo.entity.User;
import com.example.demo.security.CustomUserDetails;
import com.example.demo.service.CommentService;
import com.example.demo.service.PostLikeService;
import com.example.demo.service.PostService;
import com.example.demo.service.UserService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class MyPageController {

	private final UserService userService;
	private final PostService postService;
	private final CommentService commentService;
	private final PostLikeService postLikeService;

	@GetMapping("/mypage")
	public String myPage(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
		// 로그인 안 된 상태 방어
		if(userDetails == null) {
			return "redirect:/login";
		}
		
		User user = userService.findByUsername(userDetails.getUsername());
		model.addAttribute("user", user);
		
		return "mypage";
	}
	
	@GetMapping("/mypage/posts")
	public String myPosts(@AuthenticationPrincipal CustomUserDetails userDetails,
			@PageableDefault(size = 10) Pageable pageable,Model model) {
		User user = userService.findByUsername(userDetails.getUsername());
		model.addAttribute("posts", postService.findMyPosts(user.getId(), pageable));
		
		return "mypage/posts";
	}
	
	@GetMapping("/mypage/comments")
	public String myComments(
			@AuthenticationPrincipal CustomUserDetails userDetails,
			@PageableDefault(size = 10) Pageable pageable, Model model) {
		User user = userService.findByUsername(userDetails.getUsername());
		model.addAttribute("comments", commentService.findMyComments(user.getId(), pageable));
		
		return "mypage/comments";
	}
	
	@GetMapping("/mypage/likes")
	public String myLikes(
	        @AuthenticationPrincipal CustomUserDetails userDetails,
	        @PageableDefault(size = 10) Pageable pageable,
	        Model model) {

	    User user = userService.findByUsername(userDetails.getUsername());
	    model.addAttribute("posts",
	            postLikeService.findLikePosts(user.getId(), pageable));

	    return "mypage/likes";
	}
}
