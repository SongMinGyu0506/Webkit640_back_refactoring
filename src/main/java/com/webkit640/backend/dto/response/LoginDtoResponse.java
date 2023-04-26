package com.webkit640.backend.dto.response;

import com.webkit640.backend.dto.request.LoginDtoRequest;
import com.webkit640.backend.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class LoginDtoResponse {
    private String name;
    private String email;
    private String memberType;
    private String memberBelong;
    private String token;
    private boolean is_admin;

    public static LoginDtoResponse entityToDto(Member member, String token) {
        return LoginDtoResponse.builder()
                .email(member.getEmail())
                .name(member.getName())
                .memberBelong(member.getMemberBelong())
                .memberType(member.getMemberType())
                .is_admin(member.isAdmin())
                .token(token)
                .build();
    }
}
