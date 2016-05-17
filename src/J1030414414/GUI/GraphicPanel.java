package J1030414414.GUI;

import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.ComponentAdapter;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import javax.swing.JPanel;

import J1030414414.Function;
import J1030414414.Utils;
import J1030414414.Utils.OriginPoint;

@SuppressWarnings("serial")
public class GraphicPanel extends JPanel {

	public OriginPoint o = new OriginPoint();// 保存坐标原点以便转换
	public ArrayList<Function> fun = new ArrayList<>();

	private boolean isSignVisible = true;
	private boolean isReferenceLineVisible = true;
	private boolean isMouseLocVisible = true;
	private Point2D mouseLoc = new Point2D.Double();

	private double dpiX, dpiY, // xy轴单位坐标所占像素
			minDivX, minDivY;// xy轴的最小分度值

	private Rectangle signsRect = new Rectangle(600, 500, 0, 0);

	public void setUnitLenX(double unitLenX) {
		this.dpiX = unitLenX;
	}

	public void setUnitLenY(double unitLenY) {
		this.dpiY = unitLenY;
	}

	public void setMinDivX(double minDivX) {
		this.minDivX = minDivX;
	}

	public void setMinDivY(double minDivY) {
		this.minDivY = minDivY;
	}

	public GraphicPanel() {

		// -------------------自动设置原点居中------------------------------
		this.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent arg0) {
				int x = arg0.getComponent().getWidth();
				int y = arg0.getComponent().getHeight();
				o.setLocation(x / 2, y / 2);
				repaint();
			}
		});

		// ------------------鼠标拖拽图例-------------------------
		final Point2D mouseDownRelativeLoc = new Point2D.Double();
		this.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if (isSignVisible) {
					int x = e.getX();
					int y = e.getY();
					if (x >= signsRect.getX() && x <= signsRect.getX() + signsRect.getWidth() && y >= signsRect.getY()
							&& y <= signsRect.getY() + signsRect.getHeight()) {
						double dx = x - signsRect.getX();
						double dy = y - signsRect.getY();
						mouseDownRelativeLoc.setLocation(dx, dy);
					}
				}

			}

			@Override
			public void mouseReleased(MouseEvent e) {
				mouseDownRelativeLoc.setLocation(-1, -1);
			}

		});

		this.addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseMoved(MouseEvent e) {
				// 鼠标提示
				if (isMouseLocVisible) {
					mouseLoc = e.getPoint();
					GraphicPanel.this.repaint();
				}

			}

			@Override
			public void mouseDragged(MouseEvent e) {
				// 拖动图例
				int x = e.getX();
				int y = e.getY();
				if (mouseDownRelativeLoc.getX() >= 0) {
					int dx = (int) mouseDownRelativeLoc.getX();
					int dy = (int) mouseDownRelativeLoc.getY();
					signsRect.setLocation(x - dx, y - dy);
					GraphicPanel.this.repaint();
				}
			}

		});
		setBackground(Color.WHITE);
	}

	@Override
	protected void paintComponent(Graphics g) {

		super.paintComponent(g);

		Graphics2D g2d = (Graphics2D) g;

		// 轴长
		double axisLenX = this.getWidth() / 2 - 100, axisLenY = this.getHeight() / 2 - 100;

		// 绘制坐标轴
		double[] maxScale = paintAxis(g2d, axisLenX, axisLenY);
		int width = 0;
		for (int i = 0; i < fun.size(); i++) {
			Function f = fun.get(i);
			// 获取表达式
			String expression = "y=" + f.toString();
			// 绘制曲线
			paintLine(g2d, f, maxScale[0], maxScale[1]);
			// 绘制图例
			if (isSignVisible)
				paintSigns(g2d, expression, i);
			// 获取最大图例宽度以便确定图例边框大小
			int thisWidth = g2d.getFontMetrics().stringWidth(expression);
			if (thisWidth > width)
				width = thisWidth;
		}
		// 绘制图例边框
		if (isSignVisible && fun.size() != 0) {
			signsRect.setSize(85 + width, 30 * fun.size() + 10);
			g2d.setStroke(new BasicStroke(2));
			g2d.setPaint(Color.BLACK);
			g2d.draw(signsRect);
		}
		// 绘制鼠标提示
		if (isMouseLocVisible) {
			Point2D p = o.r(mouseLoc.getX(), mouseLoc.getY());
			String str = "(" + String.format("%.2f", p.getX() / dpiX) + "," + String.format("%.2f", p.getY() / dpiY)
					+ ")";
			g2d.setPaint(Color.BLACK);
			g2d.setFont(new Font("楷体", Font.PLAIN, 16));
			g2d.drawString(str, (int) mouseLoc.getX(), (int) mouseLoc.getY());

		}
	}

	// 绘制坐标轴，返回{x最大刻度,y最大刻度}
	private double[] paintAxis(Graphics2D g2d, double Xlen, double Ylen) {
		// 宽度为2的黑色坐标轴
		g2d.setPaint(Color.BLACK);
		g2d.setStroke(new BasicStroke(2));
		g2d.draw(new Line2D.Double(o.c(-Xlen, 0), o.c(Xlen, 0)));
		g2d.draw(new Line2D.Double(o.c(0, -Ylen), o.c(0, Ylen)));
		// 坐标轴上的黑色箭头
		GeneralPath arrowhead1 = new GeneralPath();
		Point2D.Double[] p1 = new Point2D.Double[4];
		p1[0] = o.c(Xlen - 15, 0);
		p1[1] = o.c(Xlen - 20, 7);
		p1[2] = o.c(Xlen + 5, 0);
		p1[3] = o.c(Xlen - 20, -7);
		arrowhead1.moveTo(p1[0].getX(), p1[0].getY());
		for (Point2D.Double p : p1) {
			arrowhead1.lineTo(p.getX(), p.getY());
		}
		arrowhead1.closePath();
		g2d.fill(arrowhead1);

		GeneralPath arrowhead2 = new GeneralPath();
		Point2D.Double[] p2 = new Point2D.Double[4];
		p2[0] = o.c(0, Ylen - 15);
		p2[1] = o.c(-7, Ylen - 20);
		p2[2] = o.c(0, Ylen + 5);
		p2[3] = o.c(7, Ylen - 20);
		arrowhead2.moveTo(p2[0].getX(), p2[0].getY());
		for (Point2D.Double p : p2) {
			arrowhead2.lineTo(p.getX(), p.getY());
		}
		arrowhead2.closePath();
		g2d.fill(arrowhead2);
		// 绘制刻度
		// 绘制原点
		Point2D p3 = o.c(-12, -15);
		g2d.setFont(new Font("楷体", Font.BOLD, 18));
		g2d.drawString("O", (float) p3.getX(), (float) p3.getY());
		// 绘制横向
		double d, count;
		double[] ret = new double[2];
		for (d = minDivX * dpiX, count = minDivX; d < Xlen - 30; count = Utils.add(count, minDivX), d += minDivX
				* dpiX) {
			for (int i = 1; i >= -1; i -= 2)// i为正负
			{
				g2d.setPaint(Color.BLACK);
				g2d.draw(new Line2D.Double(o.c(i * d, 0), o.c(i * d, 5)));
				Point2D p4 = o.c(i * d, -15);
				String str = Utils.DoubleToString(i * count);
				int strWidth = g2d.getFontMetrics().stringWidth(str);
				g2d.drawString(str, (float) p4.getX() - strWidth / 2, (float) p4.getY());

				// 参考线
				if (isReferenceLineVisible) {
					g2d.setPaint(Color.gray);
					g2d.setStroke(new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND, 10,
							new float[] { 3, 3 }, 0));
					g2d.draw(new Line2D.Double(o.c(i * d, -Ylen), o.c(i * d, Ylen)));

				}

			}
		}
		ret[0] = count - minDivX;
		// 绘制纵向
		for (d = minDivY * dpiY, count = minDivY; d < Ylen - 30; count = Utils.add(count, minDivY), d += minDivY
				* dpiY) {
			for (int i = 1; i >= -1; i -= 2)// i为正负
			{
				g2d.setPaint(Color.BLACK);
				g2d.draw(new Line2D.Double(o.c(0, i * d), o.c(5, i * d)));
				Point2D p4 = o.c(0, i * d);
				String str = Utils.DoubleToString(i * count);
				int strWidth = g2d.getFontMetrics().stringWidth(str);
				g2d.drawString(str, (float) p4.getX() - strWidth - 3, (float) p4.getY() + 7);
				// 参考线
				if (isReferenceLineVisible) {
					g2d.setPaint(Color.gray);
					g2d.setStroke(new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND, 10,
							new float[] { 3, 3 }, 0));
					g2d.draw(new Line2D.Double(o.c(-Xlen, i * d), o.c(Xlen, i * d)));
				}

			}
		}
		ret[1] = count - minDivY;
		return ret;
	}

	private void paintLine(Graphics2D g2d, Function fun, double maxScaleX, double maxScaleY) {
		g2d.setColor(fun.getColor());
		g2d.setStroke(new BasicStroke(3, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND, 10, fun.getDashes(), 0));
		final double unit = minDivX / 50;// 微分长度
		maxScaleX += minDivX;
		maxScaleY += minDivY;
		double x = -maxScaleX;
		double y;
		while (x < maxScaleX) {
			y = fun.getValue(x);
			GeneralPath path = new GeneralPath();
			if (Math.abs(y) < maxScaleY) {
				Point2D p = o.c(x * dpiX, y * dpiY);
				path.moveTo(p.getX(), p.getY());
				for (; Math.abs(y) < maxScaleY && x < maxScaleX; x += unit, y = fun.getValue(x)) {
					p = o.c(x * dpiX, y * dpiY);
					path.lineTo(p.getX(), p.getY());
				}
				// path.closePath();
				g2d.draw(path);
			}

			x += unit;
		}

	}

	private void paintSigns(Graphics2D g2d, String expression, int index) {
		// 绘制图例
		double y = signsRect.getY() + 20 + 30 * index;
		g2d.draw(new Line2D.Double(signsRect.getX() + 10, y, signsRect.getX() + 40, y));
		g2d.drawString(expression, (int) signsRect.getX() + 60, (int) y + 5);
	}

	public boolean isSignVisible() {
		return isSignVisible;
	}

	public void setSignVisible(boolean isSignVisible) {
		this.isSignVisible = isSignVisible;
		repaint();
	}

	public boolean isReferenceLineVisible() {
		return isReferenceLineVisible;
	}

	public void setReferenceLineVisible(boolean isReferenceLineVisible) {
		this.isReferenceLineVisible = isReferenceLineVisible;
		repaint();
	}

	public boolean isMouseLocVisible() {
		return isMouseLocVisible;
	}

	public void setMouseLocVisible(boolean isMouseLocVisible) {
		this.isMouseLocVisible = isMouseLocVisible;
		repaint();
	}

}
