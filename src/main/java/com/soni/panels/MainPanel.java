package com.soni.panels;

import com.soni.Settings;
import com.soni.util.FilterUtils;
import com.soni.util.MongoDBConnection;
import com.soni.util.Sorting;
import lombok.Getter;
import lombok.Setter;
import org.bson.Document;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

public class MainPanel extends JPanel implements ActionListener
{
    private JTextField labelMain;
    private JTextField tbSearchReport, tbSearchClient;
    private JButton btnInsertClient, btnInsertClientReport;
    private JPanel clientJPanel, reportJPanel;
    private JScrollPane clientJScrollPane, reportJScrollPane;

    private ClientPanel currClient;
    @Getter
    @Setter
    private JTextField lastFocused;

    public MainPanel()
    {
        GridBagLayout gridBagLayout = new GridBagLayout();
        gridBagLayout.columnWidths = new int[]{0, 0, 0};
        gridBagLayout.rowHeights = new int[]{0, 0, 0, 0, 0};
        gridBagLayout.columnWeights = new double[]{1.0, 1.0, Double.MIN_VALUE};
        gridBagLayout.rowWeights = new double[]{1.0, 1.0, 8.0, 1.0, Double.MIN_VALUE};
        setLayout(gridBagLayout);

        labelMain = new JTextField();
        labelMain.setEditable(false);
        labelMain.setHorizontalAlignment(SwingConstants.CENTER);
        labelMain.setFont(new Font("Bahnschrift", Font.PLAIN, 50));
        labelMain.setText("Medical System");
        GridBagConstraints gbc_txtMedicalSystem = new GridBagConstraints();
        gbc_txtMedicalSystem.gridwidth = 2;
        gbc_txtMedicalSystem.insets = new Insets(0, 0, 5, 0);
        gbc_txtMedicalSystem.fill = GridBagConstraints.BOTH;
        gbc_txtMedicalSystem.gridx = 0;
        gbc_txtMedicalSystem.gridy = 0;
        add(labelMain, gbc_txtMedicalSystem);
        labelMain.setColumns(10);

        tbSearchClient = new JTextField();
        tbSearchClient.setFont(new Font("Bahnschrift", Font.PLAIN, 20));
        GridBagConstraints gbc_textField_2 = new GridBagConstraints();
        gbc_textField_2.insets = new Insets(0, 0, 5, 5);
        gbc_textField_2.fill = GridBagConstraints.BOTH;
        gbc_textField_2.gridx = 0;
        gbc_textField_2.gridy = 1;
        add(tbSearchClient, gbc_textField_2);
        tbSearchClient.setColumns(10);

        tbSearchReport = new JTextField();
        tbSearchReport.setFont(new Font("Bahnschrift", Font.PLAIN, 20));
        GridBagConstraints gbc_textField = new GridBagConstraints();
        gbc_textField.insets = new Insets(0, 0, 5, 0);
        gbc_textField.fill = GridBagConstraints.BOTH;
        gbc_textField.gridx = 1;
        gbc_textField.gridy = 1;
        add(tbSearchReport, gbc_textField);
        tbSearchReport.setColumns(10);

        clientJScrollPane = new JScrollPane();
        clientJScrollPane.getVerticalScrollBar().setUnitIncrement(30);
        GridBagConstraints gbc_panel_1 = new GridBagConstraints();
        gbc_panel_1.insets = new Insets(0, 0, 5, 5);
        gbc_panel_1.fill = GridBagConstraints.BOTH;
        gbc_panel_1.gridx = 0;
        gbc_panel_1.gridy = 2;
        add(clientJScrollPane, gbc_panel_1);

        clientJPanel = new JPanel();
        clientJScrollPane.setViewportView(clientJPanel);


        reportJScrollPane = new JScrollPane();
        reportJScrollPane.getVerticalScrollBar().setUnitIncrement(30);
        GridBagConstraints gbc_panel = new GridBagConstraints();
        gbc_panel.insets = new Insets(0, 0, 5, 0);
        gbc_panel.fill = GridBagConstraints.BOTH;
        gbc_panel.gridx = 1;
        gbc_panel.gridy = 2;
        add(reportJScrollPane, gbc_panel);


        reportJPanel = new JPanel();
        reportJScrollPane.setViewportView(reportJPanel);

        btnInsertClient = new JButton("Insert Client");
        btnInsertClient.setFont(new Font("Bahnschrift", Font.PLAIN, 20));
        GridBagConstraints gbc_btnNewButton_1 = new GridBagConstraints();
        gbc_btnNewButton_1.fill = GridBagConstraints.BOTH;
        gbc_btnNewButton_1.insets = new Insets(0, 0, 0, 5);
        gbc_btnNewButton_1.gridx = 0;
        gbc_btnNewButton_1.gridy = 3;
        add(btnInsertClient, gbc_btnNewButton_1);
        btnInsertClient.addActionListener(this);

        btnInsertClientReport = new JButton("Insert Client Report");
        btnInsertClientReport.setFont(new Font("Bahnschrift", Font.PLAIN, 20));
        GridBagConstraints gbc_btnNewButton = new GridBagConstraints();
        gbc_btnNewButton.fill = GridBagConstraints.BOTH;
        gbc_btnNewButton.gridx = 1;
        gbc_btnNewButton.gridy = 3;
        add(btnInsertClientReport, gbc_btnNewButton);
        btnInsertClientReport.addActionListener(this);

        tbSearchReport.addActionListener(e -> {
            loadReports();
        });

        tbSearchClient.addActionListener(e -> {
            loadClients();
        });

        loadClients();
        loadReports();
    }

