package com.lab.orcamento.model.repository;

import com.lab.orcamento.model.entity.Exame;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface ExamesRepository extends JpaRepository<Exame, Integer> {

    @Query(" select n from Exame n where upper(n.nome) like upper(:nome) ")
    Page<Exame> finByNomeExame(@Param("nome") String nome, Pageable paginacao);
}
