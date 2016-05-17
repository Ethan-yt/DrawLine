package J1030414414.GUI;

import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Line2D;
import java.util.Random;

import javax.swing.*;

import J1030414414.Function;

@SuppressWarnings("serial")
public class FunctionDialog extends JDialog {

	Function fun;
	Color color = Color.RED;
	float[] dashes = null;

	public FunctionDialog(JFrame owner, String title) {
		super(owner, title);
		initUI(owner);
	}

	public FunctionDialog(JFrame owner, String title, Function f) {
		super(owner, title);
		fun = f;
		initUI(owner);
	}

	private void initUI(JFrame owner) {
		setSize(400, 500);
		setModal(true);// ģ̬�Ի���
		setLocationRelativeTo(owner); // �������

		Container container = getContentPane();
		JPanel comp = new JPanel();
		container.add(comp);
		comp.setLayout(new BorderLayout(10, 10));
		JPanel content = new JPanel();
		content.setLayout(new BorderLayout(10, 10));

		comp.add(content, BorderLayout.CENTER);
		comp.add(new JPanel(), BorderLayout.NORTH);
		comp.add(new JPanel(), BorderLayout.SOUTH);
		comp.add(new JPanel(), BorderLayout.EAST);
		comp.add(new JPanel(), BorderLayout.WEST);
		Box box = Box.createVerticalBox();

		content.add(box, BorderLayout.NORTH);

		// -----------------��ʽ����--------------------------------
		JPanel styleSetting = new JPanel(new BorderLayout(10, 10));
		styleSetting.setBorder(BorderFactory.createTitledBorder("��ʽ"));
		JButton colorBtn = new JButton("��ɫ...");
		JLabel l = new JLabel("��ˢ��");
		JComboBox<String> dashesBox = new JComboBox<String>(new String[] { "", "10,3", "10,3,3,3" });
		dashesBox.setEditable(true);

		JPanel view = new JPanel() {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				double y = getHeight() / 2;
				Graphics2D g2d = (Graphics2D) g;
				g2d.setColor(color);
				g2d.setStroke(new BasicStroke(3, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND, 10, dashes, 0));
				g2d.draw(new Line2D.Double(30, y, this.getWidth() - 30, y));

			}
		};

		colorBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				Color c = JColorChooser.showDialog(null, "��ѡ����ϲ������ɫ", color);
				if (c != null)
					FunctionDialog.this.color = c;
				view.repaint();
			}
		});
		Component editorComponent = dashesBox.getEditor().getEditorComponent();
		dashesBox.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				updateDashes(dashesBox);
				view.repaint();
			}

		});
		editorComponent.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				updateDashes(dashesBox);
				view.repaint();
			}
		});

		editorComponent.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				updateDashes(dashesBox);
				view.repaint();
			}
		});

		view.setBackground(Color.WHITE);
		view.setPreferredSize(new Dimension(280, 50));

		styleSetting.add(l, BorderLayout.WEST);
		styleSetting.add(dashesBox, BorderLayout.CENTER);
		styleSetting.add(colorBtn, BorderLayout.EAST);
		styleSetting.add(view, BorderLayout.SOUTH);

		box.add(styleSetting);

		// -----------------�������ʽ--------------------------------
		JPanel expSetting = new JPanel(new GridLayout(1, 1));
		Box expBox = Box.createVerticalBox();
		expSetting.setBorder(BorderFactory.createTitledBorder("�������ʽ"));
		JTextField expText = new JTextField("");
		expBox.add(expText);
		expSetting.add(expBox);
		box.add(expSetting);
		// ------------------��ʽ˵��------------------------------------
		JPanel hint = new JPanel(new GridLayout(1, 1));
		hint.setBorder(BorderFactory.createTitledBorder("��ʽ˵��"));
		JTextArea hintText = new JTextArea("��ˢ��ʽ��" + "\r\n" + "[ʵ�߳���][,][���߳���]...������Ϊʵ��" + "\r\n\r\n" + "���ʽ��ʽ��"
				+ "\r\n" + "���ʽ����������ɣ���������[+]��[-]���ӣ��������־�Ϊ��������" + "\r\n\r\n" + "֧�ֵ��" + "\r\n" + "1)[ϵ��]x[ָ��]");
		hintText.setEditable(false);
		hintText.setLineWrap(true);
		hintText.setWrapStyleWord(true);

		JScrollPane scroll = new JScrollPane(hintText);
		scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

		hint.add(scroll);
		content.add(hint, BorderLayout.CENTER);
		// -----------------��ť--------------------------------
		JPanel btns = new JPanel(new GridLayout(1, 3, 10, 10));
		JButton okBtn = new JButton("ȷ��");
		JButton cancelBtn = new JButton("ȡ��");
		JButton randomBtn = new JButton("���");
		// ȡ��
		cancelBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				fun = null;
				FunctionDialog.this.setVisible(false);
			}
		});
		// ȷ��
		okBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				try {
					fun = new Function(expText.getText());
					fun.setColor(color);
					fun.setDashes(dashes);
					FunctionDialog.this.setVisible(false);
				} catch (Exception e1) {
					JOptionPane.showMessageDialog(null, "��ʽ��ʽ������ο���ʽ˵�������������");
				}
			}
		});

		randomBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				Random random = new Random();
				// ������ɱ�ˢ
				int r = random.nextInt(3) + 1;
				String str = String.valueOf((random.nextInt(10) + 1));
				for (int i = 0; i < r * 2 - 1; i++)
					str += "," + (random.nextInt(10) + 1);
				((JTextField) dashesBox.getEditor().getEditorComponent()).setText(str);
				try {
					dashes = parseDashes(str);
				} catch (Exception e1) {

				}
				// ���������ɫ
				color = new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255));
				// ������ɱ��ʽ
				int order = random.nextInt(4) + 1;
				int a[] = new int[order + 1];
				for (int i = 0; i < order; i++)
					a[i] = random.nextInt(20) - 10;
				do {
					a[order] = random.nextInt(20) - 10;
				} while (a[order] == 0);

				fun = new Function(order, a);
				expText.setText(fun.toString());
				view.repaint();
			}
		});

		btns.add(okBtn);
		btns.add(randomBtn);
		btns.add(cancelBtn);

		content.add(btns, BorderLayout.SOUTH);
		setResizable(false);

		if (fun != null)// ���뽫Ҫ�޸ĵĺ���
		{
			dashes = fun.getDashes();
			if (dashes != null && dashes.length > 0) {
				String str = String.valueOf((int) dashes[0]);
				for (int i = 1; i < dashes.length; i++)
					str += "," + (int) dashes[i];

				((JTextField) dashesBox.getEditor().getEditorComponent()).setText(str);
			}
			color = fun.getColor();
			expText.setText(fun.toString());
			view.repaint();
		}
	}

	private float[] parseDashes(String str) throws Exception {
		if (str.equals(""))
			return null;
		String[] strs = str.split(",");
		float[] ret = new float[strs.length];
		for (int i = 0; i < strs.length; i++) {
			ret[i] = Float.parseFloat(strs[i]);
			if (ret[i] == 0)
				throw new Exception("��ˢ��ʽ����");
		}
		return ret;
	}

	private void updateDashes(JComboBox<String> dashesBox) {
		try {
			dashes = parseDashes((String) dashesBox.getSelectedItem());
		} catch (Exception e1) {
			JOptionPane.showMessageDialog(null, e1.getMessage());
		}
	}
}
