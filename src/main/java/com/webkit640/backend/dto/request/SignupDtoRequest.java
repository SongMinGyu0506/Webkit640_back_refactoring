package com.webkit640.backend.dto.request;
import com.webkit640.backend.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class SignupDtoRequest {
    private String email;
    private String password;
    private String memberBelong;
    private String memberType;
    private String name;

    public static Member dtoToEntity(SignupDtoRequest dto) {
        return Member.builder()
                .applicant(null)
                .boards(null)
                .counsels(null)
                .email(dto.getEmail())
                .memberBelong(dto.getMemberBelong())
                .memberType(dto.getMemberType())
                .name(dto.getName())
                .isAdmin(false)
                .password(dto.getPassword())
                .build();
    }
}
