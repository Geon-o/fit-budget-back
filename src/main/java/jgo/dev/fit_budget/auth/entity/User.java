package jgo.dev.fit_budget.auth.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
// 소셜 계정은 (provider, providerId)로만 식별한다. 이메일은 받지 않는다(네이버 email은 계정별 고유값이 아님).
@Table(name = "app_user", uniqueConstraints = @UniqueConstraint(columnNames = {"provider", "provider_id"}))
@Getter
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private String provider;

    @Column(nullable = false)
    private String providerId;

    private String name;

    @Column(nullable = false)
    private String nickname;

    public User(String provider, String providerId, String name, String nickname) {
        this.provider = provider;
        this.providerId = providerId;
        this.name = name;
        this.nickname = nickname;
    }
}
