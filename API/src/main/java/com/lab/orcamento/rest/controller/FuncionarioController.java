package com.lab.orcamento.rest.controller;

import com.lab.orcamento.model.dto.CredenciaisDTO;
import com.lab.orcamento.model.dto.TokenDTO;
import com.lab.orcamento.model.entity.Funcionario;
import com.lab.orcamento.model.impl.FuncionarioServiceImpl;
import com.lab.orcamento.model.repository.FuncionarioRepository;
import com.lab.orcamento.rest.exception.SenhaInvalidaException;
import com.lab.orcamento.rest.security.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/funcionario")
@RequiredArgsConstructor
public class FuncionarioController {

    private final FuncionarioRepository repository;
    private final FuncionarioServiceImpl funcionarioService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Funcionario salvar(@RequestBody @Valid Funcionario funcionario){
        String senhaCriptografada = passwordEncoder.encode(funcionario.getSenha());
        funcionario.setSenha(senhaCriptografada);
        return repository.save(funcionario);
    }

    @PostMapping("/auth")
    public TokenDTO autenticar(@RequestBody CredenciaisDTO credenciais){
        try {
            Funcionario funcionario = Funcionario.builder()
                    .login(credenciais.getLogin())
                    .senha(credenciais.getSenha()).build();
            UserDetails fundionarioAutenticado = funcionarioService.autenticar(funcionario);
            Funcionario byLogin = repository
                    .findByLogin(credenciais.getLogin())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Funcionario não encontrado"));
            String token = jwtService.gerarToken(funcionario);
            return new TokenDTO(funcionario.getLogin(), token, byLogin.getId());
        }catch (UsernameNotFoundException | SenhaInvalidaException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @GetMapping("{id}")
    public Funcionario acharPorId(@PathVariable Integer id){
        return repository
                .findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Funcionario " + id + " não cadastrado"));
    }

    @GetMapping
    public List<Funcionario> pesquisar(
            @RequestParam(value = "nome", required = false, defaultValue = "") String nomeDoFuncionario) {
        return repository.finByNomeFuncionario("%" + nomeDoFuncionario + "%");
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable Integer id){
        repository
                .findById(id)
                .map(funcionario -> {
                    repository.delete(funcionario);
                    return Void.TYPE;
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Funcionario " + id + " não cadastrado"));
    }

    @PutMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void atualizar(@PathVariable Integer id, @Valid @RequestBody Funcionario funcionarioAtualizado){
        repository
                .findById(id)
                .map(funcionario -> {
                    funcionario.setNome(funcionarioAtualizado.getNome());
                    funcionario.setLogin(funcionarioAtualizado.getLogin());
                    funcionario.setSenha(funcionario.getSenha());
                    funcionario.setCpf(funcionarioAtualizado.getCpf());
                    funcionario.setEmail(funcionarioAtualizado.getEmail());
                    funcionario.setTelefone(funcionarioAtualizado.getTelefone());
                    return repository.save(funcionario);
                })
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"Funcionário "+id+" não cadastrado"));
    }

}
