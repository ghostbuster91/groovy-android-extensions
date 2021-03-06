package pl.ghostbuster.utils

import spock.lang.Specification

final class StringUtilsSpec extends Specification {

    def "should make everything camelCase without underscores"() {
        expect:
        StringUtils.underscoreToCamelCase(input) == output
        where:
        input                       | output
        'lowercase'                 | 'lowercase'
        'camelCase'                 | 'camelCase'
        'UPPERCASE'                 | 'UPPERCASE'
        '_startsWithUnderscore'     | 'StartsWithUnderscore'
        'underscores_in_the_middle' | 'underscoresInTheMiddle'
        'underscoreAtTheEnd_'       | 'underscoreAtTheEnd_'
    }
}
