package com.cos.security.repository;

import com.cos.security.model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;

// CRUD 함수를 JpaRepository가 들고있.
// @Repository라는 어노테이션이 없어도 IoC가 됨.
public interface UserRepository extends JpaRepository<UserModel, Integer> {
}
