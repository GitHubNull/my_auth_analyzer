package com.protect7.authanalyzer.gui.dialog;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
import com.protect7.authanalyzer.entities.JsonParameterReplace;
import com.protect7.authanalyzer.gui.entity.SessionPanel;
import com.protect7.authanalyzer.gui.util.PlaceholderTextField;

public class JsonParameterReplaceDialog extends JDialog {

	private static final long serialVersionUID = 1L;
	private final int TEXTFIELD_WIDH = 25;
	private final JPanel listPanel = (JPanel) getContentPane();
	private final GridBagConstraints c = new GridBagConstraints();
	private final ArrayList<JsonParameterReplace> jsonParameterReplaceList;
	private final String INFO_TEXT;
	private final PlaceholderTextField pathInputText = new PlaceholderTextField(TEXTFIELD_WIDH);
	private final PlaceholderTextField replaceValueInputText = new PlaceholderTextField(TEXTFIELD_WIDH);
	private final JCheckBox removeCheckBox = new JCheckBox("移除参数");
	private final JButton addEntryButton = new JButton("\u2795");
	private final JButton okButton = new JButton("确定");
	
	public JsonParameterReplaceDialog(SessionPanel sessionPanel) {
		jsonParameterReplaceList = sessionPanel.getJsonParameterReplaceList();
		INFO_TEXT = "<html>为会话 \"" + sessionPanel.getSessionName() + "\" 配置JSON Path参数替换规则<br>" +
				"支持标准JSON Path语法，例如：$.user.name, $.items[0].price, $..author</html>";
		
		listPanel.setLayout(new GridBagLayout());
		listPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

		pathInputText.setPlaceholder("例如：$.user.id");
		replaceValueInputText.setPlaceholder("替换值（统一作为字符串处理）");

		addEntryButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				addJsonParameterReplace(pathInputText.getText(), replaceValueInputText.getText(), removeCheckBox.isSelected());
				pathInputText.setText("");
				replaceValueInputText.setText("");
				removeCheckBox.setSelected(false);
				updateJsonParameterReplaceList();
				SwingUtilities.getWindowAncestor((Component) e.getSource()).pack();
			}
		});
		updateJsonParameterReplaceList();
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);	
		setVisible(true);
		setTitle("JSON Path 参数替换 - " + sessionPanel.getSessionName());
		pack();
		setLocationRelativeTo(sessionPanel);
		
		okButton.addActionListener(e -> {
			addJsonParameterReplace(pathInputText.getText(), replaceValueInputText.getText(), removeCheckBox.isSelected());
			dispose();
		});
			
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				sessionPanel.updateJsonParameterReplaceButtonText();
			}
		});
	}

	private void updateJsonParameterReplaceList() {
		listPanel.removeAll();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(5, 5, 5, 5);
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 4;
		c.anchor = GridBagConstraints.WEST;
		listPanel.add(new JLabel(INFO_TEXT), c);
		
		// 输入行标题
		c.insets = new Insets(10, 5, 5, 5);
		c.gridwidth = 1;
		c.gridy++;
		c.gridx = 0;
		listPanel.add(new JLabel("JSON Path:"), c);
		c.gridx = 1;
		listPanel.add(new JLabel("替换值:"), c);
		c.gridx = 2;
		listPanel.add(new JLabel("移除:"), c);
		
		// 输入控件行
		c.insets = new Insets(0, 5, 10, 5);
		c.gridy++;
		c.gridx = 0;
		listPanel.add(pathInputText, c);
		c.gridx = 1;
		listPanel.add(replaceValueInputText, c);
		c.gridx = 2;
		listPanel.add(removeCheckBox, c);
		c.gridx = 3;
		listPanel.add(addEntryButton, c);

		// 现有条目
		c.insets = new Insets(2, 5, 2, 5);
		for (JsonParameterReplace jsonParamReplace : jsonParameterReplaceList) {
			c.gridy++;
			c.gridx = 0;
			listPanel.add(getFormattedLabel(jsonParamReplace.getJsonPath()), c);
			c.gridx = 1;
			if(jsonParamReplace.isRemove()) {
				listPanel.add(getFormattedLabel("[删除]"), c);
			} else {
				listPanel.add(getFormattedLabel(jsonParamReplace.getReplaceValue()), c);
			}
			c.gridx = 2;
			listPanel.add(new JLabel(jsonParamReplace.isRemove() ? "是" : "否"), c);
			
			JButton deleteEntryBtn = new JButton();
			deleteEntryBtn.setIcon(new ImageIcon(this.getClass().getClassLoader().getResource("delete.png")));
			deleteEntryBtn.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					removeGivenJsonPath(jsonParamReplace.getJsonPath());
					updateJsonParameterReplaceList();
					SwingUtilities.getWindowAncestor((Component) e.getSource()).pack();
				}
			});
			c.gridx = 3;
			listPanel.add(deleteEntryBtn, c);
		}
		
		// 确定按钮
		c.insets = new Insets(15, 5, 5, 5);
		c.gridy++;
		c.gridx = 0;
		c.gridwidth = 4;
		c.anchor = GridBagConstraints.CENTER;
		listPanel.add(okButton, c);
		
		listPanel.revalidate();
		listPanel.repaint();
		pack();
	}
	
	private JLabel getFormattedLabel(String text) {
		String formattedText;
		if(text.length() > 28) {
			formattedText = text.substring(0, 25) + "...";
		}
		else {
			formattedText = text;
		}
		JLabel label = new JLabel(formattedText);
		label.setToolTipText(text);
		return label;
	}
	
	private void addJsonParameterReplace(String jsonPath, String replaceValue, boolean isRemove) {
		if (!jsonPath.equals("")) {
			removeGivenJsonPath(jsonPath);
			jsonParameterReplaceList.add(new JsonParameterReplace(jsonPath, replaceValue, isRemove));
		}
	}
	
	private boolean removeGivenJsonPath(String jsonPath) {
		Iterator<JsonParameterReplace> it = jsonParameterReplaceList.iterator();
		while(it.hasNext()) {
			JsonParameterReplace jsonParamReplace = it.next();
			if(jsonParamReplace.getJsonPath().equals(jsonPath)) {
				it.remove();
				return true;
			}
		}
		return false;
	}

} 