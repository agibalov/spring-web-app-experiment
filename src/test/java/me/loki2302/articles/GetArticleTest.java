package me.loki2302.articles;

import me.loki2302.service.exceptions.ArticleNotFoundException;

import org.junit.Test;

public class GetArticleTest extends AbstractArticleServiceTest {
    @Test(expected = ArticleNotFoundException.class)
    public void cantGetArticleThatDoesNotExist() {
        articleService.getArticle(null, 123);
    }
}