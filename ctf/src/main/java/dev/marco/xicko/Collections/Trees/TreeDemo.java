package dev.marco.xicko.Collections.Trees;


import dev.marco.xicko.Collections.Trees.Arvore.ArrayBinarySearchTree;
import dev.marco.xicko.Collections.Trees.Arvore.ArrayBinaryTree;
import dev.marco.xicko.Collections.Trees.Arvore.LinkedBinarySearchTree;
import dev.marco.xicko.Collections.Trees.Arvore.LinkedBinaryTree;
import dev.marco.xicko.Collections.Trees.Interfaces.BinarySearchTreeADT;
import dev.marco.xicko.Collections.Trees.Interfaces.BinaryTreeADT;

public class TreeDemo {
    public static void main(String[] args) {
        BinarySearchTreeADT<Integer> tree = new LinkedBinarySearchTree<>(10);
        tree.addElement(5);
        tree.addElement(3);
        tree.addElement(2);
        tree.addElement(7);
        tree.addElement(30);
        tree.addElement(20);
        tree.addElement(35);
        tree.addElement(35);
        tree.addElement(70);

        System.out.println(tree);
        System.out.println(tree.removeMax());

        System.out.println(tree);

    }

}