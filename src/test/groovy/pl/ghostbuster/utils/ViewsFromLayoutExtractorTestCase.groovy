package pl.ghostbuster.utils

import pl.ghostbuster.grooid.model.View
import spock.lang.Specification

final class ViewsFromLayoutExtractorTestCase extends Specification {
    private ViewsFromLayoutExtractor extractor

    void setup() {
        extractor = new ViewsFromLayoutExtractor()
    }

    def "should find layout id"() {
        when:
        Collection<View> names = this.extractor.extractFieldsNames(getFileFromResources('/layout/simple_layout.xml'))

        then:
        names == [new View(id: 'some_linear_layout_id', type: 'LinearLayout')]
    }

    def "should find nested ids in layout"() {
        when:
        Collection<View> names = extractor.extractFieldsNames(getFileFromResources('/layout/layout_with_nested_ids.xml'))

        then:
        names == [new View(id: 'root_layout', type: 'LinearLayout'), new View(id: 'nested_image_view', type: 'ImageView')]
    }

    def "should return proper value of custom view type"() {
        when:
        Collection<View> names = extractor.extractFieldsNames(getFileFromResources('/layout/layout_with_custom_package.xml'))

        then:
        names.contains(new View(id: 'nested_image_view', type: 'com.custom.package.CustomObject'))
    }

    private static String getFileFromResources(String filePath) {
        return getClass().getResource(filePath).text
    }
}
