package org.eclipse.emf.refactor.modelsmell;

import java.util.LinkedList;
import java.util.List;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.refactor.metrics.core.Metric;
import org.eclipse.emf.refactor.metrics.interfaces.IMetricCalculator;
import org.eclipse.emf.refactor.modelsmell.util.ModelSmellFinderBasedOnMetrics;
import org.eclipse.emf.refactor.smells.core.MetricBasedModelSmellFinderClass;

/**
 * É definido como uma classe que possui responsabilidades demais. 
 * 
 * Estratégia de detecção:
 * 	LargeClass = NOEC > 10
 * @author Ramon
 *
 */
public final class LargeClass extends ModelSmellFinderBasedOnMetrics {

	private String metricId = "br.ufba.pesquisa.ramon.ecore.metrics.noec";
	
	@Override
	protected boolean detectionStrategyFoundSmell(EObject element) {
		return classePossuiMuitosElementos(element);
	}
	
	private boolean classePossuiMuitosElementos(EObject element) {
		double numberOfElements = calculateMetric(metricId, element);
		return numberOfElements > 10;
	}
}