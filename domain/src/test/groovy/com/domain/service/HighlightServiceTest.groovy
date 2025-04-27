package com.domain.service

import spock.lang.Specification

class HighlightServiceTest extends Specification {
    HighlightService highlightService = new HighlightService()

    def "하이라이팅 테스트"() {
        expect:
        highlightService.highlight(content, keyword) == result

        where:
        content << [
                """Spock is a testing framework. Spock makes testing Java applications easier.""",
                """Spring Boot is popular. Many developers love spring because of its simplicity."""
        ]
        keyword << [
                "Spock",
                "Spring"
        ]
        result << [
                """<em>Spock</em> is a testing framework. <em>Spock</em> makes testing Java applications easier.""",
                """<em>Spring</em> Boot is popular. Many developers love <em>spring</em> because of its simplicity."""
        ]
    }
}
