package com.gdsc.projectmiobackend.service;

import com.gdsc.projectmiobackend.dto.BookMarkDto;
import com.gdsc.projectmiobackend.dto.BookmarkAddDto;
import com.gdsc.projectmiobackend.entity.BookMark;
import com.gdsc.projectmiobackend.entity.Post;
import com.gdsc.projectmiobackend.entity.UserEntity;
import com.gdsc.projectmiobackend.repository.BookMarkRepository;
import com.gdsc.projectmiobackend.repository.PostRepository;
import com.gdsc.projectmiobackend.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class BookMarkServiceImpl implements BookMarkService{
    private final BookMarkRepository bookMarkRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    @Transactional
    @Override
    public BookmarkAddDto saveBookMark(Long postId, String email) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다."));
        UserEntity userEntity = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저가 없습니다."));
        BookMark existingBookMark = bookMarkRepository.findByPostAndUserEntity(post, userEntity);

        BookmarkAddDto dto = new BookmarkAddDto();

        if (existingBookMark == null) {
            addBookMark(post, userEntity);
            dto.setAddAndRemove("북마크 추가 완료");
        } else {
            removeBookMark(post, existingBookMark);
            dto.setAddAndRemove("북마크 제거 완료");
        }

        return dto;
    }

    private void addBookMark(Post post, UserEntity userEntity) {
        // 북마크 카운트 업데이트
        if (post.getBookMarkCount() == null) {
            post.setBookMarkCount(0L);
        }
        post.setBookMarkCount(post.getBookMarkCount() + 1);

        // 북마크 저장
        BookMark newBookMark = new BookMark(post, userEntity, true);
        postRepository.save(post);
        bookMarkRepository.save(newBookMark);
    }

    private void removeBookMark(Post post, BookMark existingBookMark) {
        // 북마크 카운트 업데이트
        if (post.getBookMarkCount() != null && post.getBookMarkCount() > 0) {
            post.setBookMarkCount(post.getBookMarkCount() - 1);
        }

        // 북마크 삭제
        postRepository.save(post);
        bookMarkRepository.delete(existingBookMark);
    }

    @Override
    public List<BookMarkDto> getUserBookMarkList(String email){
        UserEntity userEntity = userRepository.findByEmail(email).orElseThrow(()->new IllegalArgumentException("해당 유저가 없습니다."));
        List<BookMark> bookMarks = bookMarkRepository.findByUserEntity(userEntity);

        return bookMarks.stream().map(bookMark -> BookMarkDto.builder()
                .id(bookMark.getId())
                .userId(bookMark.getUserEntity().getId())
                .postId(bookMark.getPost().getId())
                .status(bookMark.getStatus())
                .build()).toList();
    }
}
