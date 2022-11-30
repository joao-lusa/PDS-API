package com.lab.orcamento.model.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

@Entity
@Getter@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Funcionario {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(nullable = false, length = 100)
    @NotEmpty(message = "Deve ser informado o nome")
    private String nome;

    @Column(nullable = false, length = 100)
    @NotEmpty(message = "O nome do login deve ser preechido")
    private String login;

    @NotEmpty(message = "O campo de senha deve ser preechido")
    @Column(nullable = false, length = 100)
    private String senha;

    @Column(nullable = false)
    @NotEmpty(message = "O campo de CPF deve ser preechido")
    private String cpf;

    @Column(length = 100)
    private String email;

    @Column(length = 100)
    private String telefone;

}
