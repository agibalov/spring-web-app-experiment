package me.loki2302.data;

import java.util.Random;

import me.loki2302.faker.Faker;
import net._01001111.text.LoremIpsum;

import org.apache.commons.lang3.text.WordUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Generator {
    private final Random random = new Random();
    private final LoremIpsum jlorem = new LoremIpsum();
    
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
        int numberOfParagraphs = 5 + random.nextInt(3);
        return jlorem.paragraphs(numberOfParagraphs).replace("  ", " ");
    }       
}