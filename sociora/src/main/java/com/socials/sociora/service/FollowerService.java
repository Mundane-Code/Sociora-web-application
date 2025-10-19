package com.socials.sociora.service;

import com.socials.sociora.entity.Follower;
import com.socials.sociora.entity.User;
import com.socials.sociora.repository.FollowerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FollowerService {

    private final FollowerRepository followerRepository;

    public FollowerService(FollowerRepository followerRepository) {
        this.followerRepository = followerRepository;
    }

    public List<Follower> getAllFollowers() {
        return followerRepository.findAll();
    }

    public List<Follower> getFollowersForUser(User user) {
        return followerRepository.findByFollowed(user);
    }

    public List<Follower> getFollowingForUser(User user) {
        return followerRepository.findByFollower(user);
    }

    public List<User> getFollowers(User user) {
        return followerRepository.findByFollowed(user)
                .stream()
                .map(Follower::getFollower)
                .collect(Collectors.toList());
    }

    public List<User> getFollowing(User user) {
        return followerRepository.findByFollower(user)
                .stream()
                .map(Follower::getFollowed)
                .collect(Collectors.toList());
    }

    public List<User> getFollowerUsers(User user) {
        return followerRepository.findByFollowed(user)
                .stream()
                .map(Follower::getFollower)
                .collect(Collectors.toList());
    }

    public List<User> getFollowedUsers(User user) {
        return followerRepository.findByFollower(user)
                .stream()
                .map(Follower::getFollowed)
                .collect(Collectors.toList());
    }

    public List<Long> getFollowedUserIds(User user) {
        return getFollowedUsers(user)
                .stream()
                .map(User::getId)
                .collect(Collectors.toList());
    }

    public boolean isFollowing(User follower, User followed) {
        return followerRepository.existsByFollowerAndFollowed(follower, followed);
    }

    public int getFollowerCount(User user) {
        return followerRepository.countByFollowed(user);
    }

    public int getFollowingCount(User user) {
        return followerRepository.countByFollower(user);
    }

    @Transactional
    public Follower followUser(User follower, User followed) {
        // Check if already following
        if (isFollowing(follower, followed)) {
            throw new IllegalArgumentException("Already following this user");
        }
        
        // Check if trying to follow self
        if (follower.getId().equals(followed.getId())) {
            throw new IllegalArgumentException("Cannot follow yourself");
        }
        
        Follower follow = new Follower();
        follow.setFollower(follower);
        follow.setFollowed(followed);
        
        return followerRepository.save(follow);
    }

    @Transactional
    public void unfollowUser(User follower, User followed) {
        Optional<Follower> follow = followerRepository.findByFollowerAndFollowed(follower, followed);
        if (follow.isPresent()) {
            followerRepository.delete(follow.get());
        } else {
            throw new IllegalArgumentException("Not following this user");
        }
    }
}