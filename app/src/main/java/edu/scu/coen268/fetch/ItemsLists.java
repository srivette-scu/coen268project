package edu.scu.coen268.fetch;

import android.util.Log;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemsLists {
    String TAG = this.getClass().getCanonicalName();

    private String currentList;
    private Map<String, List<String>> listMap = new HashMap<>();

    public void createList(String listName, List<String> newList) {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(listName));
        Preconditions.checkNotNull(newList);
        Log.i(TAG, "createList");

        if (listMap.entrySet().contains(listName)) {
            throw new RuntimeException("No duplicate lists permitted");
        }

        listMap.put(listName, newList);
    }

    public void setCurrentList(String listName) {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(listName));
        Log.i(TAG, "setCurrentList");

        if (!listMap.keySet().contains(listName)) {
            throw new RuntimeException("Cannot setCurrentList to non-existent list");
        }
        currentList = listName;
    }

    public void addToList(String listName, String listItem) {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(listItem));
        Preconditions.checkArgument(!Strings.isNullOrEmpty(listItem));
        Log.i(TAG, "addToList");

        if (!listMap.keySet().contains(listName)) {
            throw new RuntimeException("Cannot add to non-existant list");
        }

        listMap.get(listName).add(listItem);
    }

    public String getCurrentListName() {
        Log.i(TAG, "getCurrentListName");

        return currentList;
    }

    public void removeFromCurrentList(String item) {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(item));
        Log.i(TAG, "removeFromCurrentList");

        if (currentList == null) {
            throw new RuntimeException("Must initialize list before adding to it");
        }

        listMap.get(currentList).remove(item);
    }

    public List<String> getCurrentList() {
        Log.i(TAG, "getCurrentList");

        return listMap.get(currentList);
    }
}
