package com.webkit640.backend.repository.spec;

import com.webkit640.backend.entity.Image;
import org.springframework.data.jpa.domain.Specification;

public class ImageSpec {
    public static Specification<Image> likeTitle(String title) {
        return (root, query, cb) -> cb.like(root.get("title"), "%"+title+"%");
    }

    public static Specification<Image> equalUser(String name) {
        return (r,q,c) -> c.equal(r.get("member").get("name"), name);
    }

}
