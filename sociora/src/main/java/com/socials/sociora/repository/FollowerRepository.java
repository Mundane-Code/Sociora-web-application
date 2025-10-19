package com.socials.sociora.repository;

import com.socials.sociora.entity.Follower;
import com.socials.sociora.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface FollowerRepository extends JpaRepository<Follower, Long> {
    List<Follower> findByFollower(User follower);
    List<Follower> findByFollowed(User followed);
    Optional<Follower> findByFollowerAndFollowed(User follower, User followed);
    boolean existsByFollowerAndFollowed(User follower, User followed);
    void deleteByFollowerAndFollowed(User follower, User followed);
    int countByFollowed(User followed);
    int countByFollower(User follower);
}