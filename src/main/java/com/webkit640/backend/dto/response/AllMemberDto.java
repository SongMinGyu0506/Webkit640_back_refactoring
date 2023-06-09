package com.webkit640.backend.dto.response;

import com.webkit640.backend.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class AllMemberDto {
    String email;
    boolean isAdmin;
    String memberBelong;
    String memberType;
    String name;

    public static List<AllMemberDto> entityToDtos(List<Member> members) {
        List<AllMemberDto> result = new ArrayList<>();
        Stream<Member> memberStream = members.stream();
        memberStream.forEach(member -> result.add(
                AllMemberDto.builder()
                        .email(member.getEmail())
                        .isAdmin(member.isAdmin())
                        .memberBelong(member.getMemberBelong())
                        .memberType(member.getMemberType())
                        .name(member.getName())
                        .build()
        ));
        return result;
    }
    public static AllMemberDto entityToDtos(Member member) {
        return AllMemberDto.builder()
                .email(member.getEmail())
                .memberType(member.getMemberType())
                .memberBelong(member.getMemberBelong())
                .name(member.getName())
                .isAdmin(member.isAdmin())
                .build();
    }
}
