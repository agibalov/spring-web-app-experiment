package me.loki2302;

import java.util.Random;

import me.loki2302.faker.Faker;

import org.apache.commons.lang3.text.WordUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Generator {
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
        Random random = new Random();        
                
        String article = "";        
        int numberOfParagraphs = 5 + random.nextInt(3);
        for(int paragraphCounter = 0; paragraphCounter < numberOfParagraphs; ++paragraphCounter) {
            String paragraph = "";
            int numberOfSentences = 9 + random.nextInt(5);
            for(int sentenceCounter = 0; sentenceCounter < numberOfSentences; ++sentenceCounter) {
                String sentence = faker.Lorem.sentence() + ". ";
                paragraph += WordUtils.capitalize(sentence, '.');            
            }            
            article += paragraph + "\n\n";
        }
        
        return article;
    }       
}