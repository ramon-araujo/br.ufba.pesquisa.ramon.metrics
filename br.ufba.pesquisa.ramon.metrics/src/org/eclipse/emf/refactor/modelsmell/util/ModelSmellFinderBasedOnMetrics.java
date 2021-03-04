package org.eclipse.emf.refactor.modelsmell.util;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.refactor.metrics.core.Metric;
import org.eclipse.emf.refactor.metrics.interfaces.IMetricCalculator;
import org.eclipse.emf.refactor.metrics.runtime.core.Result;
import org.eclipse.emf.refactor.smells.interfaces.IModelSmellFinder;

import br.ufba.pesquisa.ramon.util.RelativeMetric;
import br.ufba.pesquisa.ramon.util.Util;

public abstract class ModelSmellFinderBasedOnMetrics implements IModelSmellFinder {

	protected List<RelativeMetric> relativeMetricList = new LinkedList<RelativeMetric>();
	
	@Override
	public LinkedList<LinkedList<EObject>> findSmell(EObject root) {
		relativeMetricList = new LinkedList<RelativeMetric>();
		calculateRelativeMetrics(root);
		return findSmellyObjects(root);
	}
	
	private void calculateRelativeMetrics(EObject root) {
		
		LinkedList<EObject> allEObjects = new LinkedList<EObject>();
		allEObjects.add(root);
		allEObjects.addAll(Util.getAllEObjects(root));
		
		for(EObject currentEObject : allEObjects) {
			for (RelativeMetric metric : relativeMetricList) {
				if(metric.getMetric().getContext().equals(currentEObject.eClass().getInstanceClass().getSimpleName())) {
					System.out.println("Calculate metric '" + metric.getMetric().getName() + "' on element '" + metric.getMetric().getContext());
					
					ArrayList<EObject> contextObjectList = new ArrayList<EObject>();
					contextObjectList.add(currentEObject);
					metric.getCalculateClass().setContext(contextObjectList);
					
					Result resultado = new Result(metric.getMetric(), contextObjectList, metric.getCalculateClass().calculate());
					metric.getGlobalResults().add(resultado);
				}
			}
		}
		
		for (RelativeMetric metric : relativeMetricList) {
			metric.ordenarResultadosGlobais();
		}
	}

	public LinkedList<LinkedList<EObject>> findSmellyObjects(EObject root) {
		
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
				smellyEObjects.addAll(findSmellyObjects(element));
			}
		}
		return smellyEObjects;
		
	}
	
	protected abstract boolean detectionStrategyFoundSmell(EObject element);
	
	protected double calculateMetric(String metricId, EObject element) {
		Metric metric = Metric.getMetricInstanceFromId(metricId);
		IMetricCalculator dipCalculateClass = metric.getCalculateClass();
		
		LinkedList<EObject> rootList = new LinkedList<EObject>();
		rootList.add(element);
		dipCalculateClass.setContext(rootList);
		
		return dipCalculateClass.calculate() ;
	}

	public void setRelativeMetricList(List<RelativeMetric> relativeMetricList) {
		this.relativeMetricList = relativeMetricList;
	}
	
	public List<RelativeMetric> getRelativeMetricList() {
		return relativeMetricList;
	}
	
	protected void addRelativeMetric(String metricId) {
		if (relativeMetricList == null) {
			relativeMetricList = new LinkedList<RelativeMetric>();
		}
		relativeMetricList.add(new RelativeMetric(metricId));
	}
}
