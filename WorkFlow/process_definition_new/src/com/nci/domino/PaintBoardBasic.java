package com.nci.domino;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.nci.domino.beans.WofoActions;
import com.nci.domino.beans.WofoBaseBean;
import com.nci.domino.beans.WofoNetBean;
import com.nci.domino.beans.desyer.WofoProcessBean;
import com.nci.domino.edit.ToolMode;
import com.nci.domino.help.Functions;
import com.nci.domino.help.WfMath;
import com.nci.domino.shape.WfActivity;
import com.nci.domino.shape.activity.topicon.WfActivityIconSituation;
import com.nci.domino.shape.basic.AbstractShape;
import com.nci.domino.shape.basic.PaintBoardShapeSelectionListener;
import com.nci.domino.shape.basic.WfTransitionBasic;

public class PaintBoardBasic extends JComponent {

	protected JScrollPane paintBoardScroll;

	/**
	 * �ֱ�����������xy��Ļ���� �ƶ�����̧��xy����
	 */
	protected double pxOld, pyOld, pxNew, pyNew;//
	/**
	 * �ֱ�����������xy�������� �ƶ�����̧��xy����
	 */
	protected double wxOld, wyOld, wxNew, wyNew;//
	/**
	 * �ֱ��ǻ�ϵ���������x��y �ϵ�ƽ��x��y
	 */
	protected double xCentOld, yCentOld, txOld, tyOld;//
	/**
	 * ��һ� �ڶ�����϶��
	 */
	protected AbstractShape draggingShape = null;

	/**
	 * ����ƶ������λ�����ڵ�ͼ��
	 */
	protected AbstractShape underMouseShape = null;

	protected boolean isCommanding = false;// ��ʾ��������ִ��״̬
	/**
	 * ��������״̬���������� �������������� ƽ��x ƽ��y ת�� ��ת����x ��ת����y
	 */
	protected double[] trans = { GlobalConstants.scaleValueOrigin, .0, .0, .0,
			.0, .0 };

