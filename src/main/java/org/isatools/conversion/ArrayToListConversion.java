package org.isatools.conversion;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by the ISA team
 *
 * @author Eamonn Maguire (eamonnmag@gmail.com)
 *         <p/>
 *         Date: 12/07/2012
 *         Time: 15:51
 */
public class ArrayToListConversion implements Converter<Object[][], List<String[]>> {

    public List<String[]> convert(Object[][] toConvert) {
        List<String[]> arrayAsList = new ArrayList<String[]>();
        for(Object[] columns : toConvert) {
            arrayAsList.add(createStringArrayFromObjectArray(columns));
        }

        return arrayAsList;
    }

    private String[] createStringArrayFromObjectArray(Object[] objectArray) {
        String[] stringArray = new String[objectArray.length];

        int count = 0;
        for(Object object : objectArray) {
            stringArray[count] = object == null ? "" : object.toString();
            count++;
        }

        return stringArray;
    }
}
