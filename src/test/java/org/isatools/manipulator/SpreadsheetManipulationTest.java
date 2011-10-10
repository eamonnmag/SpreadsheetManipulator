package org.isatools.manipulator;

import com.google.common.collect.ListMultimap;
import com.google.common.collect.Multimap;
import org.isatools.io.FileType;
import org.isatools.io.Loader;
import org.junit.Test;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

        String[] columnNames = SpreadsheetManipulation.getColumnHeaders(load());

        for (String columnName : columnNames) {
            System.out.print(columnName + "\t");
        }
    }

    @Test
    public void testGetColumnSubset() {

        System.out.println("Testing get column subset");
        System.out.println();

        List<String[]> subset = SpreadsheetManipulation.getColumnSubset(load(), true, 0, 3, 4, 5);

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

        List<String[]> subset = SpreadsheetManipulation.getRowSubset(load(), 0, 3, 4, 5);

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

        Map<String, Set<String>> subset = SpreadsheetManipulation.getDataGroupsByColumn(load(), "Factor", false);

        System.out.println("Found " + subset.size() + " groups...");

        for (String group : subset.keySet()) {
            System.out.println("For group: " + group + " we have:");
            for(String sample : subset.get(group)) {
                System.out.println("\t" + sample);
            }
            System.out.println();
        }
    }
}
