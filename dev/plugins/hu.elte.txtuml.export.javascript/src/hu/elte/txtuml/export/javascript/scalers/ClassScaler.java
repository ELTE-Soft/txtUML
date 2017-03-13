package hu.elte.txtuml.export.javascript.scalers;

import hu.elte.txtuml.export.javascript.json.model.cd.Argument;
import hu.elte.txtuml.export.javascript.json.model.cd.Attribute;
import hu.elte.txtuml.export.javascript.json.model.cd.CDNodeType;
import hu.elte.txtuml.export.javascript.json.model.cd.ClassNode;
import hu.elte.txtuml.export.javascript.json.model.cd.MemberOperation;

public class ClassScaler extends NodeScaler {

	private ClassNode node;

	// header height
	private final static double HEADER_LINE_HEIGHT = 14;
	private final static double HEADER_PADDING_VERTICAL = 10;

	// header width
	private final static int HEADER_TYPE_MARKER_LENGTH = 4;

	private final static double HEADER_TYPE_MARKER_FONT_WIDTH = 10;
	private final static double HEADER_FONT_WIDTH = 10;

	// body height
	private final static double MEMBER_LINE_HEIGHT = 14;
	private final static double MEMBER_PADDING_VERTICAL = 10;

	// body width
	private final static int MEMBER_VISIBILITY_MARKER_LENGTH = 2;

	private final static double MEMBER_FONT_WIDTH = 10;
	private final static double MEMBER_PADDING_HORIZONTAL = 10;

	private final static int TYPE_MARKER_LENGTH = 3;
	private final static int PARAM_LIST_MARKER_LENGTH = 2;
	private final static int PARAM_LIST_SEPARATOR_LENGTH = 2;

	public ClassScaler(ClassNode node) {
		this.node = node;
	}

	@Override
	protected void estimateHeight() {
		double totalHeight = HEADER_PADDING_VERTICAL * 2;
		totalHeight += HEADER_LINE_HEIGHT * (node.getType() == CDNodeType.CLASS ? 1 : 2);
		totalHeight += MEMBER_LINE_HEIGHT * node.getAttributes().size() + MEMBER_PADDING_VERTICAL * 2;
		totalHeight += MEMBER_LINE_HEIGHT * node.getOperations().size() + MEMBER_PADDING_VERTICAL * 2;
		height = (int) Math.ceil(totalHeight);
	}

	@Override
	protected void estimateWidth() {
		double totalWidth = node.getName().length() * HEADER_FONT_WIDTH + HEADER_PADDING_VERTICAL * 2;
		if (node.getType() == CDNodeType.CLASS) {
			totalWidth = Math.max(totalWidth,
					(node.getType().toString().length() + HEADER_TYPE_MARKER_LENGTH) * HEADER_TYPE_MARKER_FONT_WIDTH);
		}

		for (Attribute attr : node.getAttributes()) {
			totalWidth = Math.max(totalWidth, MEMBER_VISIBILITY_MARKER_LENGTH + attr.getName().length() * MEMBER_FONT_WIDTH
					+ MEMBER_PADDING_HORIZONTAL * 2);
		}

		for (MemberOperation op : node.getOperations()) {
			int opLength = MEMBER_VISIBILITY_MARKER_LENGTH + op.getName().length() + PARAM_LIST_MARKER_LENGTH;
			for (Argument arg : op.getArgs()) {
				opLength += arg.getName().length() + TYPE_MARKER_LENGTH;
			}
			int argCount = op.getArgs().size();
			if (argCount > 0) {
				opLength += (argCount - 1) * PARAM_LIST_SEPARATOR_LENGTH;
			}
			totalWidth = Math.max(totalWidth, opLength * MEMBER_FONT_WIDTH + MEMBER_PADDING_HORIZONTAL * 2);
			width = (int) Math.ceil(totalWidth);
		}
	}
}
