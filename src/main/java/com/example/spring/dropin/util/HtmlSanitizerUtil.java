package com.example.spring.dropin.util;

import org.owasp.html.HtmlPolicyBuilder;
import org.owasp.html.PolicyFactory;

public class HtmlSanitizerUtil {
    private static final PolicyFactory POLICY = new HtmlPolicyBuilder()
            .allowStandardUrlProtocols()
            .allowElements("b", "i", "u", "strong", "em", "p", "br", "ul", "ol", "li", "span")
            .allowAttributes("class").onElements("span") // span에 class 허용 예시
            .toFactory();

    public static String sanitize(String html) {
        return POLICY.sanitize(html);
    }
}
