package com.soni.util;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

public class FileUtils
{

    public static ArrayList<String> readLines(File file)
    {
        ArrayList<String> lines = new ArrayList<>();
        //read lines from file
        try
        {
            Scanner myReader = new Scanner(file);
            while(myReader.hasNextLine())
            {
                String data = myReader.nextLine();
                lines.add(data);
            }
            myReader.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return lines;
    }
}
