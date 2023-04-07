package com.project.SpringCubeTimer.repository;

import com.project.SpringCubeTimer.entity.SolveEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface SolveRepository extends JpaRepository<SolveEntity, Long> {

    @Query(value = "SELECT * FROM solve s WHERE s.user_id = :user_id AND s.cube_variable = :cube ORDER BY s.solve_id DESC LIMIT 1", nativeQuery = true)
    SolveEntity findLastSolveByUserIdAndCube(Long user_id, String cube);

    @Query(value = "SELECT * FROM solve s WHERE s.user_id = :userId AND s.cube_variable = :cube ORDER BY s.solve_id DESC LIMIT 5", nativeQuery = true)
    List<SolveEntity> findLastFiveSolveByUserIdAndCube(Long userId, String cube);

    @Query(value = "SELECT * FROM solve s WHERE s.user_id = :userId AND s.cube_variable = :cube ORDER BY s.solve_id DESC LIMIT 12", nativeQuery = true)
    List<SolveEntity> findLastTwelveSolveByUserIdAndCube(Long userId, String cube);
}
