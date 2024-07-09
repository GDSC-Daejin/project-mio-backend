package com.gdsc.projectmiobackend.log;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface ApiLogRepository extends JpaRepository<ApiLog, Long> {

    @Transactional
    @Modifying
    @Query("UPDATE ApiLog a SET a.responseStatus = :status, a.response = :response, a.responseTime = CURRENT_TIMESTAMP WHERE a.seq = :seq")
    void updateResponse(@Param("seq") Long seq, @Param("status") int status, @Param("response") String response);
}