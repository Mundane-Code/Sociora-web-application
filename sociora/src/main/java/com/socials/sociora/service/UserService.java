package com.socials.sociora.service;

import com.socials.sociora.entity.User;
import com.socials.sociora.repository.UserRepository;
import com.socials.sociora.service.FollowerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Transactional
    public User createUser(User user) {
        // Check if username or email already exists
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new IllegalArgumentException("Username already exists");
        }
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }

        // In a real application, you would hash the password here
        // user.setPassword(passwordEncoder.encode(user.getPassword()));

        User savedUser = userRepository.save(user);

        // Auto-follow default accounts
        autoFollowDefaultAccounts(savedUser);

        return savedUser;
    }

    private void autoFollowDefaultAccounts(User newUser) {
        // Get default accounts to follow
        String[] defaultUsernames = {"sociora", "influencer"};

        for (String username : defaultUsernames) {
            userRepository.findByUsername(username).ifPresent(defaultUser -> {
                try {
                    // Use FollowerService to follow
                    FollowerService followerService = getFollowerService();
                    if (followerService != null) {
                        followerService.followUser(newUser, defaultUser);
                    }
                } catch (Exception e) {
                    // Log error but don't fail user creation
                    System.err.println("Error auto-following " + username + ": " + e.getMessage());
                }
            });
        }
    }

    // Method to inject FollowerService (Spring will handle this)
    private FollowerService followerService;

    @Autowired
    public void setFollowerService(FollowerService followerService) {
        this.followerService = followerService;
    }

    private FollowerService getFollowerService() {
        return followerService;
    }

    @Transactional
    public User updateUser(User user) {
        // Check if user exists
        if (!userRepository.existsById(user.getId())) {
            throw new IllegalArgumentException("User not found");
        }
        
        return userRepository.save(user);
    }

    @Transactional
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
}