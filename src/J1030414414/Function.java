package J1030414414;

import java.awt.Color;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressWarnings("serial")
public class Function implements Serializable {

	private int a[];
	private String str = "";
	private float dashes[];
	private Color color = Color.RED;

	public int getA(int order) {
		return a[order];
	}

	public void setA(int order, int x) throws Exception {
		if (order > this.order)
			return;
		if (order == this.order && x == 0)
			throw new Exception("最高项系数不可以为0");
		str = "";
		this.a[order] = x;

	}

	private int order;

	public int getOrder() {
		return order;
	}

	public Function(int order, int a[]) {
		this.order = order;
		this.a = a;
	}

	public double getValue(double x) {
		double ret = 0;
		for (int i = 0; i <= order; i++) {
			double subret = a[i];
			for (int j = 0; j < i; j++) {
				subret *= x;
			}
			ret += subret;
		}
		return ret;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public float[] getDashes() {
		return dashes;
	}

	public void setDashes(float dashes[]) {
		this.dashes = dashes;
	}

	public String toString() {
		if (!str.equals(""))
			return str;
		for (int i = getOrder(); i >= 0; i--) {

			if (getA(i) != 0) {
				if (i != getOrder() && getA(i) > 0)
					str += "+";
				if (i == 1)
					if (getA(i) == 1)
						str += "x";
					else
						str += getA(i) + "x";
				else if (i == 0)
					str += getA(i);
				else if (getA(i) == 1)
					str += "x" + i;
				else
					str += getA(i) + "x" + i;
			}
		}
		return str;
	}

	public Function(String exp) {
		Function f = parseFun(exp);
		this.order = f.order;
		this.a = f.a;
	}

	public static Function parseFun(String exp) {
		if (exp.charAt(0) != '-' || exp.charAt(0) != '+')// 为第一项添加+号
			exp = "+" + exp;
		Pattern p = Pattern.compile("((-|\\+)\\d*)(x?)(\\d*)");
		Matcher m = p.matcher(exp);
		ArrayList<Integer> ratios = new ArrayList<Integer>();
		ArrayList<Integer> indices = new ArrayList<Integer>();
		int order = 0;
		while (m.find()) {
			int ratio;
			if (m.group(1).equals("+"))
				ratio = 1;
			else if (m.group(1).equals("-"))
				ratio = -1;
			else
				ratio = Integer.parseInt(m.group(1));// ((-|\\+)\\d*)
			ratios.add(ratio);

			int index;
			if (!m.group(3).equals("x"))// (x)?
				index = 0;
			else {
				if (m.group(4).equals(""))// (\\d*)
					index = 1;
				else
					index = Integer.parseInt(m.group(4));// (\\d*)
			}
			indices.add(index);
			if (index > order)
				order = index;
		}
		int a[] = new int[order + 1];
		for (int i = 0; i < ratios.size(); i++) {
			int ratio = ratios.get(i);
			int index = indices.get(i);
			a[index] = ratio;
		}
		return new Function(order, a);

	}
}
