package com.socials.sociora.repository;

import com.socials.sociora.entity.Community;
import com.socials.sociora.entity.Post;
import com.socials.sociora.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    Page<Post> findByUser(User user, Pageable pageable);
    Page<Post> findByCommunity(Community community, Pageable pageable);
    List<Post> findTop10ByOrderByCreatedAtDesc();
    Page<Post> findByUserIdInOrderByCreatedAtDesc(List<Long> userIds, Pageable pageable);
}