package com.gdsc.projectmiobackend.controller;


import com.gdsc.projectmiobackend.dto.PostDto;
import com.gdsc.projectmiobackend.dto.request.MannerUpdateRequestDto;
import com.gdsc.projectmiobackend.dto.request.PostCreateRequestDto;
import com.gdsc.projectmiobackend.dto.request.PostPatchRequestDto;
import com.gdsc.projectmiobackend.dto.request.PostVerifyFinishRequestDto;
import com.gdsc.projectmiobackend.entity.Post;
import com.gdsc.projectmiobackend.jwt.dto.UserInfo;
import com.gdsc.projectmiobackend.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@AllArgsConstructor
@RestController
@Tag(name = "게시글")
public class PostController {

    private final PostService postService;

    @Operation(summary = "게시글 생성")
    @PostMapping(value = "post/{categoryId}")
    public ResponseEntity<PostDto> create(
            @RequestBody PostCreateRequestDto postCreateRequestDto,
            @PathVariable Long categoryId,
            @AuthenticationPrincipal UserInfo user) throws Exception{
        System.out.println("create");

        postCreateRequestDto.setViewCount(0L);

        Post post = this.postService.addPostList(postCreateRequestDto, categoryId, user.getEmail());

        return ResponseEntity.ok(new PostDto(post));
    }

    @Operation(summary = "게시글 수정")
    @PatchMapping("post/{id}")
    public ResponseEntity<PostDto> update(
            @PathVariable Long id,
            @RequestBody PostPatchRequestDto patchRequestDto,
            @AuthenticationPrincipal UserInfo user){
        System.out.println("update");

        Post post = postService.updateById(id, patchRequestDto, user.getEmail());
        return ResponseEntity.ok(new PostDto(post));
    }

    @Operation(summary = "게시글 완료 수정")
    @PatchMapping("post/verfiyFinish/{id}")
    public ResponseEntity<PostDto> update(
            @PathVariable Long id,
            @RequestBody PostVerifyFinishRequestDto patchRequestDto,
            @AuthenticationPrincipal UserInfo user){
        System.out.println("update");

        Post post = postService.updateFinishById(id, patchRequestDto, user.getEmail());
        return ResponseEntity.ok(new PostDto(post));
    }

    @Operation(summary = "게시글 삭제")
    @DeleteMapping("post/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id, @AuthenticationPrincipal UserInfo user){
        System.out.println("delete");

        this.postService.deletePostList(id, user.getEmail());
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "게시글 생성 날짜순 전체 조회")
    @Parameters({
            @Parameter(name = "sort", description = "sort specification",
                    in = ParameterIn.QUERY, schema = @Schema(type = "createDate,desc"), example = "createDate,desc"),
            @Parameter(name = "page", description = "page number",
                    in = ParameterIn.QUERY, schema = @Schema(type = "integer", defaultValue = "0")),
            @Parameter(name = "size", description = "page size",
                    in = ParameterIn.QUERY, schema = @Schema(type = "integer", defaultValue = "5"))
    })
    @GetMapping("/readAll")
    public ResponseEntity<Page<PostDto>> readAll(
            @Parameter(hidden = true) Pageable pageable){
        System.out.println("read all");

        Page<PostDto> postList = this.postService.findPostList(pageable);

        return ResponseEntity.ok(postList);
    }

    @Operation(summary = "게시글 마감 날짜순 전체 조회")
    @Parameters({
            @Parameter(name = "sort", description = "sort specification",
                    in = ParameterIn.QUERY, schema = @Schema(type = "targetDate,desc"), example = "targetDate,desc"),
            @Parameter(name = "page", description = "page number",
                    in = ParameterIn.QUERY, schema = @Schema(type = "integer", defaultValue = "0")),
            @Parameter(name = "size", description = "page size",
                    in = ParameterIn.QUERY, schema = @Schema(type = "integer", defaultValue = "5"))
    })
    @GetMapping("/readAll/targetDate")
    public ResponseEntity<Page<PostDto>> readAllByTargetDate(
            @Parameter(hidden = true) Pageable pageable){
        System.out.println("read all");

        Page<PostDto> postList = this.postService.findPostList(pageable);

        return ResponseEntity.ok(postList);
    }

    @Operation(summary = "요금순 정렬")
    @Parameters({
            @Parameter(name = "sort", description = "sort specification",
                    in = ParameterIn.QUERY, schema = @Schema(type = "cost,desc"), example = "cost,asc"),
            @Parameter(name = "page", description = "page number",
                    in = ParameterIn.QUERY, schema = @Schema(type = "integer", defaultValue = "0")),
            @Parameter(name = "size", description = "page size",
                    in = ParameterIn.QUERY, schema = @Schema(type = "integer", defaultValue = "5"))
    })
    @GetMapping("/readAll/cost")
    public ResponseEntity<Page<PostDto>> readAllByCost(
            @Parameter(hidden = true) Pageable pageable){
        System.out.println("read all");

        Page<PostDto> postList = this.postService.findPostList(pageable);

        return ResponseEntity.ok(postList);
    }

