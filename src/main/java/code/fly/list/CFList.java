package code.fly.list;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class CFList {
    private CFList() {
    }

    /**
     * isNullOrEmpty
     */
    public static boolean isNullOrEmpty(List<?> list) {
        return list == null || list.isEmpty();
    }

    /**
     * UserObject: {name, age}
     * i.e. users = [{"Dong", 30}, {"Dong", 30}, {"Dong", 30}]
     * isAllSameItem(users) -> true
     * i.e. users = [{"Dong", 30}, {"Dong", 15}, {"Dong", 30}]
     * isAllSameItem(users) -> false
     */
    public static boolean isAllSameItem(List<?> list) {
        notEmpty(list);

        if (list.size() == 1) {
            return true;
        }

        final Object first = getHead(list);
        return list.stream().allMatch(first::equals);
    }

    /**
     * UserObject: {name, age}
     * i.e. users = [{"Dong", 30}, {"Dong", 30}, {"Dong", 30}, {"Dong", 30}, {"Dong", 30}]
     * chunk(users, 2) -> [[{"Dong", 30}, {"Dong", 30}], [{"Dong", 30}, {"Dong", 30}], [{"Dong", 30}]]
     */
    public static <T> List<List<T>> chunk(List<T> list, int perSize) {
        notNull(list);

        List<List<T>> result = new ArrayList<>();

        Iterator<T> iterator = list.iterator();
        int deep = 0;
        List<T> perList = null;

        while (iterator.hasNext()) {
            T item = iterator.next();
            if (deep == 0) {
                perList = new ArrayList<>();
            }

            perList.add(item);
            if (++deep == perSize) {
                deep = 0;
                result.add(perList);
                perList = null;
            }
        }

        if (perList != null) {
            result.add(perList);
        }
        return result;
    }

    /**
     * UserObject: {name, age} distinct key is 'name'
     * i.e. users = [{"Dong1", 30}, {"Dong2", 30}, {"Dong1", 30}, {"Dong2", 30}, {"Dong3", 30}]
     * distinct(users) -> [{"Dong1", 30}, {"Dong2", 30}, {"Dong3", 30}]
     *
     * @see Distinctable item object must implement Distinctable can be do distinct action
     */
    public static <T extends Distinctable> List<T> distinct(List<T> list) {
        notNull(list);

        if (list.size() == 1) {
            return list;
        }

        return list.stream()
                .filter(distinctByKey(Distinctable::getDistinctKey))
                .collect(Collectors.toList());
    }

    /**
     * UserObject: {name, age} distinct key is 'name'
     * i.e. users = [{"Dong1", 30}, {"Dong2", 30}, {"Dong1", 30}, {"Dong2", 30}, {"Dong3", 30}]
     * distinct(users, UserObject::getName) -> [{"Dong1", 30}, {"Dong2", 30}, {"Dong3", 30}]
     *
     * @param distinctKeyRule distinct by which key or rule
     */
    public static <T> List<T> distinct(List<T> list, Function<? super T, Object> distinctKeyRule) {
        notNull(list);

        if (list.size() == 1) {
            return list;
        }

        return list.stream()
                .filter(distinctByKey(distinctKeyRule))
                .collect(Collectors.toList());
    }

    /**
     * UserObject: {name, age}
     * i.e. users = [{"Jim", 12}, {"John", 18}, {"Tom", 21}, {"Leo", 30}, {"Kate", 44}, {"Lio", 50}]
     * <pre>
     * groupKeyRule = user -> {
     *     String key;
     *     if (user.getAge() <= 20) {
     *         key = "less20";
     *     } else if (user.getAge() <= 40) {
     *         key = "less40";
     *     } else {
     *         key = "more40";
     *     }
     *     return key;
     * }
     * </pre>
     * groupBy(users, groupKeyRule) -> {more40=[Kate, Lio], less40=[Tom, Leo], less20=[Jim, John]}
     *
     * @param groupKeyRule generate different key for each group base on your group rule
     */
    public static <T> Map<String, List<T>> groupBy(List<T> list, Function<T, String> groupKeyRule) {
        notEmpty(list);
        return list.stream()
                .collect(Collectors.groupingBy(groupKeyRule, Collectors.toList()));
    }

    /**
     * get max of collections
     */
    public static <T extends Comparable<T>> T max(List<T> list) {
        notNull(list);

        if (list.size() == 1) {
            return getHead(list);
        }

        return list.stream().max(Comparable::compareTo).orElse(null);
    }

    /**
     * get max or min of collections by given comparator
     */
    public static <T> T compareOutOne(List<T> list, Comparator<T> comparator) {
        notNull(list);

        if (list.size() == 1) {
            return getHead(list);
        }

        return list.stream().max(comparator).orElse(null);
    }

    /**
     * get min of collections
     */
    public static <T extends Comparable<T>> T min(List<T> list) {
        notNull(list);

        if (list.size() == 1) {
            return getHead(list);
        }

        return list.stream().max(((o1, o2) -> -o1.compareTo(o2))).orElse(null);
    }

    /**
     * get average of number collections
     */
    public static double average(List<? extends Number> list) {
        notNull(list);
        return list.stream().mapToDouble(Number::doubleValue).average().orElse(0);
    }

    /**
     * count event match numbers
     */
    public static <T> long countEvent(List<T> list, Predicate<T> event) {
        notNull(list);
        return list.stream().filter(event).count();
    }

    /**
     * i.e. [1, [2], [[3], 4], 5] -> [1, 2, 3, 4, 5]
     */
    public static <T> List<T> deepFlatten(List<List<T>> list) {
        notNull(list);
        return list.stream().flatMap(List::stream).collect(Collectors.toList());
    }

    /**
     * get difference list items from 'base' which 'reference' doesn't have
     * i.e. difference([1, 2, 3], [1, 2, 4]) -> [3]
     */
    public static <T> List<T> difference(List<T> base, List<T> reference) {
        notNull(base);
        notNull(reference);

        if (reference.isEmpty()) {
            return base;
        }

        return base.stream().filter(item -> !reference.contains(item)).collect(Collectors.toList());
    }

    /**
     * i.e. filterNonUnique([1, 2, 2, 3, 4, 4, 5]) -> [1, 3, 5]
     */
    public static <T> List<T> filterNonUnique(List<T> list) {
        notNull(list);

        if (list.size() == 1) {
            return list;
        }

        return list.stream().filter(item -> list.indexOf(item) == list.lastIndexOf(item)).collect(Collectors.toList());
    }

    /**
     * find first
     */
    public static <T> T findFirst(List<T> list, Predicate<T> filter) {
        notNull(list);

        return list.stream().filter(filter).findFirst().orElse(null);
    }

    /**
     * find last
     */
    public static <T> T findLast(List<T> list, Predicate<T> filter) {
        notNull(list);
        return findFirst(reverse(list), filter);
    }

    /**
     * reverse collections
     */
    public static <T> List<T> reverse(List<T> list) {
        notNull(list);

        if (list.size() < 2) {
            return list;
        }

        Collections.reverse(list);
        return list;
    }

    /**
     * get 2 lists' intersection
     * i.e. intersection([1, 2, 3], [1, 2, 4]) -> [1, 2]
     */
    public static <T> List<T> intersection(List<T> base, List<T> reference) {
        notNull(base);
        notNull(reference);

        if (reference.isEmpty() || base.isEmpty()) {
            return Collections.emptyList();
        }

        return base.stream().filter(reference::contains).collect(Collectors.toList());
    }

    private static <T> Predicate<T> distinctByKey(Function<? super T, Object> keyExtractor) {
        Map<Object, Boolean> seen = new ConcurrentHashMap<>();
        return object -> seen.putIfAbsent(keyExtractor.apply(object), Boolean.TRUE) == null;
    }

    private static void notNull(List<?> list) {
        if (list == null) {
            throw new NullPointerException();
        }
    }

    private static void notEmpty(List<?> list) {
        if (isNullOrEmpty(list)) {
            throw new NullPointerException();
        }
    }

    private static <T> T getHead(List<T> list) {
        return list.iterator().next();
    }
}
