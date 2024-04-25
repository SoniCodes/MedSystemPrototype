package com.soni;

import com.soni.panels.MainPanel;
import com.soni.util.ClientSortBy;
import com.soni.util.ReportSortBy;
import lombok.Getter;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class MainFrame extends JFrame
{
    private final JPanel contentPane;
    private JPanel currentPanel;

    public MainFrame()
    {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 950, 750);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(0, 0, 0, 0));

        setContentPane(contentPane);
        contentPane.setLayout(new BorderLayout(0, 0));

        currentPanel = new JPanel();
        contentPane.add(currentPanel, BorderLayout.CENTER);

        JMenuBar menuBar = new JMenuBar();
        contentPane.add(menuBar, BorderLayout.NORTH);

        JMenu mnSettings = new JMenu("Settings");
        menuBar.add(mnSettings);

        // Client Sort By menu
        JMenu mnClientSortBy = new JMenu("ClientSortBy");
        mnSettings.add(mnClientSortBy);

        ButtonGroup clientSortGroup = new ButtonGroup();
        JRadioButtonMenuItem rbClientAge = new JRadioButtonMenuItem("Age");
        JRadioButtonMenuItem rbClientName = new JRadioButtonMenuItem("Name");
        JRadioButtonMenuItem rbClientNone = new JRadioButtonMenuItem("None");

        clientSortGroup.add(rbClientAge);
        clientSortGroup.add(rbClientName);
        clientSortGroup.add(rbClientNone);

        mnClientSortBy.add(rbClientAge);
        mnClientSortBy.add(rbClientName);
        mnClientSortBy.add(rbClientNone);

        // Report Sort By menu
        JMenu mnReportSortBy = new JMenu("ReportSortBy");
        mnSettings.add(mnReportSortBy);

        ButtonGroup reportSortGroup = new ButtonGroup();
        JRadioButtonMenuItem rbReportDate = new JRadioButtonMenuItem("Date");
        JRadioButtonMenuItem rbReportDateReversed = new JRadioButtonMenuItem("Date Reversed");
        JRadioButtonMenuItem rbReportNone = new JRadioButtonMenuItem("None");

        reportSortGroup.add(rbReportDate);
        reportSortGroup.add(rbReportDateReversed);
        reportSortGroup.add(rbReportNone);

        mnReportSortBy.add(rbReportDate);
        mnReportSortBy.add(rbReportDateReversed);
        mnReportSortBy.add(rbReportNone);

        switch(Settings.getInstance().getClientSortBy())
        {
            case AGE -> rbClientAge.setSelected(true);
            case NAME -> rbClientName.setSelected(true);
            case NONE -> rbClientNone.setSelected(true);
        }
        switch(Settings.getInstance().getReportSortBy())
        {
            case NEWESTFIRST -> rbReportDate.setSelected(true);
            case OLDESTFIRST -> rbReportDateReversed.setSelected(true);
            case NONE -> rbReportNone.setSelected(true);
        }


        rbClientAge.addActionListener(e ->
        {
            Settings.getInstance().setClientSortBy(ClientSortBy.AGE);
            updateClients();
        });
        rbClientName.addActionListener(e ->
        {
            Settings.getInstance().setClientSortBy(ClientSortBy.NAME);
            updateClients();
        });
        rbClientNone.addActionListener(e ->
        {
            Settings.getInstance().setClientSortBy(ClientSortBy.NONE);
            updateClients();
        });

        rbReportDate.addActionListener(e ->
        {
            Settings.getInstance().setReportSortBy(ReportSortBy.NEWESTFIRST);
            updateReports();
        });
        rbReportDateReversed.addActionListener(e ->
        {
            Settings.getInstance().setReportSortBy(ReportSortBy.OLDESTFIRST);
            updateReports();
        });
        rbReportNone.addActionListener(e ->
        {
            Settings.getInstance().setReportSortBy(ReportSortBy.NONE);
            updateReports();
        });
    }

    private void updateClients()
    {
        if(currentPanel instanceof MainPanel mainPanel)
        {
            mainPanel.loadClients();
        }
    }

    private void updateReports()
    {
        if(currentPanel instanceof MainPanel mainPanel)
        {
            mainPanel.loadReports();
        }
    }

    public void showPanel(JPanel panel)
    {
        contentPane.remove(currentPanel);
        this.currentPanel = panel;
        contentPane.add(currentPanel, BorderLayout.CENTER);
        contentPane.revalidate();
        contentPane.repaint();
    }
}
