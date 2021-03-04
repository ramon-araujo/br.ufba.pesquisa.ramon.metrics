package org.eclipse.emf.refactor.metrics;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.refactor.metrics.interfaces.IMetricCalculator;

import br.ufba.pesquisa.ramon.util.Util;

public final class NumberOfClientPackages implements IMetricCalculator {
		
	private List<EObject> context; 
		
	@Override
	public void setContext(List<EObject> context) {
		this.context=context;
	}	
		
	@Override
	public double calculate() {	
		org.eclipse.emf.ecore.EPackage in = (org.eclipse.emf.ecore.EPackage) context.get(0);
		
		TreeIterator<EObject> allContents = in.eResource().getAllContents();
		EObject objeto;
		EClass classe;
		
		Set<EPackage> conjuntoPacotesClientes = new HashSet<EPackage>();
		
		while (allContents.hasNext()) {
			objeto = allContents.next();
			if (objeto instanceof EClass) {
				classe = (EClass) objeto;
				if ((!classe.getEPackage().equals(in)) && 
						(!conjuntoPacotesClientes.contains(classe.getEPackage())) && 
						(Util.isClasseClienteClassesPacote(classe, in))) {
					conjuntoPacotesClientes.add(classe.getEPackage());
				}
			}
		}
		
		return conjuntoPacotesClientes.size();
	}
}