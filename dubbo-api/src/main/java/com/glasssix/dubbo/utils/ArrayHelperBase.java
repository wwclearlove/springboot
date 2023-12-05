package com.glasssix.dubbo.utils;


import java.util.*;


public class ArrayHelperBase {

    /**
     * @param list
     * @return list
     * @Description: 去除list重复数据
     */
    public static <E> List<E> cleanDisRepet(List<E> list) {
        HashSet h = new HashSet(list);
        list.clear();
        list.addAll(h);
        return list;

    }

    /**
     * @param aList 本列表
     * @param bList 对照列表
     * @return 返回减少的元素组成的列表
     * @Description: 计算列表aList相对于bList的减少的情况，兼容任何类型元素的列表数据结构
     */
    public static <E> List<E> getReduceaListThanbList(List<E> aList, List<E> bList) {
        List<E> reduceaList = new ArrayList<E>();
        for (int i = 0; i < bList.size(); i++) {
            if (!myListContains(aList, bList.get(i))) {
                reduceaList.add(bList.get(i));
            }
        }
        return reduceaList;
    }


    public static <E> List<E> getAddaListThanbList(List<E> aList, List<E> bList) {
        List<E> addList = new ArrayList<E>();
        for (int i = 0; i < aList.size(); i++) {
            if (!myListContains(bList, aList.get(i))) {
                addList.add(aList.get(i));
            }
        }
        return addList;
    }

    /**
     * @param sourceList 源列表
     * @param element    待判断的包含元素
     * @return 包含返回 true，不包含返回 false
     * @Description: 判断元素element是否是sourceList列表中的一个子元素
     */
    private static <E> boolean myListContains(List<E> sourceList, E element) {
        if (sourceList == null || element == null) {
            return false;
        }
        if (sourceList.isEmpty()) {
            return false;
        }
        for (E tip : sourceList) {
            if (element.equals(tip)) {
                return true;
            }
        }
        return false;
    }


    /**
     * 按指定大小，分隔集合，将集合按规定个数分为n个部分
     *
     * @param <T>
     * @param list
     * @return
     */
    public static <T> List<List<T>> splitList(List<T> list) {
        int len = 1000;
        if (list == null || list.isEmpty()) {
            return Collections.emptyList();
        }
        List<List<T>> result = new ArrayList<>();
        int size = list.size();
        int count = (size + len - 1) / len;
        for (int i = 0; i < count; i++) {
            List<T> subList = list.subList(i * len, (Math.min((i + 1) * len, size)));
            result.add(subList);
        }
        return result;
    }

    public static <T> List<List<T>> splitList(List<T> list, int len) {
        if (list == null || list.isEmpty() || len < 1) {
            return Collections.emptyList();
        }
        List<List<T>> result = new ArrayList<>();
        int size = list.size();
        int count = (size + len - 1) / len;
        for (int i = 0; i < count; i++) {
            List<T> subList = list.subList(i * len, (Math.min((i + 1) * len, size)));
            result.add(subList);
        }
        return result;
    }

    /**
     * 在给定的数组中查找指定字符串，并返回命中的位置 -1:未找到
     *
     * @param list
     * @param value
     * @return
     */
    public static int indexOf(String[] list, String value) {
        if (ArrayHelperBase.isNotEmpty(list)) {
            for (int i = 0; i < list.length; i++) {
                String valueOfList = list[i];
                if (value == null) {
                    if (valueOfList == null) {
                        return i;
                    }
                } else {
                    if (value.equals(valueOfList)) {
                        return i;
                    }
                }
            }
        }
        return -1;
    }

    /**
     * 在给定的数组中查找指定字符串，并返回命中的位置 -1:未找到
     *
     * @param list
     * @param value
     * @return
     */
    public static int indexOfIgnoreCase(String[] list, String value) {
        if (ArrayHelperBase.isNotEmpty(list)) {
            for (int i = 0; i < list.length; i++) {
                String valueOfList = list[i];
                if (value == null) {
                    if (valueOfList == null) {
                        return i;
                    }
                } else {
                    if (value.equalsIgnoreCase(valueOfList)) {
                        return i;
                    }
                }
            }
        }
        return -1;
    }

