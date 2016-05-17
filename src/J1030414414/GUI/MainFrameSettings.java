package J1030414414.GUI;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import J1030414414.Utils;

@SuppressWarnings("serial")
public class MainFrameSettings extends JPanel {

	private GraphicPanel myPanel;
	public ArrayList<Setting> settings = new ArrayList<Setting>();

	public MainFrameSettings(GraphicPanel myPanel) {
		this.myPanel = myPanel;
		setLayout(new BorderLayout());

		JPanel axisSetting = new JPanel();
		add(axisSetting, BorderLayout.NORTH);

		axisSetting.setBorder(BorderFactory.createTitledBorder("坐标轴设置"));
		GridBagLayout axisSettingLayout = new GridBagLayout();
		axisSetting.setLayout(axisSettingLayout);

		settings.add(new Setting(0, axisSetting, axisSettingLayout, "X轴分辨率", 100));
		settings.add(new Setting(1, axisSetting, axisSettingLayout, "Y轴分辨率", 15));
		settings.add(new Setting(2, axisSetting, axisSettingLayout, "X轴分度值", 0.5));
		settings.add(new Setting(3, axisSetting, axisSettingLayout, "Y轴分度值", 2));

		JCheckBox autoSetChk = new JCheckBox("自动设置分辨率");
		
		axisSetting.add(autoSetChk);

		JTextArea hintText = new JTextArea(
				"温馨提示：\r\n1.自动设置分辨率可以锁定刻度线。\r\n2.点击绘图区后，按键盘上下左右调整坐标轴分度值。\r\n3.图例可以拖动。");
		hintText.setEditable(false);
		hintText.setLineWrap(true);
		hintText.setWrapStyleWord(true);
		axisSetting.add(hintText);
		
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		c.gridwidth = 1;
		c.weightx = c.weighty = 0;
		c.insets = new Insets(5, 5, 5, 5);
		c.gridwidth = 0;
		axisSettingLayout.setConstraints(autoSetChk, c);
		axisSettingLayout.setConstraints(hintText, c);
		autoSetChk.addItemListener(new ItemListener() {

			// 增加一个监听器以便自动设置分度值
			@Override
			public void itemStateChanged(ItemEvent arg0) {
				boolean enabled = !autoSetChk.isSelected();
				settings.get(0).setEnabled(enabled);
				settings.get(1).setEnabled(enabled);
				settings.get(2).setAutoSetting(!enabled);
				settings.get(3).setAutoSetting(!enabled);
			}
		});
		autoSetChk.setSelected(true);
	}

	// 内部类Setting为每一个项设置
	class Setting {
		
		private double unit = 0.1;// 最小设置单位
		
		private int id;
		private String name;
		private double defaultValue;
		private double currentValue;
		
		public double getCurrentValue() {
			return currentValue;
		}

		private JSlider s;
		private JTextField t;
		private JButton b;
		private JLabel l;

		private double min=0.1;
		private double max=2;

		private boolean autoSetting = false;
		//setSliderFlag为false时 不触发Slider事件
		private boolean setSliderFlag = true;
		
		public Setting(int id, JPanel panel, GridBagLayout layout, String name, double defaultValue) {

			this.id = id;
			this.name = name;
			this.defaultValue = defaultValue;

			l = new JLabel(name + ":");
			t = new JTextField();
			t.setPreferredSize(new Dimension(50, 15));
			if(id<2)
				s = new JSlider(50,100);
			else
				s = new JSlider(0, (int) (max / unit));
			b = new JButton("默认值");
			// TextField的改变与Slider同步
			t.addKeyListener(new KeyAdapter() {
				@Override
				public void keyReleased(KeyEvent e) {
					try{
						double value = Double.valueOf(t.getText());
						setCurrentValue(value,Source.JTextField,true);
					}
					catch(Exception e1){
						
					}
				}
			});

			// Slider的改变与TextField同步

			s.addChangeListener(new ChangeListener() {
				@Override
				public void stateChanged(ChangeEvent arg0) {
					if (setSliderFlag) {
						double value = ((JSlider) arg0.getSource()).getValue();
						value = Utils.mul(value, unit);
						setCurrentValue(value,Source.JSlider);
					}
				}
			});
			// 设置默认值
			b.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent arg0) {
					setCurrentValue(Setting.this.defaultValue);
				}
			});

			panel.add(l);
			panel.add(t);
			panel.add(b);
			panel.add(s);

			GridBagConstraints c = new GridBagConstraints();
			c.fill = GridBagConstraints.BOTH;
			c.gridwidth = 1;
			c.weightx = c.weighty = 0;
			c.insets = new Insets(5, 5, 5, 5);
			layout.setConstraints(l, c);
			c.gridwidth = 1;
			layout.setConstraints(t, c);
			c.gridwidth = 0;
			layout.setConstraints(b, c);
			c.gridwidth = 0;
			layout.setConstraints(s, c);
			
			setCurrentValue(defaultValue);
		}
		public void setCurrentValue(double value){
			setCurrentValue(value,Source.None);
		}
		
		
		private void setCurrentValue(double value, Source none) {
			//默认不强制放大
			setCurrentValue(value, none, false);
		}
		
		public void setCurrentValue(double value,Source source,boolean forceZoom){
			if (!forceZoom && value > max) // 若强制放大则不检查最大值是否合法
				value = max;
			else if (value < min)
				value = min;
			System.out.println(id+":"+value+":"+source);
			//设置分度值时 改变分辨率的可选范围
			
			if(id > 1 )
			{
				Setting dpi = settings.get(id-2);
				int dpiSliderMin = dpi.s.getMinimum();
				int dpiSliderMax = dpi.s.getMaximum();
				dpi.unit = 1/value;
				dpi.defaultValue = dpi.min =Utils.mul( dpi.unit,dpiSliderMin);
				dpi.max =Utils.mul( dpi.unit,dpiSliderMax);
				System.out.println("min:" + dpi.min + "  max:"+ dpi.max);
				//自动配置分辨率
				if(autoSetting)
					dpi.setCurrentValue(dpi.unit * dpi.s.getValue(),Source.AutoSetting);
				else
					dpi.setCurrentValue(dpi.currentValue);
			}
			
			//操作来自Slider时不设置Slider
			if(source != Source.JSlider)
			{
				setSliderFlag = false;
				try{
					s.setValue((int) (value / unit));
				}
				catch (Exception e) {
					
				}
				setSliderFlag = true;
			}
			//操作来自TextField时不设置TextField
			if(source != Source.JTextField)
				t.setText(String.format("%.2f", value));

			switch (name) {
			case "X轴分辨率":
				myPanel.setUnitLenX(value);
				break;
			case "Y轴分辨率":
				myPanel.setUnitLenY(value);
				break;
			case "X轴分度值":
				myPanel.setMinDivX(value);
				break;
			case "Y轴分度值":
				myPanel.setMinDivY(value);
				break;
			}

			currentValue = value;
			myPanel.repaint();
		}
		// 启用/停用设置
		public void setEnabled(boolean arg0) {
			s.setEnabled(arg0);
			t.setEnabled(arg0);
			l.setEnabled(arg0);
			b.setEnabled(arg0);
		}

		// 自动配置分辨率
		public void setAutoSetting(boolean arg0) {
			if (id <2)// 只有2,3号设置项(分度值设置)可以自动配置分辨率设置项
				return;
			autoSetting  = arg0;
		}
	}
}

enum Source {
    None,JTextField,JSlider,AutoSetting,Key;
}