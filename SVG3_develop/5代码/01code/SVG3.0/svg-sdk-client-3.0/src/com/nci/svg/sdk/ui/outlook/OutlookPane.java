/*
 * CurtainPane.java
 *
 * Created on June 10, 2007, 12:12 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.nci.svg.sdk.ui.outlook;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JLayeredPane;
import javax.swing.Timer;

import org.jdesktop.swingworker.SwingWorker;

import com.nci.svg.sdk.client.EditorAdapter;
import com.nci.svg.sdk.graphunit.SymbolTypeBean;
import com.nci.svg.sdk.logntermtask.LongtermTask;
import com.nci.svg.sdk.logntermtask.LongtermTaskManager;


/**
 * 
 * @author Qil.Wong
 */
public class OutlookPane extends JLayeredPane {
	private static double DELTA = 0.1;
	private static int ANIMATION_INTERVAL = 15;

	private ArrayList<CurtainDrawer> drawers = new ArrayList<CurtainDrawer>();
	private ArrayList<Component> paneComponents = new ArrayList<Component>();
	private int selectedPane = -1;
	private int currentLayer = -30000;
	private CurtainPaneLayout curtain_layout;
	private boolean animated;
	private EditorAdapter editor;
	
	/**
	 * 默认的起始颜色
	 */
	private Color startColor = Color.white;
	/**
	 * 默认的结束颜色
	 */
	private Color endColor = new Color(159, 188, 233);

