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
 * Estratégia de detecção:
 * MisplacedClass = (CL<0.33) and (NOED, TopValues(25%)) and (NOED>6) and (DD>3)
 * @author Ramon
 *
 */
public final class MisplacedClass2 implements IModelSmellFinder {
	
	private String noedId = "br.ufba.pesquisa.ramon.ecore.metrics.noed";
	private Metric noedMetric = Metric.getMetricInstanceFromId(noedId);
	
	private String clId = "br.ufba.pesquisa.ramon.ecore.metrics.cl";
	private Metric clMetric = Metric.getMetricInstanceFromId(clId);
	
	private String ddId = "br.ufba.pesquisa.ramon.ecore.metrics.dd";
	private Metric ddMetric = Metric.getMetricInstanceFromId(ddId);

	private List<Result> resultadosNoed;
	
	@Override
	public LinkedList<LinkedList<EObject>> findSmell(EObject root) {
		
		resultadosNoed = calculateAllNoed(root);
		
		return findSmellyObjectGroups(root);
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
		
		double noed = 0;
		for (Result resultado : resultadosNoed) {
			if (object.equals(resultado.getContext().get(0))) {
				noed = resultado.getResultValue();
				break;
			}
		}
		
		return isCLBaixoDemais(object) && 
				isNoedEntreOsMaioresDoModelo(noed) &&
				isNoedAltoDemais(noed) &&
				isDDAltoDemais(object);
	}
	
	private boolean isDDAltoDemais(EObject element) {
		IMetricCalculator ddCalculateClass = ddMetric.getCalculateClass();
		
		LinkedList<EObject> rootList = new LinkedList<EObject>();
		rootList.add(element);
		ddCalculateClass.setContext(rootList);
		
		return ddCalculateClass.calculate() > 3;
	}

	private boolean isNoedAltoDemais(double noed) {
		return noed > 6;
	}

	private boolean isNoedEntreOsMaioresDoModelo(double noed) {
		
		int linhaDeCorte = (int) (resultadosNoed.size()*0.75);
		return noed >= this.resultadosNoed.get(linhaDeCorte).getResultValue();
	}

	private boolean isCLBaixoDemais(EObject element) {
		IMetricCalculator clCalculateClass = clMetric.getCalculateClass();
		
		LinkedList<EObject> rootList = new LinkedList<EObject>();
		rootList.add(element);
		clCalculateClass.setContext(rootList);
		
		return clCalculateClass.calculate() > 0.33;
	}

	private LinkedList<Result> calculateAllNoed(EObject element) {
		
		LinkedList<Result> resultadosNoed = new LinkedList<Result>();
		IMetricCalculator noedCalculateClass = noedMetric.getCalculateClass();
		
		LinkedList<EObject> allEObjects = new LinkedList<EObject>();
		allEObjects.add(element);
		allEObjects.addAll(Util.getAllEObjects(element));
		
		for(EObject currentEObject : allEObjects) {
			if(noedMetric.getContext().equals(currentEObject.eClass().getInstanceClass().getSimpleName())) { 
				System.out.println("Calculate metric '" + noedMetric.getName() + "' on element '" + noedMetric.getContext());

				ArrayList<EObject> contextObjectList = new ArrayList<EObject>();
				contextObjectList.add(currentEObject);
				noedCalculateClass.setContext(contextObjectList);
				
				Result resultado = new Result(noedMetric, contextObjectList, noedMetric.getCalculateClass().calculate());
				resultadosNoed.add(resultado);
			}
		}
		
		ordenarResultados(resultadosNoed);

		return resultadosNoed;
	}
	
	private void ordenarResultados(LinkedList<Result> resultadosNoed) {
		Collections.sort(resultadosNoed, new Comparator<Result>() {
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

	
}