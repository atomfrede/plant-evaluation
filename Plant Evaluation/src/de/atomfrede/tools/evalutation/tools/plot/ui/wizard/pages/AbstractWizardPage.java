/**
 *  Copyright 2011 Frederik Hahne
 *
 * 	AbstractWizardPage.java is part of Plant Evaluation.
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
package de.atomfrede.tools.evalutation.tools.plot.ui.wizard.pages;

import javax.swing.JDialog;

import org.ciscavate.cjwizard.WizardPage;

@SuppressWarnings("serial")
public abstract class AbstractWizardPage extends WizardPage {

	protected JDialog parent;

	/**
	 * @param title
	 * @param description
	 */
	public AbstractWizardPage(String title, String description, JDialog parent) {
		super(title, description);
		this.parent = parent;
	}
}
