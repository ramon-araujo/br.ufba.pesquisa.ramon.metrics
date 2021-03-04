package org.eclipse.emf.refactor.metrics;

import java.util.List;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.refactor.metrics.interfaces.IMetricCalculator;

public final class NumberOfAccessMethods implements IMetricCalculator {
		
	private List<EObject> context; 
		
	@Override
	public void setContext(List<EObject> context) {
		this.context=context;
	}	
		
	@Override
	public double calculate() {	
		org.eclipse.emf.ecore.EClass in = (org.eclipse.emf.ecore.EClass) context.get(0);
		System.out.println("------" + in.getName() + "------");
		System.out.println(in.getEAllAttributes());
		System.out.println(in.getEAllReferences());
		System.out.println("------------");
		return in.getEAllAttributes().size() + in.getEAllReferences().size();
	}
}