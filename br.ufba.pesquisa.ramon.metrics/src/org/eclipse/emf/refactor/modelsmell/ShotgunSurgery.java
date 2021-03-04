package org.eclipse.emf.refactor.modelsmell;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.refactor.metrics.core.Metric;
import org.eclipse.emf.refactor.metrics.interfaces.IMetricCalculator;
import org.eclipse.emf.refactor.metrics.runtime.core.Result;
import org.eclipse.emf.refactor.smells.interfaces.IModelSmellFinder;

import br.ufba.pesquisa.ramon.util.Util;

/**
 * Acontece quando mudanças no sistema acarretam em pequenas mudanças em vários outros pontos da 
 * aplicação. A necessidade de fazer essas mudanças espalhadas no sistema em razão de uma 
 * alteração em um ponto torna a aplicação mais difícil de manter, configurando, assim uma 
 * dívida técnica.
 *  
 * Estratégia de detecção:
 * 	ShotgunSurgery = (CM+CA>10) and ((CM+CA), TopValues(20%)) and (CC>5)
 * @param object
 * @return
 */
public final class ShotgunSurgery implements IModelSmellFinder {
	
	private String cmPlusCaMetricId = "br.ufba.pesquisa.ramon.ecore.metrics.cmplusca";
	private Metric cmPlusCaMetric = Metric.getMetricInstanceFromId(cmPlusCaMetricId);
	
	private String changingClassesMetricId = "br.ufba.pesquisa.ramon.ecore.metrics.cc";
	private Metric changingClassesMetric = Metric.getMetricInstanceFromId(changingClassesMetricId);
	
	private LinkedList<Result> resultadoCMPlusCA;

	@Override
	public LinkedList<LinkedList<EObject>> findSmell(EObject root) {
		resultadoCMPlusCA = calculateAllCMPlusCA(root);
		return findSmellyObjectGroups(root);
	}
	
	private LinkedList<Result> calculateAllCMPlusCA(EObject element) {
		
		LinkedList<Result> resultadosCMPlusCA = new LinkedList<Result>();
		IMetricCalculator cmPlusCaCalculateClass = cmPlusCaMetric.getCalculateClass();
		
		LinkedList<EObject> allEObjects = new LinkedList<EObject>();
		allEObjects.add(element);
		allEObjects.addAll(Util.getAllEObjects(element));
		
		for(EObject currentEObject : allEObjects) {
			if(cmPlusCaMetric.getContext().equals(currentEObject.eClass().getInstanceClass().getSimpleName())) { 
				System.out.println("Calculate metric '" + cmPlusCaMetric.getName() + "' on element '" + cmPlusCaMetric.getContext());

				ArrayList<EObject> contextObjectList = new ArrayList<EObject>();
				contextObjectList.add(currentEObject);
				cmPlusCaCalculateClass.setContext(contextObjectList);
				
				Result resultado = new Result(cmPlusCaMetric, contextObjectList, cmPlusCaMetric.getCalculateClass().calculate());
				resultadosCMPlusCA.add(resultado);
			}
		}
		
		ordenarResultados(resultadosCMPlusCA);

		return resultadosCMPlusCA;
	}

	private void ordenarResultados(LinkedList<Result> resultadosCMPlusCA) {
		Collections.sort(resultadosCMPlusCA, new Comparator<Result>() {
		     @Override
		     public int compare(Result o1, Result o2) {
		    	 
		    	 if (o1.getResultValue() < o2.getResultValue()) {
		    		 return -1;
		    	 }
		    	 
		    	 if (o1.getResultValue() > o2.getResultValue()) {
		    		 return 1;
		    	 }
		    	 
		         return 0;
		     }
		 });
	}

	private LinkedList<LinkedList<EObject>> findSmellyObjectGroups(EObject root) {
		
		LinkedList<LinkedList<EObject>> smellyEObjects = new LinkedList<LinkedList<EObject>>();
		List<EObject> containedEObjects = root.eContents();
		
		for(EObject element : containedEObjects){
			
			if(element instanceof EClass){
				if (detectionStrategyFoundSmell(element)) {
					LinkedList<EObject> currentObjects = new LinkedList<EObject>();
					currentObjects.add(element);
					smellyEObjects.add(currentObjects);
				}
				
			} else {
				smellyEObjects.addAll(findSmellyObjectGroups(element));
			}
		}
		return smellyEObjects;
	}

	private boolean detectionStrategyFoundSmell(EObject object) {
		
		double cmPlusCa = 0;
		for (Result resultado : resultadoCMPlusCA) {
			if (object.equals(resultado.getContext().get(0))) {
				cmPlusCa = resultado.getResultValue();
				break;
			}
		}
		
		return isCmPlusCaAltoDemais(cmPlusCa) && 
				isCmPlusCaEntreOsMaioresDoModelo(cmPlusCa) &&
				isCCAltoDemais(object);
	}
	
	private boolean isCmPlusCaAltoDemais(double cmPlusCaResult) {
		return cmPlusCaResult > 10d;
	}
	
	private boolean isCmPlusCaEntreOsMaioresDoModelo(double cmPlusCaResult) {
		int linhaDeCorte = (int) (resultadoCMPlusCA.size()*0.7);
		return cmPlusCaResult >= this.resultadoCMPlusCA.get(linhaDeCorte).getResultValue();
	}
	
	private boolean isCCAltoDemais(EObject element) {
		IMetricCalculator ccCalculateClass = changingClassesMetric.getCalculateClass();
		
		LinkedList<EObject> rootList = new LinkedList<EObject>();
		rootList.add(element);
		ccCalculateClass.setContext(rootList);
		
		return ccCalculateClass.calculate() > 5;
	}
}