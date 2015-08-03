package pl.ghostbuster.utils

import spock.lang.Specification

final class FieldsFromLayoutExtractorTestCase extends Specification {


    def "should find layout id"() {
        given:
        FieldsFromLayoutExtractor extractor = new FieldsFromLayoutExtractor()

        when:
        Collection<String> names = extractor.extractFieldsNames(getFileFromResources('/layout/simple_layout.xml'))

        then:
        names == ['some_linear_layout_id']
    }

    // TODO: break down into smaller tests
    def "should find nested ids in layout"() {
        given:
        FieldsFromLayoutExtractor extractor = new FieldsFromLayoutExtractor()

        when:
        Collection<String> names = extractor.extractFieldsNames(getFileFromResources('/layout/layout_with_nested_ids.xml'))

        then:
        names == ['root_layout', 'nested_image_view', 'text_view_id', 'next_text_view', 'other_custom_oject']
    }

    private static String getFileFromResources(String filePath) {
        return getClass().getResource(filePath).text
    }
}
