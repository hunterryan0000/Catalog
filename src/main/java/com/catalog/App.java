package com.catalog;

import com.catalog.model.*;
import com.catalog.util.FileStorageService;
import com.catalog.util.exception.FileStorageException;

import java.util.*;

public class App {
    // The regex string to split the Strings in the dataset.
    private static final String FIELD_DELIMITER = "\\|";
    private static final String FILE_BASE_PATH = "src/main/resources/";

    private final Scanner keyboard = new Scanner(System.in);

    private Map<String, List<Item>> catalog = new HashMap<>();

    public static void main(String[] args) {

        App app = new App();
        app.initialize();
        app.run();
    }

    private void initialize() {
        // Requirement: Data transformation

        String membersFilePath = FILE_BASE_PATH + "members.dat";
        List<String> membersData = new ArrayList<>();
        try {
            membersData = FileStorageService.readContentsOfFile(membersFilePath);
        } catch (FileStorageException fse) {
            System.out.println("File storage exception occurred. " + fse.getMessage());
        } catch (Exception e) {
            System.out.println("Exception occurred. " + e.getMessage());
        }

        if (!membersData.isEmpty()) {
            for (String member : membersData) {
                String[] fields = member.trim().split(FIELD_DELIMITER);

                if (fields.length == 3) {
                    Member m = new Member(fields[0], fields[1]);

                    List<Item> memberItems = new ArrayList<>();

                    String itemsFilePath = fields[2];
                    List<String> itemsData = new ArrayList<>();
                    try {
                        itemsData = FileStorageService.readContentsOfFile(FILE_BASE_PATH + itemsFilePath);
                    } catch (Exception e) {
                        System.out.println("Error reading items file " + itemsFilePath + System.lineSeparator() + e);
                    }

                    if (!itemsData.isEmpty()) {
                        for (String item : itemsData) {
                            if (!item.trim().isEmpty()) {
                            String[] itemFields = item.trim().split(FIELD_DELIMITER);

                            try {
                                if (itemFields.length == 4) {
                                    if (itemFields[0].equals("book")) {
                                        Book book = new Book(itemFields[1], itemFields[2], itemFields[3]);
                                        book.registerItem();
                                        memberItems.add(book);
                                    } else if (itemFields[0].equals("movie")) {
                                        Movie movie = new Movie(itemFields[1], itemFields[2], itemFields[3]);
                                        movie.registerItem();
                                        memberItems.add(movie);
                                    } else if (itemFields[0].equals("tool")) {
                                        Tool tool = new Tool(itemFields[1], itemFields[2], itemFields[3]);
                                        tool.registerItem();
                                        memberItems.add(tool);
                                    } else {
                                        throw new Exception("Unrecognized item type.");
                                    }
                                } else {
                                    throw new Exception("Line doesn't have the correct number of fields. Found " + itemFields.length + " but expected 4." + System.lineSeparator() + "\t" + item);
                                }
                            } catch (FileStorageException fse) {
                                System.out.println("File storage exception occurred. " + fse.getMessage());
                            } catch (Exception e) {
                                System.out.println("Exception occurred. " + e.getMessage());
                            }
                             }
                        }
                    }

                    catalog.put(m.toString(), memberItems);
                } else {
                    System.out.println("String doesn't have the correct number of fields. Found " + fields.length + " but expected 3." + System.lineSeparator() + "\t" + member);
                }
            }
        }
    }

    private void run() {

        if (catalog.isEmpty()) {
            System.out.println("Catalog must not empty for application to run");
            return;
        }

        while (true) {
            // Main menu loop
            printMainMenu();
            int mainMenuSelection = promptForMenuSelection("Please choose an option: ");
            if (mainMenuSelection == 1) {
                // Display data and subsets loop
                while (true) {
                    printDataAndSubsetsMenu();
                    int dataAndSubsetsMenuSelection = promptForMenuSelection("Please choose an option: ");
                    if (dataAndSubsetsMenuSelection == 1) {
                        displayFullCatalog(catalog);
                    } else if (dataAndSubsetsMenuSelection == 2) {
                        displayUsersForItemDisplay(catalog);
                        String userName = promptForString("Enter name: ");
                        displayUserItems(catalog, userName);
                    } else if (dataAndSubsetsMenuSelection == 0) {
                        break;
                    }
                }
            }
            else if (mainMenuSelection == 2) {
                while (true) {
                    printSearchMenu();
                    int searchMenuSelection = promptForMenuSelection("Please choose an option: ");
                    if (searchMenuSelection == 1) {
                        // Search by name/title
                        String searchName = promptForString("Enter name: ");
                        displayMatchesByName(catalog, searchName);
                    } else if (searchMenuSelection == 2) {
                        // Search by creator
                        String searchCreator = promptForString("Enter creator: ");
                        displayMatchesByCreator(catalog, searchCreator);
                    } else if (searchMenuSelection == 3) {
                        // Search by publish/release year
                        int searchYear = promptForYear("Enter date (YYYY): ");
                        displayMatchesByYear(catalog, searchYear);
                    } else if (searchMenuSelection == 0) {
                        break;
                    }
                }
            } else if (mainMenuSelection == 0) {
                break;
            }
        }
    }


