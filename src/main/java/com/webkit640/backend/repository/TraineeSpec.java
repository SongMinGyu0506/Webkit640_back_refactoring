package com.webkit640.backend.repository;

import com.webkit640.backend.entity.Trainee;
import org.springframework.data.jpa.domain.Specification;


public class TraineeSpec {
    public static Specification<Trainee> equalCardinal(String cardinal) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("cardinal"),cardinal);
    }
    public static Specification<Trainee> likeMajor(String major) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("applicant").get("major"),"%"+major+"%");
    }
    public static Specification<Trainee> likeSchoolYear(String schoolYear) {
        return ((root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("applicant").get("schoolYear"),"%"+schoolYear+"%"));
    }
}
