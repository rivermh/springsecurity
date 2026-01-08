package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.demo.entity.Post;

public interface PostRepository extends JpaRepository<Post, Long> {

	@EntityGraph(attributePaths = "user")
	List<Post> findAllByOrderByCreatedAtDesc();

	
	@EntityGraph(attributePaths = "user")
	Optional<Post> findWithUserById(Long id);

	
	@Query("""
		    select p from Post p
		    join fetch p.user
		""")
		Page<Post> findAllWithUser(Pageable pageable);

}
