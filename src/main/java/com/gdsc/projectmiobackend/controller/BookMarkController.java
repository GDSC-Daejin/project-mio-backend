package com.gdsc.projectmiobackend.controller;

import com.gdsc.projectmiobackend.dto.BookMarkDto;
import com.gdsc.projectmiobackend.dto.BookmarkAddDto;
import com.gdsc.projectmiobackend.jwt.dto.UserInfo;
import com.gdsc.projectmiobackend.service.BookMarkService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/bookmark")
public class BookMarkController {

    private final BookMarkService bookMarkService;

    @Operation(summary = "북마크 추가")
    @PostMapping("{postId}")
    public ResponseEntity<BookmarkAddDto> addBookMark(@PathVariable Long postId,
                                                      @AuthenticationPrincipal UserInfo user){

        return ResponseEntity.ok(bookMarkService.saveBookMark(postId, user.getEmail()));
    }

    @Operation(summary = "회원별 북마크 조회")
    @GetMapping("/read")
    public ResponseEntity<List<BookMarkDto>> readBookMark(@AuthenticationPrincipal UserInfo user){
        List<BookMarkDto> bookMarks = this.bookMarkService.getUserBookMarkList(user.getEmail());
        return ResponseEntity.ok(bookMarks);
    }

}
