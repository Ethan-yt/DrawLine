package J1030414414;

import java.awt.geom.Point2D;
import java.math.BigDecimal;

public class Utils {

	public Utils() {
		// TODO 自动生成的构造函数存根
	}

	public static String DoubleToString(Double d) {
		String s = Double.toString(d);
		if (s.indexOf(".") > 0) {
			s = s.replaceAll("0+?$", "");// 去掉多余的0
			s = s.replaceAll("[.]$", "");// 如最后一位是.则去掉
		}
		return s;
	}

	public static double mul(double lhs, double rhs) {
		BigDecimal b1 = new BigDecimal(Double.toString(lhs));
		BigDecimal b2 = new BigDecimal(Double.toString(rhs));
		return b1.multiply(b2).doubleValue();
	}

	public static double add(double lhs, double rhs) {
		BigDecimal b1 = new BigDecimal(Double.toString(lhs));
		BigDecimal b2 = new BigDecimal(Double.toString(rhs));
		return b1.add(b2).doubleValue();
	}

	@SuppressWarnings("serial")
	public static class OriginPoint extends Point2D.Double {
		/*
		 * 变换坐标
		 */
		// 由绘图坐标系变换到系统坐标系
		public Point2D.Double c(double x, double y) {
			return new Point2D.Double(getX() + x, getY() - y);
		}

		// 由系统坐标系变换到绘图坐标系
		public Point2D.Double r(double x, double y) {
			return new Point2D.Double(x - getX(), getY() - y);
		}
	}
}
