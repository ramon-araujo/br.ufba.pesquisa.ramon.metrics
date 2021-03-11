package org.eclipse.emf.refactor.metrics;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EParameter;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.refactor.metrics.interfaces.IMetricCalculator;

/**
 * N�mero de classes externas a que uma determinada classe possui acesso. 
 * Contabilizada atrav�s do n�mero de refer�ncias a outras classes feitas em uma classe, 
 * seja atrav�s de references ou atrav�s de par�metros em m�todos.
 * 
 * @author Ramon
 *
 */
public final class AccessToForeignData implements IMetricCalculator {
		
	private List<EObject> context; 
		
	@Override
	public void setContext(List<EObject> context) {
		this.context=context;
	}	
		
	@Override
	public double calculate() {	
		org.eclipse.emf.ecore.EClass in = (org.eclipse.emf.ecore.EClass) context.get(0);
		
		Set<EClass> classesReferenciadas = new HashSet<EClass>();
		for (EReference eReference : in.getEReferences()) {
			if (eReference.getEType() != null && eReference.getEType() instanceof EClass) {
				classesReferenciadas.add((EClass)eReference.getEType());
			}
		}
		
		for (EOperation eOperation : in.getEOperations()) {
			for (EParameter eParameter : eOperation.getEParameters()) {
				if (eParameter.getEType() != null && eParameter.getEType() instanceof EClass) {
					classesReferenciadas.add((EClass) eParameter.getEType());
				}
			}
		}
		
		return classesReferenciadas.size();
	}
}