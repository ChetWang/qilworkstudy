package fr.itris.glips.svgeditor.io.managers.export.image;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.apache.batik.bridge.BridgeContext;
import org.apache.batik.bridge.DocumentLoader;
import org.apache.batik.bridge.GVTBuilder;
import org.apache.batik.bridge.UserAgentAdapter;
import org.apache.batik.gvt.GraphicsNode;
import org.apache.batik.gvt.RootGraphicsNode;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.nci.svg.sdk.client.EditorAdapter;
import com.nci.svg.sdk.client.SysSetDefines;
import com.nci.svg.sdk.client.util.EditorToolkit;

import fr.itris.glips.library.monitor.Monitor;

/**
 * the class used to generate the image of a svg file
 * 
 * @author ITRIS, Jordi SUC
 */
public class SVGDocumentImageCreator {

	/**
	 * the monitor
	 */
	private Monitor monitor;

	/**
	 * the document of the svg file
	 */
	private Document document;

	/**
	 * the user agent
	 */
	private UserAgentAdapter userAgent;

	/**
	 * the builder
	 */
	private GVTBuilder builder;

	/**
	 * the bridge context
	 */
	private BridgeContext ctx;

	/**
	 * the root graphics node
	 */
	private RootGraphicsNode gvtRoot;

	/**
	 * the image
	 */
	private BufferedImage image = null;

	/**
	 * the listeners the this image creator
	 */
	private Set<SVGDocumentImageCreatorListener> listeners = Collections
			.synchronizedSet(new HashSet<SVGDocumentImageCreatorListener>());
	
	private EditorAdapter editor;

	/**
	 * the constructor of the class
	 * 
	 * @param doc
	 *            the document of a svg file
	 * @param monitor
	 *            the monitor
	 */
	public SVGDocumentImageCreator(Document doc, Monitor monitor,EditorAdapter editor) {

		this.document = doc;
		this.monitor = monitor;
		this.editor = editor;
	}

