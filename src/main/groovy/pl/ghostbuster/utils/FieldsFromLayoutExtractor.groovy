package pl.ghostbuster.utils

import groovy.util.slurpersupport.GPathResult

import java.util.regex.Pattern

final class FieldsFromLayoutExtractor {

    private static final LinkedHashMap<String, String> androidNamespaces =
            [android: 'http://schemas.android.com/apk/res/android', tools: 'http://schemas.android.com/tools']

    Collection<String> extractFieldsNames(String layoutFile) {
        GPathResult xmlLayout = createSlurper(layoutFile)
        return extractIds(xmlLayout)
    }

    private GPathResult createSlurper(String layoutFile) {
        return new XmlSlurper()
                .parseText(layoutFile)
                .declareNamespace(androidNamespaces)
    }

    private List<String> extractIds(GPathResult xmlLayout) {
        return xmlLayout.'**'
                .grep { it['@android:id'] != '' }
                .collect(this.&removeIdPreffix)
    }

    private String removeIdPreffix(def attribute) {
        return attribute['@android:id']
                .toString()
                .replaceAll(Pattern.quote('@+id/'), '')
    }

}
