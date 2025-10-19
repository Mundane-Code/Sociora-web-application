package com.socials.sociora.service;

import com.socials.sociora.entity.Community;
import com.socials.sociora.entity.Post;
import com.socials.sociora.entity.User;
import com.socials.sociora.repository.PostRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class PostService {

    private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    public Optional<Post> getPostById(Long id) {
        return postRepository.findById(id);
    }

    public Page<Post> getPostsByUser(User user, Pageable pageable) {
        return postRepository.findByUser(user, pageable);
    }

    public Page<Post> getPostsByCommunity(Community community, Pageable pageable) {
        return postRepository.findByCommunity(community, pageable);
    }

    public List<Post> getRecentPosts() {
        return postRepository.findTop10ByOrderByCreatedAtDesc();
    }

    public Page<Post> getFeedForUser(List<Long> followingUserIds, Pageable pageable) {
        return postRepository.findByUserIdInOrderByCreatedAtDesc(followingUserIds, pageable);
    }

    @Transactional
    public Post createPost(Post post) {
        return postRepository.save(post);
    }

    @Transactional
    public Post updatePost(Post post) {
        // Check if post exists
        if (!postRepository.existsById(post.getId())) {
            throw new IllegalArgumentException("Post not found");
        }
        
        return postRepository.save(post);
    }

    @Transactional
    public void deletePost(Long id) {
        postRepository.deleteById(id);
    }

    @Transactional
    public Post incrementLikes(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));
        post.setLikesCount(post.getLikesCount() + 1);
        return postRepository.save(post);
    }

    @Transactional
    public Post incrementComments(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));
        post.setCommentsCount(post.getCommentsCount() + 1);
        return postRepository.save(post);
    }
}