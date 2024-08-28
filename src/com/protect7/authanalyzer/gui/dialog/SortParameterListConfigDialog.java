package com.protect7.authanalyzer.gui.dialog;

import burp.BurpExtender;
import com.protect7.authanalyzer.entities.SortDirection;
import com.protect7.authanalyzer.entities.SortField;
import com.protect7.authanalyzer.entities.SortParameter;
import com.protect7.authanalyzer.gui.entity.SortParameterElement;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SortParameterListConfigDialog extends JDialog {
    JPanel northPanel;
    JPanel sortParametersPanel;
    JPanel southPanel;
    JCheckBox useMethodCheckBox;
    JCheckBox usePathCheckBox;
    JCheckBox useQueryCheckBox;
    JCheckBox useHTTPVersionCheckBox;

    JButton okButton;
    JButton cancelButton;

    CopyAPIsConfigDialog copyAPIsConfigDialog;

    final static SortParameter methodSortParameter = new SortParameter(SortField.METHOD, SortDirection.ASCENDING);
    final static SortParameter pathSortParameter = new SortParameter(SortField.PATH, SortDirection.ASCENDING);
    final static SortParameter queryParamsSortParameter = new SortParameter(SortField.QUERY_PARAMS, SortDirection.ASCENDING);
    final static SortParameter httpVersionSortParameter = new SortParameter(SortField.HTTP_VERSION, SortDirection.ASCENDING);

    List<SortParameter> sortParameterList;
    final SortParameterElement useMethodSortParameterElement = new SortParameterElement(SortParameterListConfigDialog.this, SortField.METHOD, true);
    final SortParameterElement usePathSortParameterElement = new SortParameterElement(SortParameterListConfigDialog.this, SortField.PATH, true);
    final SortParameterElement useQuerySortParameterElement = new SortParameterElement(SortParameterListConfigDialog.this, SortField.QUERY_PARAMS, true);
    final SortParameterElement useHTTPVersionSortParameterElement = new SortParameterElement(SortParameterListConfigDialog.this, SortField.HTTP_VERSION, true);

    final static HashMap<SortParameter, SortParameterElement> sortParameterElementHashMap = new HashMap<>();

    public SortParameterListConfigDialog(CopyAPIsConfigDialog copyAPIsConfigDialog) {
        setTitle("Sort Field Configuration");

        initData(copyAPIsConfigDialog);
        initUI();
        initActionListeners();

        pack();
        setSize(getPreferredSize());
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

//        useMethodSortParameterElement = new SortParameterElement(SortParameterListConfigDialog.this, SortField.METHOD, true);
//        usePathSortParameterElement = new SortParameterElement(SortParameterListConfigDialog.this, SortField.PATH, true);
//        useQuerySortParameterElement = new SortParameterElement(SortParameterListConfigDialog.this, SortField.QUERY_PARAMS, true);
//        useHTTPVersionSortParameterElement = new SortParameterElement(SortParameterListConfigDialog.this, SortField.HTTP_VERSION, true);
    }

    private void initData(CopyAPIsConfigDialog copyAPIsConfigDialog) {
        this.copyAPIsConfigDialog = copyAPIsConfigDialog;
        sortParameterList = new ArrayList<>();

        sortParameterElementHashMap.put(methodSortParameter, useMethodSortParameterElement);
        sortParameterElementHashMap.put(pathSortParameter, usePathSortParameterElement);
        sortParameterElementHashMap.put(queryParamsSortParameter, useQuerySortParameterElement);
        sortParameterElementHashMap.put(httpVersionSortParameter, useHTTPVersionSortParameterElement);
    }

    private void initUI() {
        northPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
//        northSubLeftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        useMethodCheckBox = new JCheckBox("Method");
        usePathCheckBox = new JCheckBox("Path");
        useQueryCheckBox = new JCheckBox("Query");
        useHTTPVersionCheckBox = new JCheckBox("HTTP Version");

        northPanel.add(useMethodCheckBox);
        northPanel.add(usePathCheckBox);
        northPanel.add(useQueryCheckBox);
        northPanel.add(useHTTPVersionCheckBox);


        sortParametersPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        southPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        okButton = new JButton("OK");
        cancelButton = new JButton("Cancel");

        southPanel.add(okButton);
        southPanel.add(cancelButton);

        add(northPanel, BorderLayout.NORTH);
        add(sortParametersPanel, BorderLayout.CENTER);
        add(southPanel, BorderLayout.SOUTH);
    }

    private void initActionListeners() {
        useMethodCheckBox.addActionListener(e -> {
            if (useMethodCheckBox.isSelected()) {
                sortParameterList.add(methodSortParameter);
            } else {
                sortParameterList.remove(methodSortParameter);
            }

            SwingUtilities.invokeLater(this::updateSortParametersPanel);
        });

        usePathCheckBox.addActionListener(e -> {
            if (usePathCheckBox.isSelected()) {
                sortParameterList.add(pathSortParameter);
            } else {
                sortParameterList.remove(pathSortParameter);
            }

            SwingUtilities.invokeLater(this::updateSortParametersPanel);
        });

        useQueryCheckBox.addActionListener(e -> {
            if (useQueryCheckBox.isSelected()) {
                sortParameterList.add(queryParamsSortParameter);
            } else {
                sortParameterList.remove(queryParamsSortParameter);
            }

            SwingUtilities.invokeLater(this::updateSortParametersPanel);
        });

        useHTTPVersionCheckBox.addActionListener(e -> {
            if (useHTTPVersionCheckBox.isSelected()) {
                sortParameterList.add(httpVersionSortParameter);
            } else {
                sortParameterList.remove(httpVersionSortParameter);
            }

            SwingUtilities.invokeLater(this::updateSortParametersPanel);
        });


        okButton.addActionListener(e -> {
            if (sortParameterList.isEmpty()) {
                setSortParameterList();
            }
            copyAPIsConfigDialog.setSortParameterList(sortParameterList);
            copyAPIsConfigDialog.showCopyAPIsDestinationDialog();
            dispose();
        });

        cancelButton.addActionListener(e -> dispose());
    }

    private void setSortParameterList() {
        methodSortParameter.setSortDirection(SortDirection.ASCENDING);
        pathSortParameter.setSortDirection(SortDirection.ASCENDING);

        sortParameterList.add(methodSortParameter);
        sortParameterList.add(pathSortParameter);
    }

    private void updateSortParametersPanel() {
        if (sortParametersPanel.getComponentCount() > 0) {
            sortParametersPanel.removeAll();
        }
        for (SortParameter sortParameter : sortParameterList) {
            try{
                sortParametersPanel.add(sortParameterElementHashMap.get(sortParameter));
            }catch(Exception e){
                BurpExtender.stderr.println("Error adding sort parameter element: " + e);
            }

            BurpExtender.stdout.println("Adding sort parameter: ${sortParameter.getName()");
        }

        sortParametersPanel.revalidate();
        sortParametersPanel.repaint();

        pack();
        setSize(getPreferredSize());
        setLocationRelativeTo(null);
    }

    public void updateMethodSortParameter(SortDirection sortDirection) {
        methodSortParameter.setSortDirection(sortDirection);
    }

    public void updatePathSortParameter(SortDirection sortDirection) {
        pathSortParameter.setSortDirection(sortDirection);
    }

    public void updateQueryParamsSortParameter(SortDirection sortDirection) {
        queryParamsSortParameter.setSortDirection(sortDirection);
    }

    public void updateHttpVersionSortParameter(SortDirection sortDirection) {
        httpVersionSortParameter.setSortDirection(sortDirection);
    }

}
