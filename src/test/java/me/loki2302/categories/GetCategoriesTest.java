package me.loki2302.categories;

import static org.junit.Assert.assertEquals;

import java.util.List;

import me.loki2302.service.dto.category.ShortCategory;

import org.junit.Test;

public class GetCategoriesTest extends AbstractCategoryServiceTest {
    @Test
    public void thereAreNoCategoriesByDefault() {
        List<ShortCategory> categories = categoryService.getCategories(1);
        assertEquals(0, categories.size());
    }
}