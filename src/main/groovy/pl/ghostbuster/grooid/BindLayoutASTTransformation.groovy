package pl.ghostbuster.grooid

import android.view.View
import org.codehaus.groovy.ast.*
import org.codehaus.groovy.ast.builder.AstBuilder
import org.codehaus.groovy.ast.expr.PropertyExpression
import org.codehaus.groovy.ast.stmt.BlockStatement
import org.codehaus.groovy.ast.stmt.ExpressionStatement
import org.codehaus.groovy.control.CompilePhase
import org.codehaus.groovy.control.SourceUnit
import org.codehaus.groovy.transform.AbstractASTTransformation
import org.codehaus.groovy.transform.GroovyASTTransformation
import pl.ghostbuster.grooid.model.ViewObject
import pl.ghostbuster.utils.StringUtils
import pl.ghostbuster.utils.ViewsFromLayoutExtractor

@GroovyASTTransformation(phase = CompilePhase.SEMANTIC_ANALYSIS)
class BindLayoutASTTransformation extends AbstractASTTransformation {

    public static final String transformationParameter = 'value'
    public static final String constructorBindLayoutParamName = 'itemView'
    public static final String findViewByIdMethodName = 'findViewById'
    private ClassNode classNode
    private PropertyExpression annotationExpr

    @Override
    void visit(ASTNode[] nodes, SourceUnit source) {
        this.classNode = nodes[1] as ClassNode
        this.annotationExpr = extractExpressionFromAnnotation(nodes[0] as AnnotationNode)
        this.classNode.annotations.remove(nodes[0] as AnnotationNode)

        Collection<ViewObject> views = fieldsFromLayout
        addFields(views)
        addConstructor(views)
    }

    private Collection<ViewObject> getFieldsFromLayout() {
        Collection<ViewObject> views = new ViewsFromLayoutExtractor().extractFromLayout(getFileFromResources("/layout/${layoutName}.xml"))
        return views
    }

    private Collection<ViewObject> addFields(Collection<ViewObject> views) {
        views.each(this.&addField)
    }

    private void addField(ViewObject view) {
        def type = ClassHelper.make(Class.forName('android.widget.LinearLayout'))
        classNode.addField(StringUtils.underscoreToCamelCase(view.id), ACC_PUBLIC | ACC_FINAL, type, null)
    }

    private void addConstructor(Collection<ViewObject> viewObjects) {
        BlockStatement block = new BlockStatement()
        block.addStatements(viewObjects.collect(this.&createAssignableStatementFor))
        classNode.addConstructor new ConstructorNode(ACC_PUBLIC, [new Parameter(ClassHelper.make(View), constructorBindLayoutParamName)] as Parameter[], [] as ClassNode[], block)
    }

    private ExpressionStatement createAssignableStatementFor(ViewObject viewObject) {
        return new AstBuilder().buildFromSpec {
            expression {
                binary {
                    variable StringUtils.underscoreToCamelCase(viewObject.id)
                    token "="
                    methodCall {
                        variable constructorBindLayoutParamName
                        constant findViewByIdMethodName
                        argumentList {
                            property {
                                classExpression Class.forName(getRClassPackage() + '.R$id')
                                constant viewObject.id
                            }
                        }
                    }
                }
            }
        }.collect().first() as ExpressionStatement
    }

    private String getLayoutName() {
        return annotationExpr.property.value
    }

    private String getRClassPackage() {
        return annotationExpr.objectExpression.type.packageName
    }

    private static PropertyExpression extractExpressionFromAnnotation(AnnotationNode node) {
        def member = node.getMember(transformationParameter)
        return member.code.statements[0].expression
    }

    private static String getFileFromResources(String filePath) {
        return getClass().getResource(filePath).text
    }
}
