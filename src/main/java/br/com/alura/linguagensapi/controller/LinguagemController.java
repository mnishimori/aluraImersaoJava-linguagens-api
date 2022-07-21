package br.com.alura.linguagensapi.controller;

import br.com.alura.linguagensapi.model.Language;
import br.com.alura.linguagensapi.repository.LanguageRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
        return repository.findAll()
                .stream()
                .sorted(Comparator.comparing(Language::getRanking).reversed())
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable String id) {
        Optional<Language> language = repository.findById(id);
        if (language.isPresent()){
            return ResponseEntity.ok().body(language);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> update(@PathVariable String id, @RequestBody Language language){

        Optional<Language> languageOptional = repository.findById(id);

        if (languageOptional.isPresent()){
            Language languageSaved = languageOptional.get();

            BeanUtils.copyProperties(language, languageSaved, "id");

            languageSaved = repository.save(languageSaved);

            return ResponseEntity.ok(languageSaved);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable String id){
        Optional<Language> languageOptional = repository.findById(id);
        if (languageOptional.isPresent()){
            repository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
