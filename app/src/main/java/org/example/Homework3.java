package org.example;

import org.example.collection.CustomLinkedList;

import java.util.Iterator;
import java.util.stream.Stream;

import static org.example.memory.StackOverflowExample.triggerStackOverflow;

public class Homework3 {


    public static void main(String[] args) {
        CustomLinkedList<Integer> customLinkedList = createLinkedListFromStream(Stream.of(1, 2, 3, 4, 5));

        System.out.println("CustomLinkedList after converting from stream:");
        printList(customLinkedList);

        System.out.println("Demonstrating iterator:");
        demonstrateIterator(customLinkedList);
        triggerStackOverflow();
    }

    public static CustomLinkedList<Integer> createLinkedListFromStream(Stream<Integer> stream) {
        return stream.reduce(
                new CustomLinkedList<>(),
                (list, element) -> {
                    list.add(element);
                    return list;
                },
                (list1, list2) -> {
                    list1.addAll(list2);
                    return list1;
                }
        );
    }

    public static void printList(CustomLinkedList<?> list) {
        for (int i = 0; i < list.size(); i++) {
            System.out.print(list.get(i) + " -> ");
        }
        System.out.println("null");
    }

    public static void demonstrateIterator(CustomLinkedList<?> list) {
        Iterator<?> iterator = list.iterator();
        while (iterator.hasNext()) {
            System.out.print(iterator.next() + " -> ");
        }
        System.out.println("null");
    }

}