package com.gdsc.projectmiobackend.controller;

import com.gdsc.projectmiobackend.dto.PostDto;
import com.gdsc.projectmiobackend.entity.UserEntity;
import com.gdsc.projectmiobackend.jwt.dto.UserInfo;
import com.gdsc.projectmiobackend.service.PostParticipationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springdoc.core.converters.models.PageableAsQueryParam;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@Tag(name = "게시글 참여 유저")
public class ParticipantsController {

    private final PostParticipationService participantsService;
    @Operation(summary = "유저 게시글 참여")
    @PostMapping("/{postId}/participate")
    public ResponseEntity<Void> participateInPost(@PathVariable Long postId, @AuthenticationPrincipal UserInfo user) {
        participantsService.participateInPost(postId, user.getEmail());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "게시글 별 참여자 조회")
    @GetMapping("/{postId}/participants")
    public ResponseEntity<List<UserEntity>> getParticipantsByPostId(@PathVariable Long postId) {
        List<UserEntity> participants = participantsService.getParticipantsByPostId(postId);
        return new ResponseEntity<>(participants, HttpStatus.OK);
    }

    @Operation(summary = "유저 게시글 참여 취소")
    @DeleteMapping("/{postId}/participate")
    public ResponseEntity<Void> cancelParticipateInPost(@PathVariable Long postId, @AuthenticationPrincipal UserInfo user) {
        participantsService.cancelParticipateInPost(postId, user.getEmail());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "유저가 참여한 게시글 조회")
    @PageableAsQueryParam
    @GetMapping("/user/participants")
    public ResponseEntity<List<PostDto>> getParticipantsByUserId(@AuthenticationPrincipal UserInfo user) {

        List<PostDto> posts = participantsService.getPostIdsByUserEmail(user.getEmail());
        return ResponseEntity.ok(posts);
    }
}