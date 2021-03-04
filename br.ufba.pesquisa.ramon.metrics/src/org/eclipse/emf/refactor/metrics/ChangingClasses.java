package org.eclipse.emf.refactor.metrics;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.refactor.metrics.interfaces.IMetricCalculator;

public final class ChangingClasses implements IMetricCalculator {
		
	private List<EObject> context; 
		
	@Override
	public void setContext(List<EObject> context) {
		this.context=context;
	}	
		
	@Override
	public double calculate() {	
		org.eclipse.emf.ecore.EClass in = (org.eclipse.emf.ecore.EClass) context.get(0);
		
		Set<EClass> classesClientesReferenciadas = new HashSet<EClass>();
		
		for (EReference referencia : in.getEReferences()) {
			if (referencia.getEType() instanceof EClass) {
				classesClientesReferenciadas.add((EClass)referencia.getEType());
			}
		}
				
		return classesClientesReferenciadas.size();
	}
}