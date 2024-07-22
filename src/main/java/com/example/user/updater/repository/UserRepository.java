package com.example.user.updater.repository;

import com.example.user.updater.repository.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.OffsetDateTime;
import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    List<User> findByDepartmentIdAndUpdateDateBefore(Long departmentId, OffsetDateTime updateDate);
}
