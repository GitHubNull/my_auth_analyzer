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
import com.protect7.authanalyzer.entities.XmlParameterReplace;
import com.protect7.authanalyzer.gui.entity.SessionPanel;
import com.protect7.authanalyzer.gui.util.PlaceholderTextField;

public class XmlParameterReplaceDialog extends JDialog {

	private static final long serialVersionUID = 1L;
	private final int TEXTFIELD_WIDH = 25;
	private final JPanel listPanel = (JPanel) getContentPane();
	private final GridBagConstraints c = new GridBagConstraints();
	private final ArrayList<XmlParameterReplace> xmlParameterReplaceList;
	private final String INFO_TEXT;
	private final PlaceholderTextField xpathInputText = new PlaceholderTextField(TEXTFIELD_WIDH);
	private final PlaceholderTextField replaceValueInputText = new PlaceholderTextField(TEXTFIELD_WIDH);
	private final JCheckBox removeCheckBox = new JCheckBox("移除参数");
	private final JButton addEntryButton = new JButton("\u2795");
	private final JButton okButton = new JButton("确定");
	
	public XmlParameterReplaceDialog(SessionPanel sessionPanel) {
		xmlParameterReplaceList = sessionPanel.getXmlParameterReplaceList();
		INFO_TEXT = "<html>为会话 \"" + sessionPanel.getSessionName() + "\" 配置XML XPath参数替换规则<br>" +
				"支持标准XPath语法，例如：/root/user/name, //user[@id='123'], /root/users/user[1]</html>";
		
		listPanel.setLayout(new GridBagLayout());
		listPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

		xpathInputText.setPlaceholder("例如：/root/user/id");
		replaceValueInputText.setPlaceholder("替换值");

		addEntryButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				addXmlParameterReplace(xpathInputText.getText(), replaceValueInputText.getText(), removeCheckBox.isSelected());
				xpathInputText.setText("");
				replaceValueInputText.setText("");
				removeCheckBox.setSelected(false);
				updateXmlParameterReplaceList();
				SwingUtilities.getWindowAncestor((Component) e.getSource()).pack();
			}
		});
		updateXmlParameterReplaceList();
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);	
		setVisible(true);
		setTitle("XML XPath 参数替换 - " + sessionPanel.getSessionName());
		pack();
		setLocationRelativeTo(sessionPanel);
		
		okButton.addActionListener(e -> {
			addXmlParameterReplace(xpathInputText.getText(), replaceValueInputText.getText(), removeCheckBox.isSelected());
			dispose();
		});
			
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				sessionPanel.updateXmlParameterReplaceButtonText();
			}
		});
	}

	private void updateXmlParameterReplaceList() {
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
		listPanel.add(new JLabel("XPath:"), c);
		c.gridx = 1;
		listPanel.add(new JLabel("替换值:"), c);
		c.gridx = 2;
		listPanel.add(new JLabel("移除:"), c);
		
		// 输入控件行
		c.insets = new Insets(0, 5, 10, 5);
		c.gridy++;
		c.gridx = 0;
		listPanel.add(xpathInputText, c);
		c.gridx = 1;
		listPanel.add(replaceValueInputText, c);
		c.gridx = 2;
		listPanel.add(removeCheckBox, c);
		c.gridx = 3;
		listPanel.add(addEntryButton, c);

		// 现有条目
		c.insets = new Insets(2, 5, 2, 5);
		for (XmlParameterReplace xmlParamReplace : xmlParameterReplaceList) {
			c.gridy++;
			c.gridx = 0;
			listPanel.add(getFormattedLabel(xmlParamReplace.getXpath()), c);
			c.gridx = 1;
			if(xmlParamReplace.isRemove()) {
				listPanel.add(getFormattedLabel("[删除]"), c);
			} else {
				listPanel.add(getFormattedLabel(xmlParamReplace.getReplaceValue()), c);
			}
			c.gridx = 2;
			listPanel.add(new JLabel(xmlParamReplace.isRemove() ? "是" : "否"), c);
			
			JButton deleteEntryBtn = new JButton();
			deleteEntryBtn.setIcon(new ImageIcon(this.getClass().getClassLoader().getResource("delete.png")));
			deleteEntryBtn.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					removeGivenXpath(xmlParamReplace.getXpath());
					updateXmlParameterReplaceList();
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
	
	private void addXmlParameterReplace(String xpath, String replaceValue, boolean isRemove) {
		if (!xpath.equals("")) {
			removeGivenXpath(xpath);
			xmlParameterReplaceList.add(new XmlParameterReplace(xpath, replaceValue, isRemove));
		}
	}
	
	private boolean removeGivenXpath(String xpath) {
		Iterator<XmlParameterReplace> it = xmlParameterReplaceList.iterator();
		while(it.hasNext()) {
			XmlParameterReplace xmlParamReplace = it.next();
			if(xmlParamReplace.getXpath().equals(xpath)) {
				it.remove();
				return true;
			}
		}
		return false;
	}

} 