package com.soni;

import com.formdev.flatlaf.FlatDarculaLaf;
import com.soni.panels.MainPanel;
import com.soni.util.MongoDBConnection;
import lombok.Getter;

import javax.swing.*;

public class Main
{
    @Getter
    private static Main instance;
    @Getter
    private Settings settings;

    public Main()
    {
        instance = this;
        settings = new Settings();

        MongoDBConnection.startConnection();

        MainFrame mainFrame = new MainFrame();
        mainFrame.setVisible(true);

        MainPanel mainPanel = new MainPanel();
        mainFrame.showPanel(mainPanel);



        mainFrame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                System.out.println("Closing");
                MongoDBConnection.stopConnection();
                Settings.getInstance().saveSettings();
            }
        });
    }
    public static void main(String[] args) throws UnsupportedLookAndFeelException
    {
        UIManager.setLookAndFeel(new FlatDarculaLaf());
        new Main();
    }
}