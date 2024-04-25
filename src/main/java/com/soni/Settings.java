package com.soni;

import com.soni.util.ClientSortBy;
import com.soni.util.FileUtils;
import com.soni.util.ReportSortBy;
import lombok.Data;
import lombok.Getter;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

@Data
public class Settings
{
    @Getter
    private static Settings instance;

    private String mongoDBConnection;
    private ClientSortBy clientSortBy;
    private ReportSortBy reportSortBy;
    public Settings()
    {
        instance = this;
        File settingsFile = new File("medicalSystemsSettings.json");

        JSONParser parser = new JSONParser();
        try
        {
            Object obj = parser.parse(new FileReader(settingsFile));
            JSONObject jsonObject = (JSONObject) obj;
            mongoDBConnection = (String) jsonObject.get("mongoDBConnection");
            clientSortBy = ClientSortBy.valueOf((String) jsonObject.get("clientSort"));
            reportSortBy = ReportSortBy.valueOf((String) jsonObject.get("reportSort"));
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        System.out.println("Settings loaded");
    }

    public void saveSettings()
    {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("mongoDBConnection", instance.getMongoDBConnection());
        jsonObject.put("clientSort", instance.getClientSortBy().name());
        jsonObject.put("reportSort", instance.getReportSortBy().name());

        File settingsFile = new File("medicalSystemsSettings.json");

        try (FileWriter file = new FileWriter(settingsFile)) {
            file.write(jsonObject.toJSONString());
            file.flush();
            System.out.println("Settings saved successfully");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
