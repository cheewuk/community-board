package com.example.communityboard.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CommentFormDto {

    @NotBlank(message = "Content cannot be empty.")
    private String content;
}
















