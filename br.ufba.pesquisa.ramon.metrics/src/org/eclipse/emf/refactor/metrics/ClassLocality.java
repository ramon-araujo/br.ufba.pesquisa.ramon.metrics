package org.eclipse.emf.refactor.metrics;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EParameter;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.refactor.metrics.core.Metric;
import org.eclipse.emf.refactor.metrics.interfaces.IMetricCalculator;

public final class ClassLocality implements IMetricCalculator {
		
	private List<EObject> context; 
		
	@Override
	public void setContext(List<EObject> context) {
		this.context=context;
	}	
		
	@Override
	public double calculate() {	
		org.eclipse.emf.ecore.EClass in = (org.eclipse.emf.ecore.EClass) context.get(0);
		
		Set<EClassifier> dependencias = new HashSet<EClassifier>();
		Set<EClassifier> dependenciasInternas = new HashSet<EClassifier>();
		
		EList<EClass> eSuperTypes = in.getESuperTypes();
		for (EClass eClass : eSuperTypes) {
			dependencias.add(eClass);
			if (in.getEPackage().equals(eClass.getEPackage())) {
				dependenciasInternas.add(eClass);
			}
		}

		EList<EReference> eAllReferences = in.getEAllReferences();
		for (EReference eReference : eAllReferences) {
			if (eReference.getEType() != null) {
				dependencias.add(eReference.getEType());
				if (in.getEPackage().equals(eReference.getEType().getEPackage())) {
					dependenciasInternas.add(eReference.getEType());
				}
			}
		}
		
		EList<EOperation> eOperations = in.getEOperations();
		for (EOperation eOperation : eOperations) {
			if (eOperation.getEType() != null) {
				dependencias.add(eOperation.getEType());
				if (in.getEPackage().equals(eOperation.getEType().getEPackage())) {
					dependenciasInternas.add(eOperation.getEType());
				}
			}
			
			EList<EParameter> eParameters = eOperation.getEParameters();
			for (EParameter eParameter : eParameters) {
				if (eParameter.getEType() != null) {
					dependencias.add(eParameter.getEType());
					if (in.getEPackage().equals(eParameter.getEType())) {
						dependenciasInternas.add(eParameter.getEType());
					}
				}
			}
		}
		
		return dependencias.size() == 0 ? 0 : new Double(dependenciasInternas.size()) / new Double(dependencias.size());
	}
	
}