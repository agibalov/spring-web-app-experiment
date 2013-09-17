package me.loki2302.jadehelpers;

import org.pegdown.PegDownProcessor;

public class JadeMarkdownHelper {
    private final static PegDownProcessor pegDownProcessor = new PegDownProcessor();
    
    public String renderMarkdown(String markdown) {
        return pegDownProcessor.markdownToHtml(markdown);
    }
}