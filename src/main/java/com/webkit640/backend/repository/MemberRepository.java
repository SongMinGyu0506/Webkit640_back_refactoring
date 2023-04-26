package com.webkit640.backend.repository;

import com.webkit640.backend.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member,Integer> {
    boolean existsMemberByEmail(String email);
    Member findById(int id);
    void deleteById(int id);

    Member findByEmailAndPassword(String email, String password);
}
