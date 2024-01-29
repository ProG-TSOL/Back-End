package com.backend.prog.domain.member.dao;

import com.backend.prog.domain.member.domain.Member;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;


public interface MemberRepository extends JpaRepository<Member, Integer> {
    @EntityGraph(attributePaths = "roles")
    @Query("select m from Member m where m.email = :email")
    Optional<Member> getWithRoles(String email);
}
