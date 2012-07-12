package org.isatools.conversion;

/**
 * Created by the ISA team
 *
 * @author Eamonn Maguire (eamonnmag@gmail.com)
 *         <p/>
 *         Date: 12/07/2012
 *         Time: 15:51
 */
public interface Converter<T, V> {

    public V convert(T toConvert);
}
