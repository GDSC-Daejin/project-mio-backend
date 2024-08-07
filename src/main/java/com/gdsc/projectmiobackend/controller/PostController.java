package com.gdsc.projectmiobackend.controller;


import com.gdsc.projectmiobackend.dto.ParticipateGetDto;
import com.gdsc.projectmiobackend.dto.PostDto;
import com.gdsc.projectmiobackend.dto.PostMsgDto;
import com.gdsc.projectmiobackend.dto.request.MannerDriverUpdateRequestDto;
import com.gdsc.projectmiobackend.dto.request.MannerPassengerUpdateRequestDto;
import com.gdsc.projectmiobackend.dto.request.PostCreateRequestDto;
import com.gdsc.projectmiobackend.dto.request.PostPatchRequestDto;
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
        PostDto post = this.postService.addPost(postCreateRequestDto, categoryId, user.getEmail());
        return ResponseEntity.ok(post);
    }

    @Operation(summary = "게시글 수정")
    @PatchMapping("post/{id}")
    public ResponseEntity<PostDto> update(
            @PathVariable Long id,
            @RequestBody PostPatchRequestDto patchRequestDto,
            @AuthenticationPrincipal UserInfo user){
        PostDto post = postService.updateById(id, patchRequestDto, user.getEmail());
        return ResponseEntity.ok(post);
    }

    @Operation(summary = "게시글 마감 수정")
    @PatchMapping("post/deadLine/{postId}")
    public ResponseEntity<PostDto> changePostType(
            @PathVariable Long postId,
            @AuthenticationPrincipal UserInfo user) {
        PostDto post = postService.updateTypeChangeById(postId, user.getEmail());
        return ResponseEntity.ok(post);
    }

    @Operation(summary = "게시글 완료 수정")
    @PatchMapping("post/complete/{id}")
    public ResponseEntity<PostDto> update(
            @PathVariable Long id,
            @AuthenticationPrincipal UserInfo user){
        PostDto post = postService.updateFinishById(id, user.getEmail());
        return ResponseEntity.ok(post);
    }

    @Operation(summary = "게시글 삭제")
    @PatchMapping("post/delete/{id}")
    public ResponseEntity<PostMsgDto> delete(@PathVariable Long id, @AuthenticationPrincipal UserInfo user){
        PostMsgDto postMsgDto = postService.deletePostList(id, user.getEmail());
        return ResponseEntity.ok(postMsgDto);
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
        Page<PostDto> postList = postService.findPostList(pageable);

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
        Page<PostDto> postList = postService.findPostList(pageable);

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
        Page<PostDto> postList = postService.findPostList(pageable);

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
        Page<PostDto> postList = postService.findPostList(pageable);

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
        Page<PostDto> postsByCategoryList = postService.findByCategoryId(categoryId, pageable);

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
        Page<PostDto> postsByMemberList = postService.findByMemberId(userId, pageable);

        return ResponseEntity.ok(postsByMemberList);
    }

    @Operation(summary = "유저 활동지역으로 검색하는 게시글 생성순 조회")
    @Parameters({
            @Parameter(name = "sort", description = "sort specification",
                    in = ParameterIn.QUERY, schema = @Schema(type = "createDate,desc"), example = "createDate,desc"),
            @Parameter(name = "page", description = "page number",
                    in = ParameterIn.QUERY, schema = @Schema(type = "integer", defaultValue = "0")),
            @Parameter(name = "size", description = "page size",
                    in = ParameterIn.QUERY, schema = @Schema(type = "integer", defaultValue = "5"))
    })
    @GetMapping("/activityLocation")
    public ResponseEntity<Page<PostDto>> readPostsByActivityLocation(
            @Parameter(hidden = true) Pageable pageable,
            @AuthenticationPrincipal UserInfo user){
        Page<PostDto> postsByActivityLocation = postService.findByRegion3Depth(user.getEmail(), pageable);

        return ResponseEntity.ok(postsByActivityLocation);
    }

    @Operation(summary = "게시글 ID로 상세 조회")
    @GetMapping("detail/{id}")
    public ResponseEntity<PostDto> getPostById(@PathVariable Long id){
        return ResponseEntity.ok(postService.showDetailPost(id));
    }

    @Operation(summary = "위도 경도에 포함된 게시글 리스트 조회")
    @GetMapping("post/location")
    public ResponseEntity<List<PostDto>> readPostByLatitudeAndLongitude(@RequestParam("latitude") Double latitude, @RequestParam("longitude") Double longitude){
        List<PostDto> postList = postService.findByLatitudeAndLongitude(latitude, longitude);

        return ResponseEntity.ok(postList);
    }

    @Operation(summary = "위치로 게시글 리스트 조회")
    @GetMapping("post/location2")
    public ResponseEntity<List<PostDto>> readPostByLocation(@RequestParam("location") String location){
        List<PostDto> postList = postService.findByLocation(location);

        return ResponseEntity.ok(postList);
    }

    @Operation(summary = "게시글 현재 참여 인원")
    @GetMapping("/{postId}/participants/count")
    public ResponseEntity<ParticipateGetDto> getApprovalUserCountByPost(@PathVariable Long postId) {
        ParticipateGetDto participateGetDto = postService.getApprovalUserCountByPost(postId);
        return ResponseEntity.ok(participateGetDto);
    }

    @Operation(summary = "기사 매너 평가", description = "매너는 GOOD, BAD, NORMAL 중에서 선택해야 합니다.")
    @PostMapping("/post/{postId}/evaluation/driver")
    public ResponseEntity<PostMsgDto> updateDriverMannerScore(@PathVariable Long postId,
                                                     @AuthenticationPrincipal UserInfo user,
                                                     @RequestBody MannerDriverUpdateRequestDto mannerDriverUpdateRequestDto) {
        PostMsgDto postMsgDto = postService.driverUpdateManner(postId, user.getEmail(), mannerDriverUpdateRequestDto);
        return ResponseEntity.ok(postMsgDto);
    }

    @Operation(summary = "탑승자 매너 평가" , description = "매너는 GOOD, BAD, NORMAL 중에서 선택해야 합니다.")
    @PostMapping("/post/{userId}/evaluation/passenger")
    public ResponseEntity<PostMsgDto> updatePassengersMannerScore(@PathVariable Long userId,
                                                         @AuthenticationPrincipal UserInfo user,
                                                         @RequestBody MannerPassengerUpdateRequestDto mannerPassengerUpdateRequestDto) {
        PostMsgDto postMsgDto = postService.updateParticipatesManner(userId, mannerPassengerUpdateRequestDto, user.getEmail());
        return ResponseEntity.ok(postMsgDto);
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
        Page<PostDto> postList = postService.findByParticipate(user.getEmail(), pageable);
        return ResponseEntity.ok(postList);
    }

    @Operation(summary = "리뷰 작성 가능한 게시글")
    @Parameters({
            @Parameter(name = "sort", description = "sort specification",
                    in = ParameterIn.QUERY, schema = @Schema(type = "createDate,desc"), example = "createDate,desc"),
            @Parameter(name = "page", description = "page number",
                    in = ParameterIn.QUERY, schema = @Schema(type = "integer", defaultValue = "0")),
            @Parameter(name = "size", description = "page size",
                    in = ParameterIn.QUERY, schema = @Schema(type = "integer", defaultValue = "5"))
    })
    @GetMapping("/post/review")
    public ResponseEntity<Page<PostDto>> readPostByReview(@AuthenticationPrincipal UserInfo user,
                                                          @Parameter(hidden = true) Pageable pageable) {
        Page<PostDto> postList = postService.reviewsCanBeWritten(user.getEmail(), pageable);
        return ResponseEntity.ok(postList);
    }

    @Operation(summary = "특정 게시글 가까운 게시글 조회")
    @GetMapping("/post/distance/{postId}")
    public ResponseEntity<List<PostDto>> readPostByDistance(@PathVariable Long postId) {
        List<PostDto> postList = postService.findByDistance(postId);
        return ResponseEntity.ok(postList);
    }
}
