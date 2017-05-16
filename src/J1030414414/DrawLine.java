package J1030414414;

import java.awt.Font;

import javax.swing.UIManager;

import J1030414414.GUI.MainFrame;

public class DrawLine {

	public static void main(String[] args) {
		// try{
		// UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		// }
		// catch(Exception e){
		// e.printStackTrace();
		// }
		String[] DEFAULT_FONT = new String[] { "Table.font", "TableHeader.font", "CheckBox.font", "Tree.font",
				"Viewport.font", "ProgressBar.font", "RadioButtonMenuItem.font", "ToolBar.font", "ColorChooser.font",
				"ToggleButton.font", "Panel.font", "TextArea.font", "Menu.font", "TableHeader.font", "TextField.font",
				"OptionPane.font", "MenuBar.font", "Button.font", "Label.font", "PasswordField.font", "ScrollPane.font",
				"MenuItem.font", "ToolTip.font", "List.font", "EditorPane.font", "Table.font", "TabbedPane.font",
				"RadioButton.font", "CheckBoxMenuItem.font", "TextPane.font", "PopupMenu.font", "TitledBorder.font",
				"ComboBox.font" };
		try {
			org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper.launchBeautyEyeLNF();
			UIManager.put("RootPane.setupButtonVisible", false);

			// 调整默认字体
			for (int i = 0; i < DEFAULT_FONT.length; i++)
				UIManager.put(DEFAULT_FONT[i], new Font("微软雅黑", Font.PLAIN, 14));
		} catch (Exception e) {
			try {
				for (int i = 0; i < DEFAULT_FONT.length; i++)
					UIManager.put(DEFAULT_FONT[i], new Font("楷体", Font.PLAIN, 14));
			} catch (Exception e0) {
			}
		}

		new MainFrame();
	}
}
