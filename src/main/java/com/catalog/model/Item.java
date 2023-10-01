package com.catalog.model;

import com.catalog.util.exception.FileStorageException;

public interface Item {
    boolean matchesName(String searchStr);
    boolean matchesCreator(String searchStr);
    boolean matchesYear(int searchYear);
    void registerItem() throws FileStorageException; // added throws exception

    String LOG_LOCATION = "src/main/resources/logs/";
}
