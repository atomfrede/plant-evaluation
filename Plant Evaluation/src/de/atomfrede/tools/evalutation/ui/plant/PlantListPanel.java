/**
 *  Copyright 2011 Frederik Hahne
 *  
 * 	PlantListPanel.java is part of Plant Evaluation.
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

package de.atomfrede.tools.evalutation.ui.plant;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingWorker;

import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.factories.ButtonBarFactory;
import com.jgoodies.forms.layout.FormLayout;

import de.atomfrede.tools.evalutation.Plant;
import de.atomfrede.tools.evalutation.evaluator.CopyEvaluator;
import de.atomfrede.tools.evalutation.main.PlantHelper;
import de.atomfrede.tools.evalutation.ui.res.Messages;
import de.atomfrede.tools.evalutation.ui.res.icons.Icons;

public class PlantListPanel extends JPanel {

	List<Plant> plantList;
	List<PlantDataInputPanel> plantDataInputPanelList;
	JButton addButton, evaluateButton;
	JProgressBar evaluationProgressBar;

	public PlantListPanel() {
		this(new ArrayList<Plant>());
	}

	public PlantListPanel(List<Plant> plantList) {
		this.plantList = plantList;
		this.plantDataInputPanelList = new ArrayList<PlantDataInputPanel>();
		initialize();
	}

	private void initialize() {
		removeAll();
		setLayout(new BorderLayout());

		FormLayout layout = new FormLayout("fill:pref:grow"); //$NON-NLS-1$
		DefaultFormBuilder builder = new DefaultFormBuilder(layout);

		builder.appendSeparator(Messages.getString("PlantListPanel.2")); //$NON-NLS-1$

		int index = -1;
		for (Plant plant : plantList) {
			index++;
			builder.appendSeparator(Messages.getString("PlantListPanel.3") + (index + 1)); //$NON-NLS-1$
			builder.append(getPlantInputPanel(plant, index));

		}

		builder.append(ButtonBarFactory.buildOKCancelBar(getEvaluateButton(),
				getAddButton()));
		builder.append(""); //$NON-NLS-1$

		add(builder.getPanel(), BorderLayout.CENTER);

		add(getEvaluationProgressBar(), BorderLayout.SOUTH);

	}

	private void evaluate() {
		new SwingWorker<Void, Void>() {

			@Override
			protected Void doInBackground() throws Exception {
				// TODO Auto-generated method stub
				// new CopyEvaluator();
				setUpEvaluation();
				return null;
			}

			@Override
			protected void done() {
				invalidate();
				getEvaluationProgressBar().setVisible(false);
				revalidate();
			}
		}.execute();
	}

	private PlantDataInputPanel getPlantInputPanel(Plant plant, final int index) {
		PlantDataInputPanel inputPanel = new PlantDataInputPanel(plant);

		if (plantList.size() == 1)
			inputPanel.deleteButton.setEnabled(false);

		plantDataInputPanelList.add(index, inputPanel);

		inputPanel.deleteButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				removePlant(index);

			}
		});
		return inputPanel;
	}

	private JButton getAddButton() {
		if (addButton == null) {
			addButton = new JButton();
			addButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					addPlant(new Plant());

				}
			});
			addButton.setIcon(Icons.IC_ADD_SMALL);
			addButton.setMaximumSize(addButton.getPreferredSize());
		}
		return addButton;
	}

	private JButton getEvaluateButton() {
		if (evaluateButton == null) {
			evaluateButton = new JButton(Messages.getString("PlantListPanel.5")); //$NON-NLS-1$
			// evaluateButton.setIcon(IC_ADD);

			evaluateButton.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					invalidate();
					getEvaluationProgressBar().setVisible(true);
					revalidate();
					evaluate();
				}
			});
		}
		return evaluateButton;
	}

	private JProgressBar getEvaluationProgressBar() {
		if (evaluationProgressBar == null) {
			evaluationProgressBar = new JProgressBar();
			evaluationProgressBar.setIndeterminate(true);
			evaluationProgressBar.setVisible(false);
		}
		return evaluationProgressBar;
	}

	private void rebuild() {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					invalidate();
					initialize();
					revalidate();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public void removePlant(int index) {
		updatePlants();
		plantList.remove(index);
		rebuild();
	}

	public void addPlant(Plant plant) {
		updatePlants();
		plantList.add(plant);
		rebuild();

	}

	public List<Plant> getPlantList() {
		if (plantList == null)
			plantList = new ArrayList<Plant>();

		return plantList;
	}

	public void setPlantList(List<Plant> plantList) {
		this.plantList = plantList;
	}

	private void setUpEvaluation() {
		// check
		updatePlants();
		new CopyEvaluator();
	}

	private void updatePlants() {
		int i = -1;
		for (Plant p : PlantHelper.getDefaultPlantList()) {
			i++;
			p.setStartDate(plantDataInputPanelList.get(i).getStartDate());
			p.setEndDate(plantDataInputPanelList.get(i).getEndDate());
		}
	}

}