	/**
	 * creates the image at the given dimension
	 * 
	 * @param size
	 *            the dimension of the created image
	 * @param encodeAlpha
	 *            whether the image should use a alpha channel
	 */
	public void createImage(final Dimension size, final boolean encodeAlpha) {

		// 偷懒的做法，其实这个标记要执行的内容跟encodeAlpha无关
//		if (Editor.getEditor().getSelectionManager().getSelectionMode() == SelectionInfoManager.SCREEN_CAST_MODE) {
//			final Graphics gCanvas = Editor.getEditor().getHandlesManager().getCurrentHandle().getCanvas().getGraphics().create();
//			
////			g2.fillRect(350, 300, image.getWidth(), image.getHeight());
////			Graphics g = Editor.getEditor().getHandlesManager()
////					.getCurrentHandle().getCanvas().getGraphics().create(350,
////							300, image.getWidth(), image.getHeight());
//			JFrame frame = new JFrame();
////			g.draw3DRect(20, 20, 100, 150, true);
//			JLabel label = new JLabel(){
//				@Override
//				public void paintComponent(Graphics g){
//					g = gCanvas.create();
//					super.paint(g);
//				}
//			};
			
//			frame.getContentPane().add(label);
//			frame.setSize(800, 600);
//			frame.setVisible(true);
//		} else {
			try {
				image = new BufferedImage(size.width, size.height,
						encodeAlpha ? BufferedImage.TYPE_INT_ARGB
								: BufferedImage.TYPE_INT_RGB);
				// creating the graphics node
				userAgent = new UserAgentAdapter();
				ctx = new BridgeContext(userAgent, null, new DocumentLoader(
						userAgent));
				builder = new GVTBuilder();
				ctx.setDynamicState(BridgeContext.STATIC);
				if (monitor != null) {
					monitor.setProgress(5);

					if (monitor.isCancelled()) {

						monitor.stop();
						ctx.dispose();
						return;
					}
				}
				GraphicsNode gvt = null;
				synchronized (editor.getSvgSession().lock) {
					gvt = builder.build(ctx, document);
				}
				if (monitor != null) {
					monitor.setProgress(40);

					if (monitor.isCancelled()) {

						monitor.stop();
						ctx.dispose();
						return;
					}
				}

				if (gvt != null) {

					gvtRoot = gvt.getRoot();

				}
				// getting the size of the canvas
				Point2D canvasSize = getGeometryCanvasSize(document);

				// computing the scale transformation for the svg doc
				// content to fit the created image size
				AffineTransform af = AffineTransform.getScaleInstance(
						size.width / canvasSize.getX(), size.height
								/ canvasSize.getY());

				// getting the graphics object of the image
				Graphics2D g2 = image.createGraphics();
				// 背景色的处理
				if ((Boolean)editor.getGCParam(SysSetDefines.EXPORT_INCLUDE_BACKGROUND)) {
					Color bg = editor.getHandlesManager()
							.getCurrentHandle().getCanvas().getBackground();
					g2.setColor(bg);
					// g2.setColor(Color.red);
				}
				if (!encodeAlpha) {

					g2.fillRect(0, 0, image.getWidth(), image.getHeight());

				}

				handleGraphicsConfiguration(g2);
				g2.setTransform(af);
				// painting the image
				gvtRoot.paint(g2);

				g2.dispose();
				if (monitor != null)
					monitor.setProgress(50);

				// notifies the listeners that the image has been created
				for (SVGDocumentImageCreatorListener listener : new HashSet<SVGDocumentImageCreatorListener>(
						listeners)) {

					listener.imageCreated(image);
				}

				listeners.clear();
				// ctx.dispose();
				// //这代码“ctx.dispose();”原先是存在的，但是执行这段代码后，再往图上添加图元就无法正常显示。

			} catch (Exception ex) {
				ex.printStackTrace();
				if (monitor != null) {
					monitor.stop();
					monitor.dispose();
				}
				listeners.clear();
				// ctx.dispose();
			}
//		}
	}

	/**
	 * computes and returns the canvas' size of the given svg document
	 * 
	 * @param document
	 *            a svg document
	 * @return the canvas' size of the given svg document
	 */
	public static Point2D getGeometryCanvasSize(Document document) {

		// gets the root element
		if (document != null) {

			Element root = document.getDocumentElement();

			if (root != null) {

				double w = EditorToolkit.getPixelledNumber(root.getAttributeNS(
						null, "width"));
				double h = EditorToolkit.getPixelledNumber(root.getAttributeNS(
						null, "height"));

				return new Point2D.Double(w, h);
			}
		}

		return new Point2D.Double(0, 0);
	}

	/**
	 * adds a new svg document image creator listener to the list of the
	 * listeners
	 * 
	 * @param listener
	 *            a listener
	 */
	public void addImageCreatorListener(SVGDocumentImageCreatorListener listener) {

		listeners.add(listener);
	}

	/**
	 * @return the created image
	 */
	public BufferedImage getImage() {
		return image;
	}

	/**
	 * sets the parameters of the given graphics object
	 * 
	 * @param g2
	 *            a graphics object
	 */
	public static void handleGraphicsConfiguration(Graphics2D g2) {

		// setting the rendering hints
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
				RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		g2.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION,
				RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
		g2.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING,
				RenderingHints.VALUE_COLOR_RENDER_QUALITY);
		g2.setRenderingHint(RenderingHints.KEY_DITHERING,
				RenderingHints.VALUE_DITHER_DISABLE);
		g2.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS,
				RenderingHints.VALUE_FRACTIONALMETRICS_ON);
		g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
				RenderingHints.VALUE_INTERPOLATION_BICUBIC);
		g2.setRenderingHint(RenderingHints.KEY_RENDERING,
				RenderingHints.VALUE_RENDER_QUALITY);
		g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL,
				RenderingHints.VALUE_STROKE_NORMALIZE);
	}
}
