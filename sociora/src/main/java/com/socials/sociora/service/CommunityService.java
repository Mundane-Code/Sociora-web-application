package com.socials.sociora.service;

import com.socials.sociora.entity.Community;
import com.socials.sociora.entity.User;
import com.socials.sociora.repository.CommunityRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CommunityService {

    private final CommunityRepository communityRepository;

    public CommunityService(CommunityRepository communityRepository) {
        this.communityRepository = communityRepository;
    }

    public List<Community> getAllCommunities() {
        return communityRepository.findAll();
    }

    public Optional<Community> getCommunityById(Long id) {
        return communityRepository.findById(id);
    }

    public Optional<Community> getCommunityByName(String nameOfCommunity) {
        return communityRepository.findByNameOfCommunity(nameOfCommunity);
    }

    public Page<Community> getCommunitiesByUser(User user, Pageable pageable) {
        return communityRepository.findByUser(user, pageable);
    }

    public List<Community> getPopularCommunities() {
        return communityRepository.findTop10ByOrderByTotalMembersDesc();
    }

    public List<Community> searchCommunities(String keyword) {
        return communityRepository.findByNameOfCommunityContainingIgnoreCase(keyword);
    }

    public boolean existsByName(String nameOfCommunity) {
        return communityRepository.existsByNameOfCommunity(nameOfCommunity);
    }

    @Transactional
    public Community createCommunity(Community community) {
        // Check if community name already exists
        if (communityRepository.existsByNameOfCommunity(community.getNameOfCommunity())) {
            throw new IllegalArgumentException("Community name already exists");
        }
        
        // Initialize members count
        if (community.getTotalMembers() == null) {
            community.setTotalMembers(0);
        }
        
        return communityRepository.save(community);
    }

    @Transactional
    public Community updateCommunity(Community community) {
        // Check if community exists
        if (!communityRepository.existsById(community.getId())) {
            throw new IllegalArgumentException("Community not found");
        }
        
        // Check if new name conflicts with existing community
        Optional<Community> existingCommunity = communityRepository.findByNameOfCommunity(community.getNameOfCommunity());
        if (existingCommunity.isPresent() && !existingCommunity.get().getId().equals(community.getId())) {
            throw new IllegalArgumentException("Community name already exists");
        }
        
        return communityRepository.save(community);
    }

    @Transactional
    public void deleteCommunity(Long id) {
        communityRepository.deleteById(id);
    }

    @Transactional
    public Community incrementMembers(Long communityId) {
        Community community = communityRepository.findById(communityId)
                .orElseThrow(() -> new IllegalArgumentException("Community not found"));
        community.setTotalMembers(community.getTotalMembers() + 1);
        return communityRepository.save(community);
    }

    @Transactional
    public Community decrementMembers(Long communityId) {
        Community community = communityRepository.findById(communityId)
                .orElseThrow(() -> new IllegalArgumentException("Community not found"));
        if (community.getTotalMembers() > 0) {
            community.setTotalMembers(community.getTotalMembers() - 1);
        }
        return communityRepository.save(community);
    }
}