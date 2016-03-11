package hu.elte.txtuml.export.uml2.transform.exporters;

import java.util.List;

import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.IVariableBinding;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.Operation;
import org.eclipse.uml2.uml.PrimitiveType;
import org.eclipse.uml2.uml.StructuralFeature;
import org.eclipse.uml2.uml.Type;

import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.Collection;
import hu.elte.txtuml.api.model.ModelClass;
import hu.elte.txtuml.export.uml2.transform.exporters.expressions.Expr;

/**
 * Instances of this class are responsible for matching txtUML types to UML2
 * types.
 */
public class TypeExporter {

	public static final String UML2_INTEGER_NAME = "Integer";
	public static final String UML2_BOOLEAN_NAME = "Boolean";
	public static final String UML2_STRING_NAME = "String";
	public static final String UML2_REAL_NAME = "Real";
	public static final String UML2_UNLIMITED_NATURAL_NAME = "UnlimitedNatural";

	public static final String INTEGER_OPERATIONS_NAME = "IntegerOperations";
	public static final String BOOLEAN_OPERATIONS_NAME = "BooleanOperations";
	public static final String STRING_OPERATIONS_NAME = "StringOperations";
	public static final String OBJECT_OPERATIONS_NAME = "ObjectOperations";

	private final ModelExporter modelExporter;

	private final PrimitiveType UML2Integer;
	private final PrimitiveType UML2Boolean;
	private final PrimitiveType UML2String;
	private final PrimitiveType UML2Real;
	private final PrimitiveType UML2UnlimitedNatural;
	private final Class integerOperations;
	private final Class booleanOperations;
	private final Class stringOperations;
	private final Class objectOperations;

	public TypeExporter(ModelExporter modelExporter) {
		this.modelExporter = modelExporter;
		Model exportedModel = modelExporter.getExportedModel();

		UML2Integer = (PrimitiveType) exportedModel.getImportedMember(UML2_INTEGER_NAME);
		UML2Boolean = (PrimitiveType) exportedModel.getImportedMember(UML2_BOOLEAN_NAME);
		UML2String = (PrimitiveType) exportedModel.getImportedMember(UML2_STRING_NAME);
		UML2Real = (PrimitiveType) exportedModel.getImportedMember(UML2_REAL_NAME);
		UML2UnlimitedNatural = (PrimitiveType) exportedModel.getImportedMember(UML2_UNLIMITED_NATURAL_NAME);
		integerOperations = (Class) exportedModel.getImportedMember(INTEGER_OPERATIONS_NAME);
		booleanOperations = (Class) exportedModel.getImportedMember(BOOLEAN_OPERATIONS_NAME);
		stringOperations = (Class) exportedModel.getImportedMember(STRING_OPERATIONS_NAME);
		objectOperations = (Class) exportedModel.getImportedMember(OBJECT_OPERATIONS_NAME);
	}

	/**
	 * Exports the specified source txtUML type.
	 * 
	 * @param sourceType
	 *            The specified source type.
	 * @return The exported UML2 type.
	 */
	public org.eclipse.uml2.uml.Type exportType(org.eclipse.jdt.core.dom.Type sourceType) {
		if (sourceType == null) {
			return null;
		}

		return exportType(sourceType.resolveBinding());
	}

	/**
	 * Exports the specified source txtUML type.
	 * 
	 * @param sourceType
	 *            The specified source type.
	 * @return The exported UML2 type.
	 */
	public Type exportType(ITypeBinding sourceType) {
		if (sourceType == null) {
			return null;
		}

		Type ret = exportPrimitiveType(sourceType);
		if (ret == null) {
			ret = exportNonPrimitiveType(sourceType);
		}

		return ret;
	}

	/**
	 * Exports the specified source primitive txtUML type.
	 * 
	 * @param sourceType
	 *            The specified source type.
	 * @return The exported UML2 type; or <code>null</code> in case of error.
	 */
	private Type exportPrimitiveType(ITypeBinding sourceType) {
		String typeName = sourceType.getQualifiedName();
		if (sourceType.isPrimitive()) {
			if (typeName.equals("int")) {
				return UML2Integer;
			} else if (typeName.equals("boolean")) {
				return UML2Boolean;
			}
		} else if (typeName.equals("java.lang.String")) {
			return UML2String;
		}
		return null;
	}

