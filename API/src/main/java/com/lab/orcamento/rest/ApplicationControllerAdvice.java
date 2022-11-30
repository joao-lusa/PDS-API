package com.lab.orcamento.rest;

import com.lab.orcamento.rest.exception.ApiErrors;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ApplicationControllerAdvice {

    public ApiErrors retornoErroEntidade(MethodArgumentNotValidException ex){
        BindingResult bindingResult = ex.getBindingResult();
        List<String> mensagensDeErro = bindingResult
                .getAllErrors()
                .stream()
                .map(objetoErro -> objetoErro.getDefaultMessage())
                .collect(Collectors.toList());
        return new ApiErrors(mensagensDeErro);
    }
}
