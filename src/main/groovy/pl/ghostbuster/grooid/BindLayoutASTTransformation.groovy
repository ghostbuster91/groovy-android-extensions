package pl.ghostbuster.grooid

import org.codehaus.groovy.ast.ASTNode
import org.codehaus.groovy.ast.AnnotationNode
import org.codehaus.groovy.ast.ClassHelper
import org.codehaus.groovy.ast.ClassNode
import org.codehaus.groovy.control.CompilePhase
import org.codehaus.groovy.control.SourceUnit
import org.codehaus.groovy.transform.AbstractASTTransformation
import org.codehaus.groovy.transform.GroovyASTTransformation
import pl.ghostbuster.grooid.model.View
import pl.ghostbuster.utils.StringUtils
import pl.ghostbuster.utils.ViewsFromLayoutExtractor

@GroovyASTTransformation(phase = CompilePhase.SEMANTIC_ANALYSIS)
class BindLayoutASTTransformation extends AbstractASTTransformation {

    public static final String transformationParameter = 'value'
    private ClassNode classNode
    private AnnotationNode annotationNode

    @Override
    void visit(ASTNode[] nodes, SourceUnit source) {
        this.classNode = nodes[1] as ClassNode
        this.annotationNode = nodes[0] as AnnotationNode
        this.classNode.annotations.remove(this.annotationNode)

        Collection<View> views = extractFieldsFromLayout(extractLayoutNameFromAnnotation())
        views.each(this.&createFieldBasedOn)
    }

    private String extractLayoutNameFromAnnotation() {
        def member = annotationNode.getMember(transformationParameter)
        return member.code.statements[0].expression.property.value
    }

    private Collection<View> extractFieldsFromLayout(String layoutName) {
        Collection<View> views = new ViewsFromLayoutExtractor().extractFromLayout(getFileFromResources("/layout/${layoutName}.xml"))
        return views
    }

    private void createFieldBasedOn(View view) {
        def type = ClassHelper.make(Class.forName('android.widget.LinearLayout'))
        classNode.addField(StringUtils.underscoreToCamelCase(view.id), ACC_PUBLIC | ACC_FINAL, type, null)
    }

    private static String getFileFromResources(String filePath) {
        return getClass().getResource(filePath).text
    }
}
