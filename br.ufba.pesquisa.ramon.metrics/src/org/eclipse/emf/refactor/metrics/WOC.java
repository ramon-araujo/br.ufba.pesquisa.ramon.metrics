package org.eclipse.emf.refactor.metrics;

import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.refactor.metrics.interfaces.IMetricCalculator;

import br.ufba.pesquisa.ramon.util.Util;

public final class WOC implements IMetricCalculator {
		
	private List<EObject> context; 
		
	@Override
	public void setContext(List<EObject> context) {
		this.context=context;
	}	
		
	@Override
	public double calculate() {	
		org.eclipse.emf.ecore.EClass in = (org.eclipse.emf.ecore.EClass) context.get(0);
		
		double numeroDeMetodosDeServicoPublicos = 0;
		for (EOperation operacao : in.getEOperations()) {
			if (!Util.isMetodoAcesso(operacao)) {
				numeroDeMetodosDeServicoPublicos++;
			}
		}
		
		double numeroDeElementosInterface = in.getEAttributes().size() + in.getEReferences().size() + numeroDeMetodosDeServicoPublicos;
		
		return numeroDeMetodosDeServicoPublicos/numeroDeElementosInterface;
	}
}