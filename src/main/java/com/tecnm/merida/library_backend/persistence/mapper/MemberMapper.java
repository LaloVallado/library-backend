package com.tecnm.merida.library_backend.persistence.mapper;

import com.tecnm.merida.library_backend.domain.Member;
import com.tecnm.merida.library_backend.persistence.entity.Socio;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MemberMapper {

    @Mappings({
            @Mapping(source = "idSocio", target = "memberId"),
            @Mapping(source = "nombre", target = "name"),
            @Mapping(source = "apellidos", target = "lastName"),
            @Mapping(source = "telefono", target = "phone"),
            @Mapping(source = "direccion", target = "address"),
            @Mapping(source = "correoElectronico", target = "email")
    })
    Member toMember(Socio socio);

    List<Member> toMembers(List<Socio> socios);

    // La contrasena nunca viaja en el dominio Member
    @InheritInverseConfiguration
    @Mapping(target = "password", ignore = true)
    Socio toSocio(Member member);
}