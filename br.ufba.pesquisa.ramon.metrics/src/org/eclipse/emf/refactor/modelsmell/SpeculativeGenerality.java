package org.eclipse.emf.refactor.modelsmell;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.refactor.smells.interfaces.IModelSmellFinder;

import br.ufba.pesquisa.ramon.util.Util;

/**
 * 
 * Este code smell acontece quando há no código da aplicação uma complexidade para lidar 
 * com situações que não acontecem no sistema. Isto acontece quando os desenvolvedores 
 * escrevem um código esperando lidar com determinadas situações complexas no futuro, mas 
 * estas situações não se tornam realidade. A complexidade inserida pelas funcionalidades 
 * desnecessárias aumenta a dificuldade em entender e manter o sistema e, por isso é 
 * considerada uma dívida técnica.
 * 
 * Estratégia de detecção:
 * 	
 * 		SpeculativeGenerality = ANU or PNU
 * @author Ramon
 *
 */
public final class SpeculativeGenerality implements IModelSmellFinder {

	@Override
	public LinkedList<LinkedList<EObject>> findSmell(EObject root) {
		LinkedList<LinkedList<EObject>> results = new LinkedList<LinkedList<EObject>>();
		
		List<EClass> allNonConcreteClasses = Util.getAllNonConcreteClasses(root);
		if (root instanceof EClass) {
			EClass rootEClass = (EClass) root;
			if (rootEClass.isAbstract() || rootEClass.isInterface()) {
				allNonConcreteClasses.add(rootEClass);
			}
		}
		
		List<EClass> allClasses = Util.getAllClasses(root);
		int quantidadeSubclasses;
		for (EClass eClass : allNonConcreteClasses) {
			quantidadeSubclasses = 0;
			for (EClass otherClass : allClasses) {
				if (eClass.isSuperTypeOf(otherClass)) {
					quantidadeSubclasses++;
				}
				if (quantidadeSubclasses > 1) {
					break;
				}
			}
			if (quantidadeSubclasses <=1) {
				LinkedList<EObject> currentObjects = new LinkedList<EObject>();
				currentObjects.add(eClass);
				results.add(currentObjects);
			}
		}
		
		return results;
	}
}