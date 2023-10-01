package com.catalog.model;

import com.catalog.util.FileStorageService;
import com.catalog.util.exception.FileStorageException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

public class Tool implements Item {
    private String id;
    private String type;
    private String manufacturer;
    private int count;

    public Tool(String type, String manufacturer, String countStr) {
        this.type = type;
        this.manufacturer = manufacturer;
        count = Integer.parseInt(countStr);
    }

    public String getType() {
        return type;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public int getCount() {
        return count;
    }

    public String getId() {
        return id;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public boolean matchesName(String searchStr) {
        return type.toLowerCase().contains(searchStr.toLowerCase());
    }

    @Override
    public boolean matchesCreator(String searchStr) {
        return manufacturer.toLowerCase().contains(searchStr.toLowerCase());
    }

    @Override
    public boolean matchesYear(int searchYear) {
        return false;
    }

    @Override
    public void registerItem() throws FileStorageException {
        this.id = UUID.randomUUID().toString();
        FileStorageService.writeContentsToFile(
                "Tool created at " + LocalDate.now() + " " + LocalTime.now() + System.lineSeparator() + this + System.lineSeparator(),
                LOG_LOCATION + "tool-" + LocalDate.now() + ".log", true);
    }

    @Override
    public String toString() {
        return "* " + type + System.lineSeparator()
                + " - Manufactured by: " + manufacturer + System.lineSeparator()
                + " - Count: " + count + System.lineSeparator()
                + " - Id: " + id;
    }
}
