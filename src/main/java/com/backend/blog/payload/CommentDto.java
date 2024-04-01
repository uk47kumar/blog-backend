package com.backend.blog.payload;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CommentDto {
    private Long id;
    @NotBlank(message = "Name should not be empty")
    private String name;
    @NotBlank(message = "Email should not be empty")
    @Email
    private String email;
    @NotBlank(message = "Comment body should not be empty")
    @Size(min = 10, message = "Comment body must be minimum 10 characters")
    private String body;
}