	/**
	 * �����������
	 */
	protected RenderingHints rh = new RenderingHints(
			RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

	protected Dimension area = new Dimension(0, 0);

	/**
	 * ��� tranRouter ��ϣ����Ǩ����·�ɼ��� ��¼ĳ�����֮��Ŀǰ���˼��� Ҳ���ǻ��˼���Ǩ���� key�������Сid �»��� ��id
	 * ���� 123_345 123��345������id С����ǰ ����ں�
	 */
	protected HashMap<String, Integer> tranRouter = new HashMap<String, Integer>(); // @jve:decl-index=0:

	/**
	 * ������2���������������������̶��� ��������
	 */
	protected WofoProcessBean processBean;
	/**
	 * ���Ǩ���ߡ����֡�Ӿ����ͼ�εĶ���
	 */
	protected List<AbstractShape> graphVector = new Vector<AbstractShape>();

	/**
	 * ���µ�����ֵ
	 */
	protected int pressedMouseButton = -1;

	/**
	 * ��ǰѡ�е�ͼ��
	 */
	protected List<AbstractShape> selectedShapes = new Vector<AbstractShape>();

	/**
	 * ���ͼ��ѡ���¼�����
	 */
	protected List<PaintBoardShapeSelectionListener> boardShapeSelectionListeners = new ArrayList<PaintBoardShapeSelectionListener>();

	/**
	 * ���ư�����·����ɫ
	 */
	protected static Color helpLineColor = new Color(180, 0, 0);

	/**
	 * ��ǰ�϶���ͼ��
	 */
	protected List<AbstractShape> ghostDraggedShapes = new Vector<AbstractShape>();

	/**
	 * ��ǰ����Ƿ񱻱༭�ı�ǩ
	 */
	protected boolean edited = false;

	/**
	 * �Ƿ�������ק
	 */
	protected boolean dragging = false;

	/**
	 * �Ƿ�򿪵�����ʷ����
	 */
	protected boolean historyBoard = false;

	/**
	 * ����Ͻ�ͼ�������ļ�
	 */
	public static Map<String, WfActivityIconSituation> topRightConfig = null;

	public static boolean topIconResizable = false;

	public PaintBoardBasic() {
		rh.put(RenderingHints.KEY_RENDERING,
				RenderingHints.VALUE_RENDER_QUALITY);
		rh.put(RenderingHints.KEY_TEXT_ANTIALIASING,
				RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		rh.put(RenderingHints.KEY_FRACTIONALMETRICS,
				RenderingHints.VALUE_FRACTIONALMETRICS_ON);
		rh.put(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		this.addMouseMotionListener(new MouseMotionAdapter() {
			// ��껬���¼�
			public void mouseMoved(MouseEvent e) {
				mouseMove(e);
			}
		});
		this.registerKeyboardAction(new AbstractAction("zoom_out") {
			public void actionPerformed(ActionEvent e) {
				zoomSteady(1 / 1.2);
			}
		}, KeyStroke.getKeyStroke((char) KeyEvent.VK_MINUS),
				JComponent.WHEN_FOCUSED);
		this.registerKeyboardAction(new AbstractAction("zoom_in") {
			public void actionPerformed(ActionEvent e) {
				zoomSteady(1.2);
			}
		}, KeyStroke.getKeyStroke((char) KeyEvent.VK_EQUALS),
				JComponent.WHEN_FOCUSED);
		if (topRightConfig == null) {
			topRightConfig = new HashMap<String, WfActivityIconSituation>();
			Document doc = Functions.getXMLDocument(getClass().getResource(
					"/resources/activity_topright_config.xml"));
			Element root = doc.getDocumentElement();
			topIconResizable = root.getAttribute("resize").equals("true");
			NodeList icons = doc.getElementsByTagName("icon");
			for (int i = 0; i < icons.getLength(); i++) {
				WfActivityIconSituation situation = new WfActivityIconSituation(
						(Element) icons.item(i), topIconResizable);
				topRightConfig.put(situation.getSituationName()
						.toLowerCase(), situation);
			}
		}
		requestFocus();
	}

	/**
	 * ����ɿ�������ͼ��
	 * 
	 * @param e
	 */
	protected void releaseMouseToSetShape(MouseEvent e) {
		AbstractShape sh = findShapeLoaction(wxNew, wyNew);
		if (sh != null) {
			if (!e.isControlDown()) {
				setAllUnselected();
			}
			if (sh.isSelected()) {
				setShapeUnselected(sh);
			} else {
				addSelectdShape(sh);
			}
		} else {
			if (!e.isControlDown()) {
				setAllUnselected();
			}
		}
	}

	/**
	 * ����ƶ�
	 * 
	 * @param e
	 */
	protected void mouseMove(MouseEvent e) {
		pxNew = e.getX();
		pyNew = e.getY();
		double[] ret = WfMath.getWorldCoord(pxNew, pyNew, trans);
		wxNew = ret[0];
		wyNew = ret[1];
		AbstractShape sh = findShapeLoaction(wxNew, wyNew);
		if (underMouseShape != sh && sh != null) {
			sh.mouseEntered(e, this);
		}
		if (underMouseShape != null && underMouseShape != sh) {
			underMouseShape.mouseExited(e, this);
		}
		if (sh != null) {
			sh.mouseMoved(e, this);
		}
		underMouseShape = sh;
	}

	public void setAllUnselected() {
		setAllUnselected(true);
	}

	/**
	 * ������е�ѡ����
	 */
	public void setAllUnselected(boolean fireAction) {
		List<AbstractShape> changedShapes = new ArrayList<AbstractShape>();
		for (AbstractShape shape : selectedShapes) {
			changedShapes.add(shape);
			shape.setSelected(false);
		}
		selectedShapes.clear();
		if (fireAction)
			firePaintBoardShapeSelectionListeners(changedShapes);
	}

	/**
	 * ��ͼ������Ϊ��ѡ��״̬,����ͼ�α��Ϊδѡ��
	 * 
	 * @param s
	 */
	public void setSelectedShape(AbstractShape s) {
		setAllUnselected(false);
		List<AbstractShape> changedShapes = new ArrayList<AbstractShape>();
		changedShapes.add(s);
		s.setSelected(true);
		selectedShapes.add(s);
		firePaintBoardShapeSelectionListeners(changedShapes);
	}

	public void addSelectdShape(AbstractShape s) {
		List<AbstractShape> changedShapes = new ArrayList<AbstractShape>();
		changedShapes.add(s);
		s.setSelected(true);
		selectedShapes.add(s);
		firePaintBoardShapeSelectionListeners(changedShapes);
	}

	/**
	 * ��ָ����ͼ������Ϊ��ѡ��״̬
	 * 
	 * @param s
	 */
	protected void setShapeUnselected(AbstractShape s) {
		List<AbstractShape> changedShapes = new ArrayList<AbstractShape>();
		s.setSelected(false);
		selectedShapes.remove(s);
		changedShapes.add(s);
		firePaintBoardShapeSelectionListeners(changedShapes);
	}

	/**
	 * ����ѡ���ͼ��
	 * 
	 * @param shapes
	 */
	public void setSelectedShapes(List<AbstractShape> shapes) {
		setAllUnselected();
		for (AbstractShape shape : shapes) {
			shape.setSelected(true);
		}
		selectedShapes.addAll(shapes);
		firePaintBoardShapeSelectionListeners(shapes);
	}

	/**
	 * ���ͼ��ѡ���¼�
	 * 
	 * @param l
	 */
	public void addPaintBoardShapeSelectionListener(
			PaintBoardShapeSelectionListener l) {
		this.boardShapeSelectionListeners.add(l);
	}

	/**
	 * �Ƴ�ͼ��ѡ���¼�
	 * 
	 * @param l
	 *            ͼ��ѡ���¼�
	 */
	public void removePaintBoardShapeSelectionListener(
			PaintBoardShapeSelectionListener l) {
		this.boardShapeSelectionListeners.remove(l);
	}

	/**
	 * ����ͼ��ѡ���¼�
	 */
	protected void firePaintBoardShapeSelectionListeners(
			List<AbstractShape> changedShapes) {
		for (PaintBoardShapeSelectionListener l : boardShapeSelectionListeners) {
			l.shapeSelectionStateChanged(selectedShapes, changedShapes);
		}
	}

	/**
	 * ����һ�������ͼ�ζ���
	 * 
	 * @param xL
	 *            x����
	 * @param yL
	 *            y����
	 * @return
	 */
	public AbstractShape findShapeLoaction(double xL, double yL) {
		AbstractShape one;
		for (int i = graphVector.size() - 1; i >= 0; i--) {
			one = graphVector.get(i);
			if (one.isOnRange(xL, yL, GlobalConstants.fbl / trans[0])) {
				return one;
			}
		}
		return null;
	}

	/**
	 * ����λ��������һ
	 * 
	 * @param xL
	 *            x����
	 * @param yL
	 *            y����
	 * @return
	 */
	public WfActivity findActLoaction(double xL, double yL) {
		AbstractShape one;
		for (int i = graphVector.size() - 1; i >= 0; i--) {
			one = graphVector.get(i);
			if (one instanceof WfActivity
					&& one.isOnRange(xL, yL, GlobalConstants.fbl / trans[0])) {
				return (WfActivity) one;
			}
		}
		return null;
	}

	/**
	 * �ص�ԭʼ������λ��
	 */
	public void toOriginLoaction() {
		trans = new double[] { GlobalConstants.scaleValueOrigin, 0., 0., 0.,
				0., 0. };
		computePreferedSize();
		repaint();
	}

	private Color backgroundColor = new Color(222, 222, 255);

	@Override
	public void paintComponent(Graphics g) {
		g.setColor(backgroundColor);
		g.fillRect(0, 0, getWidth(), getHeight());
	}

	/*
	 * ������ͼ�� @param g Graphicsʵ��
	 */
	public void paint(Graphics g) {
		super.paint(g);
		Graphics2D g2 = (Graphics2D) g;
		// super.paint(g2);
		setGraphProperty(g2);
		for (int i = 0; i < graphVector.size(); i++) {
			graphVector.get(i).drawShape(g2, this);
		}
		drawHelpLine(g2);
		tranRouter.clear();
	}

	protected void drawHelpLine(Graphics2D g2) {
		// DO NOTHING
	}

	/**
	 * ��������������һЩ����
	 * 
	 * @param g2
	 *            Graphics2Dʵ��
	 */
	protected void setGraphProperty(Graphics2D g2) {
		g2.setRenderingHints(rh);
		g2.setStroke(GlobalConstants.SOLID_LINE_STROKE);
		g2.setFont(GlobalConstants.ACTTEXTFONT);
		g2.scale(trans[0], trans[0]);
		g2.translate(trans[1], trans[2]);
		g2.rotate(trans[3], trans[4], trans[5]);
	}

	/**
	 * �����߽絽���ʵĳߴ�
	 */
	public void adjustBounds() {
		if (graphVector.size() == 0) {
			return;
		}
		double minX = java.lang.Double.MAX_VALUE;
		double minY = java.lang.Double.MAX_VALUE;
		double maxX = java.lang.Double.MIN_VALUE;
		double maxY = java.lang.Double.MIN_VALUE;
		for (int i = 0; i < graphVector.size(); i++) {
			AbstractShape sh = graphVector.get(i);
			if (sh instanceof WfActivity) {
				WfActivity act = (WfActivity) sh;
				double pxy[] = WfMath.getScreenCoord(act.getX(), act.getY(),
						trans);
				if (pxy[0] > maxX) {
					maxX = pxy[0];
				}
				if (pxy[0] < minX) {
					minX = pxy[0];
				}
				if (pxy[1] > maxY) {
					maxY = pxy[1];
				}
				if (pxy[1] < minY) {
					minY = pxy[1];
				}
			}
		}
		minX -= 2. * GlobalConstants.SYMBOL_RAD * trans[0];
		minY -= 2. * GlobalConstants.SYMBOL_RAD * trans[0];
		maxX += 2. * GlobalConstants.SYMBOL_RAD * trans[0];
		maxY += 2. * GlobalConstants.SYMBOL_RAD * trans[0];
		openWindowZoom(minX, minY, maxX, maxY);
		computePreferedSize();
		repaint();
	}

	/**
	 * ����factor �����㲻��
	 * 
	 * @param factor
	 *            �Ŵ�ϵ��
	 */
	public void zoomSteady(double factor) {
		double wban = getSize().getWidth() / factor;
		double hban = getSize().getHeight() / factor;
		// double xt = pxNew - (pxNew / factor);
		// double yt = pyNew - (pyNew / factor);
		double xt = 0;
		double yt = 0;
		openWindowZoom(xt, yt, xt + wban, yt + hban);
		computePreferedSize();
		repaint();
	}

	/**
	 * �������� �߿������Э����ʱ�� �Ա�֤�ô��ڵ�����ȫ����ʾΪԭ�������� ����߿�������ڻ���ĸ߿���� ��������ʾ����������
	 * 
	 * @param px1
	 *            (px1,py1) �������Ͻ���Ļ����
	 * @param py1
	 * @param px2
	 *            (px2,py2) �������½���Ļ����
	 * @param py2
	 */
	protected void openWindowZoom(double px1, double py1, double px2, double py2) {
		Dimension dimsize = getSize();
		double ret1[] = WfMath.getWorldCoord(px1, py1, trans);
		double ret2[] = WfMath.getWorldCoord(px2, py1, trans);
		double ret4[] = WfMath.getWorldCoord(px1, py2, trans);
		double scValue = Math.min((double) dimsize.width
				/ WfMath.getPointPointDis(ret2, ret1), (double) dimsize.height
				/ WfMath.getPointPointDis(ret4, ret1));
		if (scValue > GlobalConstants.MAX_SCALE) {
			JOptionPane.showMessageDialog(this, "���ű������ܴ���"
					+ GlobalConstants.MAX_SCALE);
			return;// ����Ŵ���̫�� ���˳� ��ִ�б��β���
		} else if (scValue < 1. / GlobalConstants.MAX_SCALE) {
			JOptionPane.showMessageDialog(this, "���ű�������С�� " + 1.
					/ GlobalConstants.MAX_SCALE);
			return;// ����Ŵ���̫С ���˳� ��ִ�б��β���
		}
		trans[1] = -ret1[0];
		trans[2] = -ret1[1];
		trans[4] = ret1[0];
		trans[5] = ret1[1];
		trans[0] = scValue;
	}

	/**
	 * ���¼��������������������ֵı�Ҫ��
	 */
	public void computePreferedSize() {
		double maxX = 0, maxY = 0, minX = 0, minY = 0;
		for (AbstractShape sh : graphVector) {
			if (sh.getMaxXPos() > maxX) {
				maxX = sh.getMaxXPos();
			}
			if (sh.getMaxYPos() > maxY) {
				maxY = sh.getMaxYPos();
			}
			if (sh.getMinXPos() < minX) {
				minX = sh.getMinXPos();
			}
			if (sh.getMinYPos() < minY) {
				minY = sh.getMinYPos();
			}
		}
		area.setSize(minX < 0 ? getSize().getWidth() - minX * trans[0] : (maxX
				- minX + 4)
				* trans[0], minY < 0 ? getSize().getHeight() - minY * trans[0]
				: (maxY - minY + 4) * trans[0]); // �����4�ǿ���ͼ�ζ��˺���˿��ܻ�����4�����أ������Ϻÿ�
		setPreferredSize(area);
		revalidate();
	}

	/**
	 * ��ȡ����ģʽ
	 * 
	 * @return
	 */
	public int getToolMode() {
		return ToolMode.TOOL_VIEW;
	}

	/**
	 * ����ͼ�ζ������飬�÷���ֻ���õ�����ǰ��������ʱʹ�á�
	 * 
	 * <b>ע�⣺������ɾ��ͼ��ʱ������ֱ�����øö��󣬶�Ӧ��ʹ��addGraph��removeGraph</b>
	 * 
	 * @return ��ǰͼ�ζ�������
	 */
	public List<AbstractShape> getGraphVector() {
		return graphVector;
	}

	/**
	 * ��ȡ�ͼչ����ʽ��center��ʾ��visio���Ƶ��������м��ͼ�Σ�bottom��ʾ������ͼ�εײ���ͼ��
	 * 
	 * @return
	 */
	public String getActivityShapeStyle() {
		return "center";
	}

	/**
	 * ��ĳ�����֮���һ���߹� ����0 �Ժ����ε���
	 * 
	 * @param oneline
	 *            WfTransitionʵ��
	 * @return ;������
	 */
	public int getExcursionGene(WfTransitionBasic oneline) {
		String firstId = oneline.previousActivity.getWofoBean().getID();
		String secondId = oneline.nextActivity.getWofoBean().getID();
		UUID first = UUID.fromString(firstId.toString());
		UUID second = UUID.fromString(secondId.toString());
		String twoIds = first.compareTo(second) > 0 ? secondId + "_" + firstId
				: firstId + "_" + secondId;

		Object gcount = tranRouter.get(twoIds);
		int newNo = (gcount == null) ? 0 : ((Integer) gcount).intValue() + 1;
		tranRouter.put(twoIds, new Integer(newNo));
		return newNo;
	}

	/**
	 * ��ȡҵ������
	 * 
	 * @return
	 */
	public WofoProcessBean getProcessBean() {
		return processBean;
	}

	/**
	 * ����ҵ������
	 * 
	 * @param processBean
	 */
	public void setProcessBean(WofoProcessBean processBean) {
		setName(processBean.getProcessName());
		this.processBean = processBean;
	}

	/**
	 * ��ȡԭ�ȵ�PaintBoard x����
	 * 
	 * @return
	 */
	public double getPxOld() {
		return pxOld;
	}

	public void setPxOld(double pxOld) {
		this.pxOld = pxOld;
	}

	public double getPyOld() {
		return pyOld;
	}

	public void setPyOld(double pyOld) {
		this.pyOld = pyOld;
	}

	public double getPxNew() {
		return pxNew;
	}

	public void setPxNew(double pxNew) {
		this.pxNew = pxNew;
	}

	public double getPyNew() {
		return pyNew;
	}

	/**
	 * �����µ�PaintBoard�ϵ�y����
	 * 
	 * @param pyNew
	 */
	public void setPyNew(double pyNew) {
		this.pyNew = pyNew;
	}

	/**
	 * ��ȡԭ�ȵ�����x����
	 * 
	 * @return
	 */
	public double getWxOld() {
		return wxOld;
	}

	/**
	 * ����ԭ�ȵ�����x����
	 * 
	 * @param wxOld
	 */
	public void setWxOld(double wxOld) {
		this.wxOld = wxOld;
	}

	public double getWyOld() {
		return wyOld;
	}

	public void setWyOld(double wyOld) {
		this.wyOld = wyOld;
	}

	public double getWxNew() {
		return wxNew;
	}

	public void setWxNew(double wxNew) {
		this.wxNew = wxNew;
	}

	public double getWyNew() {
		return wyNew;
	}

	public void setWyNew(double wyNew) {
		this.wyNew = wyNew;
	}

	public double getxCentOld() {
		return xCentOld;
	}

	public void setxCentOld(double xCentOld) {
		this.xCentOld = xCentOld;
	}

	public double getyCentOld() {
		return yCentOld;
	}

	public void setyCentOld(double yCentOld) {
		this.yCentOld = yCentOld;
	}

	public double getTxOld() {
		return txOld;
	}

	public void setTxOld(double txOld) {
		this.txOld = txOld;
	}

	public double getTyOld() {
		return tyOld;
	}

	public void setTyOld(double tyOld) {
		this.tyOld = tyOld;
	}

	/**
	 * �õ��任�������� һ������ ������ ���ű��� ƽ��x ƽ��y ��ת�ǻ��� ��ת����x ��ת����y
	 * 
	 * @return
	 */
	public double[] getTransformParams() {
		return new double[] { trans[0], trans[1], trans[2], trans[3], trans[4],
				trans[5] };
	}

	/**
	 * ��ȡλ��
	 * 
	 * @return
	 */
	public double[] getTrans() {
		return trans;
	}

	/**
	 * ��ȡ��ǰѡ���ͼ�μ���
	 * 
	 * @return
	 */
	public List<AbstractShape> getSelectedShapes() {
		return selectedShapes;
	}

	/**
	 * �Ƿ������϶�ͼ��
	 * 
	 * @return
	 */
	public boolean isDragging() {
		return dragging;
	}

	/**
	 * �����Ƿ������϶���Э
	 * 
	 * @param dragging
	 */
	public void setDragging(boolean dragging) {
		this.dragging = dragging;
	}

	/**
	 * ��ȡ�϶�ͼ�εĿ�������
	 * 
	 * @return
	 */
	public List<AbstractShape> getGhostDraggedShapes() {
		return ghostDraggedShapes;
	}

	/**
	 * ����ҵ��id��ȡͼ��
	 * 
	 * @param id
	 * @return
	 */
	public AbstractShape getShapeById(String id) {
		for (AbstractShape s : graphVector) {
			if (id.equals(s.getWofoBean().getID())) {
				return s;
			}
		}
		return null;
	}

	/**
	 * ����ͼ�Σ����ﲻ�ܼ򵥵���GraphVector�����shape�� ������Ҫ˳���ϵ�Ҫ�󣬻򵥸�shape���ܻ����������ͼ�ζ���
	 * 
	 * @param shape
	 */
	public void addGraph(AbstractShape shape) {
		shape.addShape(this);
	}

	/**
	 * ��ȡ���������
	 * 
	 * @return
	 */
	public JScrollPane getPaintBoardScroll() {
		return paintBoardScroll;
	}

	/**
	 * ���ù��������
	 * 
	 * @param paintBoardScroll
	 */
	public void setPaintBoardScroll(PaintBoardScroll paintBoardScroll) {
		this.paintBoardScroll = paintBoardScroll;
	}

	/**
	 * �������̴�ͼ��
	 * 
	 * @param process
	 */
	public void openGraphs(WofoProcessBean process, boolean revalidate) {
		graphVector.clear();
		Map<String, Class> shapeWofoBeanConfig = new HashMap<String, Class>();
		Document shapeWofoBeanDoc = Functions.getXMLDocument(getClass()
				.getResource("/resources/shape_wofobeans.xml"));
		NodeList configList = shapeWofoBeanDoc.getDocumentElement()
				.getElementsByTagName("shape");
		for (int i = 0; i < configList.getLength(); i++) {
			Element e = (Element) configList.item(i);
			try {
				shapeWofoBeanConfig.put(e.getAttribute("id"), e.getAttribute(
						"generator").equals("") ? null : Class.forName(e
						.getAttribute("generator")));
			} catch (ClassNotFoundException e1) {
				e1.printStackTrace();
			}
		}
		// ���ö�ȡ�����������

		List<WofoBaseBean> graphBeans = process.getGraphs();
		if (graphBeans != null) {
			for (WofoBaseBean o : graphBeans) {
				Class genClass = shapeWofoBeanConfig
						.get(o.getClass().getName());
				if (genClass != null) {
					try {
						// ���������ļ�/resources/shape_wofobeans.xml����Ķ�Ӧ��ϵ����ָ����Generator���е���
						// generateShapeFromWofoBean��̬���������������ͳһ�̶��ģ����Ƽ��޸�
						Method m = genClass.getMethod(
								"generateShapeFromWofoBean", new Class[] {
										WofoBaseBean.class,
										PaintBoardBasic.class });
						AbstractShape s = (AbstractShape) m.invoke(null,
								new Object[] { o, this });
						this.addGraph(s);
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				} else {
					Logger.getLogger(PaintBoard.class.getName()).log(
							Level.WARNING,
							o.getClass() + "��δʵ��generateShapeFromWofoBean");
				}
			}
		}
		// ����ʱ�ͼ����С���Ա������������Ƿ���Ҫ��ʾ
		if (revalidate) {
			computePreferedSize();
		}
		repaint();
	}

	/**
	 * Զ�̴����̣��������̶��������ʵ��
	 * 
	 * @param processID
	 */
	public void openRemoteProcess(String processID) {
		// ͼ�ζ��������ý����������Ǹ������������Ǹ�ҵ�����
		Map<String, Class> shapeWofoBeanConfig = new HashMap<String, Class>();
		Document shapeWofoBeanDoc = Functions.getXMLDocument(getClass()
				.getResource("/resources/shape_wofobeans_basic.xml"));
		NodeList configList = shapeWofoBeanDoc.getDocumentElement()
				.getElementsByTagName("shape");
		for (int i = 0; i < configList.getLength(); i++) {
			Element e = (Element) configList.item(i);
			try {
				shapeWofoBeanConfig.put(e.getAttribute("id"), e.getAttribute(
						"generator").equals("") ? null : Class.forName(e
						.getAttribute("generator")));
			} catch (ClassNotFoundException e1) {
				e1.printStackTrace();
			}
		}

		// FIXME ������
		WofoProcessBean wpb = null;
		WofoNetBean netBean = new WofoNetBean();
		netBean.setActionName(WofoActions.LOAD_PROCESS);
		netBean.setParam(processID);
		wpb = (WofoProcessBean) Functions.getReturnNetBean(
				(String) this.getClientProperty("WOFO_SERVLETPATH"), netBean)
				.getParam();
		System.out.println(wpb);
		List graphBeans = wpb.getGraphs();
		if (graphBeans != null) {
			for (int i = 0; i < graphBeans.size(); i++) {
				Object o = graphBeans.get(i);
				if (o instanceof WofoBaseBean) {
					Class genClass = shapeWofoBeanConfig.get(o.getClass()
							.getName());
					if (genClass != null) {
						try {
							Method m = genClass.getMethod(
									"generateShapeFromWofoBeanBasic",
									new Class[] { WofoBaseBean.class,
											this.getClass() });
							AbstractShape s = (AbstractShape) m.invoke(null,
									new Object[] { o, this });
							addGraph(s);
						} catch (Exception e1) {
							e1.printStackTrace();
						}
					} else {
						Logger.getLogger(this.getClass().getName()).log(
								Level.WARNING,
								o.getClass() + "��δʵ��generateShapeFromWofoBean");
					}
				}
			}
		}
	}

	public boolean isHistoryBoard() {
		return historyBoard;
	}

	public void setHistoryBoard(boolean historyBoard) {
		this.historyBoard = historyBoard;
	}
}
