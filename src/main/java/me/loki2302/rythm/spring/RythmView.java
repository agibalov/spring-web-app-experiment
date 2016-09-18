package me.loki2302.rythm.spring;

import org.rythmengine.RythmEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.View;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public class RythmView implements View {
    private final static Logger LOGGER = LoggerFactory.getLogger(RythmView.class);

    private final RythmEngine rythmEngine;
    private final String templateName;

    public RythmView(RythmEngine rythmEngine, String templateName) {
        this.rythmEngine = rythmEngine;
        this.templateName = templateName;
    }

    @Override
    public String getContentType() {
        return "text/html";
    }

    @Override
    public void render(
            Map<String, ?> model,
            HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        String result = rythmEngine.render(templateName, model);
        response.setContentType(getContentType()); // TODO: WTF?

        response.getWriter().append(result);
    }
}
