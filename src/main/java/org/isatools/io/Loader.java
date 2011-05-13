package org.isatools.io;

import au.com.bytecode.opencsv.CSVReader;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

/**
 * Created by the ISA team
 *
 * @author Eamonn Maguire (eamonnmag@gmail.com)
 *         <p/>
 *         Date: 13/05/2011
 *         Time: 11:44
 */
public class Loader {

    public List<String[]> loadSheet(String file, FileType fileType) throws IOException {
        File spreadsheetFile = new File(file);

        if(spreadsheetFile.exists()) {

            CSVReader reader = new CSVReader(new FileReader(spreadsheetFile), fileType.getDelimiter());

            return reader.readAll();

        } else {
            throw new IOException("The file " + file  + " does not exist");
        }
    }
}
