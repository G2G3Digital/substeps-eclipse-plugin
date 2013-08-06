package com.technophobia.substeps.document.content.view.hover.model.javadoc;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;

import com.technophobia.substeps.glossary.StepDescriptor;
import com.technophobia.substeps.supplier.Transformer;

public class ASTParsedMethodUnitJavadocCollector implements Transformer<IMethod, String> {

    private final Transformer<StepDescriptor, String> stepDescriptorToStringTransformer;


    public ASTParsedMethodUnitJavadocCollector(
            final Transformer<StepDescriptor, String> stepDescriptorToStringTransformer) {
        this.stepDescriptorToStringTransformer = stepDescriptorToStringTransformer;
    }


    @Override
    public String from(final IMethod method) {
        final String methodName = method.getElementName();
        final ICompilationUnit compilationUnit = method.getCompilationUnit();
        final CompilationUnit methodCompilationUnit = parse(compilationUnit);

        final JavaDocVisitor javaDocVisitor = new JavaDocVisitor(methodName, stepDescriptorToStringTransformer);
        methodCompilationUnit.accept(javaDocVisitor);

        return javaDocVisitor.parsedJavaDoc();
    }


    @SuppressWarnings("deprecation")
    private static CompilationUnit parse(final ICompilationUnit unit) {
        final ASTParser parser = ASTParser.newParser(AST.JLS3);
        parser.setKind(ASTParser.K_COMPILATION_UNIT);
        parser.setSource(unit);
        parser.setResolveBindings(true);
        return (CompilationUnit) parser.createAST(null); // parse
    }
}
