package com.wanaright.array;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class CFListTest {

    @Test
    public void allSameItem() {
        List<UserObject> users = new ArrayList<>();
        users.add(new UserObject("Dong", 30));
        users.add(new UserObject("Dong", 30));
        users.add(new UserObject("Dong", 30));

        Assert.assertTrue(CFList.isAllSameItem(users));
    }

    @Test
    public void chunk() {
        List<UserObject> users = new ArrayList<>();
        users.add(new UserObject("Dong", 30));
        users.add(new UserObject("Dong", 30));
        users.add(new UserObject("Dong", 30));
        users.add(new UserObject("Dong", 30));
        users.add(new UserObject("Dong", 30));

        List<List<UserObject>> chunked = CFList.chunk(users, 2);

        Assert.assertEquals(chunked.size(), 3);
    }

    @Test
    public void distinct() {
        List<UserObject> users = new ArrayList<>();
        users.add(new UserObject("Dong1", 30));
        users.add(new UserObject("Dong2", 30));
        users.add(new UserObject("Dong1", 30));
        users.add(new UserObject("Dong2", 30));
        users.add(new UserObject("Dong3", 30));

        List<UserObject> distinctList = CFList.distinct(users);

        Assert.assertEquals(distinctList.size(), 3);
    }

    @Test
    public void distinctByRule() {
        List<UserObject> users = new ArrayList<>();
        users.add(new UserObject("Dong1", 30));
        users.add(new UserObject("Dong2", 30));
        users.add(new UserObject("Dong1", 30));
        users.add(new UserObject("Dong2", 30));
        users.add(new UserObject("Dong3", 30));

        List<UserObject> distinctList = CFList.distinct(users, UserObject::getName);

        Assert.assertEquals(distinctList.size(), 3);
    }

    @Test
    public void groupBy() {
        List<UserObject> users = new ArrayList<>();
        users.add(new UserObject("Dong1", 10));
        users.add(new UserObject("Dong2", 20));
        users.add(new UserObject("Dong3", 30));
        users.add(new UserObject("Dong4", 40));
        users.add(new UserObject("Dong5", 50));
        users.add(new UserObject("Dong6", 60));

        Map<String, List<UserObject>> groupUsers = CFList.groupBy(users, user -> {
            if (user.getAge() <= 20) {
                return "less30";
            } else if (user.getAge() <= 40) {
                return "less40";
            } else {
                return "more40";
            }
        });

        Assert.assertEquals(groupUsers.size(), 3);
    }

    @Test
    public void max() {
        List<Integer> integers = new ArrayList<>();
        integers.add(1);
        integers.add(2);
        integers.add(-1);
        integers.add(10);

        Assert.assertEquals(CFList.max(integers).intValue(), 10);
    }

    @Test
    public void min() {
        List<Integer> integers = new ArrayList<>();
        integers.add(1);
        integers.add(2);
        integers.add(-1);
        integers.add(10);

        Assert.assertEquals(CFList.min(integers).intValue(), -1);
    }

    @Test
    public void compareOutOne() {
        List<Integer> integers = new ArrayList<>();
        integers.add(1);
        integers.add(2);
        integers.add(-1);
        integers.add(10);

        Assert.assertEquals(CFList.compareOutOne(integers, Comparator.comparingInt(Integer::intValue)).intValue(), 10);
    }

    @Test
    public void average() {
        List<Integer> integers = new ArrayList<>();
        integers.add(1);
        integers.add(2);
        integers.add(-1);
        integers.add(10);

        Assert.assertEquals(3D, CFList.average(integers), 0.0);
    }

    @Test
    public void countEvent() {
        List<UserObject> users = new ArrayList<>();
        users.add(new UserObject("Dong1", 10));
        users.add(new UserObject("Dong2", 20));
        users.add(new UserObject("Dong3", 30));
        users.add(new UserObject("Dong4", 40));
        users.add(new UserObject("Dong5", 50));
        users.add(new UserObject("Dong6", 60));

        Assert.assertEquals(CFList.countEvent(users, user -> user.getAge() < 30), 2);
    }

    @Test
    public void deepFlatten() {
        List<UserObject> users1 = new ArrayList<>();
        users1.add(new UserObject("Dong1", 10));
        users1.add(new UserObject("Dong2", 20));

        List<UserObject> users2 = new ArrayList<>();
        users2.add(new UserObject("Dong3", 10));
        users2.add(new UserObject("Dong4", 20));

        List<UserObject> users3 = new ArrayList<>();
        users3.add(new UserObject("Dong5", 20));

        List<List<UserObject>> users = new ArrayList<>();
        users.add(users1);
        users.add(users2);
        users.add(users3);

        List<UserObject> userObjects = CFList.deepFlatten(users);

        Assert.assertEquals(userObjects.size(), 5);
    }

    @Test
    public void difference() {
        List<UserObject> users1 = new ArrayList<>();
        users1.add(new UserObject("Dong1", 10));
        users1.add(new UserObject("Dong2", 20));

        List<UserObject> users2 = new ArrayList<>();
        users2.add(new UserObject("Dong1", 10));
        users2.add(new UserObject("Dong4", 20));

        List<UserObject> differenceItems = CFList.difference(users1, users2);

        Assert.assertEquals(differenceItems.size(), 1);
        Assert.assertEquals(differenceItems.get(0), new UserObject("Dong2", 20));
    }

    @Test
    public void filterNonUnique() {
        List<UserObject> users = new ArrayList<>();
        users.add(new UserObject("Dong1", 10));
        users.add(new UserObject("Dong2", 20));
        users.add(new UserObject("Dong2", 20));
        users.add(new UserObject("Dong3", 40));
        users.add(new UserObject("Dong4", 50));
        users.add(new UserObject("Dong4", 50));

        List<UserObject> nonUnique = CFList.filterNonUnique(users);

        Assert.assertEquals(nonUnique.size(), 2);
    }

    @Test
    public void findLast() {
        List<UserObject> users = new ArrayList<>();
        users.add(new UserObject("Dong1", 10));
        users.add(new UserObject("Dong2", 20));
        users.add(new UserObject("Dong3", 20));
        users.add(new UserObject("Dong4", 40));
        users.add(new UserObject("Dong5", 20));
        users.add(new UserObject("Dong6", 60));

        UserObject last = CFList.findLast(users, userObject -> userObject.getAge() == 20);

        Assert.assertEquals(last.getName(), "Dong5");
    }

    private static class UserObject implements Distinctable {
        private String name;
        private int age;

        @Override
        public Object getDistinctKey() {
            return name;
        }

        public String getName() {
            return name;
        }

        public int getAge() {
            return age;
        }

        public UserObject(String name, int age) {
            this.name = name;
            this.age = age;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            UserObject that = (UserObject) o;

            if (age != that.age) return false;
            return name != null ? name.equals(that.name) : that.name == null;
        }

        @Override
        public int hashCode() {
            int result = name != null ? name.hashCode() : 0;
            result = 31 * result + age;
            return result;
        }
    }
}