	/** Creates a new instance of CurtainPane */
	public OutlookPane(EditorAdapter editor) {
		curtain_layout = new CurtainPaneLayout();
		setLayout(curtain_layout);
		this.editor = editor;
	}

	
	public void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		GradientPaint gradient = new GradientPaint(0, 0, startColor, this
				.getWidth(), this.getHeight(), endColor);
		g2d.setPaint(gradient);
		g.fillRect(0, 0, this.getWidth(), this.getHeight());
	}
	
	public void addPane(String title,Object userObject, Component comp) {
		addPane(title, userObject,null, comp);
	}

	/**
	 * 增加一个outlookpane内部组件
	 * 
	 * @param title
	 *            添加的组件的名称
	 * @param icon
	 *            添加的组件的图标
	 * @param comp
	 *            添加的组件
	 */
	public void addPane(String title, Object userObject, Icon icon, Component comp) {
		OutlookButton cpb = new OutlookButton(title, icon);
		cpb.setUserObject(userObject);
		CurtainDrawer drawer = new CurtainDrawer(cpb, comp);
		cpb.addActionListener(new ExtendingAction(drawer));
		cpb.addActionListener(new ShowSymbolAction());
		add(drawer, new Integer(currentLayer++));
		drawers.add(drawer);
		paneComponents.add(comp);
		selectedPane++;		
	}

	public String getSelectedButtonTitle(){
    	return drawers.get(selectedPane).getOutlookButton().getText();
    }
	
	public Object getSelectedButtonUserObject(){
		return drawers.get(selectedPane).getOutlookButton().getUserObject();
	}

	public ArrayList<Component> getPaneComponents() {
		return paneComponents;
	}

	public void setSelectedPane(int selectedIndex) {
		selectedPane = selectedIndex;
		doLayout();
	}

	public int getSelectIndex() {
		return selectedPane;
	}

	private void resetLayer(int n) {
		setLayout(curtain_layout);
		setSelectedPane(n);
	}

	public boolean isAnimated() {
		return animated;
	}

	public void setAnimated(boolean animated) {
		this.animated = animated;
	}
	
	class ShowSymbolAction implements ActionListener {


		
		public ShowSymbolAction(){

		}
		
		public void actionPerformed(ActionEvent e) {
			SwingWorker<Object, Object> worker = new SwingWorker<Object, Object>(){

				@Override
				protected Object doInBackground() throws Exception {
					editor.getOutlookSymbolPanelMap().get(getSelectedButtonUserObject()).initGraph();
					return null;
				}
				
				public void done(){
					System.gc();
				}
				
			};
			LongtermTaskManager.getInstance(editor).addAndStartLongtermTask(new LongtermTask("正在加载...",worker));
		}
		
	}

	class ExtendingAction implements ActionListener {
		private CurtainDrawer drawer;
		private Timer timer;

		public ExtendingAction(CurtainDrawer cd) {
			drawer = cd;
		}

		public void actionPerformed(ActionEvent e) {
			int layer = drawers.indexOf(drawer);
			if (layer == selectedPane) {
				layer = paneComponents.size()-1;
			}
			if (isAnimated()) {
				if (timer != null && timer.isRunning())
					timer.stop();
				timer = new Timer(ANIMATION_INTERVAL, null);
				ActionListener action = new AnimationAction(timer, layer);
				timer.addActionListener(action);
				timer.start();
			} else
				setSelectedPane(layer);
		}
	}

	class CurtainDrawer extends JComponent {
		OutlookButton cpb1;
		Component comp1;
		public CurtainDrawer(OutlookButton cpb, Component comp) {
			cpb1 = cpb;
			comp1=comp;
			setLayout(new BorderLayout());
			add(cpb1, BorderLayout.NORTH);
			add(comp1, BorderLayout.CENTER);
		}
		
		public OutlookButton getOutlookButton(){
			return cpb1;
		}
	}

	abstract class Layout implements LayoutManager {
		public void addLayoutComponent(String name, Component comp) {
		}

		public void removeLayoutComponent(Component comp) {
		}

		public Dimension preferredLayoutSize(Container parent) {
			return new Dimension(0, 0);
		}

		public Dimension minimumLayoutSize(Container parent) {
			return new Dimension(0, 0);
		}

		public void layoutContainer(Container parent) {
			int size = drawers.size();
			int h = parent.getHeight() - (size - 1)
					* OutlookButtonUI.PREFERED_HEIGHT;
			int w = parent.getWidth();
			int y = 0;
			for (int i = 0; i < size; i++) {
				CurtainDrawer drawer = drawers.get(i);
				y = layoutDrawer(drawer, i, y, w, h);
			}
		}

		protected abstract int layoutDrawer(CurtainDrawer drawer, int i, int y,
				int w, int h);
	}

	class AnimationLayout extends Layout {
		private double ratio;
		private int expandingPane;

		public AnimationLayout(double ratio, int expandingPaneIndex) {
			this.ratio = ratio;
			this.expandingPane = expandingPaneIndex;
		}

		public void setRatio(double ratio) {
			this.ratio = ratio;
		}

		protected int layoutDrawer(CurtainDrawer drawer, int i, int y, int w,
				int h) {
			if (selectedPane == i) {
				int delta = (int) (h - (h - OutlookButtonUI.PREFERED_HEIGHT)
						* ratio);
				drawer.setBounds(0, y, w, h);
				return y + delta;
			} else if (expandingPane == i) {
				int delta = (int) ((h - OutlookButtonUI.PREFERED_HEIGHT) * ratio)
						+ OutlookButtonUI.PREFERED_HEIGHT;
				drawer.setBounds(0, y, w, h);
				return y + delta;
			} else {
				drawer.setBounds(0, y, w, h);
				return y + OutlookButtonUI.PREFERED_HEIGHT;
			}
		}
	}

	class CurtainPaneLayout extends Layout {
		protected int layoutDrawer(OutlookPane.CurtainDrawer drawer, int i,
				int y, int w, int h) {
			drawer.setBounds(0, y, w, h);
			if (selectedPane == i)
				return y + h;
			else
				return y + OutlookButtonUI.PREFERED_HEIGHT;
		}
	}

	class AnimationAction implements ActionListener {
		private AnimationLayout layout;
		private Timer timer;
		private int expandingPane;
		private double ratio = 0;

		public AnimationAction(Timer timer, int expandingPaneIndex) {
			this.timer = timer;
			expandingPane = expandingPaneIndex;
			layout = new AnimationLayout(ratio, expandingPane);
			OutlookPane.this.setLayout(layout);
		}

		public void actionPerformed(ActionEvent e) {
			ratio += DELTA;
			if (ratio > 1.0) {
				resetLayer(expandingPane);
				timer.stop();
				timer = null;
			} else {
				layout.setRatio(ratio);
				OutlookPane.this.doLayout();
			}
		}
	}
}
