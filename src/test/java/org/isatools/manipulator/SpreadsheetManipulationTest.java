package org.isatools.manipulator;

import org.isatools.conversion.ArrayToListConversion;
import org.isatools.conversion.Converter;
import org.isatools.io.FileType;
import org.isatools.io.Loader;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.*;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by the ISA team
 *
 * @author Eamonn Maguire (eamonnmag@gmail.com)
 *         <p/>
 *         Date: 13/05/2011
 *         Time: 12:10
 */
public class SpreadsheetManipulationTest {

    private List<String[]> testSpreadsheet;

    @Before
    public void setUp() throws Exception {
        Loader loader = new Loader();
        try {

            testSpreadsheet = loader.loadSheet("testdata/a_proteome.txt", FileType.TAB);

        } catch (IOException e) {
            e.printStackTrace();
            testSpreadsheet = null;
        }
    }


    @Test
    public void testGetColumnNames() {

        System.out.println("Testing get column names");

        String[] columnNames = SpreadsheetManipulation.getColumnHeaders(testSpreadsheet);

        for (String columnName : columnNames) {
            System.out.print(columnName + "\t");
        }
    }

    @Test
    public void testGetColumnSubset() {

        System.out.println("Testing get column subset");
        System.out.println();

        List<String[]> subset = SpreadsheetManipulation.getColumnSubset(testSpreadsheet, true, 0, 3, 4, 5);

        for (String[] columns : subset) {

            for (String columnValue : columns) {
                System.out.print(columnValue + "\t");
            }
            System.out.println();
        }
    }

    @Test
    public void testRowManipulation() {
        System.out.println("Testing get row subset");

        System.out.println();

        List<String[]> subset = SpreadsheetManipulation.getRowSubset(testSpreadsheet, 0, 3, 4, 5);

        for (String[] columns : subset) {
            for (String columnValue : columns) {
                System.out.print(columnValue + "\t");
            }
            System.out.println();
        }
    }

    @Test
    public void testGroups() {
        System.out.println("Testing get groups");

        System.out.println();

        Map<String, Set<String>> subset = SpreadsheetManipulation.getDataGroupsByColumn(testSpreadsheet, "Factor", false);

        System.out.println("Found " + subset.size() + " groups...");

        for (String group : subset.keySet()) {
            System.out.println("For group: " + group + " we have:");
            for (String sample : subset.get(group)) {
                System.out.println("\t" + sample);
            }
            System.out.println();
        }
    }

    @Test
    public void testRemoveColumn() {
        System.out.println("Testing remove column.");

        Set<Integer> indexesToRemove = new HashSet<Integer>();
        indexesToRemove.add(3);
        indexesToRemove.add(5);

        List<String[]> sheet = SpreadsheetManipulation.removeColumns(testSpreadsheet, indexesToRemove);

        assertTrue("Should have 2 less columns in spreadsheet with column removal", sheet.get(0).length == testSpreadsheet.get(0).length - 2);
        assertTrue("Should have Labeled Extract Name in position 3 now.", sheet.get(0)[3].equals("Labeled Extract Name"));
        assertTrue("Should have Term Accession Number in position 6 now.", sheet.get(0)[6].equals("MS Assay Name"));
    }

    @Test
    public void testAddColumn() {
        System.out.println("Testing add column.");

        List<String[]> sheet = SpreadsheetManipulation.insertColumn(testSpreadsheet, "Protocol REF", 5, "labeling");

        assertTrue("Should have 1 more column in spreadsheet with column removal", sheet.get(0).length == testSpreadsheet.get(0).length + 1);
        assertTrue("Should have Protocol REF in position 6 now.", sheet.get(0)[5].equals("Protocol REF"));
        assertTrue("Should have labeling in position 6 now.", sheet.get(3)[5].equals("labeling"));
        assertTrue("Should have Term Accession Number in position 6 now.", sheet.get(0)[25].equals("Term Accession Number"));

    }

    @Test
    public void testMoveColumn() {
        System.out.println("Testing move column.");

        List<String[]> sheet = SpreadsheetManipulation.moveColumn(testSpreadsheet, 0, 5);

        assertTrue("Should have the same length in both sheets.", sheet.get(0).length == testSpreadsheet.get(0).length);
        assertTrue("Should have Protocol REF in position 0 now.", sheet.get(0)[0].equals("Protocol REF"));
        assertTrue("Should have protein extraction as a value in position 1 of column 0 now.", sheet.get(1)[0].equals("protein extraction"));
        assertTrue("Should have Sample Name in row 0 of column 5.", sheet.get(0)[5].equals("Sample Name"));
        assertTrue("Should have S-0.1-aliquot11 in row 1 of column 5.", sheet.get(1)[5].equals("S-0.1-aliquot11"));

    }

    @Test
    public void testConversionToListFromArray() {
        Object[][] testArray = new Object[3][5];
        testArray[0] = new Object[]{"Sample Name", "Protocol REF", "Extract Name", "Protocol REF", "Label"};
        testArray[1] = new Object[]{"sample1", "extract", "sample1.extract", "label", "cy5"};
        testArray[2] = new Object[]{"sample2", "extract", "sample2.extract", "label", "cy3"};

        Converter<Object[][], List<String[]>> arrayToListConversion = new ArrayToListConversion();
        List<String[]> result = arrayToListConversion.convert(testArray);

        assertTrue("Result not the expected size", result.size() == 3);
    }

    @Test
    public void testGetIndexesWithColumnName() {
        Collection<Integer> indexes = SpreadsheetManipulation.getIndexesWithThisColumnName(testSpreadsheet, "Sample Name", true);

        assertTrue("Should have got 1 instance of Sample name in sheet, but I didn't.", indexes.size() == 1);
        assertTrue("Index 0 should be in here. But it isn't.", indexes.contains(0));
        assertFalse("Index 1 should not be in here. But it is.", indexes.contains(1));
    }

    @Test
    public void testGetRowInSheetWithValueForColumn() {
        String[] row = SpreadsheetManipulation.findRowWithValue(testSpreadsheet, "Sample Name", "N-0.1-aliquot11");

        assertTrue("Row should not be null.", row != null);
        assertTrue("Index 2 of this row should contain \"N-0.1\".", row[2].equals("N-0.1"));
    }
}
