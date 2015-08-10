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
import pl.ghostbuster.utils.ClassForNameCreator
import pl.ghostbuster.utils.ResourcePathResolver
import pl.ghostbuster.utils.StringUtils
import pl.ghostbuster.utils.ViewsFromLayoutExtractor

@GroovyASTTransformation(phase = CompilePhase.SEMANTIC_ANALYSIS)
class BindLayoutASTTransformation extends AbstractASTTransformation {

    public static final String transformationParameter = 'value'
    public static final String constructorBindLayoutParamName = 'itemView'
    public static final String findViewByIdMethodName = 'findViewById'
    private ClassNode classNode
    private PropertyExpression annotationExpr
    private ResourcePathResolver resPathResolver

    @Override
    void visit(ASTNode[] nodes, SourceUnit source) {
        classNode = nodes[1] as ClassNode
        annotationExpr = extractExpressionFromAnnotation(nodes[0] as AnnotationNode)
        classNode.annotations.remove(nodes[0] as AnnotationNode)
        resPathResolver = new ResourcePathResolver(source.source.URI.toASCIIString(), getRClassPackage())

        Collection<ViewObject> views = fieldsFromLayout
        addFields(views)
        addConstructor(views)
    }

    private Collection<ViewObject> getFieldsFromLayout() {
        return new ViewsFromLayoutExtractor().extractFromLayout(getFileContentFromResources("layout/${layoutName}.xml"))
    }

    private Collection<ViewObject> addFields(Collection<ViewObject> views) {
        views.each(this.&addField)
    }

    private void addField(ViewObject viewObject) {
        def type = ClassHelper.make(ClassForNameCreator.create(viewObject.type))
        classNode.addField(StringUtils.underscoreToCamelCase(viewObject.id), ACC_PUBLIC | ACC_FINAL, type, null)
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
                    cast ClassForNameCreator.create(viewObject.type), {
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

    private String getFileContentFromResources(String fileName) {
        return new File(resPathResolver.getPath(fileName)).text
    }
}
