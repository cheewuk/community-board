package com.example.communityboard.service;

import com.example.communityboard.domain.Comment;
import com.example.communityboard.domain.Post;
import com.example.communityboard.domain.User;
import com.example.communityboard.repository.CommentRepository;
import com.example.communityboard.repository.PostRepository;
import com.example.communityboard.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public List<Comment> getCommentsByPostId(Long postId) {
        return commentRepository.findByPostId(postId);
    }

    @Transactional
    public Comment addComment(Long postId, String content, String username) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post not found: " + postId));
        User author = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + username));

        Comment comment = new Comment();
        comment.setContent(content);
        comment.setPost(post);
        comment.setAuthor(author);
        return commentRepository.save(comment);
    }

    @Transactional
    public Comment updateComment(Long commentId, String content, String username) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("Comment not found: " + commentId));

        if (!comment.getAuthor().getUsername().equals(username)) {
            throw new IllegalStateException("You are not authorized to update this comment.");
        }

        comment.setContent(content);
        return commentRepository.save(comment);
    }

    @Transactional
    public void deleteComment(Long commentId, String username) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("Comment not found: " + commentId));

        if (!comment.getAuthor().getUsername().equals(username)) {
            throw new IllegalStateException("You are not authorized to delete this comment.");
        }

        commentRepository.delete(comment);
    }
}





