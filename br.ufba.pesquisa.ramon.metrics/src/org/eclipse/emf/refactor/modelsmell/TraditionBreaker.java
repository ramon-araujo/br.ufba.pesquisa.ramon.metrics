package org.eclipse.emf.refactor.modelsmell;

import java.util.LinkedList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.refactor.modelsmell.util.ModelSmellFinderBasedOnMetrics;
import org.eclipse.emf.refactor.smells.interfaces.IModelSmellFinder;

/**
 * Acontece quando uma subclasse provê uma grande quantidade de serviços que não são 
 * relacionados aos serviços da superclasse. Neste caso, diz-se que a subclasse quebrou 
 * a tradição da superclasse, o que explica o nome deste problema de design.
 * 
 * Estratégia de detecção:
 * 
 * TraditionBreaker = ((NAS >= AVERAGE(NOM)) and (PNAS>=2/3)) 
 *	                                      and 
 *	                    (((AMW > AVERAGE) or (WMC >= VERY HIGH)) and (NOM >= HIGH))
 *	                                      and
 *	                    ((AMW > AVERAGE) and (NOM > HIGH/2) and (WMC >= VERY HIGH/2))) 
 *
 * 
 * @author Ramon
 *
 */
public final class TraditionBreaker extends ModelSmellFinderBasedOnMetrics {
	
	private static final double AVERAGE_AMW = 0;
	private static final double VERY_HIGH_WMC = 0;
	private static final double HIGH_NOM = 0;
	
	private static String nasMetricId = "br.ufba.pesquisa.ramon.ecore.metrics.nas";
	private static String nomMetricId = "br.ufba.pesquisa.ramon.ecore.metrics.nom";
	private static String pnasMetricId = "br.ufba.pesquisa.ramon.ecore.metrics.pnas";
	private static String wmcMetricId = "br.ufba.pesquisa.ramon.ecore.metrics.wmc";
	private static String amwMetricId = "br.ufba.pesquisa.ramon.ecore.metrics.amw";

	public TraditionBreaker() {
		super();
		addRelativeMetric(nomMetricId);
	}
	
	@Override
	protected boolean detectionStrategyFoundSmell(EObject element) {
		
		double nas = calculateMetric(nasMetricId, element);
		double pnas = calculateMetric(pnasMetricId, element);
		double nom = getRelativeMetricList().get(0).getMetricValue(element);
		double nomAverage = getRelativeMetricList().get(0).getAverage();
		double wmc = calculateMetric(wmcMetricId, element);
		double amw = calculateMetric(amwMetricId, element);
		
		return ((nas > nomAverage) && (pnas >= 2/3))
				&& ((amw > AVERAGE_AMW) || (wmc > VERY_HIGH_WMC) && (nom >= HIGH_NOM))
				&& ((amw > AVERAGE_AMW) && (nom > HIGH_NOM/2) && (wmc >= VERY_HIGH_WMC/2));
	}
	
}