package org.eclipse.emf.refactor.metrics;

import java.util.List;

import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EParameter;
import org.eclipse.emf.refactor.metrics.interfaces.IMetricCalculator;

public final class ClientMethods implements IMetricCalculator {
		
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
		EOperation operacao;
		
		while (allContents.hasNext()) {
			objeto = allContents.next();
			if (objeto instanceof EOperation) {
				operacao = (EOperation) objeto;
				if (operacao.getEType() != null && operacao.getEType().equals(in)) {
					ret++;
				} else {
					for (EParameter parameter : operacao.getEParameters()) {
						if (parameter.getEType().equals(in)) {
							ret++;
							break;
						}
					}
				}
			}
		}
		
		return ret;
	}
}