package pl.ghostbuster.utils

import groovy.transform.CompileStatic

import java.util.regex.Pattern

//TODO: any ideas for better strategy?
@CompileStatic
class ResourcePathResolver {

    private String rClassPackageName
    private String sourceAbsoulutePath

    ResourcePathResolver(String sourceAbsolutePath, String rClassPackageName) {
        this.rClassPackageName = rClassPackageName
        this.sourceAbsoulutePath = sourceAbsolutePath
    }

    String getPath(String resourceName) {
        return pathToGroovySrcDirectory
                .reverse()
                .replaceFirst('groovy/'.reverse(), '')
                .reverse() + "res/$resourceName"
    }

    private String getPathToGroovySrcDirectory() {
        String packageNameDotWithSlashReplaced = rClassPackageName.replaceAll(Pattern.quote('.'), '/')
        String withoutScheme = sourceAbsoulutePath
                .replaceFirst('file:', '')

        return withoutScheme.substring(0, withoutScheme.indexOf(packageNameDotWithSlashReplaced))
    }

}
