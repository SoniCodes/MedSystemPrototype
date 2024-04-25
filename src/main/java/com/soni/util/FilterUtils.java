package com.soni.util;

import org.bson.Document;

import java.util.ArrayList;

public class FilterUtils
{
    public static ArrayList<Document> filter(ArrayList<Document> documents, Filter filter)
    {
        ArrayList<Document> filteredDocuments = new ArrayList<>();
        for(Document document : documents)
        {
            if(filter.getFilter(document))
            {
                filteredDocuments.add(document);
            }
        }
        return filteredDocuments;
    }

    public interface Filter
    {
        boolean getFilter(Document document);
    }
}
