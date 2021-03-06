package org.isatools.manipulator;

import java.util.*;

/**
 * Provides a number of methods to manipulate the Spreadsheet representation (a List<String[]> where each List item
 * is a row with a number of columns) returned by the CSVReader class (http://opencsv.sourceforge.net/).
 *
 * @author Eamonn Maguire (eamonnmag@gmail.com)
 */
public class SpreadsheetManipulation {


    /**
     * Return the column Headers
     *
     * @param fileContents - @see List<String[]> pertaining to the Rows and columns of the spreadsheet
     * @return the Column Headers, assuming that row 0 is where the column headers are positioned.
     */
    public static String[] getColumnHeaders(List<String[]> fileContents) {
        return fileContents.get(0);
    }

    /**
     * Given a number of row indices, e.g. 0, 3, 4, 6, 7 this method will return a subset of the sheet.
     *
     * @param fileContents -  @see List<String[]> pertaining to the Rows and columns of the spreadsheet
     * @param rowIndices   - a number of row indexes.
     * @return - @see List<String[]> pertaining to the Rows and columns of the spreadsheet
     */
    public static List<String[]> getRowSubset(List<String[]> fileContents, int... rowIndices) {

        List<String[]> subSheet = new ArrayList<String[]>();

        for (int index : rowIndices) {
            if (index < fileContents.size()) {
                subSheet.add(fileContents.get(index));
            } else {
                throw new IndexOutOfBoundsException("You requested row " + index + "  from the spreadsheet. " +
                        "Unfortunately, this doesn't exist. Remember that row indices start at 0 and go up to RowNumber-1.");
            }
        }

        return subSheet;
    }

    /**
     * Given a number of column indices, e.g. 0, 1, 4, 5, 8 this method will return a subset of the columns in the sheet and all rows.
     *
     * @param fileContents         - @see List<String[]> pertaining to the Rows and columns of the spreadsheet
     * @param includeColumnHeaders - whether or not to return the column headers in the resulting sheet representation
     * @param columnIndices        - a number of column indices.
     * @return - @see List<String[]> pertaining to the Rows and columns of the spreadsheet
     */
    public static List<String[]> getColumnSubset(List<String[]> fileContents, boolean includeColumnHeaders, int... columnIndices) {

        List<String[]> subSheet = new ArrayList<String[]>();

        for (int rowNumber = includeColumnHeaders ? 0 : 1; rowNumber < fileContents.size(); rowNumber++) {

            String[] subsetOfColumns = new String[columnIndices.length];

            int count = 0;
            for (int columnIndex : columnIndices) {

                if (columnIndex < fileContents.get(rowNumber).length) {
                    subsetOfColumns[count] = fileContents.get(rowNumber)[columnIndex];
                    count++;
                } else {
                    throw new IndexOutOfBoundsException("You requested column " + columnIndex + "  from the spreadsheet. " +
                            "Unfortunately, this doesn't exist. Remember that column indices start at 0 and go up to ColumnNumber-1.");
                }
            }

            subSheet.add(subsetOfColumns);

        }

        return subSheet;
    }

    public static List<String[]> removeColumns(List<String[]> spreadsheet, Set<Integer> columnIndicesToRemove) {

        List<Integer> columnIndexesToKeep = new ArrayList<Integer>();
        String[] columnHeaders = getColumnHeaders(spreadsheet);

        for (int index = 0; index < columnHeaders.length; index++) {
            if (!columnIndicesToRemove.contains(index)) {
                columnIndexesToKeep.add(index);
            }
        }

        int[] indexesToKeep = new int[columnIndexesToKeep.size()];

        int count = 0;
        for (Integer index : columnIndexesToKeep) {
            indexesToKeep[count] = index;
            count++;
        }

        return getColumnSubset(spreadsheet, true, indexesToKeep);
    }

    public static List<String[]> insertColumn(List<String[]> spreadsheet, String columnName, int insertionIndex, String value) {
        List<String> values = new ArrayList<String>();
        for (int index = 0; index < spreadsheet.size(); index++) {
            values.add(value);
        }
        return insertColumn(spreadsheet, columnName, insertionIndex, values);
    }

    public static List<String[]> insertColumn(List<String[]> spreadsheet, String columnName, int insertionIndex, List<String> values) {
        int rowIndex = 0;
        List<String[]> modifiedSpreadsheet = new ArrayList<String[]>();
        for (String[] row : spreadsheet) {
            List<String> rowAsList = new ArrayList<String>();
            rowAsList.addAll(Arrays.asList(row));

            rowAsList.add(insertionIndex, rowIndex == 0 ? columnName : values.get(rowIndex - 1));

            modifiedSpreadsheet.add(rowAsList.toArray(new String[rowAsList.size()]));
            rowIndex++;
        }
        return modifiedSpreadsheet;
    }

