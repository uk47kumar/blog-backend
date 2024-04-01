package com.backend.blog.payload;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Set;

@Data
public class PostDto {
    private Long id;
    @NotBlank(message = "Post tile should not be empty")
    @Size(min = 2, message = "Post tile should have at least 2 characters")
    private String title;
    @NotBlank(message = "Post description should not be empty")
    @Size(min = 2, message = "Post description should have at least 2 characters")
    private String description;
    @NotBlank(message = "Post content should not be empty")
    private String content;
    private Set<CommentDto> comments;
}
