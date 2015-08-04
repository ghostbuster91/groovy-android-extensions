package pl.ghostbuster.utils

import pl.ghostbuster.grooid.model.View
import spock.lang.Specification

final class ViewsFromLayoutExtractorTestCase extends Specification {


    def "should find layout id"() {
        given:
        ViewsFromLayoutExtractor extractor = new ViewsFromLayoutExtractor()

        when:
        Collection<View> names = extractor.extractFieldsNames(getFileFromResources('/layout/simple_layout.xml'))

        then:
        names == [new View(id: 'some_linear_layout_id', type: 'LinearLayout')]
    }

    // TODO: break down into smaller tests
    def "should find nested ids in layout"() {
        given:
        ViewsFromLayoutExtractor extractor = new ViewsFromLayoutExtractor()

        when:
        Collection<View> names = extractor.extractFieldsNames(getFileFromResources('/layout/layout_with_nested_ids.xml'))

        then:
        names == [new View(id: 'root_layout', type: 'LinearLayout'), new View(id: 'nested_image_view', type: 'CustomObject'),
                  new View(id: 'text_view_id', type: 'TextView'), new View(id: 'next_text_view', type: 'TextView'),
                  new View(id: 'other_custom_oject', type: 'Custom2')]
    }

    private static String getFileFromResources(String filePath) {
        return getClass().getResource(filePath).text
    }
}
