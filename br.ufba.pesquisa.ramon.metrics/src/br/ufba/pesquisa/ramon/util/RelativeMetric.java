package br.ufba.pesquisa.ramon.util;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.refactor.metrics.core.Metric;
import org.eclipse.emf.refactor.metrics.interfaces.IMetricCalculator;
import org.eclipse.emf.refactor.metrics.runtime.core.MetricCalculator;
import org.eclipse.emf.refactor.metrics.runtime.core.Result;

public class RelativeMetric {
	
	private String metricId;
	private Metric metric;
	private IMetricCalculator calculateClass;
	private List<Result> globalResults;
	
	public RelativeMetric(String metricId) {
		this.metricId = metricId;
		this.metric = Metric.getMetricInstanceFromId(this.metricId);
		this.calculateClass = this.metric.getCalculateClass();
		this.globalResults = new LinkedList<Result>();
	}
	
	public String getMetricId() {
		return metricId;
	}
	
	public Metric getMetric() {
		return metric;
	}
	
	public IMetricCalculator getCalculateClass() {
		return calculateClass;
	}
	
	public List<Result> getGlobalResults() {
		return globalResults;
	}
	
	public void setGlobalResults(List<Result> globalResults) {
		this.globalResults = globalResults;
	}
	
	public void ordenarResultadosGlobais() {
		Collections.sort(this.globalResults, new Comparator<Result>() {
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
	
	public double getMetricValue(EObject element) {
		for (Result resultado : globalResults) {
			if (element.equals(resultado.getContext().get(0))) {
				return resultado.getResultValue();
			}
		}
		return 0;
	}
	
	public double getAverage() {
		double total = 0;
		for (Result resultado : globalResults) {
			total += resultado.getResultValue();
		}
		return total / globalResults.size();
	}
}
