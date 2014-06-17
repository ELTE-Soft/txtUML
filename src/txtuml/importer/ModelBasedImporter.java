package txtuml.importer;

import txtuml.api.Action;
import txtuml.api.ModelClass;
import txtuml.metamodel.MetaModel;

public class ModelBasedImporter {
	public static void main(String[] args) {
		if(args.length != 1) {
			System.out.println("One command line argument needed.");
			return;
		}

		try {
			MetaModel.Model m = importModel(args[0]);
		} catch(ImportException ie) {
			System.out.println("Error: " + ie.getMessage());
		}
	}

	public static MetaModel.Model importModel(String className) throws ImportException {
		Class modelClass = findModel(className);
		MetaModel.Model model = new MetaModel.Model();
		Action.assign(model.name, modelClass.getSimpleName());
		for(Class c : modelClass.getDeclaredClasses()) {
			if(isClass(c)) {
				MetaModel.Class m_class = new MetaModel.Class();
				Action.assign(m_class.name, c.getSimpleName());
				Action.link(MetaModel.ClassesInModel.class,"containingModel",model,"containedClass",m_class);
			}
		}
		return model;
	}
	
    static Class findModel(String className) throws ImportException {
		try {
			return Class.forName(className);
		} catch(ClassNotFoundException e) {
			throw new ImportException("Cannot find class: " + className);
		}
    }

	static boolean isClass(Class c) {
		return (c.getSuperclass() == ModelClass.class);
	}

}
