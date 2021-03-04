package org.eclipse.emf.refactor.metrics;

import java.util.List;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.refactor.metrics.interfaces.IMetricCalculator;

import br.ufba.pesquisa.ramon.util.Util;

public final class WMCParam implements IMetricCalculator {
		
	private List<EObject> context; 
		
	@Override
	public void setContext(List<EObject> context) {
		this.context=context;
	}	
		
	@Override
	public double calculate() {	
		org.eclipse.emf.ecore.EClass in = (org.eclipse.emf.ecore.EClass) context.get(0);
		double ret = 0.0;
		
		for (EOperation operacao : in.getEAllOperations()) {
			if (!Util.isMetodoAcesso(operacao)) {
				ret+=calcularWMCMetodo(operacao);
			}
		}
		
		return ret;
	}
	
	private double calcularWMCMetodo(EOperation operacao) {
		double wmc = operacao.getEParameters().size();
		
		if (operacao.getEType() != null) {
			wmc++;
		}
		
		return wmc;
	}

}