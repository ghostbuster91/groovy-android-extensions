package pl.ghostbuster.utils

import groovy.transform.CompileStatic

import java.util.regex.Pattern

//TODO: any ideas for better strategy?
@CompileStatic
class ResourcePathResolver {

    private String rClassPackageName
    private String sourceAbsolutePath

    ResourcePathResolver(String sourceAbsolutePath, String rClassPackageName) {
        this.rClassPackageName = rClassPackageName
        this.sourceAbsolutePath = sourceAbsolutePath
    }

    String getPath(String resourceName) {
        if (sourceAbsolutePath.startsWith('data:')) { // only for testing purpose...
            return "./src/test/resources/$resourceName"
        }
        return pathToGroovySrcDirectory
                .reverse()
                .replaceFirst('groovy/'.reverse(), '')
                .reverse() + "res/$resourceName"
    }

    private String getPathToGroovySrcDirectory() {
        String packageNameDotWithSlashReplaced = rClassPackageName.replaceAll(Pattern.quote('.'), '/')
        String withoutScheme = sourceAbsolutePath
                .replaceFirst('file:', '')

        return withoutScheme.substring(0, withoutScheme.indexOf(packageNameDotWithSlashReplaced))
    }

}
