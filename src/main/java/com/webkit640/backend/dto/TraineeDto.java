package com.webkit640.backend.dto;

import com.webkit640.backend.entity.Trainee;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
}
