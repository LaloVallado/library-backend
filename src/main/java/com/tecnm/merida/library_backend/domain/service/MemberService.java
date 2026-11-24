package com.tecnm.merida.library_backend.domain.service;

import com.tecnm.merida.library_backend.domain.Member;
import com.tecnm.merida.library_backend.domain.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MemberService {

    @Autowired
    private MemberRepository memberRepository;

    public List<Member> getAll() {
        return memberRepository.getAll();
    }

    public Optional<Member> getMember(int memberId) {
        return memberRepository.getMember(memberId);
    }

    public Member save(Member member) {
        validate(member);
        member.setMemberId(null); // en POST el id lo genera la base de datos
        return memberRepository.save(member);
    }

    public Optional<Member> update(int memberId, Member member) {
        validate(member);
        return getMember(memberId).map(existing -> {
            member.setMemberId(memberId);
            return memberRepository.save(member);
        });
    }

    public boolean delete(int memberId) {
        if (getMember(memberId).isPresent()) {
            memberRepository.delete(memberId);
            return true;
        }
        return false;
    }

    private void validate(Member member) {
        if (member.getName() == null || member.getName().isBlank()) {
            throw new IllegalArgumentException("name is required");
        }
        if (member.getName().length() > 60) {
            throw new IllegalArgumentException("name must have at most 60 characters");
        }
        if (member.getEmail() == null || !member.getEmail().contains("@")) {
            throw new IllegalArgumentException("A valid email is required");
        }
        if (member.getEmail().length() > 100) {
            throw new IllegalArgumentException("email must have at most 100 characters");
        }
        if (member.getPhone() != null && member.getPhone().length() > 15) {
            throw new IllegalArgumentException("phone must have at most 15 characters");
        }
        if (member.getLastName() != null && member.getLastName().length() > 100) {
            throw new IllegalArgumentException("lastName must have at most 100 characters");
        }
        if (member.getAddress() != null && member.getAddress().length() > 100) {
            throw new IllegalArgumentException("address must have at most 100 characters");
        }
    }
}