package pl.ghostbuster.utils

import groovy.util.slurpersupport.GPathResult
import pl.ghostbuster.grooid.model.View

import java.util.regex.Pattern

final class ViewsFromLayoutExtractor {

    private static final LinkedHashMap<String, String> androidNamespaces =
            [android: 'http://schemas.android.com/apk/res/android', tools: 'http://schemas.android.com/tools']

    Collection<View> extractFromLayout(String layoutFile) {
        GPathResult xmlLayout = createSlurper(layoutFile)
        return extractIds(xmlLayout)
    }

    private GPathResult createSlurper(String layoutFile) {
        return new XmlSlurper()
                .parseText(layoutFile)
                .declareNamespace(androidNamespaces)
    }

    private Collection<View> extractIds(GPathResult xmlLayout) {
        return xmlLayout.'**'
                .findAll { it['@android:id'] != '' }
                .collect { new View(id: removeIdPreffix(it), type: it.name()) }
    }

    private String removeIdPreffix(def attribute) {
        return attribute['@android:id']
                .toString()
                .replaceAll(Pattern.quote('@+id/'), '')
    }
}
