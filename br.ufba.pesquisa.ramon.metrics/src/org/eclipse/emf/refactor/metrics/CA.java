package org.eclipse.emf.refactor.metrics;

import java.util.List;

import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.refactor.metrics.interfaces.IMetricCalculator;

public final class CA implements IMetricCalculator {
		
	private List<EObject> context; 
		
	@Override
	public void setContext(List<EObject> context) {
		this.context=context;
	}	
		
	@Override
	public double calculate() {	
		org.eclipse.emf.ecore.EClass in = (org.eclipse.emf.ecore.EClass) context.get(0);
		double ret = 0.0;
		
		TreeIterator<EObject> allContents = in.eResource().getAllContents();
		EObject objeto;
		EClass classe;
		
		while (allContents.hasNext()) {
			objeto = allContents.next();
			if (objeto instanceof EClass) {
				classe = (EClass) objeto;
				if (!classe.equals(in)) {
					for (EReference referencia : classe.getEAllReferences()) {
						if (referencia.getEType().equals(in)) {
							ret++;
						}
					}
				}
			}
		}
		
		return ret;
	}
}