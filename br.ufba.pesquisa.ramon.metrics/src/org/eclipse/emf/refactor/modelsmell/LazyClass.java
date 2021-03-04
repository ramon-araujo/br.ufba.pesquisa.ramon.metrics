package org.eclipse.emf.refactor.modelsmell;

import java.util.LinkedList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.refactor.metrics.core.Metric;
import org.eclipse.emf.refactor.metrics.interfaces.IMetricCalculator;
import org.eclipse.emf.refactor.modelsmell.util.ModelSmellFinderBasedOnMetrics;
import org.eclipse.emf.refactor.smells.interfaces.IModelSmellFinder;

/**
 * É um code smell que acontece quando uma classe não faz o suficiente para justificar sua 
 * existência. Diversos motivos podem tornar uma classe uma Lazy Class, como refatorações 
 * que diminuíram sua importância, ou um design preparado para funcionalidades que acabaram 
 * não sendo implementadas. 
 * 
 * Estratégia de detecção:
 * 
 * 	LazyClass = ((NOM < 5) and (NOA < 5)) OR (DIP < 2) 
 * 
 * @author Ramon
 *
 */
public final class LazyClass extends ModelSmellFinderBasedOnMetrics {
	
	private String nomId = "br.ufba.pesquisa.ramon.ecore.metrics.nom";
	private String noaId = "br.ufba.pesquisa.ramon.ecore.metrics.noa";
	private String dipId = "br.ufba.pesquisa.ramon.ecore.metrics.dip";

	@Override
	protected boolean detectionStrategyFoundSmell(EObject element) {
		return ((possuiBaixoNumeroDeMetodos(element) && possuiBaixoNumeroDeAtributos(element)) 
				|| possuiBaixaProfundidadeDeHierarquia(element));
	}

	private boolean possuiBaixaProfundidadeDeHierarquia(EObject element) {
		return calculateMetric(dipId, element) < 2;
	}

	private boolean possuiBaixoNumeroDeAtributos(EObject element) {
		return calculateMetric(noaId, element) < 5;
	}

	private boolean possuiBaixoNumeroDeMetodos(EObject element) {
		return calculateMetric(nomId, element) < 5;
	}
	
}