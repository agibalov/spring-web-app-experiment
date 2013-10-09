package me.loki2302.articles;

import me.loki2302.AbstractServiceTest;
import me.loki2302.service.ArticleService;

import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractArticleServiceTest extends AbstractServiceTest {
    @Autowired
    protected ArticleService articleService;
}