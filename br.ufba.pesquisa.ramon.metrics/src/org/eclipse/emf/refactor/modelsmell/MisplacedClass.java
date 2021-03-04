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
import org.eclipse.emf.refactor.modelsmell.util.ModelSmellFinderBasedOnMetrics;
import org.eclipse.emf.refactor.smells.interfaces.IModelSmellFinder;

import br.ufba.pesquisa.ramon.util.RelativeMetric;
import br.ufba.pesquisa.ramon.util.Util;

/**
 * Estratégia de detecção:
 * MisplacedClass = (CL<0.33) and (NOED, TopValues(25%)) and (NOED>6) and (DD>3)
 * @author Ramon
 *
 */
public final class MisplacedClass extends ModelSmellFinderBasedOnMetrics {
	
	private String noedId = "br.ufba.pesquisa.ramon.ecore.metrics.noed";
	private String clId = "br.ufba.pesquisa.ramon.ecore.metrics.cl";
	private String ddId = "br.ufba.pesquisa.ramon.ecore.metrics.dd";
	
	public MisplacedClass() {
		super();
		relativeMetricList.add(new RelativeMetric(noedId));
	}
	
	
	@Override
	protected boolean detectionStrategyFoundSmell(EObject object) {
		
		return isCLBaixoDemais(object) && 
				isNoedEntreOsMaioresDoModelo(object) &&
				isNoedAltoDemais(object) &&
				isDDAltoDemais(object);
	}
	
	private boolean isCLBaixoDemais(EObject element) {
		return calculateMetric(clId, element) > 0.33;
	}
	
	private boolean isNoedAltoDemais(EObject element) {
		return getRelativeMetricList().get(0).getMetricValue(element) > 6;
	}

	private boolean isNoedEntreOsMaioresDoModelo(EObject element) {
		List<Result> resultadosNoed = getRelativeMetricList().get(0).getGlobalResults();
		int linhaDeCorte = (int) (resultadosNoed.size()*0.75);
		double noed = getRelativeMetricList().get(0).getMetricValue(element);
		
		return noed >= resultadosNoed.get(linhaDeCorte).getResultValue();
	}

	private boolean isDDAltoDemais(EObject element) {
		return calculateMetric(ddId, element) > 3;
	}
}