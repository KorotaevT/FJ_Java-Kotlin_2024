package org.example;

import org.example.collection.CustomLinkedList;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

public class AppTest {

    private CustomLinkedList<Integer> list;

    @Before
    public void clean() {
        list = new CustomLinkedList<>();
    }

    @Test
    public void testAddAndGet() {
        list.add(1);
        list.add(2);
        list.add(3);

        assertEquals(Integer.valueOf(1), list.get(0));
        assertEquals(Integer.valueOf(2), list.get(1));
        assertEquals(Integer.valueOf(3), list.get(2));
    }

    @Test
    public void testGetFirst() {
        list.add(1);
        list.add(2);

        assertEquals(Integer.valueOf(1), list.getFirst());
    }

    @Test
    public void testRemove() {
        list.add(1);
        list.add(2);
        list.add(3);
        list.remove(1);

        assertEquals(Integer.valueOf(1), list.get(0));
        assertEquals(Integer.valueOf(3), list.get(1));
        assertEquals(2, list.size());
    }

    @Test
    public void testContains() {
        list.add(1);
        list.add(2);
        list.add(3);

        assertTrue(list.contains(3));
        assertFalse(list.contains(4));
    }

    @Test
    public void testAddAllFromCollection() {
        list.add(1);
        list.add(3);
        List<Integer> additionalList = Arrays.asList(4, 5, 6);
        list.addAll(additionalList);

        assertEquals(5, list.size());
        assertEquals(Integer.valueOf(4), list.get(2));
        assertEquals(Integer.valueOf(6), list.get(4));
    }

    @Test
    public void testAddAllFromCustomLinkedList() {
        list.add(1);
        list.add(3);
        CustomLinkedList<Integer> otherList = new CustomLinkedList<>();
        otherList.add(7);
        otherList.add(8);

        list.addAll(otherList);

        assertEquals(4, list.size());
        assertEquals(Integer.valueOf(7), list.get(2));
        assertEquals(Integer.valueOf(8), list.get(3));
    }

}