	/**
	 * Exports the specified source non-primitive txtUML type.
	 * 
	 * @param sourceType
	 *            The specified source type.
	 * @return The exported UML2 type; or <code>null</code> in case of error.
	 */
	private Type exportNonPrimitiveType(ITypeBinding sourceType) {
		Model exportedModel = modelExporter.getExportedModel();
		if (exportedModel != null) {
			// get the generic class instead of parameterized type
			sourceType = sourceType.getErasure();
			String typeName = sourceType.getQualifiedName();
			String shortName = sourceType.getName();
			Type ret = exportedModel.getOwnedType(typeName);
			// FIXME: generate packages
			if (ret != null)
				return ret;
			ret = (Type) exportedModel.getImportedMember(typeName);
			if (ret != null)
				return ret;
			ret = exportedModel.getOwnedType(shortName);
			if (ret != null)
				return ret;
			ret = (Type) exportedModel.getImportedMember(shortName);
			return ret;
		} else {
			return null;
		}
	}

	public StructuralFeature exportFieldAsStructuralFeature(IVariableBinding field) {

		Classifier exportedOwner = (Classifier) exportNonPrimitiveType(field.getDeclaringClass());

		if (exportedOwner == null) { // TODO unknown type of field owner
			return null;
		}

		return exportedOwner.getAttribute(field.getName(), exportType(field.getType()));
	}

	public Operation exportMethodAsOperation(IMethodBinding method, List<Expr> args) {

		Classifier exportedOwner = (Classifier) exportNonPrimitiveType(method.getDeclaringClass());

		ITypeBinding returnType = method.getReturnType();
		Type exportedReturnType = null;

		if (!isVoid(returnType)) {
			exportedReturnType = exportType(returnType);
		}

		return exportMethodAsOperation(exportedOwner, method.getName(), exportedReturnType, args);
	}

	public Operation exportMethodAsOperation(Classifier exportedOwner, String name, Type returnType, List<Expr> args) {

		if (exportedOwner == null) { // TODO unknown type of method owner
			return null;
		}

		EList<Type> typeList = new BasicEList<>();

		args.forEach(arg -> {
			typeList.add(arg.getType());
		});

		if (returnType != null) {
			typeList.add(returnType);
		}

		Operation op = exportedOwner.getOperation(name, null, typeList);
		return op;
	}

	public Class getIntegerOperations() {
		return integerOperations;
	}

	public Class getBooleanOperations() {
		return booleanOperations;
	}

	public Class getStringOperations() {
		return stringOperations;
	}

	public Class getObjectOperations() {
		return objectOperations;
	}

	/**
	 * @return The UML2 Integer primitive type.
	 */
	public PrimitiveType getInteger() {
		return this.UML2Integer;
	}

	/**
	 * @return The UML2 Boolean primitive type.
	 */
	public PrimitiveType getBoolean() {
		return this.UML2Boolean;
	}

	/**
	 * @return The UML2 Real primitive type.
	 */
	public PrimitiveType getReal() {
		return this.UML2Real;
	}

	/**
	 * @return The UML2 String primitive type.
	 */
	public PrimitiveType getString() {
		return this.UML2String;
	}

	/**
	 * @return The UML2 UnlimitedNatural primitive type.
	 */
	public PrimitiveType getUnlimitedNatural() {
		return this.UML2UnlimitedNatural;
	}

	public boolean isInteger(Type type) {
		return UML2Integer.getQualifiedName().equals(type.getQualifiedName());
	}

	public boolean isBoolean(Type type) {
		return UML2Boolean.getQualifiedName().equals(type.getQualifiedName());
	}

	public boolean isString(Type type) {
		return UML2String.getQualifiedName().equals(type.getQualifiedName());
	}

	public static boolean isInteger(ITypeBinding type) {
		return type.getQualifiedName().equals(int.class.getName());
	}

	public static boolean isBoolean(ITypeBinding type) {
		return type.getQualifiedName().equals(boolean.class.getName());
	}

	public static boolean isString(ITypeBinding type) {
		return type.getQualifiedName().equals(String.class.getName());
	}

	public static boolean isVoid(ITypeBinding type) {
		return type.getQualifiedName().equals(void.class.getName());
	}

	public static boolean isAction(IMethodBinding binding) {
		return binding.getDeclaringClass().getQualifiedName().equals(Action.class.getName());
	}

	public static boolean isNavigation(IMethodBinding binding) {
		return (binding.getDeclaringClass().getQualifiedName().equals(ModelClass.class.getName())
				&& (binding.getName().equals("assoc") || binding.getName().equals("port")))
				|| (binding.getDeclaringClass().getErasure().getQualifiedName().equals(Collection.class.getName())
						&& binding.getName().equals("selectAny"));
	}

}
