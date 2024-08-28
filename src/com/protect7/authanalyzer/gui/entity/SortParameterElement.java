package com.protect7.authanalyzer.gui.entity;

import com.protect7.authanalyzer.entities.SortDirection;
import com.protect7.authanalyzer.entities.SortField;
import com.protect7.authanalyzer.gui.dialog.SortParameterListConfigDialog;

import javax.swing.*;
import java.awt.*;

public class SortParameterElement extends JPanel {
    private final JLabel label;

    // 升序
    private final JRadioButton ascendingRadioButton;

    // 降序
    private final JRadioButton descendingRadioButton;
    ButtonGroup buttonGroup;

    SortField sortField;

    SortParameterListConfigDialog sortParameterListConfigDialog;

    public SortParameterElement(SortParameterListConfigDialog sortParameterListConfigDialog, SortField sortField, boolean ascending) {
        this.sortParameterListConfigDialog = sortParameterListConfigDialog;
        this.sortField = sortField;

        setLayout(new FlowLayout(FlowLayout.LEFT));
        this.label = new JLabel(sortField.toString());
        ascendingRadioButton = new JRadioButton("\u2B06");
        descendingRadioButton = new JRadioButton("\u2B07");

        buttonGroup = new ButtonGroup();
        buttonGroup.add(ascendingRadioButton);
        buttonGroup.add(descendingRadioButton);

        if (ascending) {
            ascendingRadioButton.setSelected(true);
            descendingRadioButton.setSelected(false);
        }else{
            ascendingRadioButton.setSelected(false);
            descendingRadioButton.setSelected(true);
        }

        add(this.label);
        add(ascendingRadioButton);
        add(descendingRadioButton);

        initActionListener();
    }

    private void initActionListener() {
        ascendingRadioButton.addActionListener(e -> {
            if (ascendingRadioButton.isSelected()){
                updateSortDestination(SortDirection.ASCENDING);
            }else{
                updateSortDestination(SortDirection.DESCENDING);
            }

        });
        descendingRadioButton.addActionListener(e -> {
            if (descendingRadioButton.isSelected()){
                updateSortDestination(SortDirection.DESCENDING);
            }else {
                updateSortDestination(SortDirection.ASCENDING);
            }
        });
    }

    private void updateSortDestination(SortDirection sortDirection)  {
        if (sortParameterListConfigDialog != null) {
            switch (sortField) {
                case METHOD:
                    sortParameterListConfigDialog.updateMethodSortParameter(sortDirection);
                    break;
                case PATH:
                    sortParameterListConfigDialog.updatePathSortParameter(sortDirection);
                    break;
                case QUERY_PARAMS:
                    sortParameterListConfigDialog.updateQueryParamsSortParameter(sortDirection);
                    break;
                case HTTP_VERSION:
                    sortParameterListConfigDialog.updateHttpVersionSortParameter(sortDirection);
                    break;
                default:
                    break;
            }
        }
    }

    public boolean isAscending() {
        return ascendingRadioButton.isSelected();
    }

    public boolean isDescending() {
        return descendingRadioButton.isSelected();
    }

    public void setAscending(boolean ascending) {
        if (ascending) {
            ascendingRadioButton.setSelected(true);
            descendingRadioButton.setSelected(false);
        }else{
            ascendingRadioButton.setSelected(false);
            descendingRadioButton.setSelected(true);
        }
    }

    public void setDescending(boolean descending) {
        if (descending) {
            ascendingRadioButton.setSelected(false);
            descendingRadioButton.setSelected(true);
        }else{
            ascendingRadioButton.setSelected(true);
            descendingRadioButton.setSelected(false);
        }
    }

    public void setLabel(String label) {
        this.label.setText(label);
    }

    public String getLabel() {
        return label.getText();
    }

}
