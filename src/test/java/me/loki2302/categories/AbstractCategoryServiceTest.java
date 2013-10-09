package me.loki2302.categories;

import me.loki2302.AbstractServiceTest;
import me.loki2302.service.CategoryService;

import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractCategoryServiceTest extends AbstractServiceTest {
    @Autowired
    protected CategoryService categoryService;
}