package com.soni.util;

import org.bson.Document;

import java.util.ArrayList;
import java.util.Comparator;

public class Sorting {

    public static ArrayList<Document> sortClients(ArrayList<Document> clients, ClientSortBy clientSortBy) {
        ArrayList<Document> sortedClients = new ArrayList<>(clients);
        Comparator<Document> comparator;

        switch (clientSortBy) {
            case AGE:
                comparator = Comparator.comparing(doc -> doc.getInteger("Age"));
                break;
            case NAME:
                comparator = Comparator.comparing(doc -> doc.getString("First-Name") + doc.getString("Last-Name"));
                break;
            case NONE:
            default:
                return clients;
        }

        return mergeSort(sortedClients, comparator);
    }

    public static ArrayList<Document> sortReports(ArrayList<Document> reports, ReportSortBy reportSortBy) {
        ArrayList<Document> sortedReports = new ArrayList<>(reports);
        Comparator<Document> comparator;

        switch (reportSortBy) {
            case NEWESTFIRST,OLDESTFIRST:
                comparator = Comparator.comparing(doc -> doc.getDate("Date"));
                if(reportSortBy == ReportSortBy.NEWESTFIRST)
                {
                    comparator = comparator.reversed();
                }
                break;
            case NONE:
            default:
                return reports;
        }

        return mergeSort(sortedReports, comparator);
    }

    private static ArrayList<Document> mergeSort(ArrayList<Document> clients, Comparator<Document> comparator) {
        if (clients.size() <= 1) {
            return clients;
        }

        int middle = clients.size() / 2;
        ArrayList<Document> left = new ArrayList<>(clients.subList(0, middle));
        ArrayList<Document> right = new ArrayList<>(clients.subList(middle, clients.size()));

        left = mergeSort(left, comparator);
        right = mergeSort(right, comparator);

        return merge(left, right, comparator);
    }

    private static ArrayList<Document> merge(ArrayList<Document> left, ArrayList<Document> right, Comparator<Document> comparator) {
        int leftIndex = 0, rightIndex = 0;
        ArrayList<Document> merged = new ArrayList<>();

        while (leftIndex < left.size() && rightIndex < right.size()) {
            if (comparator.compare(left.get(leftIndex), right.get(rightIndex)) <= 0) {
                merged.add(left.get(leftIndex++));
            } else {
                merged.add(right.get(rightIndex++));
            }
        }

        while (leftIndex < left.size()) {
            merged.add(left.get(leftIndex++));
        }
        while (rightIndex < right.size()) {
            merged.add(right.get(rightIndex++));
        }

        return merged;
    }
}
