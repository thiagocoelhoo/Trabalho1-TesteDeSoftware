package org.example;

import org.example.utils.CircularLinkedList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Iterator;

import static org.assertj.core.api.Assertions.*;

class CircularLinkedListTest {
    private CircularLinkedList<String> list;

    @BeforeEach
    void setUp() {
        list = new CircularLinkedList<>();
    }

    @Test
    void testNewListShouldBeEmpty() {
        assertThat(list.isEmpty()).isTrue();
        assertThat(list.size()).isZero();
    }

    @Test
    void testAddingSingleElementShouldSetSizeToOne() {
        list.add("A");

        assertThat(list.size()).isEqualTo(1);
        assertThat(list.isEmpty()).isFalse();
        assertThat(list.toList()).containsExactly("A");
    }

    @Test
    void testAddingMultipleElementsShouldPreserveOrder() {
        list.add("A");
        list.add("B");
        list.add("C");

        assertThat(list.size()).isEqualTo(3);
        assertThat(list.toList()).containsExactly("A", "B", "C");
    }

    @Test
    void testGetShouldReturnCorrectElementAtIndex() {
        list.add("A");
        list.add("B");
        list.add("C");

        assertThat(list.get(0)).isEqualTo("A");
        assertThat(list.get(1)).isEqualTo("B");
        assertThat(list.get(2)).isEqualTo("C");
    }

    @Test
    void testGetWithNegativeIndexShouldThrow() {
        list.add("A");

        assertThatThrownBy(() -> list.get(-1))
                .isInstanceOf(IndexOutOfBoundsException.class);
    }

    @Test
    void testGetWithIndexEqualToSizeShouldThrow() {
        list.add("A");

        assertThatThrownBy(() -> list.get(1))
                .isInstanceOf(IndexOutOfBoundsException.class);
    }

    @Test
    void testRemoveElementByValueShouldShrinkSize() {
        list.add("A");
        list.add("B");
        list.add("C");

        list.remove("B");

        assertThat(list.size()).isEqualTo(2);
        assertThat(list.toList()).containsExactly("A", "C");
    }

    @Test
    void testRemoveElementByIndexShouldShrinkSize() {
        list.add("A");
        list.add("B");
        list.add("C");

        list.remove(1); // remove B

        assertThat(list.size()).isEqualTo(2);
        assertThat(list.toList()).containsExactly("A", "C");
    }

    @Test
    void testRemoveElementAtZeroShouldRemoveHead() {
        list.add("A");
        list.add("B");

        list.remove(0);

        assertThat(list.toList()).containsExactly("B");
    }

    @Test
    void testRemoveOnlyElementShouldLeaveListEmpty() {
        list.add("X");
        list.remove("X");

        assertThat(list.size()).isZero();
        assertThat(list.isEmpty()).isTrue();
    }

    @Test
    void testIteratorShouldLoopCircularly() {
        list.add("A");
        list.add("B");
        list.add("C");

        Iterator<String> it = list.iterator();
        assertThat(it.next()).isEqualTo("A");
        assertThat(it.next()).isEqualTo("B");
        assertThat(it.next()).isEqualTo("C");
        assertThat(it.next()).isEqualTo("A"); // Circularity
    }

    @Test
    void testIteratorShouldThrowIfEmpty() {
        Iterator<String> it = list.iterator();

        assertThatThrownBy(it::next)
                .isInstanceOf(java.util.NoSuchElementException.class);
    }

    @Test
    void testToListOnEmptyListShouldReturnEmptyList() {
        assertThat(list.toList()).isEmpty();
    }

    @Test
    void testRemoveElemetThatsNotInTheList() {
        list.add("A");
        list.add("B");
        list.add("C");

        assertThatThrownBy(() -> list.remove("G"))
                .isInstanceOf(java.util.NoSuchElementException.class);
    }
}
