package hu.elte.txtuml.seqdiag.export.plantuml.exporters;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map.Entry;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;

import hu.elte.txtuml.seqdiag.export.plantuml.generator.PlantUmlCompiler;

/**
 * Exporter implementation, which is responsible for exporting the message
 * sending from the SequenceDiagrams ({@code Sequence.send()} and
 * {@code Sequence.fromActor()})
 */
public class SequenceExporter extends MethodInvocationExporter {

	public SequenceExporter(final PlantUmlCompiler compiler) {
		super(compiler);
	}

	@Override
	public boolean validElement(ASTNode curElement) {
		if (super.validElement(curElement)) {
			String fullName = ExporterUtils.getFullyQualifiedName((MethodInvocation) curElement);
			return fullName.equals("hu.elte.txtuml.api.model.seqdiag.Sequence.send")
					|| fullName.equals("hu.elte.txtuml.api.model.seqdiag.Sequence.fromActor");
		}
		return false;
	}

	@Override
	public boolean preNext(MethodInvocation curElement) {
		// Sequence.fromActor call
		if (curElement.arguments().size() == 2) {
			return true;
		}
		
		// Sequence.send call
		Expression sender = (Expression) curElement.arguments().get(0);
        String senderName = searchForRealLifelineName(compiler.lastMethodNames.lastElement(), sender);

		Expression target = (Expression) curElement.arguments().get(2);
		String targetName = searchForRealLifelineName(compiler.lastMethodNames.lastElement(), target);

		Expression signal = (Expression) curElement.arguments().get(1);
		String signalExpr = signal.resolveTypeBinding().getQualifiedName();

		compiler.println(senderName + "->" + targetName + " : " + signalExpr);
		return true;
	}

	@Override
	public void afterNext(MethodInvocation curElement) {
	}

    private String searchForRealLifelineName(String methodContextName, Expression lifeLineExpression) {
        String currentLifeLineName = lifeLineExpression.toString();
        
        HashMap<String, Collection<String>> innerMapForMethodContext = PlantUmlCompiler.lifelineNamesInContexts.get(methodContextName);
        
        if(innerMapForMethodContext.containsKey(currentLifeLineName)) {
        	// it has aliases but we got the original lileLine name, just use it
            return currentLifeLineName;
        }
        
        // we may got an alias for the original lileLine name, so we need to search for the original name and use that instead        
        // TODO reverse search, not too effective
        for(Entry<String, Collection<String>> aliases : innerMapForMethodContext.entrySet()) {
            if(aliases.getValue().contains(currentLifeLineName)) {
                return aliases.getKey();
            }
        }
        
        // just return the given name, since it has no aliases, it must be an original name
        return currentLifeLineName;
    }
}
