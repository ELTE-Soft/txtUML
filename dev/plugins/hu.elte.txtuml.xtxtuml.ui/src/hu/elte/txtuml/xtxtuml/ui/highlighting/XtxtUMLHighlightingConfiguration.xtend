package hu.elte.txtuml.xtxtuml.ui.highlighting;

import org.eclipse.swt.SWT
import org.eclipse.swt.graphics.RGB
import org.eclipse.xtext.ui.editor.syntaxcoloring.IHighlightingConfigurationAcceptor
import org.eclipse.xtext.xbase.ui.highlighting.XbaseHighlightingConfiguration

class XtxtUMLHighlightingConfiguration extends XbaseHighlightingConfiguration {

	public static val DOCUMENTATION_COMMENT = "xtxtuml.documentation.comment";
	public static val FORMAL_PARAMETER = "xtxtuml.formal.parameter";
	public static val MULTIPLICITY = "xtxtuml.multiplicity";
	public static val ENUMERATION_LITERAL = "xtxtuml.enumeration.literal"

	override configure(IHighlightingConfigurationAcceptor acceptor) {
		acceptor.acceptDefaultHighlighting(DOCUMENTATION_COMMENT, "Documentation comment", documentationComment);
		acceptor.acceptDefaultHighlighting(FORMAL_PARAMETER, "Formal parameter", formalParameter);
		acceptor.acceptDefaultHighlighting(MULTIPLICITY, "Multiplicity", multiplicity);
		acceptor.acceptDefaultHighlighting(ENUMERATION_LITERAL, "Enumeration literal", enumerationLiteral);

		super.configure(acceptor);
	}
	
	def documentationComment() {
		val textStyle = defaultTextStyle.copy;
		textStyle.color = new RGB(63, 63, 191);
		return textStyle;
	}

	def formalParameter() {
		val textStyle = defaultTextStyle.copy;
		textStyle.color = new RGB(106, 62, 62);
		return textStyle;
	}

	def multiplicity() {
		val textStyle = defaultTextStyle.copy;
		textStyle.color = new RGB(125, 125, 125);
		return textStyle;
	}

	def enumerationLiteral() {
		val textStyle = defaultTextStyle.copy;
		textStyle.color = new RGB(0, 0, 192);
		textStyle.style = SWT.ITALIC;
		return textStyle;
	}

}
