package org.eclipse.emf.refactor.metrics;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EParameter;
import org.eclipse.emf.refactor.metrics.interfaces.IMetricCalculator;

/**
 * É uma métrica definida em [Bansiya, 1999] que calcula a coesão de uma classe baseada no número 
 * de métodos que possuem o mesmo parâmetro em uma classe. 
 * 
 * @author Ramon
 */
public final class CohesionAmongMethods implements IMetricCalculator {
		
	private List<EObject> context; 
		
	@Override
	public void setContext(List<EObject> context) {
		this.context=context;
	}	
		
	@Override
	public double calculate() {	
		org.eclipse.emf.ecore.EClass in = (org.eclipse.emf.ecore.EClass) context.get(0);
		
		double numberOfDistinctParameterTypeOfEachMethod = 0;
		
		Set<EClass> tiposDeParametros = new HashSet<EClass>();
		for (EOperation eOperation : in.getEAllOperations()) {
			Set<EClass> tiposDeParametrosDoMetodo = new HashSet<EClass>();
			for (EParameter eParameter : eOperation.getEParameters()) {
				if (eParameter.getEType() != null && eParameter.getEType() instanceof EClass) {
					tiposDeParametrosDoMetodo.add((EClass) eParameter.getEType());
					tiposDeParametros.add((EClass) eParameter.getEType());
				}
			}
			numberOfDistinctParameterTypeOfEachMethod += tiposDeParametrosDoMetodo.size();
		}
		double numberOfDistinctParameterTypes = tiposDeParametros.size();
		double numberOfMethods = in.getEAllOperations().size();
		
		if (numberOfDistinctParameterTypeOfEachMethod != 0 && numberOfMethods != 0) {
			return numberOfDistinctParameterTypeOfEachMethod / (numberOfMethods * numberOfDistinctParameterTypes);
		}
		
		return 0;
	}
}