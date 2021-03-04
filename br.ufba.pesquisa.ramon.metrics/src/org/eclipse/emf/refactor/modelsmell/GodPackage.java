package org.eclipse.emf.refactor.modelsmell;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.refactor.metrics.core.Metric;
import org.eclipse.emf.refactor.metrics.interfaces.IMetricCalculator;
import org.eclipse.emf.refactor.metrics.runtime.core.Result;
import org.eclipse.emf.refactor.smells.interfaces.IModelSmellFinder;

import br.ufba.pesquisa.ramon.util.Util;

/**
 * Estratégia de detecção: GodPackage = (PS>20) and (PS, TopValues(25%)) and
 * (NOCC>20) and (NOCP>3) and (PC<0,33)
 * 
 * @param object
 * @return
 */
public final class GodPackage implements IModelSmellFinder {

	private String psMetricId = "br.ufba.pesquisa.ramon.ecore.metrics.ps";
	private Metric psMetric = Metric.getMetricInstanceFromId(psMetricId);

	private String noccMetricId = "br.ufba.pesquisa.ramon.ecore.metrics.nocc";
	private Metric noccMetric = Metric.getMetricInstanceFromId(noccMetricId);
	
	private String pcMetricId = "br.ufba.pesquisa.ramon.ecore.metrics.pc";
	private Metric pcMetric = Metric.getMetricInstanceFromId(pcMetricId);

	private LinkedList<Result> resultadoPS;

	@Override
	public LinkedList<LinkedList<EObject>> findSmell(EObject root) {
		resultadoPS = calculateAllPS(root);
		return findSmellyObjectGroups(root);
	}

	private LinkedList<LinkedList<EObject>> findSmellyObjectGroups(EObject root) {

		LinkedList<LinkedList<EObject>> smellyEObjects = new LinkedList<LinkedList<EObject>>();
		List<EObject> containedEObjects = root.eContents();

		for (EObject element : containedEObjects) {

			if (element instanceof EPackage) {
				EPackage pacote = (EPackage) element;
				if (detectionStrategyFoundSmell(pacote)) {
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

	private LinkedList<Result> calculateAllPS(EObject element) {

		LinkedList<Result> resultadosPS = new LinkedList<Result>();
		IMetricCalculator psCalculateClass = psMetric.getCalculateClass();

		LinkedList<EObject> allEObjects = new LinkedList<EObject>();
		allEObjects.add(element);
		allEObjects.addAll(Util.getAllEObjects(element));

		for (EObject currentEObject : allEObjects) {
			if (psMetric.getContext().equals(currentEObject.eClass().getInstanceClass().getSimpleName())) {
				System.out
						.println("Calculate metric '" + psMetric.getName() + "' on element '" + psMetric.getContext());

				ArrayList<EObject> contextObjectList = new ArrayList<EObject>();
				contextObjectList.add(currentEObject);
				psCalculateClass.setContext(contextObjectList);

				Result resultado = new Result(psMetric, contextObjectList, psMetric.getCalculateClass().calculate());
				resultadosPS.add(resultado);
			}
		}

		ordenarResultados(resultadosPS);

		return resultadosPS;
	}

	private void ordenarResultados(LinkedList<Result> resultadosCMPlusCA) {
		Collections.sort(resultadosCMPlusCA, new Comparator<Result>() {
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

	private boolean detectionStrategyFoundSmell(EPackage pacote) {

		double psResult = 0;
		for (Result resultado : resultadoPS) {
			if (pacote.equals(resultado.getContext().get(0))) {
				psResult = resultado.getResultValue();
				break;
			}
		}

		return isPSAltoDemais(psResult) && 
				isPSEntreMaioresDoModelo(psResult) && 
				isNOCCAltoDemais(pacote) &&
				isPCBaixoDemais(pacote);
	}

	private boolean isPSAltoDemais(double psResult) {
		return psResult > 20;
	}

	private boolean isPSEntreMaioresDoModelo(double psResult) {
		int linhaDeCorte = (int) (resultadoPS.size() * 0.75);
		return psResult >= this.resultadoPS.get(linhaDeCorte).getResultValue();
	}

	private boolean isNOCCAltoDemais(EPackage pacote) {
		IMetricCalculator ccCalculateClass = noccMetric.getCalculateClass();

		LinkedList<EObject> rootList = new LinkedList<EObject>();
		rootList.add(pacote);
		ccCalculateClass.setContext(rootList);

		return ccCalculateClass.calculate() > 20;
	}
	
	private boolean isPCBaixoDemais(EPackage pacote) {
		IMetricCalculator pcCalculateClass = pcMetric.getCalculateClass();
		
		LinkedList<EObject> rootList = new LinkedList<EObject>();
		rootList.add(pacote);
		pcCalculateClass.setContext(rootList);

		return pcCalculateClass.calculate() < 0.33;
	}

}