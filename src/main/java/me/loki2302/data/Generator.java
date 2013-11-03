package me.loki2302.data;

import java.util.Random;

import me.loki2302.charlatan.Charlatan;

import org.springframework.stereotype.Component;

@Component
public class Generator {
    private final Charlatan charlatan = new Charlatan();    
    private final Random random = new Random();
    
    public String username() {
        return charlatan.userName();
    }
    
    public String categoryName() {
        return charlatan.sentence(2 + random.nextInt(2));
    }
    
    public String articleTitle() {
        return charlatan.sentence(5 + random.nextInt(2));
    }
    
    public String articleMarkdown() {
        return charlatan.text(5 + random.nextInt(3));        
    }       
    
    public String commentMarkdown() {        
        return charlatan.text(1 + random.nextInt(2));
    }
}