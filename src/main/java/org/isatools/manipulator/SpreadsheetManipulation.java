package org.isatools.manipulator;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by the ISA team
 *
 * @author Eamonn Maguire (eamonnmag@gmail.com)
 *         <p/>
 *         Date: 13/05/2011
 *         Time: 11:44
 */
public class SpreadsheetManipulation {

    private List<String[]> fileContents;

    public SpreadsheetManipulation(List<String[]> fileContents) {
        this.fileContents = fileContents;
    }

    /**
     * Return the column Headers
     *
     * @return the Column Headers, assuming that row 0 is where the column headers are positioned.
     */
    public String[] getColumnHeaders() {
        return fileContents.get(0);
    }

    /**
     * Given a number of row indices, e.g. 0, 3, 4, 6, 7 this method will return a subset of the sheet.
     *
     * @param rowIndices - a number of row indexes.
     * @return - @see List<String[]> pertaining to the
     */
    public List<String[]> getRowSubset(int... rowIndices) {

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

    public List<String[]> getColumnSubset(boolean includeColumnHeaders, int... columnIndices) {

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
