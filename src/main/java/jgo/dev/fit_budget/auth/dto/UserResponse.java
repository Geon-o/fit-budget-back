package jgo.dev.fit_budget.auth.dto;

import jgo.dev.fit_budget.auth.entity.User;

import java.util.UUID;

public record UserResponse(
        UUID id,
        String name,
        String nickname
) {
    public static UserResponse from(User user) {
        return new UserResponse(user.getId(), user.getName(), user.getNickname());
    }
}
