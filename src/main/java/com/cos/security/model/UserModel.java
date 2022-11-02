package com.cos.security.model;

import java.io.Serializable;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Data
@NoArgsConstructor
public class UserModel implements Serializable {

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

    private String provider;

    private String providerId;

    @CreationTimestamp
    private Timestamp createDate;

    @Builder
    public UserModel(String username, String password, String email, String role, String provider, String providerId, Timestamp createDate) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
        this.provider = provider;
        this.providerId = providerId;
        this.createDate = createDate;
    }
}
