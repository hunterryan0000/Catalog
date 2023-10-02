# Catalog

## Table of Contents
1. [About The Project](#about-the-project)
2. [Features](#features)
3. [Use Cases](#use-cases)
4. [Getting Started](#getting-started)

## About The Project

The design of this application is a catalog of items that users of the application can lend to each other. Instead of reading the data in from a hard-coded array, you'll read the data in from text files. The dataset has been expanded to include movies and tools, and is in multiple files. Practices and technologies used:

* Using an unordered collection
* Splitting and parsing strings
* Implementing and using classes
* Implementing polymorphism using an interface
* Read and parse files
* Write to files

## Features

* `App.java` - The "main" class of the application
* `model/CatalogItem.java` - An interface to implement specific methods on catalog items
* `util/FileStorageService.java` - A File I/O class for you to put your I/O logic in and use in your code
* `util/exception/FileStorageException.java` - A custom exception for the `FileStorageService` class

## Use Cases

### Requirement: Class design

Create four classes in the `model` folder/package. Each class has a list of members and methods that it must implement.

Each class must have the appropriate constructor to create a new instance of the object with the fields from the data files. The `id` field doesn't exist in the data file, so don't pass that into the constructor. See `registerItem()` in the following [Interface methods](#interface-methods) section for more information.

The `toString()` method for `Member` must be first name and last name separated by a space. You can use any format you want for the item `toString()` methods, as long as it displays all four fields. Keep in mind that the UI uses the `toString()` method to display items in the UI, so readability is a concern.

#### Implementation of interface methods

The `CatalogItem` interfaces defines four methods that the `Book`, `Movie`, and `Tool` classes must implement:

* `matchesName(String searchStr)`
    * Return a boolean indicating if the "name" field contains the search string `searchStr`. The match must be case-insensitive.
    * The "name" field for the classes are:
        * `Book.title`
        * `Movie.name`
        * `Tool.type`

* `matchesCreator(String searchStr)`
    * Return a boolean indicating if the "creator" field contains the search string `searchStr`. The match must be case-insensitive.
    * The "creator" field for the classes are:
        * `Book.author`
        * `Movie.director`
        * `Tool.manufacturer`

* `matchesYear(int searchStr)`
    * Return a boolean indicating if the "date" field is in year specified in the parameter `searchYear`.
    * The "date" field for the classes are:
        * `Book.publishDate`
        * `Movie.releaseDate`
        * `Tool` has no year field, so this method must only return `false`

* `registerItem()`
    * This method assigns a unique ID to the `id` field. Use the code `UUID.randomUUID().toString()` to generate a **universally unique identifier**—or UUID—and assign it to the `id` field.
        * Note: this is a common technique for creating a unique ID in code. This is also sometimes referred to a *globally unique identifier* or GUID.
    * Write a message to a log file indicating that the book, movie, or tool was created. A good log entry includes the date, time, and properties of the object. Consider separate log files, one for each type. You can write your log files to `src/main/resources/logs` in the project folder.
        * Hint: use the `toString()` method to print the properties to the log.
        * See the following [File I/O](#file-io) section for more information.

### Requirement: File I/O

The `FileStorageService` class provides two `static` methods, one to write to a file, the other to read from a file. These methods can be called in your code without instantiating a `FileStorageService` object, you can just call `FileStorageService.writeContentsToFile(...)` and `FileStorageService.readContentsOfFile(...)` with the appropriate parameters.

You must implement the appropriate logic to read and write from files, along with handling any issues that may arise.

### Requirement: Data transformation

The `initialize()` method in the `App` class is for setting up the data for the application. You can create other methods to assist with processing the data if you wish, as long as everything occurs from the call of `initialize()` and before the call to `run()`.

You'll store the data you import from the `items-*.dat` files in the collection `Map<String, List<CatalogItem>> catalog` defined near the top of the `App` class.

The `members.dat` file contains three fields separated by a pipe character—`|`. The fields are in the order of:

1) First name
2) Last name
3) Name of the file that contains their items

The corresponding `items-*` files have four fields separated by a pipe character—`|`. The fields are in the order of:

1) Item type—such as book, movie, or tool
2) The book title, movie name, or tool type
3) The book author, movie director, or tool manufacturer
4) The book publish date, movie release date, or number of tools that member has for lending

Items may appear in any order in the file—for example, you might find a book listed between two movies. The fields are always in the same order.

Your code for processing the items must make sure to handle:

* Empty lines
* Lines that have too many or too few fields
* An item type that isn't "book", "movie", or "tool"

As you process each item, you must:

1) Instantiate it
2) Call its `registerItem()` method
3) Add it to the collection

## Getting Started

The project provides a basic menu interface to retrieve and print data. Option 1 gives you the ability to print the entire catalog, or just a particular user's catalog items. Option 2 has the search functions of the application. You can search by name, creator, and year.
