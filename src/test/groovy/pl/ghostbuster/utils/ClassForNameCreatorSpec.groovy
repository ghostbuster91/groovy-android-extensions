package pl.ghostbuster.utils

import spock.lang.Specification

class ClassForNameCreatorSpec extends Specification {

    def "should create proper class for all inputed names"() {
        expect:
        ClassForNameCreator.create(input).name == Class.forName(output).name
        where:
        input                                      | output
        'pl.ghostbuster.utils.ClassForNameCreator' | 'pl.ghostbuster.utils.ClassForNameCreator'
        'LinearLayout'                             | 'android.widget.LinearLayout'
    }
}
