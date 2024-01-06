package com.protect7.authanalyzer.gui.dialog;

import com.protect7.authanalyzer.entities.API_ELEMENT;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ConfigDeduplicationDialog extends JDialog {


    /**
     *
     */
    private static final long serialVersionUID = -6308425329795295343L;

    JPanel northPanel;
    JPanel centerPanel;
    JPanel showFinallyUsedPanel;
    JPanel southPanel;

    List<API_ELEMENT> apiELEMENTList;
    JCheckBox useMethodCheckBox;
    JCheckBox usePathCheckBox;
    JCheckBox useQueryCheckBox;
    JCheckBox useHTTPVersionCheckBox;

    final static JLabel useMethodLabel = new JLabel(API_ELEMENT.METHOD.name());
    final static JLabel usePathLabel = new JLabel(API_ELEMENT.PATH.name());
    final static JLabel useQueryLabel = new JLabel(API_ELEMENT.QUERY_PARAMS.name());
    final static JLabel useHTTPVersionLabel = new JLabel(API_ELEMENT.HTTP_VERSION.name());

    final static HashMap<API_ELEMENT, JLabel> apiElementLabelMap = new HashMap<>();

    JButton okButton;
    JButton cancelButton;

    CopyAPIsConfigDialog copyAPIsConfigDialog;

    public ConfigDeduplicationDialog(CopyAPIsConfigDialog copyAPIsConfigDialog) {
        setTitle("Config Deduplication");

        initData(copyAPIsConfigDialog);
        initUI();
        initActionListeners();

        pack();
        setSize(getPreferredSize());
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    private void initData(CopyAPIsConfigDialog copyAPIsConfigDialog) {
        this.copyAPIsConfigDialog = copyAPIsConfigDialog;
        apiELEMENTList = new ArrayList<>();
        apiElementLabelMap.put(API_ELEMENT.METHOD, useMethodLabel);
        apiElementLabelMap.put(API_ELEMENT.PATH, usePathLabel);
        apiElementLabelMap.put(API_ELEMENT.QUERY_PARAMS, useQueryLabel);
        apiElementLabelMap.put(API_ELEMENT.HTTP_VERSION, useHTTPVersionLabel);
    }

    private void initUI() {
        northPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        JLabel titleLabel = new JLabel("Select");
        northPanel.add(titleLabel);

        useMethodCheckBox = new JCheckBox("Method");
        usePathCheckBox = new JCheckBox("Path");
        useQueryCheckBox = new JCheckBox("Query");
        useHTTPVersionCheckBox = new JCheckBox("HTTP Version");

        northPanel.add(useMethodCheckBox);
        northPanel.add(usePathCheckBox);
        northPanel.add(useQueryCheckBox);
        northPanel.add(useHTTPVersionCheckBox);

        centerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel useFormLabel = new JLabel("Use for deduplication:");
        showFinallyUsedPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        centerPanel.add(useFormLabel);
        centerPanel.add(showFinallyUsedPanel);

        southPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        okButton = new JButton("OK");
        cancelButton = new JButton("Cancel");

        southPanel.add(okButton);
        southPanel.add(cancelButton);

        add(northPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(southPanel, BorderLayout.SOUTH);
    }

    private void initActionListeners() {
        useMethodCheckBox.addActionListener(e->{
            if(useMethodCheckBox.isSelected()){
                apiELEMENTList.add(API_ELEMENT.METHOD);
            }else{
                apiELEMENTList.remove(API_ELEMENT.METHOD);
            }
            SwingUtilities.invokeLater(this::updateCenterPanel);
        });

        usePathCheckBox.addActionListener(e->{
            if(usePathCheckBox.isSelected()){
                apiELEMENTList.add(API_ELEMENT.PATH);
            }else{
                apiELEMENTList.remove(API_ELEMENT.PATH);
            }
            SwingUtilities.invokeLater(this::updateCenterPanel);
        });

        useQueryCheckBox.addActionListener(e->{
            if(useQueryCheckBox.isSelected()){
                apiELEMENTList.add(API_ELEMENT.QUERY_PARAMS);
            }else{
                apiELEMENTList.remove(API_ELEMENT.QUERY_PARAMS);
            }
            SwingUtilities.invokeLater(this::updateCenterPanel);
        });

        useHTTPVersionCheckBox.addActionListener(e->{
            if(useHTTPVersionCheckBox.isSelected()){
                apiELEMENTList.add(API_ELEMENT.HTTP_VERSION);
            }else{
                apiELEMENTList.remove(API_ELEMENT.HTTP_VERSION);
            }
            SwingUtilities.invokeLater(this::updateCenterPanel);
        });

        okButton.addActionListener(e->{
            dispose();
            if (null != apiELEMENTList && !apiELEMENTList.isEmpty()){
                copyAPIsConfigDialog.setDuplicationAPIElementList(apiELEMENTList);
            }else{
                copyAPIsConfigDialog.setDuplicationAPIElementList(null);
            }
        });

        cancelButton.addActionListener(e-> dispose());
    }

    private void updateCenterPanel() {
        if (showFinallyUsedPanel.getComponentCount()>0){
            showFinallyUsedPanel.removeAll();
        }
        for (API_ELEMENT apiELEMENT : apiELEMENTList) {
            showFinallyUsedPanel.add(apiElementLabelMap.get(apiELEMENT));
            showFinallyUsedPanel.add(new JLabel(" + "));
        }
        if (showFinallyUsedPanel.getComponentCount()>1){
            showFinallyUsedPanel.remove(showFinallyUsedPanel.getComponentCount()-1);
        }

        showFinallyUsedPanel.revalidate();
        showFinallyUsedPanel.repaint();

        pack();
        setSize(getPreferredSize());
        setLocationRelativeTo(null);
    }

}
