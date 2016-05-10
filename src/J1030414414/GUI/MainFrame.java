package J1030414414.GUI;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import J1030414414.Function;

@SuppressWarnings("serial")
public class MainFrame extends JFrame {

	private GraphicPanel myPanel = new GraphicPanel();

	private MainFrameSettings settingPanel = new MainFrameSettings(myPanel);
	
	public MainFrame() {
		super("���߻���");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		Container container = getContentPane();

		// ���ò���
		container.setLayout(new BorderLayout());
		container.add(myPanel, BorderLayout.CENTER);
		container.add(settingPanel, BorderLayout.EAST);

		// ʵ���������˵���
		JMenuBar bar = new JMenuBar();
		JMenu fileMenu = new JMenu("�ļ�(F)");
		fileMenu.setMnemonic('F');

		JMenuItem file_new = new JMenuItem("�½�(N)");
		file_new.setMnemonic('N');
		fileMenu.add(file_new);

		JMenuItem file_open = new JMenuItem("��(O)...");
		file_open.setMnemonic('O');
		fileMenu.add(file_open);

		JMenuItem file_save = new JMenuItem("����(S)...");
		file_save.setMnemonic('S');
		fileMenu.add(file_save);
		fileMenu.addSeparator();

		JMenuItem file_export = new JMenuItem("����ͼƬ(E)...");
		file_export.setMnemonic('E');
		fileMenu.add(file_export);

		file_export.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser fc = new JFileChooser("./");
				fc.setSelectedFile(new File("δ����.jpg"));
				fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
				fc.setFileFilter(new FileNameExtensionFilter("JPGͼƬ(*.jpg)", "jpg"));
				if (fc.showSaveDialog(MainFrame.this) == JFileChooser.APPROVE_OPTION) {
					String fname = null;
					File file = fc.getSelectedFile();
					fname = file.getAbsolutePath();
					if (fname != null && fname.trim().length() > 0) {
						if (!fname.endsWith(".jpg"))
							fname += ".jpg";
						file = new File(fname);
						if (file.exists()) {
							int i = JOptionPane.showConfirmDialog(MainFrame.this, "��ͼƬ�Ѿ����ڣ�ȷ��Ҫ������", "��ʾ",
									JOptionPane.YES_NO_OPTION);
							if (i != javax.swing.JOptionPane.YES_OPTION)
								return;
						}
						try {
							Dimension size = myPanel.getSize();
							BufferedImage image = new BufferedImage(size.width, size.height,
									BufferedImage.TYPE_INT_RGB);
							Graphics g = image.getGraphics();
							myPanel.paint(g);
							try {
								ImageIO.write(image, "jpg", file);
								JOptionPane.showMessageDialog(null, "ͼƬ����ɹ�", "����ɹ�", JOptionPane.INFORMATION_MESSAGE);
							} catch (IOException e1) {
								JOptionPane.showMessageDialog(MainFrame.this, "����ͼƬ����", "����",
										JOptionPane.ERROR_MESSAGE);
							}
							image.flush();
							g.dispose();
						} catch (Exception ex) {
							JOptionPane.showMessageDialog(MainFrame.this, "����" + ex.getMessage());
						}
					}

				}

			}
		});
		fileMenu.addSeparator();

		JMenuItem file_exit = new JMenuItem("�˳�(X)");
		file_exit.setMnemonic('X');
		fileMenu.add(file_exit);

		bar.add(fileMenu);

		JMenu editMenu = new JMenu("�༭(E)");
		editMenu.setMnemonic('E');

		JMenuItem edit_insert = new JMenuItem("���뺯��(I)...");
		edit_insert.setMnemonic('I');
		editMenu.add(edit_insert);

		JMenu edit_edit = new JMenu("�޸ĺ���(E)");
		edit_edit.setMnemonic('E');
		editMenu.add(edit_edit);

		JMenu edit_delete = new JMenu("ɾ������(D)");
		edit_delete.setMnemonic('D');
		editMenu.add(edit_delete);
		// map���ڽ��˵��ͺ�����������
		Map<JMenuItem, Function> map = new HashMap<JMenuItem, Function>();
		// ----------------------------���뺯��--------------------------------
		edit_insert.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				FunctionDialog functionDialog = new FunctionDialog(MainFrame.this, "���뺯��");
				functionDialog.setVisible(true);
				if (functionDialog.fun != null) {
					// ���뺯��
					myPanel.fun.add(functionDialog.fun);
					myPanel.repaint();
					addFunMenu(edit_edit, edit_delete, map, functionDialog.fun);
				}
			}
		});
		// -----------------------------�½�-------------------------------------
		file_new.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				myPanel.fun.clear();
				edit_edit.removeAll();
				edit_delete.removeAll();
				map.clear();
				myPanel.repaint();
			}
		});
		// ------------------------���ļ�---------------------------
		file_open.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser fc = new JFileChooser("./");
				fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
				fc.setFileFilter(new FileNameExtensionFilter("�����ļ�(*.fun)", "fun"));
				if (fc.showOpenDialog(MainFrame.this) == JFileChooser.APPROVE_OPTION) {
					File file = fc.getSelectedFile();
					try {
						FileInputStream fs = new FileInputStream(file);
						ObjectInputStream in = new ObjectInputStream(fs);

						if (!myPanel.fun.isEmpty()) {
							int i = JOptionPane.showConfirmDialog(MainFrame.this, "ȷ��������ǰ�Ĺ�����", "��ʾ",
									JOptionPane.YES_NO_OPTION);
							if (i != javax.swing.JOptionPane.YES_OPTION)
								return;
							// ��յ�ǰ��
							myPanel.fun.clear();
							edit_edit.removeAll();
							edit_delete.removeAll();
							map.clear();
							myPanel.repaint();
						}

						myPanel.fun = (ArrayList<Function>) in.readObject();
						for (Function fun : myPanel.fun)
							addFunMenu(edit_edit, edit_delete, map, fun);

						myPanel.repaint();
						in.close();
					} catch (Exception ex) {
						JOptionPane.showMessageDialog(MainFrame.this, "����" + ex.getMessage());
					}
				}
			}
		});

		// ------------------------�����ļ�---------------------------
		file_save.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser fc = new JFileChooser("./");
				fc.setSelectedFile(new File("δ����.fun"));
				fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
				fc.setFileFilter(new FileNameExtensionFilter("�����ļ�(*.fun)", "fun"));
				if (fc.showSaveDialog(MainFrame.this) == JFileChooser.APPROVE_OPTION) {
					String fname = null;
					File file = fc.getSelectedFile();
					fname = file.getAbsolutePath();
					if (fname != null && fname.trim().length() > 0) {
						if (!fname.endsWith(".fun"))
							fname += ".fun";
						file = new File(fname);
						if (file.exists()) {
							int i = JOptionPane.showConfirmDialog(MainFrame.this, "���ļ��Ѿ����ڣ�ȷ��Ҫ������", "��ʾ",
									JOptionPane.YES_NO_OPTION);
							if (i != javax.swing.JOptionPane.YES_OPTION)
								return;
						}
						try {
							FileOutputStream fs = new FileOutputStream(fname);
							ObjectOutputStream os = new ObjectOutputStream(fs);
							os.writeObject(myPanel.fun);
							os.close();
						} catch (Exception ex) {
							JOptionPane.showMessageDialog(MainFrame.this, "����" + ex.getMessage());
						}
					}

				}
			}
		});

		bar.add(editMenu);

		JMenu viewMenu = new JMenu("��ͼ(V)");
		viewMenu.setMnemonic('V');

		JCheckBoxMenuItem view_Settings = new JCheckBoxMenuItem("����������(A)");
		view_Settings.setMnemonic('A');
		viewMenu.add(view_Settings);

		JCheckBoxMenuItem view_Signs = new JCheckBoxMenuItem("ͼ��(S)");
		view_Signs.setMnemonic('S');
		viewMenu.add(view_Signs);

		bar.add(viewMenu);

		JMenu helpMenu = new JMenu("����(H)");
		helpMenu.setMnemonic('H');

		JMenuItem help_about = new JMenuItem("����(A)...");
		help_about.setMnemonic('A');
		helpMenu.add(help_about);

		bar.add(helpMenu);

		this.setJMenuBar(bar);

		


		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				MainFrame.this.requestFocus();
			}
		});
		
		myPanel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				MainFrame.this.requestFocus();
			}
		});
		
		addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent arg0) {
				switch (arg0.getKeyCode()) {
				case KeyEvent.VK_UP:
					
					break;
				default:
					System.out.println(arg0.getKeyCode());
					break;
				}
			}

			@Override
			public void keyReleased(KeyEvent arg0) {
				double v = settingPanel.settings.get(3).getCurrentValue();
				settingPanel.settings.get(3).setCurrentValue(v+2, Source.Key, true);
			}

		});

		setSize(1200, 800);// ��ʼ��С
		this.setMinimumSize(new Dimension(1200, 800));// �޶���С��С

		setVisible(true);
		this.requestFocus();
	}

	private void addFunMenu(JMenu edit_edit, JMenu edit_delete, Map<JMenuItem, Function> map, Function fun) {
		// ���²˵�
		String itemTittle = fun.toString();
		JMenuItem editItem = new JMenuItem(itemTittle);
		JMenuItem deleteItem = new JMenuItem(itemTittle);
		map.put(editItem, fun);
		// �޸ĺ���
		editItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {

				FunctionDialog functionDialog = new FunctionDialog(MainFrame.this, "�޸ĺ���", map.get(editItem));
				functionDialog.setVisible(true);
				myPanel.fun.set(myPanel.fun.indexOf(map.get(editItem)), functionDialog.fun);
				myPanel.repaint();
				editItem.setText(functionDialog.fun.toString());
				deleteItem.setText(functionDialog.fun.toString());
				map.put(editItem, functionDialog.fun);
			}
		});
		// ɾ������
		deleteItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				myPanel.fun.remove(map.get(editItem));
				myPanel.repaint();
				edit_edit.remove(editItem);
				edit_delete.remove(deleteItem);
			}
		});
		edit_edit.add(editItem);
		edit_delete.add(deleteItem);
	}

}
