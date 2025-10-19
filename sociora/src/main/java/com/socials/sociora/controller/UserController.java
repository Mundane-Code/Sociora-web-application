package com.socials.sociora.controller;

import com.socials.sociora.entity.User;
import com.socials.sociora.service.FollowerService;
import com.socials.sociora.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private FollowerService followerService;

    @GetMapping("/profile/{username}")
    public String viewProfile(@PathVariable String username, Model model) {
        Optional<User> userOptional = userService.getUserByUsername(username);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            model.addAttribute("user", user);
            model.addAttribute("followerCount", followerService.getFollowerCount(user));
            model.addAttribute("followingCount", followerService.getFollowingCount(user));
            return "profile";
        } else {
            return "redirect:/";
        }
    }

    @GetMapping("/profile")
    public String myProfile() {
        // In a real application, you'd get the current user from session/security context
        // For now, redirect to a default profile or login
        return "redirect:/login";
    }

    @PostMapping("/update")
    public String updateProfile(@ModelAttribute User user) {
        // In a real application, validate that the user can only update their own profile
        userService.updateUser(user);
        return "redirect:/users/profile/" + user.getUsername();
    }

    @GetMapping("/followers/{username}")
    public String viewFollowers(@PathVariable String username, Model model) {
        Optional<User> userOptional = userService.getUserByUsername(username);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            model.addAttribute("user", user);
            model.addAttribute("followers", followerService.getFollowerUsers(user));
            return "followers";
        } else {
            return "redirect:/";
        }
    }

    @GetMapping("/following/{username}")
    public String viewFollowing(@PathVariable String username, Model model) {
        Optional<User> userOptional = userService.getUserByUsername(username);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            model.addAttribute("user", user);
            model.addAttribute("following", followerService.getFollowedUsers(user));
            return "following";
        } else {
            return "redirect:/";
        }
    }
}
