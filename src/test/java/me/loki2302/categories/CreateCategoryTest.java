package me.loki2302.categories;

import org.junit.Test;

public class CreateCategoryTest extends AbstractCategoryServiceTest {
    @Test        
    public void canCreateCategory() {
        categoryService.createCategory("my category");            
    }
}