    /**
     * 在给定的数组中查找指定字符串，并返回命中的位置 -1:未找到
     *
     * @param list
     * @param value
     * @return
     */
    public static int indexOf(char[] list, char value) {
        if (ArrayHelperBase.isNotEmpty(list)) {
            for (int i = 0; i < list.length; i++) {
                char valueOfList = list[i];
                if (valueOfList == value) {
                    return i;
                }
            }
        }
        return -1;
    }

    /**
     * 在给定的数组中查找指定字符串，并返回命中的位置 -1:未找到
     *
     * @param list
     * @param value
     * @return
     */
    public static int indexOfIgnoreCase(char[] list, char value) {
        char value2 = 0;
        if (value >= 'A' && value <= 'Z') {
            value2 = (char) (value + ('a' - 'A')); // 转换为小写
        } else if (value >= 'a' && value <= 'z') {
            value2 = (char) (value - ('a' - 'A')); // 转换为大写
        }
        if (ArrayHelperBase.isNotEmpty(list)) {
            for (int i = 0; i < list.length; i++) {
                char valueOfList = list[i];
                if (valueOfList == value) {
                    return i;
                }
                if (value2 != 0) {
                    if (valueOfList == value2) {
                        return i;
                    }
                }
            }
        }
        return -1;
    }

    /**
     * 在给定的数组中查找指定字符串，并返回命中的位置 -1:未找到
     *
     * @param list
     * @param value
     * @return
     */
    public static int indexOf(List<String> list, String value) {
        if (ArrayHelperBase.isNotEmpty(list)) {
            for (int i = 0; i < list.size(); i++) {
                String valueOfList = list.get(i);
                if (value == null) {
                    if (valueOfList == null) {
                        return i;
                    }
                } else {
                    if (value.equals(valueOfList)) {
                        return i;
                    }
                }
            }
        }
        return -1;
    }

    /**
     * 在给定的数组中查找指定字符串，并返回命中的位置 -1:未找到
     *
     * @param list
     * @param value
     * @return
     */
    public static int indexOfIgnoreCase(List<String> list, String value) {
        if (ArrayHelperBase.isNotEmpty(list)) {
            for (int i = 0; i < list.size(); i++) {
                String valueOfList = list.get(i);
                if (value == null) {
                    if (valueOfList == null) {
                        return i;
                    }
                } else {
                    if (value.equalsIgnoreCase(valueOfList)) {
                        return i;
                    }
                }
            }
        }
        return -1;
    }

    /**
     * 从升序列表中查询指定数据，返回位置
     *
     * @param list
     * @param value
     * @return
     */
    public static int indexOf4SortAsc(List<String> list, String value) {
        if (ArrayHelperBase.isNotEmpty(list)) {
            if (value == null) { // 对于有序列表，NULL只会出现在开始位置
                if (list.get(0) == null) {
                    return 0;
                }
            } else {
                int startIndex = 0, endIndex = list.size() - 1;
                while (endIndex >= startIndex) {
                    int index = (endIndex + startIndex) / 2;
                    String s = list.get(index);
                    int compare = value.compareTo(s);
                    if (compare == 0) {
                        return index;
                    } else if (compare < 0) { // 小于，则向左移
                        endIndex = index - 1;
                    } else { // 大于，则向右移
                        startIndex = index + 1;
                    }
                }
            }
        }
        return -1;
    }

