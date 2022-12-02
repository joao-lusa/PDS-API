package com.lab.orcamento.rest.controller;

import com.lab.orcamento.model.dto.ExameDTO;
import com.lab.orcamento.model.entity.Exame;
import com.lab.orcamento.model.entity.Funcionario;
import com.lab.orcamento.model.repository.ExamesRepository;
import com.lab.orcamento.model.repository.FuncionarioRepository;
import com.lab.orcamento.model.util.BigDecimalConverter;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/exames")
@RequiredArgsConstructor
@Api("Api exames")
@CrossOrigin("http://localhost:8080/login")
public class ExameController {

    private final ExamesRepository examesRepository;
    private final FuncionarioRepository funcionarioRepository;
    private final BigDecimalConverter bigDecimalConverter;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Exame post(@RequestBody ExameDTO exameDTO){
        BigDecimal valorPreco = bigDecimalConverter.converter(exameDTO.getPreco());
        Integer idFuncionario = exameDTO.getIdFuncionario();

        Funcionario funcionario = funcionarioRepository
                .findById(idFuncionario)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "O exame " + idFuncionario + " não existe no nosso sistema"));

        Exame exame = new Exame();
        exame.setNome(exameDTO.getNome());
        exame.setPreco(valorPreco);
        exame.setSigla(exameDTO.getSigla());
        exame.setJejum(exameDTO.getJejum());
        exame.setFuncionario(funcionario);

        return examesRepository.save(exame);
    }

    @GetMapping("{id}")
    public Exame acharPorId(@PathVariable Integer id){
        return examesRepository
                .findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Exame " + id + " não achado no sistema"));
    }

    @GetMapping
    public Page<Exame> pesquisar(
            @RequestParam(value = "nome", required = false, defaultValue = "") String nomeDoExame,
            @RequestParam(required = false) int pagina,
            @RequestParam(required = false) int qtd){
        pagina -= 1;
        Pageable paginacao = PageRequest.of(pagina, qtd);
        if (nomeDoExame == null){
            Page<Exame> exame = examesRepository.findAll(paginacao);
            return exame;
        } else {
            Page<Exame> exame = examesRepository.finByNomeExame("%" + nomeDoExame + "%", paginacao);
            return exame;
        }
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable Integer id){
        examesRepository
                .findById(id)
                .map(exame -> {
                    examesRepository.delete(exame);
                    return Void.TYPE;
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Exame " + id + " não encontado"));
    }

    @PutMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void atualizar(@PathVariable Integer id, @RequestBody ExameDTO dadoAtualizado){
        BigDecimal valorPreco = bigDecimalConverter.converter(dadoAtualizado.getPreco());

        Integer idFuncionario = dadoAtualizado.getIdFuncionario();

        Funcionario funcionario = funcionarioRepository
                .findById(idFuncionario)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "O exame " + idFuncionario + " não existe no nosso sistema"));
        examesRepository
                .findById(id)
                        .map(exame -> {
                            exame.setFuncionario(funcionario);
                            exame.setPreco(valorPreco);
                            exame.setSigla(dadoAtualizado.getSigla());
                            exame.setNome(dadoAtualizado.getNome());
                            return examesRepository.save(exame);
                        })
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                "Exame " + id + " não encontrado!"));
    }
}