    public static List<String[]> moveColumn(List<String[]> spreadsheet, int indexOfColumnToMove, int indexToMoveTo) {
        List<String[]> modifiedSpreadsheet = new ArrayList<String[]>();
        for (String[] row : spreadsheet) {
            List<String> rowAsList = new ArrayList<String>();
            rowAsList.addAll(Arrays.asList(row));

            String value = rowAsList.remove(indexOfColumnToMove);
            rowAsList.add(indexToMoveTo, value);

            modifiedSpreadsheet.add(rowAsList.toArray(new String[rowAsList.size()]));
        }
        return modifiedSpreadsheet;
    }

    /**
     * Groups up types of columns such as factors and returns the concatenation of those values on a per row basis.
     *
     * @param fileContents - Representation of file, like that emitted from the Loader. @see Loader
     * @param group        - e.g. Factor, Characteristic
     * @param exactMatch   - if the Group should be an exact match to a table value (e.g. Factor could be any factor but Factor Value[Run Time] would be a perfect match.)
     * @return ListMultimap<String, List<Object>> where list will be Strings if return Sample Names or Row indexes
     */
    public static Map<String, Set<String>> getDataGroupsByColumn(List<String[]> fileContents, String group, boolean exactMatch) {
        return getDataGroupsWithTypeByColumn(fileContents, group, exactMatch, false);
    }

    /**
     * Groups up types of columns such as factors and returns the concatenation of those values on a per row basis.
     *
     * @param fileContents       - Representation of file, like that emitted from the Loader. @see Loader
     * @param group              - e.g. Factor, Characteristic
     * @param exactMatch         - if the Group should be an exact match to a table value (e.g. Factor could be any factor but Factor Value[Run Time] would be a perfect match.)
     * @param includeColumnNames - will return the name of the column corresponding to the value
     * @return ListMultimap<String, List<Object>> where list will be Strings if return Sample Names or Row indexes
     */
    public static Map<String, Set<String>> getDataGroupsWithTypeByColumn(List<String[]> fileContents, String group, boolean exactMatch, boolean includeColumnNames) {
        Map<String, Set<String>> groups = new HashMap<String, Set<String>>();

        String[] columnNames = SpreadsheetManipulation.getColumnHeaders(fileContents);

        boolean allowedUnit = false;
        for (int row = 1; row < fileContents.size(); row++) {
            String groupVal = "";
            for (int col = 0; col < columnNames.length; col++) {
                String column = columnNames[col];

                boolean match = false;

                if (exactMatch) {
                    if (column.equalsIgnoreCase(group)) {
                        match = true;
                    } else if (allowedUnit && column.equalsIgnoreCase("unit")) {
                        match = true;
                    }
                } else {
                    if (column.contains(group)) {
                        match = true;
                    } else if (allowedUnit && column.equalsIgnoreCase("unit")) {
                        match = true;
                    }
                }

                if (match) {
                    groupVal += " " + (includeColumnNames ? column : "") + " " + fileContents.get(row)[col];
                    allowedUnit = true;
                } else allowedUnit = column.contains("Term Source REF") || column.contains("Term Accession Number");
            }
            if (!groupVal.equals("")) {
                groupVal = groupVal.trim();

                if (!groups.containsKey(groupVal)) {
                    groups.put(groupVal, new HashSet<String>());
                }

                groups.get(groupVal).add(getColValAtRow(fileContents, columnNames, "Sample Name", row));
            }
        }

        return groups;
    }

    public static String getColValAtRow(List<String[]> fileContents, String[] columnNames, String colName, int rowNumber) {
        for (int col = 0; col < columnNames.length; col++) {

            if (columnNames[col].equalsIgnoreCase(colName)) {
                // safety precaution to finalise any cells. otherwise their value would be missed!
                return fileContents.get(rowNumber)[col];
            }
        }
        return "";
    }

    public static Set<Integer> getIndexesWithThisColumnName(List<String[]> fileContents, String lookingFor, boolean ignoreCase) {
        Set<Integer> columnIndexes = new HashSet<Integer>();

        int index = 0;
        for (String columnName : getColumnHeaders(fileContents)) {
            if (ignoreCase ? columnName.equalsIgnoreCase(lookingFor) : columnName.equals(lookingFor)) {
                columnIndexes.add(index);
            }
            index++;
        }

        return columnIndexes;
    }

    public static String[] findRowWithValue(List<String[]> fileContents, String columnName, String value) {
        // getting the indexes in advance means we only check the columns that we actually want, not all.
        Set<Integer> columnIndexesToLookAt = getIndexesWithThisColumnName(fileContents, columnName, true);
        for (String[] row : fileContents) {
            for (Integer columnIndex : columnIndexesToLookAt) {
                if (row[columnIndex].equals(value)) {
                    return row;
                }
            }
        }
        return null;
    }

    public static void printSpreadsheet(List<String[]> spreadsheet) {
        for (String[] row : spreadsheet) {
            for (String column : row) {
                System.out.print(column + "\t");
            }
            System.out.print("\n");
        }
    }
}
