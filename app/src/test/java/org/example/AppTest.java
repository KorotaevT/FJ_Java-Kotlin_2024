package org.example;

import org.example.collection.CustomLinkedList;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

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

        assertThat(list.get(0)).isEqualTo(1);
        assertThat(list.get(1)).isEqualTo(2);
        assertThat(list.get(2)).isEqualTo(3);
    }

    @Test
    public void testGetFirst() {
        list.add(1);
        list.add(2);

        assertThat(list.getFirst()).isEqualTo(1);
    }

    @Test
    public void testRemove() {
        list.add(1);
        list.add(2);
        list.add(3);
        list.remove(1);

        assertThat(list.get(0)).isEqualTo(1);
        assertThat(list.get(1)).isEqualTo(3);
        assertThat(list.size()).isEqualTo(2);
    }

    @Test
    public void testContains() {
        list.add(1);
        list.add(2);
        list.add(null);
        list.add(3);

        assertThat(list.contains(3)).isEqualTo(true);
        assertThat(list.contains(null)).isEqualTo(true);
        assertThat(list.contains(4)).isEqualTo(false);
    }

    @Test
    public void testAddAllFromCollection() {
        list.add(1);
        list.add(3);
        List<Integer> additionalList = Arrays.asList(4, 5, 6);
        list.addAll(additionalList);

        assertThat(list.size()).isEqualTo(5);
        assertThat(list.get(2)).isEqualTo(4);
        assertThat(list.get(4)).isEqualTo(6);
    }

    @Test
    public void testAddAllFromCustomLinkedList() {
        list.add(1);
        list.add(3);
        CustomLinkedList<Integer> otherList = new CustomLinkedList<>();
        otherList.add(7);
        otherList.add(8);

        list.addAll(otherList);

        assertThat(list.size()).isEqualTo(4);
        assertThat(list.get(2)).isEqualTo(7);
        assertThat(list.get(3)).isEqualTo(8);
    }

}