    /**
     * 从降序列表中查询指定数据，返回位置
     *
     * @param list
     * @param value
     * @return
     */
    public static int indexOf4SortDesc(List<String> list, String value) {
        if (ArrayHelperBase.isNotEmpty(list)) {
            if (value == null) { // 对于有序列表，NULL只会出现在开始位置
                if (list.get(list.size() - 1) == null) {
                    return list.size() - 1;
                }
            } else {
                int startIndex = 0, endIndex = list.size() - 1;
                while (endIndex >= startIndex) {
                    int index = (endIndex + startIndex) / 2;
                    String s = list.get(index);
                    int compare = value.compareTo(s);
                    if (compare == 0) {
                        return index;
                    } else if (compare > 0) { // 大于，则向左移
                        endIndex = index - 1;
                    } else { // 小于，则向右移
                        startIndex = index + 1;
                    }
                }
            }
        }
        return -1;
    }

    /**
     * 是否存在。即在给定的LIST中是否存在给定的值。如果都是NULL，也认为是相同的
     *
     * @param list
     * @param value
     * @return
     */
    public static boolean isContain(String[] list, String value) {
        return indexOf(list, value) >= 0;
    }

    /**
     * 是否存在。即在给定的LIST中是否存在给定的值。如果都是NULL，也认为是相同的
     *
     * @param list
     * @param value
     * @return
     */
    public static boolean isContainIgnoreCase(String[] list, String value) {
        return indexOfIgnoreCase(list, value) >= 0;
    }

    /**
     * 是否存在。即在给定的LIST中是否存在给定的值。如果都是NULL，也认为是相同的
     *
     * @param list
     * @param value
     * @return
     */
    public static boolean isContain(char[] list, char value) {
        return indexOf(list, value) >= 0;
    }

    /**
     * 是否存在。即在给定的LIST中是否存在给定的值。如果都是NULL，也认为是相同的
     *
     * @param list
     * @param value
     * @return
     */
    public static boolean isContainIgnoreCase(char[] list, char value) {
        return indexOfIgnoreCase(list, value) >= 0;
    }

    /**
     * 是否存在。即在给定的LIST中是否存在给定的值。如果都是NULL，也认为是相同的
     *
     * @param list
     * @param value
     * @return
     */
    public static boolean isContain(List<String> list, String value) {
        return indexOf(list, value) >= 0;
    }

    /**
     * 是否存在。即在给定的LIST中是否存在给定的值。如果都是NULL，也认为是相同的
     *
     * @param list
     * @param value
     * @return
     */
    public static boolean isContainIgnoreCase(List<String> list, String value) {
        return indexOfIgnoreCase(list, value) >= 0;
    }

    /**
     * 对数组进行排序
     *
     * @param list
     * @param isAsc 是升序吗
     * @return
     */
    private static int sort(List<String> list, boolean isAsc) {
        if (list == null || list.size() == 0) {
            return 0;
        }
        int count = 0;
        for (int i = 0; i < list.size() - 1; i++) {
            for (int j = i + 1; j < list.size(); j++) {
                String valueI = list.get(i);
                String valueJ = list.get(j);
                boolean needExchange = false;
                if (valueI == null) { // 数据为NULL，则应该交换，将其置于最后
                    needExchange = true;
                } else if (valueJ != null) { // 被比较数据为NULL，则不必交换了
                    if (isAsc == true && valueI.compareTo(valueJ) > 0) {
                        needExchange = true;
                    } else if (isAsc == false && valueI.compareTo(valueJ) < 0) {
                        needExchange = true;
                    }
                }
                if (needExchange == true) {
                    list.set(i, valueJ);
                    list.set(j, valueI);
                    count++;
                }
            }
        }
        return count;
    }

    /**
     * 对数组进行排序，升序
     *
     * @param list
     * @return
     */
    public static int sortAsc(List<String> list) {
        return ArrayHelperBase.sort(list, true);
    }

    /**
     * 对数组进行排序，降序
     *
     * @param list
     * @return
     */
    public static int sortDesc(List<String> list) {
        return ArrayHelperBase.sort(list, false);
    }

