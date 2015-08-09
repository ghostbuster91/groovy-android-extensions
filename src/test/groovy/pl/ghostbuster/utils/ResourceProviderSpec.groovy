package pl.ghostbuster.utils

import spock.lang.Specification

class ResourceProviderSpec extends  Specification{

    def "Should extract module name"(){
        expect:
        ResourceProvider.extractModulesNames('include \':app\'') == ['app']
    }

    def "Should extract two modules names"(){
        expect:
        ResourceProvider.extractModulesNames('include \':first\', \':second\'') == ['first', 'second']
    }
}
