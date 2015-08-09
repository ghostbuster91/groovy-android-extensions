package pl.ghostbuster.utils

import pl.ghostbuster.grooid.model.ViewObject
import spock.lang.Specification

final class ViewsFromLayoutExtractorTestCase extends Specification {
    private ViewsFromLayoutExtractor extractor

    void setup() {
        extractor = new ViewsFromLayoutExtractor()
    }

    def "should find layout id"() {
        when:
        Collection<ViewObject> names = this.extractor.extractFromLayout(getFileFromResources('/layout/simple_layout.xml'))

        then:
        names == [new ViewObject(id: 'some_linear_layout_id', type: 'LinearLayout')]
    }

    def "should find nested ids in layout"() {
        when:
        Collection<ViewObject> names = extractor.extractFromLayout(getFileFromResources('/layout/layout_with_nested_ids.xml'))

        then:
        names == [new ViewObject(id: 'root_layout', type: 'LinearLayout'), new ViewObject(id: 'nested_image_view', type: 'ImageView')]
    }

    def "should return proper value of custom view type"() {
        when:
        Collection<ViewObject> names = extractor.extractFromLayout(getFileFromResources('/layout/layout_with_custom_package.xml'))

        then:
        names.contains(new ViewObject(id: 'nested_image_view', type: 'com.custom.packagename.CustomObject'))
    }

    private static String getFileFromResources(String filePath) {
        return getClass().getResource(filePath).text
    }
}
