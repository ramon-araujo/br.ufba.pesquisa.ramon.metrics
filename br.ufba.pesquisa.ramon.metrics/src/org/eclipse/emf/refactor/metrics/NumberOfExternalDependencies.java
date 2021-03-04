package org.eclipse.emf.refactor.metrics;

import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EParameter;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.refactor.metrics.interfaces.IMetricCalculator;

public final class NumberOfExternalDependencies implements IMetricCalculator {
		
	private List<EObject> context; 
		
	@Override
	public void setContext(List<EObject> context) {
		this.context=context;
	}	
		
	@Override
	public double calculate() {	
		org.eclipse.emf.ecore.EClass in = (org.eclipse.emf.ecore.EClass) context.get(0);
		double ret = 0.0;
		
		System.out.println("CALCULATE NOED IN "+in.getName());
		
		EList<EClass> eSuperTypes = in.getESuperTypes();
		for (EClass superType : eSuperTypes) {
			if (!superType.getEPackage().getName().equals("ecore") && !in.getEPackage().equals(superType.getEPackage())) {
				System.out.println("NOED -> SUPERTYPE: " + superType.getName() + " " + superType.getEPackage().getName());
				ret++;
			}
		}
		
		EList<EReference> eReferences = in.getEReferences();
		for (EReference eReference : eReferences) {
			if (eReference.getEType() != null && !eReference.getEType().getEPackage().getName().equals("ecore") && eReference.getEType() != null && !in.getEPackage().equals(eReference.getEType().getEPackage())) {
				System.out.println("NOED -> REFERENCE: " + eReference.getName()+ " " + eReference.getEType().getEPackage().getName());
				ret++;
			}
		}
		
		EList<EOperation> eOperations = in.getEOperations();
		for (EOperation eOperation : eOperations) {
			if (eOperation.getEType() != null && !eOperation.getEType().getEPackage().getName().equals("ecore") && eOperation.getEType() != null && !in.getEPackage().equals(eOperation.getEType().getEPackage())) {
				System.out.println("NOED -> OPERATION: " + eOperation.getName() + " " + eOperation.getEType().getEPackage().getName());
				ret++;
			}
			
			EList<EParameter> eParameters = eOperation.getEParameters();
			for (EParameter eParameter : eParameters) {
				if (eParameter.getEType() != null && !eParameter.getEType().getEPackage().getName().equals("ecore") && eParameter.getEType() != null && !in.getEPackage().equals(eParameter.getEType().getEPackage())) {
					System.out.println("NOED -> PARAMETER: " + eParameter.getName() + " " + eParameter.getEType().getEPackage().getName());
					ret++;
				}
			}
		}
		
		return ret;
	}
}