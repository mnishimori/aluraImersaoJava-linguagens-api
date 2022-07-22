package br.com.alura.linguagensapi.controller;

import br.com.alura.linguagensapi.exception.EntityEmptyException;
import br.com.alura.linguagensapi.exception.EntityNotFoundException;
import br.com.alura.linguagensapi.model.Language;
import br.com.alura.linguagensapi.repository.LanguageRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Field;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
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

        try {
            Language languageSaved = this.getLanguageById(id);

            BeanUtils.copyProperties(language, languageSaved, "id");

            languageSaved = repository.save(languageSaved);

            return ResponseEntity.ok(languageSaved);

        } catch (EntityNotFoundException e) {

            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> partialUpdate(@PathVariable String id,
                                           @RequestBody Map<String, Object> languageMap) {

        try {
            Language languageSaved = this.getLanguageById(id);

            languageSaved = this.generateLanguageToUpdate(languageSaved, languageMap);

            languageSaved = repository.save(languageSaved);

            return ResponseEntity.ok(languageSaved);

        } catch (EntityNotFoundException e) {

            return ResponseEntity.notFound().build();
        }
    }

    private Language generateLanguageToUpdate(Language languageSaved, Map<String, Object> languageMap) {

        if (languageMap == null || languageMap.isEmpty()) {
            throw new EntityEmptyException("A entidade está vazia e inválida para realizar a operação!");
        }

        ObjectMapper objectMapper = new ObjectMapper();
        Language languageNewValues = objectMapper.convertValue(languageMap, Language.class);

        languageMap.forEach((propertyName, value) -> {
            Field field = ReflectionUtils.findField(Language.class, propertyName);
            field.setAccessible(true);

            Object newValue = ReflectionUtils.getField(field, languageNewValues);

            ReflectionUtils.setField(field, languageSaved, newValue);
        });

        return languageSaved;
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

    private Language getLanguageById(String id){
        Optional<Language> languageOptional = repository.findById(id);
        if (!languageOptional.isPresent()){
            throw new EntityNotFoundException("Entidade não encontrada com o id " + id);
        }
        return languageOptional.get();
    }
}
