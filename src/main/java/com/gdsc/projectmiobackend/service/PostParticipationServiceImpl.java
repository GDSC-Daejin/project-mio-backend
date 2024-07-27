package com.gdsc.projectmiobackend.service;

import com.gdsc.projectmiobackend.common.ApprovalOrReject;
import com.gdsc.projectmiobackend.dto.ParticipateDto;
import com.gdsc.projectmiobackend.dto.PostDto;
import com.gdsc.projectmiobackend.entity.Alarm;
import com.gdsc.projectmiobackend.entity.Participants;
import com.gdsc.projectmiobackend.entity.Post;
import com.gdsc.projectmiobackend.entity.UserEntity;
import com.gdsc.projectmiobackend.notification.service.impl.NotificationServiceImpl;
import com.gdsc.projectmiobackend.repository.AlarmRepository;
import com.gdsc.projectmiobackend.repository.ParticipantsRepository;
import com.gdsc.projectmiobackend.repository.PostRepository;
import com.gdsc.projectmiobackend.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
public class PostParticipationServiceImpl implements PostParticipationService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final ParticipantsRepository participantsRepository;
    private final NotificationServiceImpl notificationService;
    private final AlarmRepository alarmRepository;

    private Post getPost(Long postId){
        return postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글 정보가 없습니다 : " + postId));
    }

    private UserEntity getUser(String email){
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("유저정보가 없습니다."));
    }

    @Override
    public void participateInPost(Long postId, String email, String content) {
        UserEntity user = getUser(email);
        Post post = getPost(postId);

        if(participantsRepository.findByPostIdAndUserId(postId, user.getId()) != null){
            throw new IllegalArgumentException("이미 신청한 게시글입니다.");
        }
        /*
        if(Objects.equals(user.getEmail(), post.getUser().getEmail())){
            throw new IllegalArgumentException("자신의 게시글에는 신청할 수 없습니다.");
        }*/

        Participants participants = Participants.builder()
                .post(post)
                .user(user)
                .content(content)
                .approvalOrReject(ApprovalOrReject.WAITING)
                .verifyFinish(false)
                .driverMannerFinish(false)
                .passengerMannerFinish(false)
                .postUserId(post.getUser().getId())
                .build();

        // 게시글 작성자에게 유저 신청 알림
        notificationService.customNotify(post.getUser().getId(), post.getId(), user.getStudentId() + " 님이 카풀(택시)을 신청하였어요.", "participate");
        Alarm alarm = Alarm.builder()
                .post(post)
                .userEntity(post.getUser())
                .content(user.getStudentId() + " 님이 카풀(택시)을 신청하였어요.")
                .build();
        alarmRepository.save(alarm);


        participantsRepository.save(participants);
    }

    @Override
    public Boolean checkParticipate(Long postId, String email){
        UserEntity user = getUser(email);
        Post post = getPost(postId);

        List<Participants> participants1 = participantsRepository.findByUserId(user.getId());

        for (Participants p : participants1) {
            if(p.getApprovalOrReject() == ApprovalOrReject.APPROVAL
                    && p.getPost().getTargetDate().isEqual(post.getTargetDate())
                    && p.getPost().getVerifyGoReturn() == post.getVerifyGoReturn()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public List<Participants> getParticipantsByPostId(Long postId) {
        List<Participants> participants = participantsRepository.findByPostId(postId);

        if(participants == null){
            throw new IllegalArgumentException("해당 게시글에 참여한 유저가 없습니다.");
        }

        return participants;
    }

    @Override
    public void cancelParticipateInPost(Long postId, String email) {
        UserEntity user = getUser(email);
        Post post = getPost(postId);

        if(Objects.equals(post.getTargetDate(), LocalDate.now())){
            throw new IllegalArgumentException("당일 카풀은 취소할 수 없습니다.");
        }

        Participants participants = participantsRepository.findByPostIdAndUserId(postId, user.getId());

        if(participants.getApprovalOrReject() == ApprovalOrReject.APPROVAL){
            post.setParticipantsCount(post.getParticipantsCount() - 1);
        }

        // 게시글 작성자에게 카풀을 취소한 사람이 있다는 알림 전송
        notificationService.customNotify(post.getUser().getId(), post.getId(), user.getStudentId() + " 님이 카풀(택시)을 취소하였어요.", "participate");
        Alarm alarm = Alarm.builder()
                .post(post)
                .userEntity(post.getUser())
                .content(user.getStudentId() + " 님이 카풀(택시)을 취소하였어요.")
                .build();
        alarmRepository.save(alarm);
        participantsRepository.delete(participants);
    }

    @Override
    public List<ParticipateDto> getPostIdsByUserEmail(String email) {
        List<Participants> participants = participantsRepository.findPostListByUserId(getUser(email).getId());
        List<ParticipateDto> newParticipants = new ArrayList<>();
        for (Participants participant : participants) {
            if(participant.getPostUserId() != participant.getUser().getId()){
                newParticipants.add(participant.toDto());
            }
        }
        return newParticipants;
    }

    @Override
    public void participateApproval(Long participateId, String email) {
        Participants participants = participantsRepository.findById(participateId).orElseThrow(() -> new IllegalArgumentException("해당 참여 정보가 없습니다 : " + participateId));
        Post post = participants.getPost().getUser().getEmail().equals(email) ? participants.getPost() : null;

        if(post == null){
            throw new IllegalArgumentException("해당 유저는 이 게시글의 주최자가 아닙니다.");
        }

        if(post.getParticipantsCount() == null){
            post.setParticipantsCount(0L);
        }

        if(participants.getApprovalOrReject() == ApprovalOrReject.APPROVAL){
            throw new IllegalArgumentException("이미 승인된 유저입니다.");
        }

        if(post.getNumberOfPassengers() <= post.getParticipantsCount()){
            throw new IllegalArgumentException("해당 게시글의 최대 탑승인원을 초과하였습니다.");
        }

        participants.setApprovalOrReject(ApprovalOrReject.APPROVAL);
        post.setParticipantsCount(post.getParticipantsCount() + 1);
        //UserEntity user = participants.getUser();

/*
        List<Participants> participants1 = participantsRepository.findByUserId(user.getId());

        for (Participants p : participants1) {
            if (!p.getId().equals(participateId)) {
                if(!p.getVerifyFinish())
                    participantsRepository.delete(p);
            }
        }*/

        // 게시글 참가자에게 승인 알림
        notificationService.customNotify(participants.getUser().getId(), post.getId(), post.getTitle() + " 글의 카풀(택시) 신청이 승인되었어요.", "participate");

        Alarm alarm = Alarm.builder()
                .post(post)
                .userEntity(participants.getUser())
                .content(post.getTitle() + " 글의 카풀(택시) 신청이 승인되었어요.")
                .build();
        alarmRepository.save(alarm);

        participantsRepository.save(participants);
    }

    public void rejectParticipateInPost(Long participateId, String email){
        Participants participants = participantsRepository.findById(participateId).orElseThrow(() -> new IllegalArgumentException("해당 참여 정보가 없습니다 : " + participateId));
        Post post = participants.getPost().getUser().getEmail().equals(email) ? participants.getPost() : null;

        if(post == null){
            throw new IllegalArgumentException("해당 유저는 이 게시글의 주최자가 아닙니다.");
        }

        if(participants.getApprovalOrReject() == ApprovalOrReject.APPROVAL){
            throw new IllegalArgumentException("이미 승인된 유저입니다.");
        }

        if(post.getParticipantsCount() == null){
            post.setParticipantsCount(0L);
        }

        participants.setApprovalOrReject(ApprovalOrReject.REJECT);
        // 게시글 참가자에게 거절 알림
        notificationService.customNotify(participants.getUser().getId(), post.getId(), post.getTitle() + " 글의 카풀(택시) 신청이 거절되었어요.", "participate");

        Alarm alarm = Alarm.builder()
                .post(post)
                .userEntity(participants.getUser())
                .content(post.getTitle() + " 글의 카풀(택시) 신청이 거절되었어요.")
                .build();
        alarmRepository.save(alarm);

        participantsRepository.delete(participants);
    }


    @Override
    public List<PostDto> getApprovalUser(String email){
        UserEntity user = getUser(email);
        List<Participants> participants = participantsRepository.findByUserId(user.getId());
        List<PostDto> postList = new ArrayList<>();
        if(participants.isEmpty()){
            throw new IllegalArgumentException("해당 유저는 참여한 게시글이 없습니다.");
        }

        for (Participants participant : participants) {
            if(participant.getApprovalOrReject() == ApprovalOrReject.APPROVAL){
                postList.add(participant.getPost().toDto());
            }
        }
        return postList;
    }
}
