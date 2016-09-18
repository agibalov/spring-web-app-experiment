package me.loki2302.rythm.spring;

import org.rythmengine.RythmEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;

import java.util.Locale;

public class RythmViewResolver implements ViewResolver {
    private final static Logger LOGGER = LoggerFactory.getLogger(RythmViewResolver.class);

    private final RythmEngine rythmEngine;
    private String prefix = "";
    private String suffix = "";

    public RythmViewResolver(RythmEngine rythmEngine) {
        this.rythmEngine = rythmEngine;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    @Override
    public View resolveViewName(String viewName, Locale locale) throws Exception {
        String templateName = makeTemplateName(viewName);
        return new RythmView(rythmEngine, templateName);
    }

    private String makeTemplateName(String viewName) {
        return prefix + viewName + suffix;
    }
}
