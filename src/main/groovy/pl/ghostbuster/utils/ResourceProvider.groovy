package pl.ghostbuster.utils

//TODO extends this class to be able to link
//proper module name with R class package name used within manifest file
class ResourceProvider {

    static File getResource(String name) {
        String settingsFileText = new File('./settings.gradle').text
        if(isDevProjectBasedOnGradleSettings(settingsFileText)){
            return new File(getClass().getResource("/$name").toURI())

        }
        String moduleName = extractModulesNames(settingsFileText).first()
        return new File("./${moduleName}/src/main/res/${name}")
    }

    static Collection<String> extractModulesNames(String text) {
        String includeProjectNames = text - 'include'
        return ((includeProjectNames =~ /:(\w+)/) as Collection<Collection<String>>)
                .collect { it[1] }
    }

    private static boolean isDevProjectBasedOnGradleSettings(String s) {
        return 'rootProject.name = \'groovy-android-extentions\'\n' == s
    }
}
