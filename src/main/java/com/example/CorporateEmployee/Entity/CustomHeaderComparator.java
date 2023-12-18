package com.example.CorporateEmployee.Entity;

import java.util.Comparator;

import org.springframework.stereotype.Component;

public class CustomHeaderComparator implements Comparator<String> {
    private final String[] desiredOrder;

    public CustomHeaderComparator(String[] desiredOrder) {
        this.desiredOrder = desiredOrder;
    }

    @Override
    public int compare(String column1, String column2) {
        // Get the index of the columns in the desired order
        int index1 = indexOf(column1);
        int index2 = indexOf(column2);

        // Compare based on the index
        return Integer.compare(index1, index2);
    }

    private int indexOf(String column) {
        for (int i = 0; i < desiredOrder.length; i++) {
            if (desiredOrder[i].equalsIgnoreCase(column)) {
                return i;
            }
        }
        // If not found, place it at the end
        return desiredOrder.length;
    }
}
