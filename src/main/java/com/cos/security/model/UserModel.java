package com.cos.security.model;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Data
public class UserModel {

    @Id //pk
    @GeneratedValue(strategy = GenerationType.IDENTITY) //프로젝트에 연결된 디비의 넘버링 전략을 사용
    private int id; // sequence, auto_increment

//    @Column(nullable = false, length = 100, unique = true)
    private String username;

//    @Column(nullable = false, length = 100)
    private String password;

//    @Column(nullable = false, length = 50)
    private String email;

//    @Enumerated(EnumType.STRING)
    private String role;

    @CreationTimestamp
    private Timestamp createDate;
}
