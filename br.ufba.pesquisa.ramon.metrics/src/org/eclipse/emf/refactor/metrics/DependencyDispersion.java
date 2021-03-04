package org.eclipse.emf.refactor.metrics;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EParameter;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.refactor.metrics.interfaces.IMetricCalculator;

public final class DependencyDispersion implements IMetricCalculator {
		
	private List<EObject> context; 
		
	@Override
	public void setContext(List<EObject> context) {
		this.context=context;
	}	
		
	@Override
	public double calculate() {	
		org.eclipse.emf.ecore.EClass in = (org.eclipse.emf.ecore.EClass) context.get(0);
		double ret = 0.0;
		
		Set<EPackage> packages = new HashSet<EPackage>();
		
		EList<EClass> eSuperTypes = in.getESuperTypes();
		for (EClass superType : eSuperTypes) {
			if (superType.getEPackage() != null) {
				packages.add(superType.getEPackage());
			}
		}
		
		EList<EReference> eReferences = in.getEReferences();
		for (EReference eReference : eReferences) {
			if (eReference.getEType() != null && eReference.getEType().getEPackage() != null) {
				packages.add(eReference.getEType().getEPackage());
			}
		}
		
		EList<EOperation> eOperations = in.getEOperations();
		for (EOperation eOperation : eOperations) {
			if (eOperation.getEType() != null && eOperation.getEType().getEPackage() != null) {
				packages.add(eOperation.getEType().getEPackage());
			}
			
			EList<EParameter> eParameters = eOperation.getEParameters();
			for (EParameter eParameter : eParameters) {
				if (eParameter.getEType() != null && eParameter.getEType().getEPackage() != null) {
					packages.add(eParameter.getEType().getEPackage());
				}
			}
		}
		
		for (EPackage ePackage : packages) {
			if (!ePackage.equals(in.getEPackage())) {
				ret++;
			}
		}
		
		return ret;
	}
}