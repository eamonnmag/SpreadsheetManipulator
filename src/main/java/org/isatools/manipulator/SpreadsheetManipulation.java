package org.isatools.manipulator;

import java.util.ArrayList;
import java.util.List;

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
    public String[] getColumnHeaders(List<String[]> fileContents) {
        return fileContents.get(0);
    }

    /**
     * Given a number of row indices, e.g. 0, 3, 4, 6, 7 this method will return a subset of the sheet.
     *
     * @param fileContents -  @see List<String[]> pertaining to the Rows and columns of the spreadsheet
     * @param rowIndices - a number of row indexes.
     * @return - @see List<String[]> pertaining to the Rows and columns of the spreadsheet
     */
    public List<String[]> getRowSubset(List<String[]> fileContents, int... rowIndices) {

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
     * @param fileContents - @see List<String[]> pertaining to the Rows and columns of the spreadsheet
     * @param includeColumnHeaders - whether or not to return the column headers in the resulting sheet representation
     * @param columnIndices - a number of column indices.
     * @return - @see List<String[]> pertaining to the Rows and columns of the spreadsheet
     */
    public List<String[]> getColumnSubset(List<String[]> fileContents,boolean includeColumnHeaders, int... columnIndices) {

        List<String[]> subSheet = new ArrayList<String[]>();

        for (int rowNumber = includeColumnHeaders ? 0 : 1; rowNumber < fileContents.size(); rowNumber++) {


            String[] subsetOfColumns = new String[columnIndices.length];

            int count = 0;
            for (int columnIndex : columnIndices) {

                if (columnIndex < fileContents.get(rowNumber).length) {
                    subsetOfColumns[count] = fileContents.get(rowNumber)[columnIndex];

                    count++;
                } else {
                    throw new IndexOutOfBoundsException("You requested column " + columnIndex+ "  from the spreadsheet. " +
                        "Unfortunately, this doesn't exist. Remember that column indices start at 0 and go up to ColumnNumber-1.");
                }
            }

            subSheet.add(subsetOfColumns);

        }

        return subSheet;
    }
}
