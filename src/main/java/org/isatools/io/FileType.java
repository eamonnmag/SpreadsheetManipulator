package org.isatools.io;

/**
 * Created by the ISA team
 *
 * @author Eamonn Maguire (eamonnmag@gmail.com)
 *         <p/>
 *         Date: 13/05/2011
 *         Time: 13:42
 */
public enum FileType {

    TAB('\t'), CSV(',');


    private char delimiter;

    FileType(char delimiter) {
        this.delimiter = delimiter;
    }

    public char getDelimiter() {
        return delimiter;
    }
}
