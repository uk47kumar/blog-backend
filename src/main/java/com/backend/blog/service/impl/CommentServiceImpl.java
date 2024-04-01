package com.backend.blog.service.impl;

import com.backend.blog.entity.Comment;
import com.backend.blog.entity.Post;
import com.backend.blog.exception.BlogApiException;
import com.backend.blog.exception.ResourceNotFoundException;
import com.backend.blog.payload.CommentDto;
import com.backend.blog.repository.CommentRepository;
import com.backend.blog.repository.PostRepository;
import com.backend.blog.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@RequiredArgsConstructor
@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final ModelMapper modelMapper;

    @Override
    public CommentDto createComment(Long postId, CommentDto commentDto) {

        Comment comment = mapToEntity(commentDto);

        // retrieve post entity by id
        Post post = getPost(postId);

        // set post to comment entity
        comment.setPost(post);
        Comment savedComment = commentRepository.save(comment);

        return mapToDTO(savedComment);
    }

    @Override
    public List<CommentDto> getCommentsByPostId(Long postId) {

        List<Comment> comments = commentRepository.findByPostId(postId);

        return comments.stream()
                .map(comment -> mapToDTO(comment))
                .collect(Collectors.toList());
    }

    @Override
    public CommentDto getCommentById(Long postId, Long commentId) {
        // retrieve post by postId
        Post post = getPost(postId);
        // retrieve comment by commentId
        Comment comment = getComment(commentId);
        // throwing exception if comment post id is not equals to post id
        commentPostIdNotEqualsPostIdThrowException(post, comment);

        return mapToDTO(comment);
    }

    @Override
    public CommentDto updateComment(Long postId, Long commentId, CommentDto commentDto) {

        Post post = getPost(postId);
        Comment comment = getComment(commentId);

        commentPostIdNotEqualsPostIdThrowException(post, comment);

        comment.setName(commentDto.getName());
        comment.setEmail(commentDto.getEmail());
        comment.setBody(commentDto.getBody());

        Comment updateComment = commentRepository.save(comment);

        return mapToDTO(updateComment);
    }

    @Override
    public void deleteComment(Long postId, Long commentId) {
        Post post = getPost(postId);
        Comment comment = getComment(commentId);
        commentPostIdNotEqualsPostIdThrowException(post, comment);
        commentRepository.delete(comment);
    }

    private static void commentPostIdNotEqualsPostIdThrowException(Post post, Comment comment) {
        if (!comment.getPost().getId().equals(post.getId())) {
            throw new BlogApiException(BAD_REQUEST, "Comment does not belong to post");
        }
    }

    private Post getPost(Long id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post", "id", id));
    }

    private Comment getComment(Long id) {
        return commentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Comment", "id", id));
    }

    private CommentDto mapToDTO(Comment comment) {
        return modelMapper.map(comment, CommentDto.class);
    }

    private Comment mapToEntity(CommentDto commentDto) {
        return modelMapper.map(commentDto, Comment.class);
    }
}
