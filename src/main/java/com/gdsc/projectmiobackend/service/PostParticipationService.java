package com.gdsc.projectmiobackend.service;

import com.gdsc.projectmiobackend.dto.PostDto;
import com.gdsc.projectmiobackend.entity.Participants;
import java.util.List;

public interface PostParticipationService {
    void participateInPost(Long postId, String email, String content);

    Boolean checkParticipate(Long postId, String email);

    List<Participants> getParticipantsByPostId(Long postId);

    void cancelParticipateInPost(Long postId, String email);

    List<PostDto> getPostIdsByUserEmail(String email);

    void participateApproval(Long participantId, String email);

    PostDto getApprovalUser(String email);

    void rejectParticipateInPost(Long participateId, String email);
}
