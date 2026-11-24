package com.tecnm.merida.library_backend.persistence;

import com.tecnm.merida.library_backend.domain.Member;
import com.tecnm.merida.library_backend.domain.repository.MemberRepository;
import com.tecnm.merida.library_backend.persistence.crud.SocioCrudRepository;
import com.tecnm.merida.library_backend.persistence.entity.Socio;
import com.tecnm.merida.library_backend.persistence.mapper.MemberMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class SocioRepository implements MemberRepository {

    @Autowired
    private SocioCrudRepository socioCrudRepository;

    @Autowired
    private MemberMapper mapper;

    @Override
    public List<Member> getAll() {
        List<Socio> socios = (List<Socio>) socioCrudRepository.findAll();
        return mapper.toMembers(socios);
    }

    @Override
    public Optional<Member> getMember(int memberId) {
        return socioCrudRepository.findById(memberId)
                .map(socio -> mapper.toMember(socio));
    }

    @Override
    public Member save(Member member) {
        Socio socio = mapper.toSocio(member);

        // Si es una actualizacion (PUT), conservamos la contrasena encriptada que ya tenia
        if (socio.getIdSocio() != null) {
            socioCrudRepository.findById(socio.getIdSocio())
                    .ifPresent(actual -> socio.setPassword(actual.getPassword()));
        }

        return mapper.toMember(socioCrudRepository.save(socio));
    }

    @Override
    public void delete(int memberId) {
        socioCrudRepository.deleteById(memberId);
    }
}