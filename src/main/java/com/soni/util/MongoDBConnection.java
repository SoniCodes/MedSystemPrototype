package com.soni.util;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.soni.Settings;
import lombok.Getter;
import org.bson.Document;

import java.util.ArrayList;
import java.util.UUID;

public class MongoDBConnection
{
    @Getter
    private static MongoDBConnection instance;
    private final MongoClient mongoClient;
    private final MongoDatabase medicalSystemDB;
    private final MongoCollection<Document> clients;
    private final MongoCollection<Document> reports;

    private MongoDBConnection()
    {
        instance = this;
        this.mongoClient = MongoClients.create(Settings.getInstance().getMongoDBConnection());
        this.medicalSystemDB = this.mongoClient.getDatabase("MedicalSystemDBPrototype");
        this.clients = this.medicalSystemDB.getCollection("Clients");
        this.reports = this.medicalSystemDB.getCollection("Reports");
    }

    public static void startConnection()
    {
        System.out.println("Starting MongoDB Connection");
        new MongoDBConnection();
    }

    public static void stopConnection()
    {
        System.out.println("Stopping MongoDB Connection");
        if(instance != null && instance.mongoClient != null)
        {
            instance.mongoClient.close();
        }
    }

    public void insertClientDocument(UUID uuid, Document document)
    {
        this.clients.insertOne(document.append("UUID", uuid.toString()));
    }


    public Document getClientDocument(UUID uuid)
    {
        return this.clients.find(new Document("UUID", uuid.toString())).first();
    }

    public void updateClientDocument(UUID uuid, Document document)
    {
        this.clients.updateOne(new Document("UUID", uuid.toString()), new Document("$set", document));
    }

    public void insertReportDocument(UUID uuid, Document document)
    {
        this.reports.insertOne(document.append("UUID", uuid.toString()));
    }

    public Document getReportDocument(UUID uuid)
    {
        return this.reports.find(new Document("UUID", uuid.toString())).first();
    }

    public void updateReportDocument(UUID uuid, Document document)
    {
        this.reports.updateOne(new Document("UUID", uuid.toString()), new Document("$set", document));
    }

    public ArrayList<Document> getClients()
    {
        ArrayList<Document> documents = new ArrayList<>();
        this.clients.find().forEach(documents::add);
        return documents;
    }

    public ArrayList<Document> getReports(UUID uuid){
        ArrayList<Document> documents = new ArrayList<>();
        this.reports.find(new Document("Client-UUID", uuid.toString())).forEach(documents::add);
        return documents;
    }
}