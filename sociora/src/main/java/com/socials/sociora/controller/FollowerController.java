package com.socials.sociora.controller;

import com.socials.sociora.entity.User;
import com.socials.sociora.service.FollowerService;
import com.socials.sociora.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/followers")
public class FollowerController {

    @Autowired
    private FollowerService followerService;

    @Autowired
    private UserService userService;

    @PostMapping("/follow")
    @ResponseBody
    public ResponseEntity<?> followUser(@RequestParam Long followerId, @RequestParam Long followedId) {
        try {
            Optional<User> followerOptional = userService.getUserById(followerId);
            Optional<User> followedOptional = userService.getUserById(followedId);

            if (followerOptional.isPresent() && followedOptional.isPresent()) {
                followerService.followUser(followerOptional.get(), followedOptional.get());
                return ResponseEntity.ok().body("{\"success\": true, \"message\": \"Followed successfully\"}");
            } else {
                return ResponseEntity.badRequest().body("{\"success\": false, \"message\": \"User not found\"}");
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("{\"success\": false, \"message\": \"" + e.getMessage() + "\"}");
        }
    }

    @PostMapping("/unfollow")
    @ResponseBody
    public ResponseEntity<?> unfollowUser(@RequestParam Long followerId, @RequestParam Long followedId) {
        try {
            Optional<User> followerOptional = userService.getUserById(followerId);
            Optional<User> followedOptional = userService.getUserById(followedId);

            if (followerOptional.isPresent() && followedOptional.isPresent()) {
                followerService.unfollowUser(followerOptional.get(), followedOptional.get());
                return ResponseEntity.ok().body("{\"success\": true, \"message\": \"Unfollowed successfully\"}");
            } else {
                return ResponseEntity.badRequest().body("{\"success\": false, \"message\": \"User not found\"}");
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("{\"success\": false, \"message\": \"" + e.getMessage() + "\"}");
        }
    }

    @GetMapping("/is-following")
    @ResponseBody
    public ResponseEntity<?> isFollowing(@RequestParam Long followerId, @RequestParam Long followedId) {
        Optional<User> followerOptional = userService.getUserById(followerId);
        Optional<User> followedOptional = userService.getUserById(followedId);

        if (followerOptional.isPresent() && followedOptional.isPresent()) {
            boolean isFollowing = followerService.isFollowing(followerOptional.get(), followedOptional.get());
            return ResponseEntity.ok().body("{\"isFollowing\": " + isFollowing + "}");
        } else {
            return ResponseEntity.badRequest().body("{\"error\": \"User not found\"}");
        }
    }

    @GetMapping("/count")
    @ResponseBody
    public ResponseEntity<?> getCounts(@RequestParam Long userId) {
        Optional<User> userOptional = userService.getUserById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            int followerCount = followerService.getFollowerCount(user);
            int followingCount = followerService.getFollowingCount(user);
            return ResponseEntity.ok().body("{\"followers\": " + followerCount + ", \"following\": " + followingCount + "}");
        } else {
            return ResponseEntity.badRequest().body("{\"error\": \"User not found\"}");
        }
    }

    @GetMapping("/{username}/followers")
    public String showFollowers(@PathVariable String username, Model model) {
        Optional<User> userOptional = userService.getUserByUsername(username);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            List<User> followers = followerService.getFollowers(user);
            model.addAttribute("user", user);
            model.addAttribute("followers", followers);
            return "followers";
        } else {
            return "error/404";
        }
    }

    @GetMapping("/{username}/following")
    public String showFollowing(@PathVariable String username, Model model) {
        Optional<User> userOptional = userService.getUserByUsername(username);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            List<User> following = followerService.getFollowing(user);
            model.addAttribute("user", user);
            model.addAttribute("following", following);
            return "following";
        } else {
            return "error/404";
        }
    }
}