    @Operation(summary = "게시글 조회수 순 전체 조회")
    @Parameters({
            @Parameter(name = "sort", description = "sort specification",
                    in = ParameterIn.QUERY, schema = @Schema(type = "viewCount,desc"), example = "viewCount,desc"),
            @Parameter(name = "page", description = "page number",
                    in = ParameterIn.QUERY, schema = @Schema(type = "integer", defaultValue = "0")),
            @Parameter(name = "size", description = "page size",
                    in = ParameterIn.QUERY, schema = @Schema(type = "integer", defaultValue = "5"))
    })
    @GetMapping("/viewCount")
    public ResponseEntity<Page<PostDto>> readAllByViewCount(
            @Parameter(hidden = true) Pageable pageable){
        System.out.println("read all");

        Page<PostDto> postList = this.postService.findPostList(pageable);

        return ResponseEntity.ok(postList);
    }

    @Operation(summary = "카테고리 ID로 게시글 생성순 전체 조회")
    @Parameters({
            @Parameter(name = "sort", description = "sort specification",
                    in = ParameterIn.QUERY, schema = @Schema(type = "createDate,desc"), example = "createDate,desc"),
            @Parameter(name = "page", description = "page number",
                    in = ParameterIn.QUERY, schema = @Schema(type = "integer", defaultValue = "0")),
            @Parameter(name = "size", description = "page size",
                    in = ParameterIn.QUERY, schema = @Schema(type = "integer", defaultValue = "5"))
    })
    @GetMapping("categoryPost/{categoryId}")
    public ResponseEntity<Page<PostDto>> readPostsByCategory(
            @Parameter(hidden = true) Pageable pageable,
            @PathVariable Long categoryId){
        System.out.println("Posts by category");

        Page<PostDto> postsByCategoryList = this.postService.findByCategoryId(categoryId, pageable);

        return ResponseEntity.ok(postsByCategoryList);
    }

    @Operation(summary = "회원 ID로 게시글 생성순 전체 조회")
    @Parameters({
            @Parameter(name = "sort", description = "sort specification",
                    in = ParameterIn.QUERY, schema = @Schema(type = "createDate,desc"), example = "createDate,desc"),
            @Parameter(name = "page", description = "page number",
                    in = ParameterIn.QUERY, schema = @Schema(type = "integer", defaultValue = "0")),
            @Parameter(name = "size", description = "page size",
                    in = ParameterIn.QUERY, schema = @Schema(type = "integer", defaultValue = "5"))
    })
    @GetMapping("memberPost/{userId}")
    public ResponseEntity<Page<PostDto>> readPostsByUser(
            @Parameter(hidden = true) Pageable pageable,
            @PathVariable Long userId){
        System.out.println("Posts by member");

        Page<PostDto> postsByMemberList = this.postService.findByMemberId(userId, pageable);

        return ResponseEntity.ok(postsByMemberList);
    }

    @Operation(summary = "게시글 ID로 상세 조회")
    @GetMapping("detail/{id}")
    public ResponseEntity<PostDto> getPostById(@PathVariable Long id){
        Post post = postService.showDetailPost(id);
        return ResponseEntity.ok(new PostDto(post));
    }

    @Operation(summary = "위도 경도에 포함된 게시글 리스트 조회")
    @GetMapping("post/location")
    public ResponseEntity<List<PostDto>> readPostByLatitudeAndLongitude(@RequestParam("latitude") Double latitude, @RequestParam("longitude") Double longitude){
        System.out.println("read all");

        List<PostDto> postList = this.postService.findByLatitudeAndLongitude(latitude, longitude);

        return ResponseEntity.ok(postList);
    }

    @Operation(summary = "게시글 현재 참여 인원")
    @GetMapping("/{postId}/participants/count")
    public ResponseEntity<String> getApprovalUserCountByPost(@PathVariable Long postId) {
        String result = postService.getApprovalUserCountByPost(postId);
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "기사 매너 평가")
    @PostMapping("/post/{postId}/evaluation/driver")
    public ResponseEntity<?> updateDriverMannerScore(@PathVariable Long postId,
                                                     @AuthenticationPrincipal UserInfo user,
                                                     @RequestBody MannerUpdateRequestDto mannerUpdateRequestDto) {
        postService.driverUpdateManner(postId, user.getEmail(), mannerUpdateRequestDto);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "탑승자 매너 평가")
    @PostMapping("/post/{userId}/evaluation/passenger")
    public ResponseEntity<?> updatePassengersMannerScore(@PathVariable Long userId,
                                                         @AuthenticationPrincipal UserInfo user,
                                                         @RequestBody MannerUpdateRequestDto mannerUpdateRequestDto) {
        postService.updateParticipatesManner(userId, mannerUpdateRequestDto, user.getEmail());
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "탑승자로 참여한 게시글")
    @Parameters({
            @Parameter(name = "sort", description = "sort specification",
                    in = ParameterIn.QUERY, schema = @Schema(type = "createDate,desc"), example = "createDate,desc"),
            @Parameter(name = "page", description = "page number",
                    in = ParameterIn.QUERY, schema = @Schema(type = "integer", defaultValue = "0")),
            @Parameter(name = "size", description = "page size",
                    in = ParameterIn.QUERY, schema = @Schema(type = "integer", defaultValue = "5"))
    })
    @GetMapping("/post/participate")
    public ResponseEntity<Page<PostDto>> readPostByParticipate(@AuthenticationPrincipal UserInfo user,
                                                               @Parameter(hidden = true) Pageable pageable) {
        Page<PostDto> postList = this.postService.findByParticipate(user.getEmail(), pageable);
        return ResponseEntity.ok(postList);
    }
}
