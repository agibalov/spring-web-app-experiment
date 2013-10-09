package me.loki2302.categories;

import static org.junit.Assert.*;
import me.loki2302.service.dto.category.CompleteCategory;
import me.loki2302.service.exceptions.CategoryNotFoundException;

import org.junit.Test;

public class GetCategoryTest extends AbstractCategoryServiceTest {
    @Test
    public void canGetCategory() {
        int categoryId = categoryService.createCategory("my category");
        CompleteCategory category = categoryService.getCategory(categoryId, 1, 0);
        assertEquals(categoryId, category.CategoryId);
        assertEquals("my category", category.Name);
    }
    
    @Test(expected = CategoryNotFoundException.class)
    public void cantGetCategoryThatDoesNotExist() {
        categoryService.getCategory(123, 1, 0);
    }
}