package com.socials.sociora;

import com.socials.sociora.entity.Community;
import com.socials.sociora.entity.User;
import com.socials.sociora.service.CommunityService;
import com.socials.sociora.service.FollowerService;
import com.socials.sociora.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserService userService;

    @Autowired
    private CommunityService communityService;

    @Autowired
    private FollowerService followerService;

    @Override
    public void run(String... args) throws Exception {
        // Create default users (official accounts)
        createDefaultUsers();

        // Create default communities
        createDefaultCommunities();
    }

    private void createDefaultUsers() {
        // Create official Sociora account
        if (!userService.existsByUsername("sociora")) {
            User socioraUser = new User();
            socioraUser.setUsername("sociora");
            socioraUser.setEmail("admin@sociora.com");
            socioraUser.setPassword("admin123"); // In production, this should be hashed
            socioraUser.setFullName("Sociora Official");
            socioraUser.setBio("Welcome to Sociora - Your Social Media Platform!");
            socioraUser.setLocation("Global");
            socioraUser.setWebsite("https://sociora.com");
            socioraUser.setInterests("Social Media, Technology, Community Building");
            userService.createUser(socioraUser);
        }

        // Create popular influencer account
        if (!userService.existsByUsername("influencer")) {
            User influencerUser = new User();
            influencerUser.setUsername("influencer");
            influencerUser.setEmail("influencer@sociora.com");
            influencerUser.setPassword("influencer123");
            influencerUser.setFullName("Popular Influencer");
            influencerUser.setBio("Sharing amazing content with the world!");
            influencerUser.setLocation("New York");
            influencerUser.setWebsite("https://influencer.com");
            influencerUser.setInterests("Photography, Travel, Lifestyle");
            userService.createUser(influencerUser);
        }
    }

    private void createDefaultCommunities() {
        // Create default communities
        String[] communityNames = {
            "Technology Enthusiasts",
            "Photography Lovers",
            "Travel Adventures",
            "Food & Cooking",
            "Fitness & Health",
            "Art & Creativity",
            "Music Fans",
            "Book Club",
            "Gaming Community",
            "Pet Lovers"
        };

        String[] themes = {
            "tech",
            "photography",
            "travel",
            "food",
            "fitness",
            "art",
            "music",
            "books",
            "gaming",
            "pets"
        };

        for (int i = 0; i < communityNames.length; i++) {
            if (!communityService.existsByName(communityNames[i])) {
                Community community = new Community();
                community.setNameOfCommunity(communityNames[i]);
                community.setTheme(themes[i]);
                community.setTotalMembers(0);

                // Set the creator as the official Sociora account
                User socioraUser = userService.getUserByUsername("sociora").orElseThrow();
                community.setUser(socioraUser);

                communityService.createCommunity(community);
            }
        }
    }
}
