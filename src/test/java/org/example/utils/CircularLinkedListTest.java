package org.example.utils;

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

    // ============================
    // TESTES DE DOMÍNIO
    // ============================

    // Teste de domínio: adicionando um único elemento à lista
    @Test
    void testAddingSingleElementShouldSetSizeToOne() {
        list.add("A");

        assertThat(list.size()).isEqualTo(1);
        assertThat(list.isEmpty()).isFalse();
        assertThat(list.toList()).containsExactly("A");
    }

    // Teste de domínio: adicionando múltiplos elementos mantém a ordem
    @Test
    void testAddingMultipleElementsShouldPreserveOrder() {
        list.add("A");
        list.add("B");
        list.add("C");

        assertThat(list.size()).isEqualTo(3);
        assertThat(list.toList()).containsExactly("A", "B", "C");
    }

    // Teste de domínio: acesso por índice válido
    @Test
    void testGetShouldReturnCorrectElementAtIndex() {
        list.add("A");
        list.add("B");
        list.add("C");

        assertThat(list.get(0)).isEqualTo("A");
        assertThat(list.get(1)).isEqualTo("B");
        assertThat(list.get(2)).isEqualTo("C");
    }

    // Teste de domínio: remover elemento existente por valor
    @Test
    void testRemoveElementByValueShouldShrinkSize() {
        list.add("A");
        list.add("B");
        list.add("C");

        list.remove("B");

        assertThat(list.size()).isEqualTo(2);
        assertThat(list.toList()).containsExactly("A", "C");
    }

    // Teste de domínio: remover elemento por índice válido
    @Test
    void testRemoveElementByIndexShouldShrinkSize() {
        list.add("A");
        list.add("B");
        list.add("C");

        list.remove(1); // remove "B"

        assertThat(list.size()).isEqualTo(2);
        assertThat(list.toList()).containsExactly("A", "C");
    }

    // Teste de domínio: remover o primeiro elemento da lista
    @Test
    void testRemoveElementAtZeroShouldRemoveHead() {
        list.add("A");
        list.add("B");

        list.remove(0);

        assertThat(list.toList()).containsExactly("B");
    }

    // Teste de domínio: remover um elemento que não está na lista
    @Test
    void testRemoveElemetThatsNotInTheList() {
        list.add("A");
        list.add("B");
        list.add("C");

        assertThatThrownBy(() -> list.remove("G"))
                .isInstanceOf(java.util.NoSuchElementException.class);
    }

    // Teste de domínio: iterador deve percorrer circularmente
    @Test
    void testIteratorShouldLoopCircularly() {
        list.add("A");
        list.add("B");
        list.add("C");

        Iterator<String> it = list.iterator();
        assertThat(it.next()).isEqualTo("A");
        assertThat(it.next()).isEqualTo("B");
        assertThat(it.next()).isEqualTo("C");
        assertThat(it.next()).isEqualTo("A"); // circularidade
    }

    // ============================
    // TESTES DE FRONTEIRA
    // ============================

    // Teste de fronteira: lista recém-criada deve estar vazia
    @Test
    void testNewListShouldBeEmpty() {
        assertThat(list.isEmpty()).isTrue();
        assertThat(list.size()).isZero();
    }

    // Teste de fronteira: acesso com índice negativo deve lançar exceção
    @Test
    void testGetWithNegativeIndexShouldThrow() {
        list.add("A");

        assertThatThrownBy(() -> list.get(-1))
                .isInstanceOf(IndexOutOfBoundsException.class);
    }

    // Teste de fronteira: acesso com índice igual ao tamanho da lista deve lançar exceção
    @Test
    void testGetWithIndexEqualToSizeShouldThrow() {
        list.add("A");

        assertThatThrownBy(() -> list.get(1))
                .isInstanceOf(IndexOutOfBoundsException.class);
    }

    // Teste de fronteira: remoção do único elemento da lista deve deixá-la vazia
    @Test
    void testRemoveOnlyElementShouldLeaveListEmpty() {
        list.add("X");
        list.remove("X");

        assertThat(list.size()).isZero();
        assertThat(list.isEmpty()).isTrue();
    }

    // Teste de fronteira: iterador em lista vazia deve lançar exceção
    @Test
    void testIteratorShouldThrowIfEmpty() {
        Iterator<String> it = list.iterator();

        assertThatThrownBy(it::next)
                .isInstanceOf(java.util.NoSuchElementException.class);
    }

    // Teste de fronteira: conversão de lista vazia deve retornar lista vazia
    @Test
    void testToListOnEmptyListShouldReturnEmptyList() {
        assertThat(list.toList()).isEmpty();
    }
}
