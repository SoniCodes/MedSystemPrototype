package com.soni.panels;

import net.miginfocom.swing.MigLayout;
import org.bson.Document;

import javax.swing.*;
import java.awt.*;

public class ReportPanel extends JPanel
{
    private Document client;
    private JTextField textField;
    public ReportPanel(Document client)
    {
        this.client = client;
        setLayout(new MigLayout("", "[grow,fill]"));

        String displayString = client.getString("Report") + " - " + client.getDate("Date");

        textField = new JTextField(displayString);
        textField.setHorizontalAlignment(SwingConstants.LEADING);
        textField.setEditable(false);
        textField.setFont(new Font("Bahnschrift", Font.PLAIN, 25));
        textField.setColumns(10);

        add(textField, "cell 0 0,grow");

    }
}
