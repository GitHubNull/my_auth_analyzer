package com.protect7.authanalyzer.gui.dialog;

import burp.BurpExtender;
import com.protect7.authanalyzer.entities.*;
import com.protect7.authanalyzer.gui.util.CopyAPIConfig;
import com.protect7.authanalyzer.gui.util.CopyAPIsTool;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CopyAPIsConfigDialog extends JDialog {
    final static JLabel useMethodLabel = new JLabel(API_ELEMENT.METHOD.name());
    final static JLabel usePathLabel = new JLabel(API_ELEMENT.PATH.name());
    final static JLabel useQueryLabel = new JLabel(API_ELEMENT.QUERY_PARAMS.name());
    final static JLabel useHTTPVersionLabel = new JLabel(API_ELEMENT.HTTP_VERSION.name());
    final static HashMap<API_ELEMENT, JLabel> apiElementLabelMap = new HashMap<>();
    JPanel northPanel;
    JPanel northSubNorthPanel;
    JPanel northSubSouthPanel;
    JPanel centerPanel;
    JPanel finallyShowViewPanel;
    JPanel southPanel;
    List<API_ELEMENT> apiELEMENTList;
    List<API_ELEMENT> duplicationAPIELEMENTList;
    JCheckBox useMethodCheckBox;
    JCheckBox usePathCheckBox;
    JCheckBox useQueryCheckBox;
    JCheckBox useHTTPVersionCheckBox;
    JTextField apiElementSeparatorTextField;
    JButton useCurrentAPISeparatorButton;

    JButton okButton;
    JButton configDeduplicationButton;
    JButton cancelButton;

    List<OriginalRequestResponse> originalRequestResponseList;
    DeduplicationOperation deduplicationOperation;
    CopyAPIConfig copyAPIConfig;

    public CopyAPIsConfigDialog(List<OriginalRequestResponse> originalRequestResponseList, DeduplicationOperation deduplicationOperation) {
        setTitle("Copy APIs Configuration");

        initData(originalRequestResponseList, deduplicationOperation);
        initUI();
        initActionListeners();

        pack();
        setSize(getPreferredSize());
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    public void setDuplicationAPIElementList(List<API_ELEMENT> duplicationAPIELEMENTList) {
        this.duplicationAPIELEMENTList = duplicationAPIELEMENTList;
    }

    private void initData(List<OriginalRequestResponse> originalRequestResponseList,
                          DeduplicationOperation deduplicationOperation) {
        this.originalRequestResponseList = originalRequestResponseList;
        this.deduplicationOperation = deduplicationOperation;

        copyAPIConfig = new CopyAPIConfig();
        copyAPIConfig.setDeduplicationOperation(deduplicationOperation);

        apiELEMENTList = new ArrayList<>();

        duplicationAPIELEMENTList = new ArrayList<>();
        setDuplicationAPIElementListDefaultValue();

        apiElementLabelMap.put(API_ELEMENT.METHOD, useMethodLabel);
        apiElementLabelMap.put(API_ELEMENT.PATH, usePathLabel);
        apiElementLabelMap.put(API_ELEMENT.QUERY_PARAMS, useQueryLabel);
        apiElementLabelMap.put(API_ELEMENT.HTTP_VERSION, useHTTPVersionLabel);
    }


    private void initUI() {
        northPanel = new JPanel(new BorderLayout());

        northSubNorthPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel apiElementLabel = new JLabel("API Element");
        useMethodCheckBox = new JCheckBox("Method");
        usePathCheckBox = new JCheckBox("Path");
        useQueryCheckBox = new JCheckBox("Query");
        useHTTPVersionCheckBox = new JCheckBox("HTTP Version");

        northSubNorthPanel.add(apiElementLabel);
        northSubNorthPanel.add(useMethodCheckBox);
        northSubNorthPanel.add(usePathCheckBox);
        northSubNorthPanel.add(useQueryCheckBox);
        northSubNorthPanel.add(useHTTPVersionCheckBox);

        northSubSouthPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel apiElementSeparatorLabel = new JLabel("API Element Separator");
        apiElementSeparatorTextField = new JTextField(" ", 4);
        useCurrentAPISeparatorButton = new JButton("Use Current API Separator");
        northSubSouthPanel.add(apiElementSeparatorLabel);
        northSubSouthPanel.add(apiElementSeparatorTextField);
        northSubSouthPanel.add(useCurrentAPISeparatorButton);

        northPanel.add(northSubNorthPanel, BorderLayout.NORTH);
        northPanel.add(northSubSouthPanel, BorderLayout.SOUTH);

        centerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel finallySHowViewLabel = new JLabel("Finally Show View: ");
        finallyShowViewPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        centerPanel.add(finallySHowViewLabel);
        centerPanel.add(finallyShowViewPanel);

        southPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        okButton = new JButton("OK");
        configDeduplicationButton = new JButton("Config Deduplication");
        cancelButton = new JButton("Cancel");

        southPanel.add(okButton);
        southPanel.add(configDeduplicationButton);
        southPanel.add(cancelButton);

        add(northPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(southPanel, BorderLayout.SOUTH);
    }

    private void initActionListeners() {
        useMethodCheckBox.addActionListener(e -> {
            if (useMethodCheckBox.isSelected()) {
                apiELEMENTList.add(API_ELEMENT.METHOD);
            } else {
                apiELEMENTList.remove(API_ELEMENT.METHOD);
            }
            SwingUtilities.invokeLater(this::updateFinallyShowViewPanel);
        });

        usePathCheckBox.addActionListener(e -> {
            if (usePathCheckBox.isSelected()) {
                apiELEMENTList.add(API_ELEMENT.PATH);
            } else {
                apiELEMENTList.remove(API_ELEMENT.PATH);
            }
            SwingUtilities.invokeLater(this::updateFinallyShowViewPanel);
        });

        useQueryCheckBox.addActionListener(e -> {
            if (useQueryCheckBox.isSelected()) {
                apiELEMENTList.add(API_ELEMENT.QUERY_PARAMS);
            } else {
                apiELEMENTList.remove(API_ELEMENT.QUERY_PARAMS);
            }
            SwingUtilities.invokeLater(this::updateFinallyShowViewPanel);
        });

        useHTTPVersionCheckBox.addActionListener(e -> {
            if (useHTTPVersionCheckBox.isSelected()) {
                apiELEMENTList.add(API_ELEMENT.HTTP_VERSION);
            } else {
                apiELEMENTList.remove(API_ELEMENT.HTTP_VERSION);
            }
            SwingUtilities.invokeLater(this::updateFinallyShowViewPanel);
        });

        useCurrentAPISeparatorButton.addActionListener(e -> SwingUtilities.invokeLater(this::updateFinallyShowViewPanel));

        okButton.addActionListener(e -> {

            if (duplicationAPIELEMENTList != null && duplicationAPIELEMENTList.isEmpty()) {
                setDuplicationAPIElementListDefaultValue();
            } else if (duplicationAPIELEMENTList == null) {
                duplicationAPIELEMENTList = new ArrayList<>();
                setDuplicationAPIElementListDefaultValue();
            }

            if (apiELEMENTList != null && apiELEMENTList.isEmpty()) {
                setApiElementListDefaultValue();
            } else if (apiELEMENTList == null) {
                apiELEMENTList = new ArrayList<>();
                setApiElementListDefaultValue();
            }

            String separator = apiElementSeparatorTextField.getText();
            if (separator.isEmpty()) {
                separator = " ";
            }

            copyAPIConfig.setApiElementList(apiELEMENTList);
            copyAPIConfig.setSeparator(separator);
            copyAPIConfig.setDuplicationAPIElementList(duplicationAPIELEMENTList);

            SortParameterListConfigDialog sortParameterListConfigDialog = new SortParameterListConfigDialog(CopyAPIsConfigDialog.this);
            sortParameterListConfigDialog.setVisible(true);

            dispose();

        });
        configDeduplicationButton.addActionListener(e -> {
            ConfigDeduplicationDialog configDeduplicationDialog = new ConfigDeduplicationDialog(CopyAPIsConfigDialog.this);
            configDeduplicationDialog.setVisible(true);
        });
        cancelButton.addActionListener(e -> dispose());
    }

    public void showCopyAPIsDestinationDialog(){
        CopyAPIsDestinationDialog copyAPIsDestinationDialog = new CopyAPIsDestinationDialog(CopyAPIsConfigDialog.this);
        copyAPIsDestinationDialog.setVisible(true);
    }

    public void copyAPIS(){
        BurpExtender.stdout.println("Copy APIs");
        List<OriginalRequestResponse> finallyOriginalRequestResponseList;
        if (copyAPIConfig.getDeduplicationOperation().equals(DeduplicationOperation.YES)){
//            BurpExtender.stdout.println("Deduplicate APIs Line-230");
            finallyOriginalRequestResponseList = CopyAPIsTool.deduplicateAPIs(originalRequestResponseList, copyAPIConfig);
        }else{
//            BurpExtender.stdout.println("Deduplicate APIs Line-233");
            finallyOriginalRequestResponseList = originalRequestResponseList;
        }
//        BurpExtender.stdout.println("Deduplicate APIs Line-236");
        finallyOriginalRequestResponseList = CopyAPIsTool.sortOriginalRequestResponseList(finallyOriginalRequestResponseList, copyAPIConfig.getSortParameterList());

        StringBuilder sb = new StringBuilder();
        for (OriginalRequestResponse originalRequestResponse : finallyOriginalRequestResponseList) {
            String line = CopyAPIsTool.getAPIStr(copyAPIConfig, originalRequestResponse);
            if(null != line && !line.isEmpty()){
                String tmpLine = line + System.lineSeparator();
                sb.append(tmpLine);
            }
        }

        String content = sb.toString();
        DestinationInfo destinationInfo = copyAPIConfig.getCopyToDestinationInfo();

        if (destinationInfo.getCopyDestination().equals(CopyDestination.FILE)){
            CopyAPIsTool.writeToFile(content, destinationInfo.getFilePath());
        }else{
//            BurpExtender.stdout.println("Copy APIs to clipboard before!");
            CopyAPIsTool.copyToClipboard(content);
//            BurpExtender.stdout.println("Copy APIs to clipboard");
        }

    }

    public void setDestinationInfo(DestinationInfo destinationInfo){
        copyAPIConfig.setCopyToDestinationInfo(destinationInfo);
    }

    public void setSortParameterList(List<SortParameter> sortParameterList){
        copyAPIConfig.setSortParameterList(sortParameterList);
    }

    private void setDuplicationAPIElementListDefaultValue() {
        duplicationAPIELEMENTList.add(API_ELEMENT.METHOD);
        duplicationAPIELEMENTList.add(API_ELEMENT.PATH);
        duplicationAPIELEMENTList.add(API_ELEMENT.HTTP_VERSION);
    }

    private void setApiElementListDefaultValue() {
        apiELEMENTList.add(API_ELEMENT.METHOD);
        apiELEMENTList.add(API_ELEMENT.PATH);
        apiELEMENTList.add(API_ELEMENT.HTTP_VERSION);
    }

    private void updateFinallyShowViewPanel() {
        String currentAPISeparator = apiElementSeparatorTextField.getText();
        if (!currentAPISeparator.isEmpty()) {
            if (finallyShowViewPanel.getComponentCount() > 0) {
                finallyShowViewPanel.remove(0);
            }

            finallyShowViewPanel.revalidate();
            finallyShowViewPanel.repaint();
            for (API_ELEMENT apiELEMENT : apiELEMENTList) {
                finallyShowViewPanel.add(apiElementLabelMap.get(apiELEMENT));
                finallyShowViewPanel.add(new JLabel(currentAPISeparator));
            }
            if (finallyShowViewPanel.getComponentCount() > 1) {
                finallyShowViewPanel.remove(finallyShowViewPanel.getComponentCount() - 1);
            }
            finallyShowViewPanel.revalidate();
            finallyShowViewPanel.repaint();

            pack();
            setSize(getPreferredSize());
            setLocationRelativeTo(null);
        }
    }

}
