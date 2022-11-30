package com.lab.orcamento.model.repository;

import com.lab.orcamento.model.entity.Funcionario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FuncionarioRepository extends JpaRepository<Funcionario, Integer> {
    @Query(" select n from Funcionario n where upper(n.nome) like upper(:nome) ")
    List<Funcionario> finByNomeFuncionario(@Param("nome") String nome);

    Optional<Funcionario> findByLogin(String login);
}
