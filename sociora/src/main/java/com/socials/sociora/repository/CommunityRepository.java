package com.socials.sociora.repository;

import com.socials.sociora.entity.Community;
import com.socials.sociora.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface CommunityRepository extends JpaRepository<Community, Long> {
    Page<Community> findByUser(User user, Pageable pageable);
    Optional<Community> findByNameOfCommunity(String nameOfCommunity);
    boolean existsByNameOfCommunity(String nameOfCommunity);
    List<Community> findTop10ByOrderByTotalMembersDesc();
    List<Community> findByNameOfCommunityContainingIgnoreCase(String keyword);
}