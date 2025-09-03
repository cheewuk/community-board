package com.example.communityboard.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class PostFormDto {

    @NotBlank(message = "Title cannot be empty.")
    @Size(max = 100, message = "Title must not exceed 100 characters.")
    private String title;

    @NotBlank(message = "Content cannot be empty.")
    private String content;
}





