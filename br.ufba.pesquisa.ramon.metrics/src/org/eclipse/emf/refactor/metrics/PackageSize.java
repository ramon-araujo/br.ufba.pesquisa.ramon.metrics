package org.eclipse.emf.refactor.metrics;

import java.util.List;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.refactor.metrics.interfaces.IMetricCalculator;

public final class PackageSize implements IMetricCalculator {
		
	private List<EObject> context; 
		
	@Override
	public void setContext(List<EObject> context) {
		this.context=context;
	}	
		
	@Override
	public double calculate() {	
		org.eclipse.emf.ecore.EPackage in = (org.eclipse.emf.ecore.EPackage) context.get(0);
		return in.getEClassifiers().size();
	}
}