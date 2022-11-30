package com.lab.orcamento.model.impl;

import com.lab.orcamento.model.entity.Funcionario;
import com.lab.orcamento.model.repository.FuncionarioRepository;
import com.lab.orcamento.rest.exception.SenhaInvalidaException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class FuncionarioServiceImpl implements UserDetailsService {

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private FuncionarioRepository repository;

    @Transactional
    public Funcionario salvar(Funcionario funcionario){
        return  repository.save(funcionario);
    }

    public UserDetails autenticar( Funcionario funcionario){
        UserDetails user = loadUserByUsername(funcionario.getLogin());
        boolean senhasBatem = encoder.matches(funcionario.getSenha(), user.getPassword());
        if (senhasBatem){
            return user;
        }
        throw new SenhaInvalidaException();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
       Funcionario funcionario = repository.findByLogin(username)
               .orElseThrow(() -> new UsernameNotFoundException("Usuario não encontrado na base de dados"));

       return User
               .builder()
               .username(funcionario.getLogin())
               .password(funcionario.getSenha())
               .roles("FUNCIONARIO")
               .build();
    }
}
