package pl.ghostbuster.utils

import java.util.regex.Pattern


class ClassForNameCreator {

    private static final String androidWidgetPackage = 'android.widget.'

    static Class<?> create(String name) {
        return name.findAll(Pattern.quote('.')).size() > 0 ? Class.forName(name) : Class.forName(androidWidgetPackage + name)
    }
}
