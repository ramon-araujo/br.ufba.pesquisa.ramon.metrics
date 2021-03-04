package org.eclipse.emf.refactor.metrics;

import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EParameter;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.refactor.metrics.interfaces.IMetricCalculator;

public final class PackageCohesion implements IMetricCalculator {
		
	private List<EObject> context; 
		
	@Override
	public void setContext(List<EObject> context) {
		this.context=context;
	}	
		
	@Override
	public double calculate() {	
		org.eclipse.emf.ecore.EPackage in = (org.eclipse.emf.ecore.EPackage) context.get(0);
	
		double dependenciasInternas = 0;
		double dependenciasExternas = 0;
		
		EClass eClass = null;
		for (EClassifier element : in.getEClassifiers()) {
			if (element instanceof EClass) {
				eClass = (EClass) element;
				
				for (EClass eSuperType : eClass.getESuperTypes()) {
					if (in.equals(eSuperType.getEPackage())) {
						dependenciasInternas++; 
					} else {
						dependenciasExternas++;
					}
				}
				
				for (EReference reference : eClass.getEReferences()) {
					if (reference.getEType() != null) {
						if (in.equals(reference.getEType().getEPackage())) {
							dependenciasInternas++;
						} else {
							dependenciasExternas++;
						}
					}
				}
				
				for (EOperation operation : eClass.getEAllOperations()) {
					
					if (operation.getEType() != null) {
						if (in.equals(operation.getEType().getEPackage())) {
							dependenciasInternas++;
						} else {
							dependenciasExternas++;
						}
					}
					
					for (EParameter parameter : operation.getEParameters()) {
						if (parameter.getEType()!=null) {
							if (in.equals(parameter.getEType().getEPackage())) {
								dependenciasInternas++;
							} else {
								dependenciasExternas++;
							}
						}
					}
				}
			}
		}
		
		if (dependenciasExternas + dependenciasInternas == 0) {
			return 1;
		}
		
		return dependenciasInternas / dependenciasInternas + dependenciasExternas;
	}
}