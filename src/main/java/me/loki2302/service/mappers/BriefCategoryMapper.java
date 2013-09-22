package me.loki2302.service.mappers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.loki2302.dao.rows.CategoryRow;
import me.loki2302.service.dto.category.BriefCategory;

import org.springframework.stereotype.Component;

@Component
public class BriefCategoryMapper {
    public BriefCategory makeBriefCategory(CategoryRow categoryRow) {
        BriefCategory category = new BriefCategory();
        category.CategoryId = categoryRow.Id;
        category.Name = categoryRow.Name;            
        return category;
    }
    
    public List<BriefCategory> makeBriefCategories(List<CategoryRow> categoryRows) {
        List<BriefCategory> briefCategories = new ArrayList<BriefCategory>();
        for(CategoryRow categoryRow : categoryRows) {
            BriefCategory briefCategory = makeBriefCategory(categoryRow);
            briefCategories.add(briefCategory);
        }        
        return briefCategories;
    }
    
    public Map<Integer, BriefCategory> makeBriefCategoriesMap(List<CategoryRow> categoryRows) {
        Map<Integer, BriefCategory> categoriesMap = new HashMap<Integer, BriefCategory>();
        for(CategoryRow categoryRow : categoryRows) {
            BriefCategory briefCategory = makeBriefCategory(categoryRow);
            categoriesMap.put(briefCategory.CategoryId, briefCategory);
        }
        return categoriesMap;
    }
}