package me.loki2302.service.dto.category;

import me.loki2302.dao.rows.Page;
import me.loki2302.service.dto.article.ShortArticle;

public class CompleteCategory {
    public int CategoryId;
    public String Name;
    public Page<ShortArticle> Articles;
    
    @Override
    public String toString() {
        return String.format(
                "CompleteCategory2{CategoryId=%d,Name=%s,Articles=%s}",
                CategoryId,
                Name,
                Articles);
    }
}