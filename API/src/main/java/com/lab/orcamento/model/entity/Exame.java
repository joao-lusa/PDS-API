package com.lab.orcamento.model.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Entity
@Getter@Setter
public class Exame {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @NotNull(message = "Deve ser preechido o nome do exame!")
    @Column(nullable = false, length = 100)
    private String nome;

    @Column
    @Min(value = 1, message = "valor menor que 1 não é permetido!")
    private BigDecimal preco;

    @NotNull(message = "deve ser informado uma sigla no exame!")
    @Column(length = 3)
    private String sigla;

    @NotNull(message = "deve ser informado se o cliente deve ficar em jejum!")
    @Column(length = 100)
    private String jejum;

    @ManyToOne
    @NotNull(message = "Deve ser informada o funcionario para fazer fazer alterações em exames!")
    @JoinColumn(name = "id_funcionario")
    private Funcionario funcionario;

}
