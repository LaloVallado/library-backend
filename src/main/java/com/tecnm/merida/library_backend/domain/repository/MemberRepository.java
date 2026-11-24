package com.tecnm.merida.library_backend.domain.repository;

import com.tecnm.merida.library_backend.domain.Member;

import java.util.List;
import java.util.Optional;

public interface MemberRepository {

    List<Member> getAll();
    Optional<Member> getMember(int memberId);
    Member save(Member member);
    void delete(int memberId);
}