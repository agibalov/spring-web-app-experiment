package me.loki2302.service.mappers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.loki2302.dao.rows.ArticleVoteStatsRow;

import org.springframework.stereotype.Component;

@Component
public class ArticleVoteStatsMapper {
    public Map<Integer, ArticleVoteStatsRow> makeArticleVoteStatsMap(List<ArticleVoteStatsRow> articleVoteStatsRows) {
        Map<Integer, ArticleVoteStatsRow> articleVoteStatsMap = new HashMap<Integer, ArticleVoteStatsRow>();
        for(ArticleVoteStatsRow articleVoteStatsRow : articleVoteStatsRows) {
            int articleId = articleVoteStatsRow.ArticleId;
            articleVoteStatsMap.put(articleId, articleVoteStatsRow);
        }
        return articleVoteStatsMap;
    }
}