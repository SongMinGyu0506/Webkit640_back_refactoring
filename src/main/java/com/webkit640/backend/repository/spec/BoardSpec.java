package com.webkit640.backend.repository.spec;

import com.webkit640.backend.entity.Board;
import org.springframework.data.jpa.domain.Specification;

public class BoardSpec {
    public static Specification<Board> equalType(String type) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("boardType"),type);
    }
    public static Specification<Board> equalAuthor(String author) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("member").get("name"),author);
    }
    public static Specification<Board> likeTitle(String title) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("title"),"%"+title+"%");
    }
    public static Specification<Board> equalIsAdd(boolean isAdd) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("isAdd"),isAdd);
    }
}
