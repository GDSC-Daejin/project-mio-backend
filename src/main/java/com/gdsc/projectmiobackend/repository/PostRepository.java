package com.gdsc.projectmiobackend.repository;


import com.gdsc.projectmiobackend.entity.Category;
import com.gdsc.projectmiobackend.entity.Post;
import com.gdsc.projectmiobackend.entity.UserEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    Page<Post> findByCategoryAndIsDeleteYN(Category category, Pageable pageable, String isDeleteYN);

    Page<Post> findByUserAndIsDeleteYN(UserEntity user, Pageable pageable, String isDeleteYN);

    List<Post> findByLatitudeAndLongitudeAndIsDeleteYN(Double latitude, Double longitude, String isDeleteYN);

    List<Post> findByLocationContainingAndIsDeleteYN(String location, String isDeleteYN);

    @Query("SELECT p FROM Post p WHERE (6371 * acos(cos(radians((SELECT latitude FROM Post WHERE id = ?1))) * cos(radians(p.latitude)) * cos(radians(p.longitude) - radians((SELECT longitude FROM Post WHERE id = ?1))) + sin(radians((SELECT latitude FROM Post WHERE id = ?1))) * sin(radians(p.latitude)))) < 3")
    List<Post> findByDistanceAndIsDeleteYN(Long postId, String isDeleteYN);

    @Modifying
    @Transactional
    @Query("UPDATE Post p SET p.isDeleteYN = 'Y' WHERE p.user.id = :userId and p.id = :id")
    void deletePost(Long userId, Long id);

    Page<Post> findAllByIsDeleteYN(String isDeleteYN, Pageable pageable);
}
