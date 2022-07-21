package br.com.alura.linguagensapi.repository;

import br.com.alura.linguagensapi.model.Language;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface LanguageRepository extends MongoRepository<Language, String> {
}
