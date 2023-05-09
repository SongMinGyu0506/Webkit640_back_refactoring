package com.webkit640.backend.dto;

import com.webkit640.backend.entity.Applicant;
import com.webkit640.backend.entity.FileEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationDto {
    private String name;
    private String major;
    private String school;
    private String schoolNumber;
    private String application;
    private String schoolYear;

    public static Applicant dtoToEntity(ApplicationDto dto) {
        return Applicant.builder()
                .name(dto.getName())
                .application(dto.getApplication())
                .isApply(false)
                .isAdminApply(false)
                .member(null)
                .isSelect(false)
                .schoolYear(dto.getSchoolYear())
                .major(dto.getMajor())
                .school(dto.getSchool())
                .schoolNum(dto.getSchoolNumber())
                .build();
    }


    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ApplicationResponseDto {
        private String name;
        private String major;
        private String school;
        private String schoolNumber;
        private String schoolYear;
        private String fileName;

        public static ApplicationResponseDto entityToDto(Applicant applicant, FileEntity file) {
            return ApplicationResponseDto.builder()
                    .name(applicant.getName())
                    .major(applicant.getMajor())
                    .school(applicant.getSchool())
                    .schoolNumber(applicant.getSchool())
                    .schoolYear(applicant.getSchoolYear())
                    .fileName(file.getFileName())
                    .build();
        }
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SelectionRequestDto {
        List<String> emails;
    }
}
