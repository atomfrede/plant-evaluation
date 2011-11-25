/**
 *  Copyright 2011 Frederik Hahne 
 *
 * 	CO2AbsoluteOnlyEvaluation.java is part of Plant Evaluation.
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
import de.atomfrede.tools.evalutation.evaluator.concrete.CO2AbsoluteOnlyEvaluator;
import de.atomfrede.tools.evalutation.evaluator.concrete.CopyEvaluator;

public class CO2AbsoluteOnlyEvaluation extends AbstractEvaluation {

	private final Log log = LogFactory.getLog(CO2AbsoluteOnlyEvaluation.class);

	CopyEvaluator copyEvaluator;
	CO2AbsoluteOnlyEvaluator co2absEvaluator;

	public CO2AbsoluteOnlyEvaluation() {
		copyEvaluator = new CopyEvaluator();
		co2absEvaluator = new CO2AbsoluteOnlyEvaluator(
				copyEvaluator.getOutputFile());
		evaluators.add(copyEvaluator);
		evaluators.add(co2absEvaluator);
	}

	@Override
	public void evaluate() throws Exception {
		log.trace("CO2 Absolute Only Evaluation started.");
		int i = 0;
		boolean done = true;
		while (i < evaluators.size()) {
			if (i == evaluators.size())
				break;
			AbstractEvaluator evaluator = evaluators.get(i);
			done = evaluator.evaluate();
			if (!done)
				break;
			else {
				if (evaluator instanceof CopyEvaluator) {
					CopyEvaluator cpe = (CopyEvaluator) evaluator;
					co2absEvaluator.setInputFile(cpe.getOutputFile());
					i++;
					continue;
				}
			}
			i++;
		}
	}
}
