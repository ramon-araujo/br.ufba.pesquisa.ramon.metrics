package org.eclipse.emf.refactor.modelsmell;

import java.util.LinkedList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.refactor.metrics.core.Metric;
import org.eclipse.emf.refactor.metrics.interfaces.IMetricCalculator;
import org.eclipse.emf.refactor.modelsmell.util.ModelSmellFinderBasedOnMetrics;
import org.eclipse.emf.refactor.smells.interfaces.IModelSmellFinder;

/**
 * � um code smell que acontece quando uma classe n�o faz o suficiente para justificar sua 
 * exist�ncia. Diversos motivos podem tornar uma classe uma Lazy Class, como refatora��es 
 * que diminu�ram sua import�ncia, ou um design preparado para funcionalidades que acabaram 
 * n�o sendo implementadas. 
 * 
 * Estrat�gia de detec��o:
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