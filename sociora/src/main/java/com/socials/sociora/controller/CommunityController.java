package com.socials.sociora.controller;

import com.socials.sociora.entity.Community;
import com.socials.sociora.entity.User;
import com.socials.sociora.service.CommunityService;
import com.socials.sociora.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/communities")
public class CommunityController {

    @Autowired
    private CommunityService communityService;

    @Autowired
    private UserService userService;

    @GetMapping
    public String listCommunities(@RequestParam(defaultValue = "0") int page,
                                  @RequestParam(defaultValue = "10") int size,
                                  Model model) {
        Pageable pageable = PageRequest.of(page, size);
        // For now, show all communities (in a real app, you'd filter by user)
        List<Community> communities = communityService.getAllCommunities();
        model.addAttribute("communities", communities);
        return "communities";
    }

    @GetMapping("/{id}")
    public String viewCommunity(@PathVariable Long id, Model model) {
        Optional<Community> communityOptional = communityService.getCommunityById(id);
        if (communityOptional.isPresent()) {
            model.addAttribute("community", communityOptional.get());
            return "community-detail";
        } else {
            return "redirect:/communities";
        }
    }

    @GetMapping("/popular")
    public String popularCommunities(Model model) {
        List<Community> popularCommunities = communityService.getPopularCommunities();
        model.addAttribute("communities", popularCommunities);
        return "communities";
    }

    @GetMapping("/search")
    public String searchCommunities(@RequestParam String keyword, Model model) {
        List<Community> communities = communityService.searchCommunities(keyword);
        model.addAttribute("communities", communities);
        model.addAttribute("searchKeyword", keyword);
        return "communities";
    }

    @PostMapping("/create")
    public String createCommunity(@ModelAttribute Community community,
                                  @RequestParam Long userId) {
        Optional<User> userOptional = userService.getUserById(userId);
        if (userOptional.isPresent()) {
            community.setUser(userOptional.get());
            communityService.createCommunity(community);
        }
        return "redirect:/communities";
    }

    @PostMapping("/{id}/join")
    public String joinCommunity(@PathVariable Long id) {
        // In a real application, you'd get the current user and add them to the community
        // For now, just increment member count
        try {
            communityService.incrementMembers(id);
        } catch (Exception e) {
            // Handle error
        }
        return "redirect:/communities/" + id;
    }

    @PostMapping("/{id}/leave")
    public String leaveCommunity(@PathVariable Long id) {
        // In a real application, you'd get the current user and remove them from the community
        // For now, just decrement member count
        try {
            communityService.decrementMembers(id);
        } catch (Exception e) {
            // Handle error
        }
        return "redirect:/communities/" + id;
    }
}
