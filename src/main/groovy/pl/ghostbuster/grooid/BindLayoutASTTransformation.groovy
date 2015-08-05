package pl.ghostbuster.grooid

import org.codehaus.groovy.ast.ASTNode
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

    private ClassNode classNode

    @Override
    void visit(ASTNode[] nodes, SourceUnit source) {
        this.classNode = nodes[1] as ClassNode
        def extractor = new ViewsFromLayoutExtractor()
        Collection<View> views = extractor.extractFromLayout(getFileFromResources('/layout/simple_layout.xml'))
        views.each(this.&createFieldBasedOn)
    }

    private void createFieldBasedOn(View view) {
        def type = ClassHelper.make(Class.forName('android.widget.LinearLayout'))
        classNode.addField(StringUtils.underscoreToCamelCase(view.id), ACC_PUBLIC | ACC_FINAL, type, null)
    }

    private static String getFileFromResources(String filePath) {
        return getClass().getResource(filePath).text
    }
}
