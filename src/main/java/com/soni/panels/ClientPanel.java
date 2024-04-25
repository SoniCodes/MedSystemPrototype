package com.soni.panels;

import lombok.Getter;
import org.bson.Document;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;

public class ClientPanel extends JPanel
{
    @Getter
    private Document client;
    private JButton btnSelect;
    private JTextField textField;
    private MainPanel instance;
    public ClientPanel(Document client, MainPanel instance)
    {
        this.client = client;
        this.instance = instance;
        setLayout(new MigLayout("", "[grow,fill][32px:50px:50px,grow,fill]", "[grow,fill]"));

        btnSelect = new JButton(" ");
        btnSelect.addActionListener(e -> {
            select(instance);
        });
        String displayString = client.getString("First-Name") + " " + client.getString("Last-Name") + " - " + client.getInteger("Age");

        textField = new JTextField(displayString);
        textField.setHorizontalAlignment(SwingConstants.LEADING);
        textField.setEditable(false);
        textField.setFont(new Font("Bahnschrift", Font.PLAIN, 25));
        textField.setColumns(10);

        textField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                if(instance.getLastFocused() != null) {
                    instance.getLastFocused().setFocusable(true);
                }
                select(instance);
                ((JTextField)evt.getSource()).setFocusable(false);
                instance.setLastFocused((JTextField)evt.getSource());
            }
        });
        add(textField, "cell 0 0,grow");
        add(btnSelect, "cell 1 0,grow");

    }

    private void select(MainPanel instance)
    {
        btnSelect.setBackground(Color.GREEN);
        instance.setCurrClient(this);
    }

    public void deselect()
    {
        btnSelect.setBackground(new JButton().getBackground());
    }
}