    /**
     * 对数组进行排序
     *
     * @param list
     * @param isAsc 是升序吗
     * @return
     */
    private static int sort(String[] list, boolean isAsc) {
        if (list == null || list.length == 0) {
            return 0;
        }
        int count = 0;
        for (int i = 0; i < list.length - 1; i++) {
            for (int j = i + 1; j < list.length; j++) {
                String valueI = list[i];
                String valueJ = list[j];
                boolean needExchange = false;
                if (valueI == null) { // 数据为NULL，则应该交换，将其置于最后
                    needExchange = true;
                } else if (valueJ != null) { // 被比较数据为NULL，则不必交换了
                    if (isAsc == true && valueI.compareTo(valueJ) > 0) {
                        needExchange = true;
                    } else if (isAsc == false && valueI.compareTo(valueJ) < 0) {
                        needExchange = true;
                    }
                }
                if (needExchange == true) {
                    list[i] = valueJ;
                    list[j] = valueI;
                    count++;
                }
            }
        }
        return count;
    }

    /**
     * 对数组进行排序，升序
     *
     * @param list
     * @return
     */
    public static int sortAsc(String[] list) {
        return ArrayHelperBase.sort(list, true);
    }

    /**
     * 对数组进行排序，降序
     *
     * @param list
     * @return
     */
    public static int sortDesc(String[] list) {
        return ArrayHelperBase.sort(list, false);
    }

    public static String toString(List<String> list, String separator) {
        if (list == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            String s = list.get(i);
            if (i > 0) {
                sb.append(separator);
            }
            sb.append(s);
        }
        return sb.toString();
    }

    public static String toString(List<String> list) {
        return toString(list, ",");
    }

    public static String toString(String[] values, String separator) {
        if (values == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < values.length; i++) {
            String s = values[i];
            if (i > 0) {
                sb.append(separator);
            }
            sb.append(s);
        }
        return sb.toString();
    }

    public static String toString(Object[] beans) {
        return toString(beans, ",");
    }