    // UI methods

    private void printMainMenu() {
        System.out.println("1: Display catalog");
        System.out.println("2: Search catalog");
        System.out.println("0: Exit");
        System.out.println();
    }

    private void printDataAndSubsetsMenu() {
        System.out.println("1: Display full catalog");
        System.out.println("2: Display all items from a user");
        System.out.println("0: Return to main menu");
        System.out.println();
    }

    private void printSearchMenu() {
        System.out.println("1: Search items by name");
        System.out.println("2: Search items by creator");
        System.out.println("3: Search by year");
        System.out.println("0: Return to main menu");
        System.out.println();
    }

    private void displayFullCatalog(Map<String, List<Item>> catalog) {
        System.out.println("Full Catalog");
        System.out.println("-------");
        for (Map.Entry<String, List<Item>> entry : catalog.entrySet()) {
            System.out.println(entry.getKey() + ": ");
            for (Item item : entry.getValue()) {
                System.out.println(item.toString());
            }
            System.out.println();
        }
        System.out.println();
        promptForReturn();
    }

    private void displayUsersForItemDisplay(Map<String, List<Item>> catalog) {
        System.out.println("Users");
        System.out.println("-------");
        for (Map.Entry<String, List<Item>> entry : catalog.entrySet()) {
            System.out.println(entry.getKey());
        }
        System.out.println();
    }

    private void displayUserItems(Map<String, List<Item>> catalog, String userName) {
        System.out.println("Items from " + userName);
        System.out.println("-------");
        if (catalog.containsKey(userName)) {
            List<Item> userItems = catalog.get(userName);
            for (Item item : userItems) {
                System.out.println(item);
            }
        } else {
            System.out.println("No user found for '" + userName + "'");
        }
        System.out.println();
        promptForReturn();
    }

    private void displayMatchesByName(Map<String, List<Item>> catalog, String searchName) {
        System.out.println("Matches by name: " + searchName);
        System.out.println("-------");
        for (Map.Entry<String, List<Item>> entry : catalog.entrySet()) {
            boolean hasMatches = false;
            System.out.println(entry.getKey() + ": ");
            for (Item item : entry.getValue()) {
                if (item.matchesName(searchName)) {
                    System.out.println(item);
                    hasMatches = true;
                }
            }
            if (!hasMatches) {
                System.out.println("--No matching items--");
            }
            System.out.println();
        }
        System.out.println();
        promptForReturn();
    }

    private void displayMatchesByCreator(Map<String, List<Item>> catalog, String searchCreator) {
        System.out.println("Matches by creator: " + searchCreator);
        System.out.println("-------");
        for (Map.Entry<String, List<Item>> entry : catalog.entrySet()) {
            boolean hasMatches = false;
            System.out.println(entry.getKey() + ": ");
            for (Item item : entry.getValue()) {
                if (item.matchesCreator(searchCreator)) {
                    System.out.println(item);
                    hasMatches = true;
                }
            }
            if (!hasMatches) {
                System.out.println("--No matching items--");
            }
            System.out.println();
        }
        System.out.println();
        promptForReturn();
    }

    private void displayMatchesByYear(Map<String, List<Item>> catalog, int searchYear) {
        System.out.println("Matches by year: " + searchYear);
        System.out.println("-------");
        for (Map.Entry<String, List<Item>> entry : catalog.entrySet()) {
            boolean hasMatches = false;
            System.out.println(entry.getKey() + ": ");
            for (Item item : entry.getValue()) {
                if (item.matchesYear(searchYear)) {
                    System.out.println(item);
                    hasMatches = true;
                }
            }
            if (!hasMatches) {
                System.out.println("--No matching items--");
            }
            System.out.println();
        }
        System.out.println();
        promptForReturn();
    }

    private int promptForMenuSelection(String prompt) {
        System.out.print(prompt);
        int menuSelection;
        try {
            menuSelection = Integer.parseInt(keyboard.nextLine());
        } catch (NumberFormatException e) {
            menuSelection = -1;
        }
        return menuSelection;
    }

    private String promptForString(String prompt) {
        System.out.print(prompt);
        return keyboard.nextLine();
    }

    private int promptForYear(String prompt) {
        int year;
        while (true) {
            System.out.println(prompt);
            try {
                year = Integer.parseInt(keyboard.nextLine());
                break;
            } catch (NumberFormatException e) {
                System.out.println("The year provided is not well-formed. It must be YYYY.");
            }
        }
        return year;
    }

    private void promptForReturn() {
        System.out.println("Press RETURN to continue.");
        keyboard.nextLine();
    }
}
