package org.isatools.manipulator;

import org.isatools.io.FileType;
import org.isatools.io.Loader;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

/**
 * Created by the ISA team
 *
 * @author Eamonn Maguire (eamonnmag@gmail.com)
 *         <p/>
 *         Date: 13/05/2011
 *         Time: 12:10
 */
public class SpreadsheetManipulationTest {


    public List<String[]> load() {
        Loader loader = new Loader();
        try {

            return loader.loadSheet("testdata/a_proteome.txt", FileType.TAB);

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Test
    public void testGetColumnNames() {

        System.out.println("Testing get column names");

        SpreadsheetManipulation manipulation = new SpreadsheetManipulation();

        String[] columnNames = manipulation.getColumnHeaders(load());

        for (String columnName : columnNames) {
            System.out.print(columnName + "\t");
        }
    }

    @Test
    public void testGetColumnSubset() {

        System.out.println("Testing get column subset");

        SpreadsheetManipulation manipulation = new SpreadsheetManipulation();

        System.out.println();

        List<String[]> subset = manipulation.getColumnSubset(load(), true, 0, 3, 4, 5);

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

        SpreadsheetManipulation manipulation = new SpreadsheetManipulation();

        System.out.println();

        List<String[]> subset = manipulation.getRowSubset(load(), 0, 3, 4, 5);

        for (String[] columns : subset) {
            for (String columnValue : columns) {
                System.out.print(columnValue + "\t");
            }
            System.out.println();
        }
    }

}
