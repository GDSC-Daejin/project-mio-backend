package com.gdsc.projectmiobackend.service;

import com.gdsc.projectmiobackend.dto.CommentDto;
import com.gdsc.projectmiobackend.dto.request.CommentFirstCreateRequestDto;
import com.gdsc.projectmiobackend.dto.request.CommentPatchRequestDto;
import com.gdsc.projectmiobackend.dto.request.CommentRequestDto;
import com.gdsc.projectmiobackend.entity.Alarm;
import com.gdsc.projectmiobackend.entity.Comment;
import com.gdsc.projectmiobackend.entity.Post;
import com.gdsc.projectmiobackend.entity.UserEntity;
import com.gdsc.projectmiobackend.notification.service.impl.NotificationServiceImpl;
import com.gdsc.projectmiobackend.repository.AlarmRepository;
import com.gdsc.projectmiobackend.repository.CommentRepository;
import com.gdsc.projectmiobackend.repository.PostRepository;
import com.gdsc.projectmiobackend.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class CommentServiceImpl implements CommentService{

    private CommentRepository commentRepository;

    private PostRepository postRepository;

    private UserRepository userRepository;

    private NotificationServiceImpl notificationService;
    private final AlarmRepository alarmRepository;

    private UserEntity getUser(String email){
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("유저정보가 없습니다."));
    }

    private Comment getComment(Long commentId){
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("찾으시는 댓글 정보가 없습니다 : " + commentId));
    }

    @Override
    public CommentDto addFirstComment(CommentFirstCreateRequestDto commentRequestDto, Long postId, String email) {
        UserEntity user = getUser(email);
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));

        Comment comment = commentRequestDto.toEntity(post, user);

        if(!user.getId().equals(post.getUser().getId())) {
            notificationService.customNotify(post.getUser().getId(), post.getId()+":"+user.getStudentId()+" 님이 댓글을 작성하셨습니다.", user.getStudentId()+" 님이 댓글을 작성하셨습니다.", "comment");
            Alarm alarm = Alarm.builder()
                    .post(post)
                    .userEntity(post.getUser())
                    .content(user.getStudentId()+" 님이 댓글을 작성하셨습니다.")
                    .createDate(LocalDateTime.now())
                    .build();
            alarmRepository.save(alarm);
        }
        commentRepository.save(comment);

        return comment.toDto();
    }

    @Override
    public CommentDto addChildComment(CommentRequestDto commentRequestDto, Long parentId, String email) {
        UserEntity user = getUser(email);
        Comment parentComment = getComment(parentId);
        Post post = parentComment.getPost();

        Comment comment = commentRequestDto.toEntity(post, user);
        comment.setParentComment(parentComment);

        if(!user.getId().equals(post.getUser().getId())) {
            notificationService.customNotify(parentComment.getUser().getId(), post.getId()+":"+user.getStudentId()+" 님이 대댓글을 작성하셨습니다.", user.getStudentId()+" 님이 대댓글을 작성하셨습니다.", "comment");
            Alarm alarm = Alarm.builder()
                    .post(post)
                    .userEntity(parentComment.getUser())
                    .content(user.getStudentId()+" 님이 대댓글을 작성하셨습니다.")
                    .createDate(LocalDateTime.now())
                    .build();
            alarmRepository.save(alarm);
        }
        commentRepository.save(comment);

        return comment.toDto();
    }

    @Override
    public List<CommentDto> getCommentList(Long postId) {
        List<Comment> commentList = commentRepository.findByPostId(postId);
        return commentList.stream().map(Comment::toDto).toList();
    }

    @Override
    public List<CommentDto> getParentCommentList(Long postId) {
        List<Comment> parentComments = commentRepository.findByPostId(postId);
        List<Comment> filteredParentComments = parentComments.stream()
                .filter(comment -> comment.getParentComment() == null)
                .toList();
        return filteredParentComments.stream().map(Comment::toDto).toList();
    }

    @Override
    public List<CommentDto> getChildCommentList(Long parentId) {
        Comment parentComment = getComment(parentId);
        return parentComment.getChildComments().stream().map(Comment::toDto).toList();
    }

    @Override
    public CommentDto updateComment(CommentPatchRequestDto commentPatchRequestDto, Long commentId, String email) {
        UserEntity user = getUser(email);
        Comment comment = getComment(commentId);

        if (!comment.getUser().getEmail().equals(user.getEmail())) {
            throw new IllegalStateException("해당 댓글을 수정할 권한이 없습니다.");
        }
        comment.setContent(commentPatchRequestDto.getContent());
        commentRepository.save(comment);
        return comment.toDto();
    }

    @Override
    public CommentDto deleteComment(Long commentId, String email) {
        UserEntity user = getUser(email);
        Comment comment = getComment(commentId);

        if (!comment.getUser().getEmail().equals(user.getEmail())) {
            throw new IllegalStateException("해당 댓글을 삭제할 권한이 없습니다.");
        }
        comment.setContent("삭제된 댓글입니다.");
        commentRepository.save(comment);
        return comment.toDto();
    }
}