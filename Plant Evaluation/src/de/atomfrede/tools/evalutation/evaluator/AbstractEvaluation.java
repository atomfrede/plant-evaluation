/**
 *  Copyright 2011 Frederik Hahne 
 *
 * 	AbstractEvaluation.java is part of Plant Evaluation.
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

import java.util.ArrayList;
import java.util.List;

import de.atomfrede.tools.evalutation.evaluator.common.AbstractEvaluator;

public abstract class AbstractEvaluation {

	public enum EvaluationType {
		CO2ABSOLUTE, JULIANE, INGO
	}

	protected List<AbstractEvaluator> evaluators = new ArrayList<AbstractEvaluator>();

	public List<AbstractEvaluator> getEvaluators() {
		return evaluators;
	}

	public void setEvaluators(List<AbstractEvaluator> evaluator) {
		this.evaluators = evaluator;
	}

	public abstract void evaluate() throws Exception;
}
