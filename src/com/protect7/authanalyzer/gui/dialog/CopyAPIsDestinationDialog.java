package com.protect7.authanalyzer.gui.dialog;

import burp.BurpExtender;
import com.protect7.authanalyzer.entities.CopyDestination;
import com.protect7.authanalyzer.entities.DestinationInfo;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;

public class CopyAPIsDestinationDialog extends JDialog {
    JPanel northPanel;
    JPanel centerPanel;

    JRadioButton toCliBoardRadioButton;
    JRadioButton toFileRadioButton;
    ButtonGroup buttonGroup;

    JButton okButton;
    JButton cancelButton;

    String filePath;

    CopyAPIsConfigDialog copyAPIsConfigDialog;

    public CopyAPIsDestinationDialog(CopyAPIsConfigDialog copyAPIsConfigDialog) {
        setTitle("Copy APIs Destination");

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
        filePath = "";
    }

    private void initUI() {
        northPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        toCliBoardRadioButton = new JRadioButton("Copy to clipboard");
        toFileRadioButton = new JRadioButton("Copy to file");

        buttonGroup = new ButtonGroup();
        buttonGroup.add(toCliBoardRadioButton);
        buttonGroup.add(toFileRadioButton);
        toCliBoardRadioButton.setSelected(true);
        toFileRadioButton.setSelected(false);

        northPanel.add(toCliBoardRadioButton);
        northPanel.add(toFileRadioButton);

        centerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        okButton = new JButton("OK");
        cancelButton = new JButton("Cancel");

        centerPanel.add(okButton);
        centerPanel.add(cancelButton);

        add(northPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
    }

    private void initActionListeners() {
        toFileRadioButton.addActionListener(e->{
            if (toFileRadioButton.isSelected()) {
                // TODO show the fileChooser to select the destination file
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setDialogTitle("Select the destination file");
                fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("Text files", "txt"));
                int returnValue = fileChooser.showSaveDialog(this);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    filePath = fileChooser.getSelectedFile().getAbsolutePath();
                    BurpExtender.stdout.println(filePath);
                }
            }
        });


        okButton.addActionListener(e->{
            if (toCliBoardRadioButton.isSelected()) {
                //TODO save string to clipboard
                DestinationInfo destinationInfo = new DestinationInfo(CopyDestination.CLI_BOARD, null);
                copyAPIsConfigDialog.setDestinationInfo(destinationInfo);
                BurpExtender.stdout.println("Copy to clipboard");

            } else if (toFileRadioButton.isSelected())  {
                //TODO save string to file
                if (filePath != null && !filePath.isEmpty()) {
                    DestinationInfo destinationInfo = new DestinationInfo(CopyDestination.FILE, filePath);
                    copyAPIsConfigDialog.setDestinationInfo(destinationInfo);
                }else{
                    BurpExtender.stdout.println("File path is empty");
                    // throw new IllegalArgumentException("File path is empty");
                    JOptionPane.showMessageDialog(this, "File path is empty", "Error", JOptionPane.ERROR_MESSAGE);
                    throw new IllegalArgumentException("File path is empty");
                }
            }
            copyAPIsConfigDialog.copyAPIS();
            dispose();
        });

        cancelButton.addActionListener(e-> dispose());
    }
}
