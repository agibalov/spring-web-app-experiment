package me.loki2302.categories;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.List;

import me.loki2302.service.dto.category.BriefCategory;

import org.junit.Test;

public class GetBriefCategoriesTest extends AbstractCategoryServiceTest {
    @Test
    public void thereAreNoCategoriesByDefault() {
        List<BriefCategory> categories = categoryService.getBriefCategories();
        assertEquals(0, categories.size());
    }
    
    @Test
    public void canGetCategoryList() {
        int category1Id = categoryService.createCategory("my category #1");
        int category2Id = categoryService.createCategory("my category #2");
        List<BriefCategory> categories = categoryService.getBriefCategories();
        assertEquals(2, categories.size());
        assertCategoryListHasCategory(categories, category1Id, "my category #1");
        assertCategoryListHasCategory(categories, category2Id, "my category #2");
    }
    
    private static void assertCategoryListHasCategory(List<BriefCategory> briefCategories, int categoryId, String categoryName) {
        for(BriefCategory category : briefCategories) {
            if(category.CategoryId == categoryId) {
                assertEquals(categoryName, category.Name);
                return;
            }
        }
        
        fail();
    }
}