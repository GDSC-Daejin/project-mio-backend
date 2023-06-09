package com.gdsc.projectmiobackend.service;

import com.gdsc.projectmiobackend.entity.BookMark;

import java.util.List;

public interface BookMarkService {

    String saveBookMark(Long postId , String email);

    List<BookMark> getUserBookMarkList(String email);

}
