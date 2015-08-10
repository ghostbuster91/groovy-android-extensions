package pl.ghostbuster.utils

import groovy.transform.CompileStatic

import java.util.regex.Pattern

@CompileStatic
class ClassForNameCreator {

    private static final String androidWidgetPackage = 'android.widget.'

    static Class<?> create(String name) {
        return name.findAll(Pattern.quote('.')).size() > 0 ? Class.forName(name) : Class.forName(androidWidgetPackage + name)
    }
}
