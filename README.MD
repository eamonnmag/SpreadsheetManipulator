#How to use

##Loading files

This part relies on the opencsv (http://opencsv.sourceforge.net/) project.

##To load a TAB file, call the Loader class as follows.

```Loader loader = new Loader();
try {
    List<String[]> spreadsheet = loader.loadSheet("file", FileType.TAB);
} catch (IOException e) {
    e.printStackTrace();
}```

##To load a CSV file, call the Loader class as follows.

```Loader loader = new Loader();
try {
    List<String[]> spreadsheet = loader.loadSheet("file", FileType.CSV);
} catch (IOException e) {
    e.printStackTrace();
}```

##Manipulating the spreadsheet object

When we load the CSV/TAB file, the resulting Object will be a List<String[]>. Each list item is a row. From this, we
can use the SpreadsheetManipulation class to perform some operations.

### Get the column names
```SpreadsheetManipulation manipulation = new SpreadsheetManipulation();
String[] columnNames = manipulation.getColumnHeaders(spreadsheet);```

### Get a subset of columns and values from the spreadsheet
```SpreadsheetManipulation manipulation = new SpreadsheetManipulation();
// You can specify as many columns as you like in this call
List<String[]> subset = manipulation.getColumnSubset(spreadsheet, true, 0, 3, 4, 5);```

### Get a subset of rows and values from the spreadsheet
```SpreadsheetManipulation manipulation = new SpreadsheetManipulation();
List<String[]> subset = manipulation.getRowSubset(spreadsheet, 0, 1, 2, 6);```



