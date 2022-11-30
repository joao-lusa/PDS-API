package com.lab.orcamento.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter@Setter
public class ExameDTO {
    private String nome;
    private String preco;
    private String sigla;
    private String jejum;
    private Integer idFuncionario;

    public ExameDTO(){

    }
}

