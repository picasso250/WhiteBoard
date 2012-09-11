package me.so_corp.graphics.xc.test;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.imageio.ImageIO;

public class MainRunner extends Frame implements KeyListener, MouseListener, ComponentListener, WindowListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5722200702606280288L;
	private static final int IMG_TYPE = BufferedImage.TYPE_INT_RGB;
	private static final String title = "白板-So.Me";
	private Point2D point = null;
	private Point2D prevPoint = null;
	private boolean drawing = false;
	private Color color = null;
	private BufferedImage bufferedImage = null; // double buffer

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		final MainRunner mr = new MainRunner(title);
		mr.setVisible(true);
		mr.toFront();
		mr.clearBackground();
		mr.drawIntroduction();
		class DrawThread extends Thread {
			public void run() {
				while (true) {
					mr.chalk();
					try {
						sleep(1);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}
		Thread thread = new DrawThread();
		thread.run();
	}

	private void drawIntroduction() {
		Graphics2D graphics = (Graphics2D) this.getGraphics();
		graphics.setColor(new Color(130, 130, 170));
		String intro = "左键画图，右键强调，空格键清除屏幕， S 键保存截图。";
		String by = "小池制作 So.Me";
		int y = 100;
		String fontName = "monospaced";
		graphics.setFont(new Font(fontName, Font.PLAIN, 22));
		graphics.drawString(intro, 100, y);
		graphics.setFont(new Font(fontName, Font.PLAIN, 14));
		graphics.drawString(by, 100, y+50);
	}

	public void chalk() {
		Point mousePosition = this.getMousePosition();
		this.prevPoint = this.point;
		if (mousePosition != null) {
			this.point = mousePosition;
		}
		if (drawing) {
			Graphics graphics = getGraphics();
			if (graphics != null) {
				if (point != null && prevPoint != null) {
					Graphics2D g = this.bufferedImage.createGraphics();
					g.setStroke(new BasicStroke(2.0f));
					g.setColor(this.color);
					g.draw(new Line2D.Double(point, prevPoint));
					graphics.drawImage(this.bufferedImage, 0, 0, this);
				}
			}
		}
	}

	private static Dimension getScreenSize() {
		Toolkit defaultToolkit = Toolkit.getDefaultToolkit();
		Dimension screenSize = defaultToolkit.getScreenSize();
		return screenSize;
	}

	public MainRunner(String title) {
		super(title);
		Dimension screenSize = getScreenSize();
		this.setSize((int)(screenSize.width * 0.8), (int)(screenSize.height * 0.8));
		this.addKeyListener(this);
		this.addMouseListener(this);
		this.addComponentListener(this);
		this.addWindowListener(this);
		bufferedImage  = new BufferedImage(this.getWidth(), this.getHeight(), MainRunner.IMG_TYPE);
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		int keyCode = e.getKeyCode();
		switch (keyCode) {
		case KeyEvent.VK_ESCAPE:
			// exit, but how to exit elegantly?
			exit();
			break;
		case KeyEvent.VK_SPACE:
			clearBackground();
			break;
		case KeyEvent.VK_S:
			saveSnapshot();
			break;
		default:
			break;
		}
	}

	private void exit() {
		this.dispose();
		System.exit(0);
	}

	private void saveSnapshot() {
		try {
			String fileExt = "png";
			Date date = new Date();
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日H时mm分ss秒");
			String fileName = dateFormat.format(date);
			File file = new File(fileName + "." + fileExt);
			ImageIO.write(this.bufferedImage, fileExt, file);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	private void clearBackground() {
		Graphics graphics = this.getGraphics();
		if (graphics != null) {
			Graphics2D g = this.bufferedImage.createGraphics();
			g.setBackground(Color.white);
			g.clearRect(0, 0, this.getWidth(), this.getHeight());
			graphics.drawImage(bufferedImage, 0, 0, this);
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {}

	@Override
	public void keyTyped(KeyEvent e) {}

	@Override
	public void mouseClicked(MouseEvent e) {}

	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {}

	@Override
	public void mousePressed(MouseEvent e) {
		drawing  = true;
		int button = e.getButton();
		switch (button) {
		case MouseEvent.BUTTON1:
			this.color = Color.black;
			break;
		case MouseEvent.BUTTON3:
			this.color = Color.red;
			break;
		default:
			break;
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		drawing = false;
	}

	@Override
	public void componentHidden(ComponentEvent e) {}

	@Override
	public void componentMoved(ComponentEvent e) {}

	@Override
	public void componentResized(ComponentEvent e) {
		// 太多执行次数，如何减少？
		BufferedImage oldImage = this.bufferedImage;
		this.bufferedImage = new BufferedImage(this.getWidth(), this.getHeight(), MainRunner.IMG_TYPE);
		this.clearBackground();
		Graphics2D graphics2d = this.bufferedImage.createGraphics();
		graphics2d.drawImage(oldImage, 0, 0, this); // copy old image
		updateImage();
	}

	private void updateImage() {
		Graphics graphics = this.getGraphics();
		if (graphics != null) {
			graphics.drawImage(bufferedImage, 0, 0, this);
		}
	}

	@Override
	public void componentShown(ComponentEvent e) {}

	@Override
	public void windowActivated(WindowEvent e) {}

	@Override
	public void windowClosed(WindowEvent e) {}

	@Override
	public void windowClosing(WindowEvent e) {
		exit();
	}

	@Override
	public void windowDeactivated(WindowEvent e) {}

	@Override
	public void windowDeiconified(WindowEvent e) {
		updateImage();
	}

	@Override
	public void windowIconified(WindowEvent e) {}

	@Override
	public void windowOpened(WindowEvent e) {}

}
