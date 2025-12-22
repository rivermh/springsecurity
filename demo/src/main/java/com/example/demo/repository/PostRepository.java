package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.Post;

public interface PostRepository extends JpaRepository<Post, Long> {

	@EntityGraph(attributePaths = "user")
	List<Post> findAllByOrderByCreatedAtDesc();

	
	@EntityGraph(attributePaths = "user")
	Optional<Post> findWithUserById(Long id);

}