    public void loadReports()
    {
        if(currClient == null)
        {
            return;
        }
        ArrayList<Document> reports = MongoDBConnection.getInstance().getReports(UUID.fromString(currClient.getClient().getString("UUID")));
        reports = Sorting.sortReports(reports, Settings.getInstance().getReportSortBy());
        String currSearch = tbSearchReport.getText();
        if(!currSearch.isEmpty())
        {
            reports = FilterUtils.filter(reports, (document -> document.getString("Report").toLowerCase().contains(currSearch.toLowerCase())));
        }



        int amount = reports.size();
        System.out.println("Amount of reports: " + amount);
        reportJPanel.removeAll();
        reportJPanel.setLayout(new BoxLayout(reportJPanel, BoxLayout.Y_AXIS));

        for (int i = 0; i < amount; i++)
        {
            Document report = reports.get(i);
            ReportPanel reportPanel = new ReportPanel(report);
            reportJPanel.add(reportPanel);
        }

        repaint();
        revalidate();
    }

    public void loadClients()
    {
        lastFocused = null;
        currClient = null;
        reportJPanel.removeAll();
        reportJPanel.setLayout(new BoxLayout(reportJPanel, BoxLayout.Y_AXIS));

        ArrayList<Document> clients = MongoDBConnection.getInstance().getClients();
        clients = Sorting.sortClients(clients, Settings.getInstance().getClientSortBy());

        String currSearch = tbSearchClient.getText();
        if(!currSearch.isEmpty())
        {
            clients = FilterUtils.filter(clients, (document -> (document.getString("First-Name").toLowerCase()+ " " + document.getString("Last-Name").toLowerCase()).contains(currSearch.toLowerCase())));
        }

        int amount = clients.size();
        System.out.println("Amount of clients: " + amount);
        clientJPanel.removeAll();
        clientJPanel.setLayout(new BoxLayout(clientJPanel, BoxLayout.Y_AXIS));

        for (int i = 0; i < amount; i++)
        {
            Document client = clients.get(i);
            ClientPanel clientPanel = new ClientPanel(client, this);
            clientJPanel.add(clientPanel);
        }

        repaint();
        revalidate();
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        if(e.getSource().equals(btnInsertClientReport))
        {
            System.out.println("Inserting Client Report");
            if(currClient != null)
            {
                System.out.println("Inserting Report for Client: " + currClient.getClient().getString("First-Name"));
                Date date = new Date();
                String report = JOptionPane.showInputDialog("Enter Report");
                Document document = new Document();
                document.append("Client-UUID", currClient.getClient().getString("UUID"));
                document.append("Date", date);
                document.append("Report", report);
                MongoDBConnection.getInstance().insertReportDocument(java.util.UUID.randomUUID(), document);
                loadReports();
            }
            else
            {
                JOptionPane.showMessageDialog(null, "No Client Selected", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        else if(e.getSource().equals(btnInsertClient))
        {
            System.out.println("Inserting new Client");
            String fname = JOptionPane.showInputDialog("Enter First Name");
            String lname = JOptionPane.showInputDialog("Enter Last Name");
            String email = JOptionPane.showInputDialog("Enter Email");
            //gender options Female Male
            Object[] genderOptions = {"Female", "Male"};
            String gender = (String) JOptionPane.showInputDialog(null, "Choose Gender", "Gender Selection",
                    JOptionPane.QUESTION_MESSAGE, null, genderOptions, genderOptions[0]);

            int age = Integer.parseInt(JOptionPane.showInputDialog("Enter Age"));

            Document document = new Document();
            document.append("First-Name", fname);
            document.append("Last-Name", lname);
            document.append("Email", email);
            document.append("Gender", gender);
            document.append("Age", age);
            MongoDBConnection.getInstance().insertClientDocument(UUID.randomUUID(), document);
            loadClients();

            System.out.println("Inserted new Client");
        }

    }

    public void setCurrClient(ClientPanel clientPanel)
    {
        if (currClient != null)
        {
            currClient.deselect();
        }
        currClient = clientPanel;
        loadReports();
    }
}
