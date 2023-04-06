package com.project.SpringCubeTimer.repository;

import com.project.SpringCubeTimer.entity.SolveEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface SolveRepository extends JpaRepository<SolveEntity, Long> {

    @Query(value = "SELECT * FROM solve s WHERE s.user_id = :user_id ORDER BY s.solve_id DESC LIMIT 1", nativeQuery = true)
    SolveEntity findLastSolveByUserId(Long user_id);

    @Query(value = "SELECT * FROM solve s WHERE s.user_id = :userId ORDER BY s.solve_id DESC LIMIT 5", nativeQuery = true)
    List<SolveEntity> getLastFiveSolveByUser_id(Long userId);

    @Query(value = "SELECT * FROM solve s WHERE s.user_id = :userId ORDER BY s.solve_id DESC LIMIT 12", nativeQuery = true)
    List<SolveEntity> getLastTwelveSolveByUser_id(Long userId);
}
