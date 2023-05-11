package com.webkit640.backend.repository;

import com.webkit640.backend.entity.Applicant;
import org.springframework.data.jpa.domain.Specification;




public class ApplicantSpec {
    public static Specification<Applicant> likeSchool(String school) {
        return (root, query, CriteriaBuilder) -> CriteriaBuilder.like(root.get("school"),"%" + school + "%");
    }

    public static Specification<Applicant> likeMajor(String major) {
        return (root,query, CriteriaBuilder) -> CriteriaBuilder.like(root.get("major"),"%"+major+"%");
    }
    public static Specification<Applicant> likeCreateDate(String year) {
        return (root, query, CriteriaBuilder) -> CriteriaBuilder.like(root.get("createDate"),"%"+year+"%");
    }
    public static Specification<Applicant> equalMemberId(int memberId) {
        return (root, query, CriteriaBuilder) -> CriteriaBuilder.equal(root.get("member"),memberId);
    }
}
