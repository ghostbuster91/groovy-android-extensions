package pl.ghostbuster.utils

import groovy.transform.NotYetImplemented
import spock.lang.Specification

class ResourcePathResolverSpec extends Specification {

    def "Should find proper path to resource based on sourcePath and rClassPackageName"() {
        given:
        String sourcePath = 'file:/workspace/sampleApp/app/src/main/groovy/com/sample/app/domain/someHolder.groovy'
        String rClassPackageName = 'com.sample.app'

        when:
        def resolver = new ResourcePathResolver(sourcePath, rClassPackageName)

        then:
        resolver.getPath('layout/my_row.xml') == '/workspace/sampleApp/app/src/main/res/layout/my_row.xml'
    }

    @NotYetImplemented
//TODO: a proof that implementation is wrong
    def "Should find proper path to resource when the transforming node package name is different then r class packege name"() {
        given:
        String sourcePath = 'file:/workspace/sampleApp/app/src/main/groovy/pl/waw/domain/someHolder.groovy'
        String rClassPackageName = 'com.sample.app'

        when:
        def resolver = new ResourcePathResolver(sourcePath, rClassPackageName)

        then:
        resolver.getPath('layout/my_row.xml') == '/workspace/sampleApp/app/src/main/res/layout/my_row.xml'
    }
}
