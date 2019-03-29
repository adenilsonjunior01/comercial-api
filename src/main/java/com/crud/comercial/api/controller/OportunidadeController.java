package com.crud.comercial.api.controller;

import com.crud.comercial.api.model.Oportunidade;
import com.crud.comercial.api.repository.OportunidadeRepository;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * Author: Adenilson Junior
 * */

@CrossOrigin(origins = "*", maxAge = 3600)
@JsonIgnoreProperties(ignoreUnknown = true)
@RestController
@RequestMapping("/oportunidades")
public class OportunidadeController {




    @Autowired
    private OportunidadeRepository oportunidades;


    @GetMapping
    public List<Oportunidade> listar() {
        return oportunidades.findAll();
    }


    @GetMapping("/{id}")
    public ResponseEntity<?> retornar(@PathVariable("id") Long id){
        return oportunidades.findById(id)
                .map(obj -> ResponseEntity.ok().body(obj))
                .orElse(ResponseEntity.notFound().build());

    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public Oportunidade adicionar(@Valid @RequestBody Oportunidade oportunidade) {
        Optional<Oportunidade> oportunidadeExiste = oportunidades
                .findByDescricaoAndNomeProspecto(oportunidade.getDescricao(), oportunidade.getNomeProspecto());

        if (oportunidadeExiste.isPresent()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Já existe uma oportunidade com esse nome.");
        }

        return oportunidades.save(oportunidade);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> excluir(@PathVariable("id") Long id) {
        return oportunidades.findById(id)
                .map(obj -> {
                    oportunidades.deleteById(id);
                    return ResponseEntity.ok().build();
                }).orElse(ResponseEntity.noContent().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Oportunidade> update(@PathVariable("id") Long id, @RequestBody Oportunidade oportunidade) {

        return oportunidades.findById(id)
                .map(obj -> {
                    obj.setNomeProspecto(oportunidade.getNomeProspecto());
                    obj.setDescricao(oportunidade.getDescricao());
                    obj.setValor(oportunidade.getValor());
                    Oportunidade updated = oportunidades.save(obj);
                    return ResponseEntity.ok().body(updated);
                }).orElse(ResponseEntity.notFound().build());
    }
}
