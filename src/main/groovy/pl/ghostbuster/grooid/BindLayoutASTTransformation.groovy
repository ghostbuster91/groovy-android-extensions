package pl.ghostbuster.grooid

import org.codehaus.groovy.ast.ASTNode
import org.codehaus.groovy.ast.ClassNode
import org.codehaus.groovy.control.CompilePhase
import org.codehaus.groovy.control.SourceUnit
import org.codehaus.groovy.transform.AbstractASTTransformation
import org.codehaus.groovy.transform.GroovyASTTransformation

@GroovyASTTransformation(phase = CompilePhase.SEMANTIC_ANALYSIS)
class BindLayoutASTTransformation extends AbstractASTTransformation {

    private ClassNode classNode

    @Override
    void visit(ASTNode[] nodes, SourceUnit source) {
        this.classNode = nodes[1] as ClassNode
    }

}
