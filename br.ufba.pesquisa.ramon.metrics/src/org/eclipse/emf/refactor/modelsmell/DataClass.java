package org.eclipse.emf.refactor.modelsmell;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.refactor.metrics.core.Metric;
import org.eclipse.emf.refactor.metrics.interfaces.IMetricCalculator;
import org.eclipse.emf.refactor.smells.interfaces.IModelSmellFinder;

/**
 * Estratégia de detecção:
 * 	DataClass = (WOC<0.33) and ((NOAM>2) and WMC<3) or ((NOAM>5) and WMC<4)
 * @param object
 * @return
 */
public final class DataClass implements IModelSmellFinder {
	
	private String wocMetricId = "br.ufba.pesquisa.ramon.ecore.metrics.woc";
	private Metric wocMetric = Metric.getMetricInstanceFromId(wocMetricId);
	
	private String noamMetricId = "br.ufba.pesquisa.ramon.ecore.metrics.noam";
	private Metric noamMetric = Metric.getMetricInstanceFromId(noamMetricId);

	private String wmcMetricId = "br.ufba.pesquisa.ramon.ecore.metrics.wmcparam";
	private Metric wmcMetric = Metric.getMetricInstanceFromId(wmcMetricId);

	@Override
	public LinkedList<LinkedList<EObject>> findSmell(EObject root) {
		
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
				smellyEObjects.addAll(findSmell(element));
			}
		}
		return smellyEObjects;	
	}
	
	private boolean detectionStrategyFoundSmell(EObject object) {
		return isClasseRevelaMaisDadosQueServicos(object) && 
				isClasseRevelaMuitosAtributosSemSerComplexa(object);
	}

	private boolean isClasseRevelaMuitosAtributosSemSerComplexa(EObject element) {

		IMetricCalculator noamCalculateClass = noamMetric.getCalculateClass();
		IMetricCalculator wmcCalculateClass = wmcMetric.getCalculateClass();

		LinkedList<EObject> rootList = new LinkedList<EObject>();
		rootList.add(element);
		
		noamCalculateClass.setContext(rootList);
		double noam = noamCalculateClass.calculate();
		
		wmcCalculateClass.setContext(rootList);
		double wmc = wmcCalculateClass.calculate();
		
		return ((noam>2) && (wmc<3) || (noam>5) && (wmc<4));
	}

	private boolean isClasseRevelaMaisDadosQueServicos(EObject element) {
		
		IMetricCalculator wocCalculateClass = wocMetric.getCalculateClass();
		
		LinkedList<EObject> rootList = new LinkedList<EObject>();
		rootList.add(element);
		wocCalculateClass.setContext(rootList);
		
		return wocCalculateClass.calculate() < 0.33;
	}
	
}