package me.loki2302;

import java.util.Random;

import me.loki2302.faker.Faker;

import org.apache.commons.lang3.text.WordUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Generator {
    private final Random random = new Random();
    
    @Autowired
    private Faker faker;
    
    public String username() {
        String firstName = faker.Name.firstName();
        String lastName = faker.Name.lastName();
        return String.format("%c%s", firstName.charAt(0), lastName).toLowerCase();
    }
    
    public String articleTitle() {
        return WordUtils.capitalize(faker.Lorem.sentence(5, 2));
    }
    
    public String articleMarkdown() {
        StringBuilder articleBuilder = new StringBuilder();
                        
        int numberOfParagraphs = 5 + random.nextInt(3);
        for(int paragraphCounter = 0; paragraphCounter < numberOfParagraphs; ++paragraphCounter) {
            StringBuilder paragraphBuilder = new StringBuilder();
            int numberOfSentences = 9 + random.nextInt(5);
            for(int sentenceCounter = 0; sentenceCounter < numberOfSentences; ++sentenceCounter) {
                String sentence = faker.Lorem.sentence() + ". ";
                paragraphBuilder.append(WordUtils.capitalize(sentence, '.'));            
            }            
            
            articleBuilder.append(paragraphBuilder);
            articleBuilder.append("\n\n");
        }
        
        return articleBuilder.toString();
    }       
}