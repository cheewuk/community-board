package com.example.communityboard.controller;

import com.example.communityboard.domain.Post;
import com.example.communityboard.dto.PostFormDto;
import com.example.communityboard.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import com.example.communityboard.dto.CommentFormDto;

@Controller
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;

    @GetMapping
    public String listPosts(Model model, @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<Post> posts = postService.findAllPosts(pageable);
        model.addAttribute("posts", posts);
        return "posts/list";
    }

    @GetMapping("/{id}")
    public String viewPost(@PathVariable Long id, Model model) {
        Optional<Post> postOptional = postService.findPostById(id);
        if (postOptional.isPresent()) {
            model.addAttribute("post", postOptional.get());
            model.addAttribute("commentFormDto", new CommentFormDto()); // 댓글 폼 추가
            return "posts/detail";
        } else {
            return "redirect:/posts";
        }
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("postFormDto", new PostFormDto());
        return "posts/form";
    }

    @PostMapping("/new")
    public String createPost(@Valid @ModelAttribute("postFormDto") PostFormDto postFormDto,
                             BindingResult bindingResult, @AuthenticationPrincipal UserDetails userDetails) {
        if (bindingResult.hasErrors()) {
            return "posts/form";
        }
        postService.createPost(postFormDto.getTitle(), postFormDto.getContent(), userDetails.getUsername());
        return "redirect:/posts";
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model, @AuthenticationPrincipal UserDetails userDetails) {
        Optional<Post> postOptional = postService.findPostById(id);
        if (postOptional.isPresent()) {
            Post post = postOptional.get();
            if (!post.getAuthor().getUsername().equals(userDetails.getUsername())) {
                return "redirect:/posts"; // 권한 없음
            }
            PostFormDto postFormDto = new PostFormDto();
            postFormDto.setTitle(post.getTitle());
            postFormDto.setContent(post.getContent());
            model.addAttribute("post", post);
            model.addAttribute("postFormDto", postFormDto);
            return "posts/form";
        } else {
            return "redirect:/posts";
        }
    }

    @PostMapping("/{id}/edit")
    public String updatePost(@PathVariable Long id, @Valid @ModelAttribute("postFormDto") PostFormDto postFormDto,
                             BindingResult bindingResult, @AuthenticationPrincipal UserDetails userDetails) {
        if (bindingResult.hasErrors()) {
            return "posts/form";
        }
        try {
            postService.updatePost(id, postFormDto.getTitle(), postFormDto.getContent(), userDetails.getUsername());
        } catch (IllegalStateException e) {
            return "redirect:/posts"; // 권한 없음
        }
        return "redirect:/posts/{id}";
    }

    @PostMapping("/{id}/delete")
    public String deletePost(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {
        try {
            postService.deletePost(id, userDetails.getUsername());
        } catch (IllegalStateException e) {
            // 권한 없음 또는 게시글 없음
        }
        return "redirect:/posts";
    }
}