    public static String toString(Object[] beans, String separator) {
        if (beans == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (Object obj : beans) {
            if (sb.length() > 0) {
                sb.append(separator);
            }
            sb.append(obj.toString());
        }
        return sb.toString();
    }

    public static String toString(String[] values) {
        return toString(values, ",");
    }

    /**
     * 添加不重复的元素
     *
     * @param list
     * @param newValue
     * @return 添加（即无重复）则返回TRUE，未添加，则返回FALSE
     */
    public static boolean addNoRepeat(List<String> list, String newValue) {
        boolean isExist = false;
        for (String value : list) {
            if (value.equals(newValue)) {
                isExist = true;
                break;
            }
        }
        if (isExist == false) {
            list.add(newValue);
            return true;
        }
        return false;
    }

    /**
     * 添加不重复的元素。将新值列表中的所有元素均添加到LIST中。
     *
     * @param list
     * @param newList
     * @return 返回添加数量
     */
    public static int addNoRepeat(List<String> list, List<String> newList) {
        int count = 0;
        for (String newValue : newList) {
            if (addNoRepeat(list, newValue)) {
                count++;
            }
        }
        return count;
    }

    /**
     * 倒序传入集合中的所有数据
     *
     * @param list
     */
    public static void reverse(List<?> list) {
        Collections.reverse(list);
    }

    public static boolean isEmpty(List<?> list) {
        if (list == null) {
            return true;
        }
        return list.size() == 0;
    }

    public static boolean isNotEmpty(List<?> list) {
        return !isEmpty(list);
    }

    public static boolean isEmpty(String[] list) {
        if (list == null) {
            return true;
        }
        return list.length == 0;
    }

    public static boolean isNotEmpty(String[] list) {
        return !isEmpty(list);
    }

    public static boolean isEmpty(char[] list) {
        if (list == null) {
            return true;
        }
        return list.length == 0;
    }

    public static boolean isNotEmpty(char[] list) {
        return !isEmpty(list);
    }

    public static boolean isEmpty(Integer[] list) {
        if (list == null) {
            return true;
        }
        return list.length == 0;
    }

    public static boolean isNotEmpty(Integer[] list) {
        return !isEmpty(list);
    }

    public static boolean isEmpty(int[] list) {
        if (list == null) {
            return true;
        }
        return list.length == 0;
    }

    public static boolean isNotEmpty(int[] list) {
        return !isEmpty(list);
    }

    public static boolean isEmpty(Long[] list) {
        if (list == null) {
            return true;
        }
        return list.length == 0;
    }

    public static boolean isNotEmpty(Long[] list) {
        return !isEmpty(list);
    }

    public static boolean isEmpty(long[] list) {
        if (list == null) {
            return true;
        }
        return list.length == 0;
    }

    public static boolean isNotEmpty(long[] list) {
        return !isEmpty(list);
    }

    public static String[] toStringArray(List<String> list) {
        if (list == null) {
            return null;
        }
        String[] values = new String[list.size()];
        for (int i = 0; i < list.size(); i++) {
            values[i] = list.get(i);
        }
        return values;
    }
//	public static <T> T getByFieldValue(List<T> list, String fieldName, String fieldValue) throws IllegalArgumentException, NoSuchFieldException, IllegalAccessException{
//		for(T t : list){
//			Object value = BeanHelper.getFieldValue(t, fieldName);
//			if (value == null){
//				if (fieldValue == null){
//					return t;
//				} 
//			}else{
//				if (fieldValue.equals(value)){
//					return t;
//				}
//			}
//		}
//		return null;
//	}
//	public static <T> List<T> getAllByFieldValue(List<T> list, String fieldName, String fieldValue) throws IllegalArgumentException, NoSuchFieldException, IllegalAccessException{
//		List<T> valueList = new ArrayList<T>();
//		for(T t : list){
//			Object value = BeanHelper.getFieldValue(t, fieldName);
//			if (value == null){
//				if (fieldValue == null){
//					valueList.add(t);
//				}
//			}else{
//				if (fieldValue.equals(value)){
//					valueList.add(t);
//				}
//			}
//		}
//		return valueList;
//	}

    public static <T> T[] merge(T[] array1, T[] array2, T[] arrayDest) {
        int index = 0;
        if (array1 != null) {
            for (T t : array1) {
                arrayDest[index++] = t;
            }
        }
        if (array2 != null) {
            for (T t : array2) {
                arrayDest[index++] = t;
            }
        }
        return arrayDest;
    }

    public static <T> List<T> mergeNoRepeat(List<T> list1, List<T> list2) {
        List<T> list = new ArrayList<T>();
        if (list1 != null) {
            for (T t : list1) {
                if (list.contains(t) == false) {
                    list.add(t);
                }
            }
        }
        if (list2 != null) {
            for (T t : list2) {
                if (list.contains(t) == false) {
                    list.add(t);
                }
            }
        }
        return list;
    }

    public static <T> List<T> toList(T[] values) {
        if (values == null) {
            return null;
        }
        List<T> valueList = new ArrayList<T>();
        for (T value : values) {
            valueList.add(value);
        }
        return valueList;
    }

    public static String[] toArray(String values, String separator) {
        if (values == null) {
            return null;
        }
        return values.split(separator);
    }

    public static String[] toArrayNotNull(String values, String separator) {
        if (values == null) {
            return new String[0];
        }
        return values.split(separator);
    }

    public static String[] toArray(String values) {
        return toArray(values, ",");
    }

    public static String[] toArrayNotNull(String values) {
        return toArrayNotNull(values, ",");
    }

    public static <T> List<T> removeAllIndex(List<T> valueList, List<Integer> removeIndexList) {
        if (ArrayHelperBase.isEmpty(removeIndexList) || ArrayHelperBase.isEmpty(valueList)) {
            return valueList;
        }
        // 倒序排序后，再从大到小删除
        Collections.sort(removeIndexList, new Comparator<Integer>() { // 实现整数的倒序
            public int compare(Integer arg0, Integer arg1) {
                return arg1.compareTo(arg0);
            }
        });
        for (Integer index : removeIndexList) {
            valueList.remove(index.intValue());
        }
        return valueList;
    }

    /**
     * 比较两个数组是否相同（长度相同，元素内容相同）。有一个为null，则认为不同。不要求顺序相同。
     *
     * @param value1List
     * @param value2List
     * @return
     */
    public static <T> boolean equalsContent(List<T> value1List, List<T> value2List) {
        if (value1List == null || value2List == null) {
            return false;
        }
        if (value1List.size() != value2List.size()) {
            return false;
        }
        for (T value1 : value1List) {
            boolean isExist = false;
            for (T value2 : value2List) {
                if (value1 == null) {
                    if (value2 == null) {
                        isExist = true;
                        break;
                    }
                } else if (value1.equals(value2)) {
                    isExist = true;
                    break;
                }
            }
            if (isExist == false) {
                return false;
            }
        }
        return true;
    }

    /**
     * 比较两个数组是否相同（长度相同，元素内容相同）。有一个为null，则认为不同。不要求顺序相同。
     *
     * @param value1List
     * @param value2List
     * @return
     */
    public static <T> boolean equalsContentArr(T[] value1List, T[] value2List) {
        if (value1List == null || value2List == null) {
            return false;
        }
        if (value1List.length != value2List.length) {
            return false;
        }
        for (T value1 : value1List) {
            boolean isExist = false;
            for (T value2 : value2List) {
                if (value1 == null) {
                    if (value2 == null) {
                        isExist = true;
                        break;
                    }
                } else if (value1.equals(value2)) {
                    isExist = true;
                    break;
                }
            }
            if (isExist == false) {
                return false;
            }
        }
        return true;
    }

    /**
     * 比较两个数组是否相同（长度相同，元素内容相同）。两个都NULL，认为相同。不要求顺序相同。
     *
     * @param value1List
     * @param value2List
     * @return
     */
    public static <T> boolean equalsContentWithNull(List<T> value1List, List<T> value2List) {
        if (value1List == null && value2List == null) {
            return true;
        }
        return equalsContent(value1List, value2List);
    }

    /**
     * 用于数组的判断 （长度相同，元素内容相同）。两个都NULL，认为相同。不要求顺序相同。
     *
     * @param //value1List
     * @param //value2List
     * @return
     */
    public static <T> boolean equalsContentWithNull(T[] value1Arr, T[] value2Arr) {
        if (value1Arr == null && value2Arr == null) {
            return true;
        }
        return equalsContentArr(value1Arr, value2Arr);
    }

    /**
     * 比较两个数组是否相同（长度相同，元素内容相同），排序相同。有一个为null，则认为不同。
     *
     * @param value1List
     * @param value2List
     * @return
     */
    public static <T> boolean equals(List<T> value1List, List<T> value2List) {
        if (value1List == null || value2List == null) {
            return false;
        }
        if (value1List.size() != value2List.size()) {
            return false;
        }
        for (int i = 0; i < value1List.size(); i++) {
            T value1 = value1List.get(i);
            T value2 = value2List.get(i);
            if (value1 == null) {
                if (value2 != null) {
                    return false;
                }
            } else if (value1.equals(value2) == false) {
                return false;
            }
        }
        return true;
    }

    /**
     * 比较两个数组是否相同（长度相同，元素内容相同），排序相同，两个都NULL，认为相同
     *
     * @param value1List
     * @param value2List
     * @return
     */
    public static <T> boolean equalsWithNull(List<T> value1List, List<T> value2List) {
        if (value1List == null && value2List == null) {
            return true;
        }
        return equals(value1List, value2List);
    }

    public static int getSize(List<?> valueList) {
        if (valueList != null) {
            return valueList.size();
        }
        return 0;
    }
//	public static void main(String[] argv){
//		List<String> list = new ArrayList<String>();
//		list.add("a");
//		list.add("b");
//		list.add("c");
//		//System.out.println(list);
//		for(String str : list){
//			if(StringHelper.equals(str,"a")){
//				list.remove("a");
//			}
//		}
//		List<String> sub = list.subList(1, 2);
//		System.out.println(sub);
//	}
}
