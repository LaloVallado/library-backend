package com.tecnm.merida.library_backend.persistence;

import com.tecnm.merida.library_backend.persistence.crud.SocioCrudRepository;
import com.tecnm.merida.library_backend.persistence.entity.Socio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

// Le dice a Spring Security como buscar un usuario: es un socio buscado por su correo
@Service
public class SocioUserDetailsService implements UserDetailsService {

    @Autowired
    private SocioCrudRepository socioCrudRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Socio socio = socioCrudRepository.findByCorreoElectronico(email)
                .orElseThrow(() -> new UsernameNotFoundException("Member not found: " + email));

        if (socio.getPassword() == null) {
            throw new UsernameNotFoundException("Member has no password: " + email);
        }

        return User.withUsername(socio.getCorreoElectronico())
                .password(socio.getPassword())
                .roles("MEMBER")
                .build();
    }
}