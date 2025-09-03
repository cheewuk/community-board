package com.example.communityboard.service;

import com.example.communityboard.domain.Post;
import com.example.communityboard.domain.User;
import com.example.communityboard.repository.PostRepository;
import com.example.communityboard.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public Page<Post> findAllPosts(Pageable pageable) {
        return postRepository.findAll(pageable);
    }

    public Optional<Post> findPostById(Long id) {
        return postRepository.findById(id);
    }

    @Transactional
    public Post createPost(String title, String content, String username) {
        User author = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + username));

        Post post = new Post();
        post.setTitle(title);
        post.setContent(content);
        post.setAuthor(author);
        return postRepository.save(post);
    }

    @Transactional
    public Post updatePost(Long id, String title, String content, String username) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Post not found: " + id));

        if (!post.getAuthor().getUsername().equals(username)) {
            throw new IllegalStateException("You are not authorized to update this post.");
        }

        post.setTitle(title);
        post.setContent(content);
        return postRepository.save(post);
    }

    @Transactional
    public void deletePost(Long id, String username) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Post not found: " + id));

        if (!post.getAuthor().getUsername().equals(username)) {
            throw new IllegalStateException("You are not authorized to delete this post.");
        }

        postRepository.delete(post);
    }
}





