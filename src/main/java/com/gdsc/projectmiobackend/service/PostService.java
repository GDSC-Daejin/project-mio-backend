package com.gdsc.projectmiobackend.service;


import com.gdsc.projectmiobackend.dto.PostDto;
import com.gdsc.projectmiobackend.dto.request.MannerUpdateRequestDto;
import com.gdsc.projectmiobackend.dto.request.PostCreateRequestDto;
import com.gdsc.projectmiobackend.dto.request.PostPatchRequestDto;
import com.gdsc.projectmiobackend.dto.request.PostVerifyFinishRequestDto;
import com.gdsc.projectmiobackend.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.util.List;

public interface PostService {

    Post findById(Long id);

    Post addPostList(PostCreateRequestDto postCreateRequestDto, Long categoryId, String email) throws IOException;

    void deletePostList(Long id, String email);

    Post updateById(Long id, PostPatchRequestDto postPatchRequestDto, String email);

    Post updateFinishById(Long id, PostVerifyFinishRequestDto postPatchRequestDto, String email);

    Page<PostDto> findPostList(Pageable pageable);

    Page<PostDto> findByCategoryId(Long categoryId, Pageable pageable);

    Page<PostDto> findByMemberId(Long userId, Pageable pageable);

    Post showDetailPost(Long id);

    List<PostDto> findByLatitudeAndLongitude(Double latitude, Double longitude);

    String getApprovalUserCountByPost(Long postId);

    void driverUpdateManner(Long id, String email, MannerUpdateRequestDto mannerUpdateRequestDto);

    void updateParticipatesManner(Long userId, MannerUpdateRequestDto mannerUpdateRequestDto, String email);

    Page<PostDto> findByParticipate(String email, Pageable pageable);
}
