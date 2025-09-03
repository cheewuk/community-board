package com.example.communityboard.controller;

import com.example.communityboard.dto.CommentFormDto;
import com.example.communityboard.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/posts/{postId}/comments")
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    public String addComment(@PathVariable Long postId,
                             @Valid @ModelAttribute("commentFormDto") CommentFormDto commentFormDto,
                             BindingResult bindingResult, @AuthenticationPrincipal UserDetails userDetails) {
        if (bindingResult.hasErrors()) {
            return "posts/detail"; // TODO: 에러 메시지를 detail 페이지에 전달하는 방법 개선
        }
        commentService.addComment(postId, commentFormDto.getContent(), userDetails.getUsername());
        return "redirect:/posts/{postId}";
    }

    @PostMapping("/{commentId}/edit")
    public String updateComment(@PathVariable Long postId, @PathVariable Long commentId,
                                @Valid @ModelAttribute("commentFormDto") CommentFormDto commentFormDto,
                                BindingResult bindingResult, @AuthenticationPrincipal UserDetails userDetails) {
        if (bindingResult.hasErrors()) {
            return "redirect:/posts/{postId}"; // TODO: 에러 메시지를 detail 페이지에 전달하는 방법 개선
        }
        try {
            commentService.updateComment(commentId, commentFormDto.getContent(), userDetails.getUsername());
        } catch (IllegalStateException e) {
            // 권한 없음 또는 댓글 없음
        }
        return "redirect:/posts/{postId}";
    }

    @PostMapping("/{commentId}/delete")
    public String deleteComment(@PathVariable Long postId, @PathVariable Long commentId,
                                @AuthenticationPrincipal UserDetails userDetails) {
        try {
            commentService.deleteComment(commentId, userDetails.getUsername());
        } catch (IllegalStateException e) {
            // 권한 없음 또는 댓글 없음
        }
        return "redirect:/posts/{postId}";
    }
}





