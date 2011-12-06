/**
 *  Copyright 2011 Frederik Hahne 
 *
 * 	Evaluation.java is part of Plant Evaluation.
 *
 *  Plant Evaluation is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Plant Evaluation is distributed in the hope that it will be useful,
 * 	but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with Plant Evaluation.  If not, see <http://www.gnu.org/licenses/>.
 */

package de.atomfrede.tools.evalutation.evaluator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.atomfrede.tools.evalutation.evaluator.common.AbstractEvaluator;
import de.atomfrede.tools.evalutation.evaluator.common.MultipleInputFileEvaluator;
import de.atomfrede.tools.evalutation.evaluator.common.SingleInputFileEvaluator;
import de.atomfrede.tools.evalutation.evaluator.common.SingleInputMultipleOutputFileEvaluator;
import de.atomfrede.tools.evalutation.evaluator.concrete.CO2DiffEvaluator;
import de.atomfrede.tools.evalutation.evaluator.concrete.CopyEvaluator;
import de.atomfrede.tools.evalutation.evaluator.concrete.Delta13Evaluator;
import de.atomfrede.tools.evalutation.evaluator.concrete.MeanValueEvaluator;
import de.atomfrede.tools.evalutation.evaluator.concrete.PhotoSynthesisEvaluator;
import de.atomfrede.tools.evalutation.evaluator.concrete.PlantDivider;
import de.atomfrede.tools.evalutation.evaluator.concrete.StandardDeviationEvaluator;
import de.atomfrede.tools.evalutation.evaluator.concrete.TemperatureEvaluator;

public class StandardEvaluation extends AbstractEvaluation {

	private final Log log = LogFactory.getLog(StandardEvaluation.class);

	CopyEvaluator copyEvaluator;
	MeanValueEvaluator meanEvaluator;
	CO2DiffEvaluator co2DiffEvaluator;
	Delta13Evaluator delta13Evaluator;
	TemperatureEvaluator temperature;
	PlantDivider plantDivider;
	PhotoSynthesisEvaluator psr;
	StandardDeviationEvaluator sd;

	public StandardEvaluation() {
		copyEvaluator = new CopyEvaluator();
		meanEvaluator = new MeanValueEvaluator(copyEvaluator.getOutputFile());
		co2DiffEvaluator = new CO2DiffEvaluator(meanEvaluator.getOutputFile(),
				meanEvaluator.getStandardDerivationOutputFile());
		delta13Evaluator = new Delta13Evaluator(
				co2DiffEvaluator.getOutputFile(),
				co2DiffEvaluator.getStandardDerivationOutputFile());
		temperature = new TemperatureEvaluator(
				delta13Evaluator.getOutputFile(),
				delta13Evaluator.getStandardDerivationOutputFile());
		plantDivider = new PlantDivider(temperature.getOutputFile(),
				temperature.getStandardDerivationOutputFile());
		psr = new PhotoSynthesisEvaluator(plantDivider.getOutputFiles(),
				plantDivider.getStandardDerivationOutputFiles());
		sd = new StandardDeviationEvaluator(psr.getOutputFiles(),
				psr.getStandardDeviationOutputFiles());

		evaluators.add(copyEvaluator);
		evaluators.add(meanEvaluator);
		evaluators.add(co2DiffEvaluator);
		evaluators.add(delta13Evaluator);
		evaluators.add(temperature);
		evaluators.add(plantDivider);
		evaluators.add(psr);
		evaluators.add(sd);
	}

	@Override
	public void evaluate() throws Exception {
		log.trace("Standard Evaluation started");
		int i = 0;
		boolean done = true;
		while (i < evaluators.size()) {
			if (i == evaluators.size())
				break;
			AbstractEvaluator evaluator = getEvaluators().get(i);
			done = evaluator.evaluate();
			if (!done)
				break;
			else {
				if (evaluator instanceof CopyEvaluator) {
					CopyEvaluator cpe = (CopyEvaluator) evaluator;
					meanEvaluator.setInputFile(cpe.getOutputFile());
					i++;
					continue;
				}
				if (i + 1 < evaluators.size()
						&& evaluators.get(i + 1) instanceof SingleInputFileEvaluator) {
					((SingleInputFileEvaluator) evaluators.get(i + 1))
							.setInputFile(((SingleInputFileEvaluator) evaluator)
									.getOutputFile());
					((SingleInputFileEvaluator) evaluators.get(i + 1))
							.setStandardDerivationInputFile(((SingleInputFileEvaluator) evaluator)
									.getStandardDerivationOutputFile());
					i++;
					continue;
				}
				if (i + 1 < evaluators.size()
						&& evaluators.get(i + 1) instanceof SingleInputMultipleOutputFileEvaluator) {
					((SingleInputMultipleOutputFileEvaluator) evaluators
							.get(i + 1))
							.setInputFile(((SingleInputFileEvaluator) evaluator)
									.getOutputFile());
					((SingleInputMultipleOutputFileEvaluator) evaluators
							.get(i + 1))
							.setStandardDerivationInputFile(((SingleInputFileEvaluator) evaluator)
									.getStandardDerivationOutputFile());
					i++;
					continue;
				}
				if (i + 1 < evaluators.size()
						&& evaluators.get(i + 1) instanceof MultipleInputFileEvaluator) {
					if (evaluator instanceof SingleInputMultipleOutputFileEvaluator) {
						((MultipleInputFileEvaluator) evaluators.get(i + 1))
								.setInputFiles(((SingleInputMultipleOutputFileEvaluator) evaluator)
										.getOutputFiles());
						((MultipleInputFileEvaluator) evaluators.get(i + 1))
								.setStandardDeviationInputFiles(((SingleInputMultipleOutputFileEvaluator) evaluator)
										.getStandardDerivationOutputFiles());
						i++;
						continue;
					}
					if (evaluator instanceof MultipleInputFileEvaluator) {
						((MultipleInputFileEvaluator) evaluators.get(i + 1))
								.setInputFiles(((MultipleInputFileEvaluator) evaluator)
										.getOutputFiles());
						((MultipleInputFileEvaluator) evaluators.get(i + 1))
								.setStandardDeviationInputFiles(((MultipleInputFileEvaluator) evaluator)
										.getStandardDeviationOutputFiles());
						i++;
						continue;
					}
				}
			}
			i++;

		}
	}
}
