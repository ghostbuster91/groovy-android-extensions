package pl.ghostbuster.utils

import groovy.util.slurpersupport.GPathResult
import pl.ghostbuster.grooid.model.ViewObject

import java.util.regex.Pattern

final class ViewsFromLayoutExtractor {

    private static final LinkedHashMap<String, String> androidNamespaces =
            [android: 'http://schemas.android.com/apk/res/android', tools: 'http://schemas.android.com/tools']

    Collection<ViewObject> extractFromLayout(String layoutFile) {
        GPathResult xmlLayout = createSlurper(layoutFile)
        return extractIds(xmlLayout)
    }

    private GPathResult createSlurper(String layoutFile) {
        return new XmlSlurper()
                .parseText(layoutFile)
                .declareNamespace(androidNamespaces)
    }

    private Collection<ViewObject> extractIds(GPathResult xmlLayout) {
        return xmlLayout.'**'
                .findAll { it['@android:id'] != '' }
                .collect { new ViewObject(id: removeIdPreffix(it), type: it.name()) }
    }

    private String removeIdPreffix(def attribute) {
        return attribute['@android:id']
                .toString()
                .replaceAll(Pattern.quote('@+id/'), '')
    }
}
