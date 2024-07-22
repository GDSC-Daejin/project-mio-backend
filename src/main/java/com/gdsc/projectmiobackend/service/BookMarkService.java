package com.gdsc.projectmiobackend.service;

import com.gdsc.projectmiobackend.dto.BookMarkDto;
import com.gdsc.projectmiobackend.dto.BookmarkAddDto;

import java.util.List;

public interface BookMarkService {

    BookmarkAddDto saveBookMark(Long postId , String email);

    List<BookMarkDto> getUserBookMarkList(String email);

}
