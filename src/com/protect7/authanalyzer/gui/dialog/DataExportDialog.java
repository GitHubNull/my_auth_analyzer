package com.protect7.authanalyzer.gui.dialog;

import burp.BurpExtender;
import com.protect7.authanalyzer.entities.OriginalRequestResponse;
import com.protect7.authanalyzer.gui.main.CenterPanel;
import com.protect7.authanalyzer.util.CurrentConfig;
import com.protect7.authanalyzer.util.DataExporter;
import com.protect7.authanalyzer.util.Setting;
import com.protect7.authanalyzer.util.PathTruncationUtil;
import org.oxff.util.JarResourceExtractor;

import javax.swing.*;
import java.awt.FlowLayout;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.EnumSet;

public class DataExportDialog {

    // Member variables for Postman options
    private JSpinner truncationSpinner;

    public DataExportDialog(CenterPanel centerPanel) {
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.PAGE_AXIS));

        inputPanel.add(new JLabel("Choose the format of the export."));
        JRadioButton htmlReport = new JRadioButton("HTML Export", true);
        JRadioButton interactiveHTMLReport = new JRadioButton("Inter active HTML Export");
        JRadioButton xmlReport = new JRadioButton("XML Export");
        JRadioButton postmanReport = new JRadioButton("Postman Collection v2.1");
        ButtonGroup group = new ButtonGroup();
        group.add(htmlReport);
        group.add(interactiveHTMLReport);
        group.add(xmlReport);
        group.add(postmanReport);
        inputPanel.add(htmlReport);
        inputPanel.add(interactiveHTMLReport);
        inputPanel.add(xmlReport);
        inputPanel.add(postmanReport);
        JCheckBox doBase64Encode = new JCheckBox("Base64-encode requests and responses", true);
        doBase64Encode.setEnabled(false);
        interactiveHTMLReport.addActionListener(e -> doBase64Encode.setEnabled(false));
        htmlReport.addActionListener(e -> doBase64Encode.setEnabled(false));
        postmanReport.addActionListener(e -> doBase64Encode.setEnabled(false));
        xmlReport.addActionListener(e -> doBase64Encode.setEnabled(true));
        inputPanel.add(doBase64Encode);

        // Postman-specific options
        JPanel postmanOptionsPanel = new JPanel();
        postmanOptionsPanel.setLayout(new BoxLayout(postmanOptionsPanel, BoxLayout.PAGE_AXIS));
        postmanOptionsPanel.setBorder(BorderFactory.createTitledBorder("Postman Options"));

        JCheckBox includeOriginalRequests = new JCheckBox("Include original requests in collection", true);
        postmanOptionsPanel.add(includeOriginalRequests);

        // Path truncation configuration
        JPanel truncationPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        truncationPanel.add(new JLabel("Path Truncation Length:"));

        // Get current setting value
        int currentTruncationLength = Setting.getValueAsInteger(Setting.Item.POSTMAN_PATH_TRUNCATE_LENGTH);

        SpinnerNumberModel spinnerModel = new SpinnerNumberModel(currentTruncationLength, 8, 128, 1);
        JSpinner truncationSpinner = new JSpinner(spinnerModel);
        truncationSpinner.setToolTipText("Range: 8-128 characters (Short paths < 8 chars will not be modified)");
        truncationPanel.add(truncationSpinner);

        postmanOptionsPanel.add(truncationPanel);

        inputPanel.add(postmanOptionsPanel);
        postmanOptionsPanel.setEnabled(false);

        // Enable/disable Postman options based on radio button selection
        postmanReport.addActionListener(e -> postmanOptionsPanel.setEnabled(true));
        htmlReport.addActionListener(e -> postmanOptionsPanel.setEnabled(false));
        interactiveHTMLReport.addActionListener(e -> postmanOptionsPanel.setEnabled(false));
        xmlReport.addActionListener(e -> postmanOptionsPanel.setEnabled(false));

        inputPanel.add(new JLabel(" "));
        inputPanel.add(new JSeparator(JSeparator.HORIZONTAL));
        inputPanel.add(new JLabel(" "));

        inputPanel.add(new JLabel("Select Columns to include in export."));

        EnumSet<DataExporter.MainColumn> mainColumns = EnumSet.allOf(DataExporter.MainColumn.class);
        for (DataExporter.MainColumn mainColumn : DataExporter.MainColumn.values()) {
            JCheckBox checkBox = new JCheckBox(mainColumn.getName(), true);
            checkBox.addActionListener(e -> {
                if (checkBox.isSelected()) {
                    mainColumns.add(mainColumn);
                } else {
                    mainColumns.remove(mainColumn);
                }
            });
            inputPanel.add(checkBox);
        }
        EnumSet<DataExporter.SessionColumn> sessionColumns = EnumSet.allOf(DataExporter.SessionColumn.class);
        for (DataExporter.SessionColumn sessionColumn : DataExporter.SessionColumn.values()) {
            JCheckBox checkBox;
            if (sessionColumn == DataExporter.SessionColumn.REQUEST || sessionColumn == DataExporter.SessionColumn.RESPONSE) {
                checkBox = new JCheckBox(sessionColumn.getName(), false);
                sessionColumns.remove(sessionColumn);
            } else {
                checkBox = new JCheckBox(sessionColumn.getName(), true);
            }
            checkBox.addActionListener(e -> {
                if (checkBox.isSelected()) {
                    sessionColumns.add(sessionColumn);
                } else {
                    sessionColumns.remove(sessionColumn);
                }
            });
            inputPanel.add(checkBox);
        }
        inputPanel.add(new JLabel(" "));

        int result = JOptionPane.showConfirmDialog(centerPanel, inputPanel, "Export Table Data",
                JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            JFileChooser chooser = new JFileChooser();
            if (htmlReport.isSelected()) {
                chooser.setSelectedFile(new File("Auth_Analyzer_Report.html"));
            } else if (interactiveHTMLReport.isSelected()) {
                chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                chooser.setSelectedFile(new File("interActiveHTMLReport"));
            } else if (postmanReport.isSelected()) {
                chooser.setSelectedFile(new File("Auth_Analyzer_Collection.postman_collection.json"));
            } else {
                chooser.setSelectedFile(new File("Auth_Analyzer_Report.xml"));
            }
            int status = chooser.showSaveDialog(centerPanel);
            if (status == JFileChooser.APPROVE_OPTION) {
                File file = chooser.getSelectedFile();
                // Handle file extensions based on selected format
                if (htmlReport.isSelected() && !file.getName().endsWith(".html")) {
                    String newFileName = file.getAbsolutePath();
                    if (file.getName().lastIndexOf(".") != -1) {
                        int index = file.getAbsolutePath().lastIndexOf(".");
                        newFileName = file.getAbsolutePath().substring(0, index);
                    }
                    file = new File(newFileName + ".html");
                } else if (postmanReport.isSelected() && !file.getName().endsWith(".json")) {
                    String newFileName = file.getAbsolutePath();
                    if (file.getName().lastIndexOf(".") != -1) {
                        int index = file.getAbsolutePath().lastIndexOf(".");
                        newFileName = file.getAbsolutePath().substring(0, index);
                    }
                    file = new File(newFileName + ".json");
                } else if (xmlReport.isSelected() && !file.getName().endsWith(".xml")) {
                    String newFileName = file.getAbsolutePath();
                    if (file.getName().lastIndexOf(".") != -1) {
                        int index = file.getAbsolutePath().lastIndexOf(".");
                        newFileName = file.getAbsolutePath().substring(0, index);
                    }
                    file = new File(newFileName + ".xml");
                }
                ArrayList<OriginalRequestResponse> filteredRequestResponseList = centerPanel.getFilteredRequestResponseList();
                boolean success = false;
                if (htmlReport.isSelected()) {
                    success = DataExporter.getDataExporter().createHTML(file, filteredRequestResponseList, CurrentConfig.getCurrentConfig().getSessions(),
                            mainColumns, sessionColumns);
                    if (success) {
                        JOptionPane.showMessageDialog(centerPanel, "Successfully exported to\n" + file.getAbsolutePath());
                    } else {
                        JOptionPane.showMessageDialog(centerPanel, "Failed to export data");
                    }
                } else if (interactiveHTMLReport.isSelected()) {
                    try {
                        JarResourceExtractor.extractResourcesTo(file.getAbsolutePath());
                        File finalFile = file;

                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                DataExporter.getDataExporter().createInteractiveHTMLData(finalFile,
                                        filteredRequestResponseList,
                                        CurrentConfig.getCurrentConfig().getSessions());
                            }
                        }).start();
                    } catch (IOException e) {
                        e.printStackTrace();
                        BurpExtender.callbacks.issueAlert("Failed to extract resources to " + file.getAbsolutePath());
                    }
                } else if (postmanReport.isSelected()) {
                    // Save the truncation length setting
                    int truncationLength = (Integer) truncationSpinner.getValue();
                    Setting.setValue(Setting.Item.POSTMAN_PATH_TRUNCATE_LENGTH, String.valueOf(truncationLength));

                    success = DataExporter.getDataExporter().createPostmanCollection(file, filteredRequestResponseList,
                            CurrentConfig.getCurrentConfig().getSessions(), includeOriginalRequests.isSelected());

                    if (success) {
                        JOptionPane.showMessageDialog(centerPanel, "Successfully exported Postman collection to\n" + file.getAbsolutePath());
                    } else {
                        JOptionPane.showMessageDialog(centerPanel, "Failed to export Postman collection");
                    }
                } else {
                    success = DataExporter.getDataExporter().createXML(file, filteredRequestResponseList, CurrentConfig.getCurrentConfig().getSessions(),
                            mainColumns, sessionColumns, doBase64Encode.isSelected());

                    if (success) {
                        JOptionPane.showMessageDialog(centerPanel, "Successfully exported to\n" + file.getAbsolutePath());
                    } else {
                        JOptionPane.showMessageDialog(centerPanel, "Failed to export data");
                    }
                }

            }
        }
    }
}