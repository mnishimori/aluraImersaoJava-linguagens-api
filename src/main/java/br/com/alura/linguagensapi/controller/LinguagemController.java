package br.com.alura.linguagensapi.controller;

import br.com.alura.linguagensapi.model.Language;
import br.com.alura.linguagensapi.repository.LanguageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/languages")
public class LinguagemController {

    @Autowired
    private LanguageRepository repository;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Language save(@RequestBody Language language){
        return repository.save(language);
    }

    @GetMapping
    public List<Language> getLanguages(){
        return repository.findAll();
    }
}
