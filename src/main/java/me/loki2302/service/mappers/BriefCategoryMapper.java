package me.loki2302.service.mappers;

import java.util.ArrayList;
import java.util.List;
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
        List<BriefCategory> briefCategories = new ArrayList<BriefCategory>(categoryRows.size());
        for(CategoryRow categoryRow : categoryRows) {
            BriefCategory briefCategory = makeBriefCategory(categoryRow);
            briefCategories.add(briefCategory);
        }        
        return briefCategories;
    }
}