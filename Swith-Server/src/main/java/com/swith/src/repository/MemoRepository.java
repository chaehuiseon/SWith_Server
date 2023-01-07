package com.swith.src.repository;

import com.swith.domain.memo.entity.Memo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface MemoRepository extends JpaRepository<Memo, Long> {

    @Query("select m from Memo m " +
            "where m.user.userIdx = :userIdx " +
            "and m.session.sessionIdx = :sessionIdx")
    Optional<Memo> findByUserAndSession(Long userIdx, Long sessionIdx);

    @Query("select (count(m) > 0) from Memo m " +
            "where m.user.userIdx = :userIdx " +
            "and m.session.sessionIdx = :sessionIdx")
    boolean existsByUserAndSession(Long userIdx, Long sessionIdx);
}
