package org.eclipse.emf.refactor.modelsmell;

import java.util.LinkedList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.refactor.modelsmell.util.ModelSmellFinderBasedOnMetrics;
import org.eclipse.emf.refactor.smells.interfaces.IModelSmellFinder;

/**
 * Acontece quando uma classe centraliza grande parte da inteligência do sistema. Isso 
 * vai contra os bons princípios de design orientado a objetos, onde a inteligência do 
 * sistema costuma ser uniformemente distribuída entre as classes existentes. Quando 
 * este code smell ocorre, geralmente a God Class contém uma grande quantidade de lógicas 
 * em seus métodos e delega apenas pequenas atividades para outras classes do sistema.
 * 
 * Estratégia de detecção:
 * 
 * 	GodClasses = (ATFD>4) and (WMC>=25) and (CAM<0.33) 
 * 
 * Obs: A métrica WMC usada neste code smell considera que cada método possui complexidade
 * igual a 1, ou seja, possuirá valor sempre igual à métrica Number Of Methods. Por este
 * motivo, foi utilizada a métrica Number Of Methods no cálculo deste code smell.
 * 
 * @author Ramon
 */
public final class GodClass extends ModelSmellFinderBasedOnMetrics {
	
	private String atfdMetricId = "br.ufba.pesquisa.ramon.ecore.metrics.atfd";
	private String nomMetricId = "br.ufba.pesquisa.ramon.ecore.metrics.nom";
	private String camMetricId = "br.ufba.pesquisa.ramon.ecore.metrics.cam";

	@Override
	protected boolean detectionStrategyFoundSmell(EObject element) {
		return possuiAtfdAltoDemais(element) && possuiWmcAltoDemais(element) && possuiCamBaixoDemais(element);
	}

	private boolean possuiCamBaixoDemais(EObject element) {
		return calculateMetric(camMetricId, element) < 0.33;
	}

	private boolean possuiWmcAltoDemais(EObject element) {
		return calculateMetric(nomMetricId, element) >= 15;
	}

	private boolean possuiAtfdAltoDemais(EObject element) {
		return calculateMetric(atfdMetricId, element) > 4;
	}
	
}