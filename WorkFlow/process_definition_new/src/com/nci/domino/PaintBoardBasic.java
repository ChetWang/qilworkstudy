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
	 * 分别是鼠标的落下xy屏幕坐标 移动或者抬起xy坐标
	 */
	protected double pxOld, pyOld, pxNew, pyNew;//
	/**
	 * 分别是鼠标的落下xy世界坐标 移动或者抬起xy坐标
	 */
	protected double wxOld, wyOld, wxNew, wyNew;//
	/**
	 * 分别是活动老的中心坐标x和y 老的平移x和y
	 */
	protected double xCentOld, yCentOld, txOld, tyOld;//
	/**
	 * 第一活动 第二活动和拖动活动
	 */
	protected AbstractShape draggingShape = null;

	/**
	 * 鼠标移动下鼠标位置所在的图形
	 */
	protected AbstractShape underMouseShape = null;

	protected boolean isCommanding = false;// 表示正在命令执行状态
	/**
	 * 代表坐标状态的六个参数 依次是缩放因子 平移x 平移y 转角 旋转中心x 旋转中心y
	 */
	protected double[] trans = { GlobalConstants.scaleValueOrigin, .0, .0, .0,
			.0, .0 };

	/**
	 * 锯齿消除对象
	 */
	protected RenderingHints rh = new RenderingHints(
			RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

	protected Dimension area = new Dimension(0, 0);

	/**
	 * 这个 tranRouter 哈希表是迁移线路由计数 记录某两个活动之间目前走了几趟 也就是画了几条迁移线 key的组成是小id 下划线 大id
	 * 比如 123_345 123和345是两个id 小的在前 大的在后
	 */
	protected HashMap<String, Integer> tranRouter = new HashMap<String, Integer>(); // @jve:decl-index=0:

	/**
	 * 以下这2个属性描述了完整的流程定义 流程属性
	 */
	protected WofoProcessBean processBean;
	/**
	 * 活动、迁移线、文字、泳道等图形的对象化
	 */
	protected List<AbstractShape> graphVector = new Vector<AbstractShape>();

	/**
	 * 按下的鼠标键值
	 */
	protected int pressedMouseButton = -1;

	/**
	 * 当前选中的图形
	 */
	protected List<AbstractShape> selectedShapes = new Vector<AbstractShape>();

	/**
	 * 面板图形选择事件集合
	 */
	protected List<PaintBoardShapeSelectionListener> boardShapeSelectionListeners = new ArrayList<PaintBoardShapeSelectionListener>();

	/**
	 * 绘制帮助线路的颜色
	 */
	protected static Color helpLineColor = new Color(180, 0, 0);

	/**
	 * 当前拖动的图形
	 */
	protected List<AbstractShape> ghostDraggedShapes = new Vector<AbstractShape>();

	/**
	 * 当前面板是否被编辑的标签
	 */
	protected boolean edited = false;

	/**
	 * 是否正在拖拽
	 */
	protected boolean dragging = false;

	/**
	 * 是否打开的是历史数据
	 */
	protected boolean historyBoard = false;

	/**
	 * 活动右上角图形配置文件
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
			// 鼠标滑动事件
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
	 * 鼠标松开后设置图形
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
	 * 鼠标移动
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
	 * 清除所有的选择标记
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
	 * 将图形设置为已选择状态,其余图形标记为未选中
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
	 * 将指定的图形设置为不选择状态
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
	 * 设置选择的图形
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
	 * 添加图形选择事件
	 * 
	 * @param l
	 */
	public void addPaintBoardShapeSelectionListener(
			PaintBoardShapeSelectionListener l) {
		this.boardShapeSelectionListeners.add(l);
	}

	/**
	 * 移除图形选择事件
	 * 
	 * @param l
	 *            图形选择事件
	 */
	public void removePaintBoardShapeSelectionListener(
			PaintBoardShapeSelectionListener l) {
		this.boardShapeSelectionListeners.remove(l);
	}

	/**
	 * 触发图形选择事件
	 */
	protected void firePaintBoardShapeSelectionListeners(
			List<AbstractShape> changedShapes) {
		for (PaintBoardShapeSelectionListener l : boardShapeSelectionListeners) {
			l.shapeSelectionStateChanged(selectedShapes, changedShapes);
		}
	}

	/**
	 * 给出一个点查找图形对象
	 * 
	 * @param xL
	 *            x坐标
	 * @param yL
	 *            y坐标
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
	 * 按照位置坐标查找活动
	 * 
	 * @param xL
	 *            x坐标
	 * @param yL
	 *            y坐标
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
	 * 回到原始的坐标位置
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
	 * 画整个图形 @param g Graphics实例
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
	 * 设置整个画布的一些属性
	 * 
	 * @param g2
	 *            Graphics2D实例
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
	 * 调整边界到合适的尺寸
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
	 * 缩放factor 鼠标落点不动
	 * 
	 * @param factor
	 *            放大系数
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
	 * 窗口缩放 高宽比例不协调的时候 以保证该窗口的内容全部显示为原则来调整 如果高宽比例等于画板的高宽比例 则正好显示到整个画板
	 * 
	 * @param px1
	 *            (px1,py1) 窗口左上角屏幕坐标
	 * @param py1
	 * @param px2
	 *            (px2,py2) 窗口右下角屏幕坐标
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
			JOptionPane.showMessageDialog(this, "缩放比例不能大于"
					+ GlobalConstants.MAX_SCALE);
			return;// 如果放大倍数太大 则退出 不执行本次操作
		} else if (scValue < 1. / GlobalConstants.MAX_SCALE) {
			JOptionPane.showMessageDialog(this, "缩放比例不能小于 " + 1.
					/ GlobalConstants.MAX_SCALE);
			return;// 如果放大倍数太小 则退出 不执行本次操作
		}
		trans[1] = -ret1[0];
		trans[2] = -ret1[1];
		trans[4] = ret1[0];
		trans[5] = ret1[1];
		trans[0] = scValue;
	}

	/**
	 * 重新计算面积，计算滚动条出现的必要性
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
				: (maxY - minY + 4) * trans[0]); // 这里加4是考虑图形顶端和左端可能会留空4个像素，美观上好看
		setPreferredSize(area);
		revalidate();
	}

	/**
	 * 获取操作模式
	 * 
	 * @return
	 */
	public int getToolMode() {
		return ToolMode.TOOL_VIEW;
	}

	/**
	 * 返回图形对象数组，该方法只适用迭代当前对象数组时使用。
	 * 
	 * <b>注意：新增或删除图形时不建议直接引用该对象，而应该使用addGraph和removeGraph</b>
	 * 
	 * @return 当前图形对象数组
	 */
	public List<AbstractShape> getGraphVector() {
		return graphVector;
	}

	/**
	 * 获取活动图展现形式，center表示和visio类似的文字在中间的图形，bottom表示文字在图形底部的图形
	 * 
	 * @return
	 */
	public String getActivityShapeStyle() {
		return "center";
	}

	/**
	 * 在某两个活动之间第一次走过 返回0 以后依次递增
	 * 
	 * @param oneline
	 *            WfTransition实例
	 * @return 途经次数
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
	 * 获取业务流程
	 * 
	 * @return
	 */
	public WofoProcessBean getProcessBean() {
		return processBean;
	}

	/**
	 * 设置业务流程
	 * 
	 * @param processBean
	 */
	public void setProcessBean(WofoProcessBean processBean) {
		setName(processBean.getProcessName());
		this.processBean = processBean;
	}

	/**
	 * 获取原先的PaintBoard x坐标
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
	 * 设置新的PaintBoard上的y坐标
	 * 
	 * @param pyNew
	 */
	public void setPyNew(double pyNew) {
		this.pyNew = pyNew;
	}

	/**
	 * 获取原先的世界x坐标
	 * 
	 * @return
	 */
	public double getWxOld() {
		return wxOld;
	}

	/**
	 * 设置原先的世界x坐标
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
	 * 得到变换参数数组 一共六个 依次是 缩放比例 平移x 平移y 旋转角弧度 旋转中心x 旋转中心y
	 * 
	 * @return
	 */
	public double[] getTransformParams() {
		return new double[] { trans[0], trans[1], trans[2], trans[3], trans[4],
				trans[5] };
	}

	/**
	 * 获取位移
	 * 
	 * @return
	 */
	public double[] getTrans() {
		return trans;
	}

	/**
	 * 获取当前选择的图形集合
	 * 
	 * @return
	 */
	public List<AbstractShape> getSelectedShapes() {
		return selectedShapes;
	}

	/**
	 * 是否正在拖动图形
	 * 
	 * @return
	 */
	public boolean isDragging() {
		return dragging;
	}

	/**
	 * 设置是否正在拖动妥协
	 * 
	 * @param dragging
	 */
	public void setDragging(boolean dragging) {
		this.dragging = dragging;
	}

	/**
	 * 获取拖动图形的拷贝集合
	 * 
	 * @return
	 */
	public List<AbstractShape> getGhostDraggedShapes() {
		return ghostDraggedShapes;
	}

	/**
	 * 根据业务id获取图形
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
	 * 新增图形，这里不能简单得往GraphVector中添加shape， 可能需要顺序上的要求，或单个shape可能会包含其它的图形对象
	 * 
	 * @param shape
	 */
	public void addGraph(AbstractShape shape) {
		shape.addShape(this);
	}

	/**
	 * 获取滚动父组件
	 * 
	 * @return
	 */
	public JScrollPane getPaintBoardScroll() {
		return paintBoardScroll;
	}

	/**
	 * 设置滚动父组件
	 * 
	 * @param paintBoardScroll
	 */
	public void setPaintBoardScroll(PaintBoardScroll paintBoardScroll) {
		this.paintBoardScroll = paintBoardScroll;
	}

	/**
	 * 根据流程打开图形
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
		// 配置读取结束，打开面板

		List<WofoBaseBean> graphBeans = process.getGraphs();
		if (graphBeans != null) {
			for (WofoBaseBean o : graphBeans) {
				Class genClass = shapeWofoBeanConfig
						.get(o.getClass().getName());
				if (genClass != null) {
					try {
						// 根据配置文件/resources/shape_wofobeans.xml定义的对应关系，从指定的Generator类中调用
						// generateShapeFromWofoBean静态方法，这个方法是统一固定的，不推荐修改
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
							o.getClass() + "还未实现generateShapeFromWofoBean");
				}
			}
		}
		// 开启时就计算大小，以便计算出滚动条是否需要显示
		if (revalidate) {
			computePreferedSize();
		}
		repaint();
	}

	/**
	 * 远程打开流程，包括流程定义和流程实例
	 * 
	 * @param processID
	 */
	public void openRemoteProcess(String processID) {
		// 图形对象定义配置解析，定义那个解析器对象那个业务对象
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

		// FIXME 打开流程
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
								o.getClass() + "还未实现generateShapeFromWofoBean");
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
