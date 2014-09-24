package markdown2book

import org.pegdown.Extensions

class SelectExtensionsSpec extends BaseSpec {

    final EXPECTED_LINES = [
        'The intended purpose of the documentation is to give an understanding',
        'of the main features offered by TextMate and should provide you with',
        'those details which are not easily found alone by trial-and error. The',
        'documentation is not exhaustive.'
    ]

    def "all extensions created by default"() {
        when: 'do not specify extensions'
        def dest = generate("original-example")

        then: 'get content with e.g. hard line breaks'
        new File(dest, 'preface.html').text.contains(EXPECTED_LINES.join('<br/>'))
    }

    def "only extensions specified are used"() {
        when: 'do not specify extensions'
        def dest = generate("original-example", Extensions.NONE)

        then: 'get content without hard line breaks'
        new File(dest, 'preface.html').text.contains(EXPECTED_LINES.join(' '))
    }
}
