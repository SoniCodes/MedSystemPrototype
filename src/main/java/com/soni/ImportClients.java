package com.soni;

import com.soni.util.FileUtils;
import com.soni.util.MongoDBConnection;
import org.bson.Document;

import java.io.File;
import java.util.ArrayList;
import java.util.UUID;

public class ImportClients
{
    public /**static*/ void main(String[] args)
    {
        new Settings();
        MongoDBConnection.startConnection();
        File file = new File("C:\\Users\\Soni\\Desktop\\GitHub\\MedicalSystem\\data.txt");
        ArrayList<String> lines = FileUtils.readLines(file);
        for(String line : lines)
        {
            String[] parts = line.split(",");
            String fname = parts[0];
            String lname = parts[1];
            String email = parts[2];
            String gender = parts[3];

            //random age between 18 and 100
            int age = (int) (Math.random() * 82) + 18;
            Document document = new Document();
            document.append("First-Name", fname);
            document.append("Last-Name", lname);
            document.append("Email", email);
            document.append("Gender", gender);
            document.append("Age", age);

            MongoDBConnection.getInstance().insertClientDocument(UUID.randomUUID(), document);
            System.out.println("Inserted: " + document.toJson() + " into MongoDB");
        }
        Settings.getInstance().saveSettings();
        MongoDBConnection.stopConnection();

    }
}
