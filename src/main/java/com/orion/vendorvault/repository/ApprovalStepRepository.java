package com.orion.vendorvault.repository;

import com.orion.vendorvault.model.entity.ApprovalStep;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ApprovalStepRepository extends JpaRepository<ApprovalStep, Long> {
    
    @Query("SELECT s FROM ApprovalStep s WHERE s.action = com.orion.vendorvault.model.enums.ApprovalAction.PENDING AND s.dueAt < :now")
    List<ApprovalStep> findSlaBreaches(@Param("now") LocalDateTime now);

    @Query("SELECT s FROM ApprovalStep s WHERE s.action = com.orion.vendorvault.model.enums.ApprovalAction.PENDING AND (s.assignedToUser.id = :userId OR s.roleRequired IN (SELECT r.roleName FROM User u JOIN u.roles r WHERE u.id = :userId))")
    List<ApprovalStep> findPendingStepsForUser(@Param("userId") Long userId);
}
