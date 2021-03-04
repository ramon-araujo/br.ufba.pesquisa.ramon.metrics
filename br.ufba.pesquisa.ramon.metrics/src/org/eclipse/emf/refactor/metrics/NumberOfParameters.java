package org.eclipse.emf.refactor.metrics;

import java.util.List;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EParameter;
import org.eclipse.emf.refactor.metrics.interfaces.IMetricCalculator;

public final class NumberOfParameters implements IMetricCalculator {
		
	private List<EObject> context; 
		
	@Override
	public void setContext(List<EObject> context) {
		this.context=context;
	}	
		
	@Override
	public double calculate() {	
		org.eclipse.emf.ecore.EOperation in = (org.eclipse.emf.ecore.EOperation) context.get(0);
		return in.getEParameters().size();
	}
}