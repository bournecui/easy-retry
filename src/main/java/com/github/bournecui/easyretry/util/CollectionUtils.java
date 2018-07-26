package com.github.bournecui.easyretry.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class CollectionUtils {

    /**
     * Constant to avoid repeated object creation
     */
    private static Integer INTEGER_ONE = new Integer(1);

    /**
     * <code>CollectionUtils</code> should not normally be instantiated.
     */
    public CollectionUtils() {
    }

    /**
     * Returns a {@link Collection} containing the intersection
     * of the given {@link Collection}s.
     * <p>
     * The cardinality of each element in the returned {@link Collection}
     * will be equal to the minimum of the cardinality of that element
     * in the two given {@link Collection}s.
     *
     * @param a the first collection, must not be null
     * @param b the second collection, must not be null
     *
     * @return the intersection of the two collections
     *
     * @see Collection#retainAll
     */
    public static Collection intersection(final Collection a, final Collection b) {
        ArrayList list = new ArrayList();
        Map mapa = getCardinalityMap(a);
        Map mapb = getCardinalityMap(b);
        Set elts = new HashSet(a);
        elts.addAll(b);
        Iterator it = elts.iterator();
        while (it.hasNext()) {
            Object obj = it.next();
            for (int i = 0, m = Math.min(getFreq(obj, mapa), getFreq(obj, mapb)); i < m; i++) {
                list.add(obj);
            }
        }
        return list;
    }

    /**
     * Returns a {@link Map} mapping each unique element in the given
     * {@link Collection} to an {@link Integer} representing the number
     * of occurrences of that element in the {@link Collection}.
     * <p>
     * Only those elements present in the collection will appear as
     * keys in the map.
     *
     * @param coll the collection to get the cardinality map for, must not be null
     *
     * @return the populated cardinality map
     */
    public static Map getCardinalityMap(final Collection coll) {
        Map count = new HashMap();
        for (Iterator it = coll.iterator(); it.hasNext(); ) {
            Object obj = it.next();
            Integer c = (Integer) (count.get(obj));
            if (c == null) {
                count.put(obj, INTEGER_ONE);
            } else {
                count.put(obj, new Integer(c.intValue() + 1));
            }
        }
        return count;
    }

    /**
     * Null-safe check if the specified collection is empty.
     * <p>
     * Null returns true.
     *
     * @param coll the collection to check, may be null
     *
     * @return true if empty or null
     *
     * @since Commons Collections 3.2
     */
    public static boolean isEmpty(Collection coll) {
        return (coll == null || coll.isEmpty());
    }

    /**
     * Null-safe check if the specified collection is not empty.
     * <p>
     * Null returns false.
     *
     * @param coll the collection to check, may be null
     *
     * @return true if non-null and non-empty
     *
     * @since Commons Collections 3.2
     */
    public static boolean isNotEmpty(Collection coll) {
        return !CollectionUtils.isEmpty(coll);
    }

    private static final int getFreq(final Object obj, final Map freqMap) {
        Integer count = (Integer) freqMap.get(obj);
        if (count != null) {
            return count.intValue();
        }
        return 0;
    }
}
