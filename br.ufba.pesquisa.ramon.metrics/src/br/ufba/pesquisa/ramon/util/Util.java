package br.ufba.pesquisa.ramon.util;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EParameter;
import org.eclipse.emf.ecore.EReference;

public class Util {
	
	public static List<EObject> getAllEObjects(EObject root) {
		ArrayList<EObject> eObjects = new ArrayList<EObject>();
		List<EObject> directContents = root.eContents();
		eObjects.addAll(directContents);
		for(EObject contentObject : directContents){
			eObjects.addAll(getAllEObjects(contentObject));
		}
		return eObjects;
	}
	
	public static List<EClass> getAllNonConcreteClasses(EObject root) {
		ArrayList<EClass> eClasses = new ArrayList<EClass>();
		List<EObject> directContents = root.eContents();
		
		for (EObject eObject : directContents) {
			if (eObject instanceof EClass) {
				EClass eClass = (EClass) eObject;
				if (eClass.isAbstract() || eClass.isInterface()) {
					eClasses.add((EClass) eObject);
				}
			}
			eClasses.addAll(getAllNonConcreteClasses(eObject));
		}
		return eClasses;
	}
	
	public static List<EAttribute> getAllAttributesOfClass(EClass classe) {
		List<EAttribute> atributos = new LinkedList<EAttribute>();
		
		for (EAttribute atributo : classe.getEAllAttributes()) {
			if (atributo.getEContainingClass().equals(classe)) {
				atributos.add(atributo);
			}
		}
		
		return atributos;
	}
	
	public static List<EReference> getAllReferencesOfClass(EClass classe) {
		List<EReference> referencias = new LinkedList<EReference>();
		
		for (EReference referencia : classe.getEAllReferences()) {
			if (referencia.getEContainingClass().equals(classe)) {
				referencias.add(referencia);
			}
		}
		
		return referencias;
	}
	
	public static List<EOperation> getAllOperationsOfClass(EClass classe) {
		List<EOperation> metodos = new LinkedList<EOperation>();
		
		for (EOperation metodo : classe.getEAllOperations()) {
			if (metodo.getEContainingClass().equals(classe)) {
				metodos.add(metodo);
			}
		}
		
		return metodos;
	}
	
	public static List<EClass> getAllClasses(EObject root) {
		ArrayList<EClass> eClasses = new ArrayList<EClass>();
		List<EObject> directContents = root.eContents();
		
		for (EObject eObject : directContents) {
			if (eObject instanceof EClass) {
				eClasses.add((EClass) eObject);
			}
			eClasses.addAll(getAllClasses(eObject));
		}
		return eClasses;
	}
	
	public static boolean isMetodoAcesso(EOperation operacao) {
		if (operacao.getName().startsWith("get")) {
			for (EAttribute atributo : operacao.getEContainingClass().getEAllAttributes()) {
				if (operacao.getName().equalsIgnoreCase("get"+atributo.getName())) {
					return true;
				}
			}
		}
		return false;
	}

	public static boolean isClasseClienteClassesPacote(EClass classe, EPackage pacote) {
		
		for (EClass supertipo : classe.getESuperTypes()) {
			if (supertipo.getEPackage().equals(pacote)) {
				return true;
			}
		}
		
		for (EReference reference : classe.getEReferences()) {
			if (reference.getEType().getEPackage().equals(pacote)) {
				return true;
			}
		}
		
		for (EOperation operation : classe.getEAllOperations()) {
			for (EParameter parameter : operation.getEParameters()) {
				if (parameter.getEType().getEPackage().equals(pacote)) {
					return true;
				}
			}
		}
		
		return false;
	}
	
}
