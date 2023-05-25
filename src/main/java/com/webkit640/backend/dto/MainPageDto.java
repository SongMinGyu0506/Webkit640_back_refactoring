package com.webkit640.backend.dto;

import com.webkit640.backend.entity.MainPageEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MainPageDto {
    private int id;
    private String recruitmentDate;     //모집 기간
    private String recruitmentTarget;    //모집 대상
    private String totalRecruitment;    //총 선발 인원
    private String eligibility;    //지원 자격
    private String documentSubmissionPeriod;    //서류 제출 기간
    private String additionalRecruitmentPeriod;    //추가 모집 기간
    private String passAnnouncementDate;    //합격 발표일
    private String trainingStartDate;    //교육 시작일
    private String cumulativeStudents;    //총 교육생 수
    private String completeCardinalNumber;    //완료 기수
    private String nonMajor;    //비전공자 수
    private String contact;    //조교 연락처
    private String imagePath;    //팝업 이미지 저장
    private String employmentRate;    //취업생 퍼센트 저장
    private Boolean showEmployment;    //취업 비율 화면표시 여부
    private String employmentEnterprise;    //취업 기업 String

    public static MainPageDto toDto(MainPageEntity mainPage) {
        return MainPageDto.builder()
              .id(mainPage.getId())
              .recruitmentDate(mainPage.getRecruitmentDate())
              .recruitmentTarget(mainPage.getRecruitmentTarget())
              .totalRecruitment(mainPage.getTotalRecruitment())
              .eligibility(mainPage.getEligibility())
              .documentSubmissionPeriod(mainPage.getDocumentSubmissionPeriod())
              .additionalRecruitmentPeriod(mainPage.getAdditionalRecruitmentPeriod())
              .passAnnouncementDate(mainPage.getPassAnnouncementDate())
              .trainingStartDate(mainPage.getTrainingStartDate())
              .cumulativeStudents(mainPage.getCumulativeStudents())
              .completeCardinalNumber(mainPage.getCompleteCardinalNumber())
              .nonMajor(mainPage.getNonMajor())
              .contact(mainPage.getContact())
              .imagePath(mainPage.getImagePath()).build();
    }
    public static MainPageEntity toEntity(MainPageDto mainPageDto) {
        return MainPageEntity.builder()
             .id(mainPageDto.getId())
             .recruitmentDate(mainPageDto.getRecruitmentDate())
             .recruitmentTarget(mainPageDto.getRecruitmentTarget())
             .totalRecruitment(mainPageDto.getTotalRecruitment())
             .eligibility(mainPageDto.getEligibility())
             .documentSubmissionPeriod(mainPageDto.getDocumentSubmissionPeriod())
             .additionalRecruitmentPeriod(mainPageDto.getAdditionalRecruitmentPeriod())
             .passAnnouncementDate(mainPageDto.getPassAnnouncementDate())
             .trainingStartDate(mainPageDto.getTrainingStartDate())
             .cumulativeStudents(mainPageDto.getCumulativeStudents())
             .completeCardinalNumber(mainPageDto.getCompleteCardinalNumber())
             .nonMajor(mainPageDto.getNonMajor())
             .contact(mainPageDto.getContact()).build();
    }
}
