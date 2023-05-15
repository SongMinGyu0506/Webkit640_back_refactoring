package com.webkit640.backend.dto;

import com.webkit640.backend.entity.Trainee;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TraineeDto {
    private String cardinal;
    private String name;
    private String major;
    private String school;
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SaveTraineeResponseDto {
        private String cardinal;
        private String name;
        private String major;
        private String school;

        public static SaveTraineeResponseDto entityToDto(Trainee trainee) {
            return SaveTraineeResponseDto.builder()
                    .name(trainee.getApplicant().getName())
                    .major(trainee.getApplicant().getMajor())
                    .school(trainee.getApplicant().getSchool())
                    .cardinal(trainee.getCardinal())
                    .build();
        }
    }
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TraineeListResponseDto {
        private String email;
        private String name;
        private String school;
        private String major;
        private String grade;
        private String cardinal;

        public static List<TraineeListResponseDto> entitiesToDto(List<Trainee> entities) {
            List<TraineeListResponseDto> result = new ArrayList<>();
            entities.forEach(entity -> result.add(
                    TraineeListResponseDto.builder()
                            .email(entity.getApplicant().getMember().getEmail())
                            .name(entity.getApplicant().getName())
                            .school(entity.getApplicant().getSchool())
                            .major(entity.getApplicant().getMajor())
                            .grade(entity.getApplicant().getSchoolYear())
                            .cardinal(entity.getCardinal())
                            .build()
            ));
            return result;
        }
    }
}
