package J1030414414;

import java.awt.geom.Point2D;
import java.math.BigDecimal;

public class Utils {

	public Utils() {
		// TODO �Զ����ɵĹ��캯�����
	}

	public static String DoubleToString(Double d) {
		String s = Double.toString(d);
		if (s.indexOf(".") > 0) {
			s = s.replaceAll("0+?$", "");// ȥ�������0
			s = s.replaceAll("[.]$", "");// �����һλ��.��ȥ��
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
		 * �任����
		 */
		// �ɻ�ͼ����ϵ�任��ϵͳ����ϵ
		public Point2D.Double c(double x, double y) {
			return new Point2D.Double(getX() + x, getY() - y);
		}

		// ��ϵͳ����ϵ�任����ͼ����ϵ
		public Point2D.Double r(double x, double y) {
			return new Point2D.Double(x - getX(), getY() - y);
		}
	}
}
