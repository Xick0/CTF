package dev.marco.xicko.Collections.Heaps;

import dev.marco.xicko.Collections.Trees.Arvore.BinaryTreeNode;

public class HeapNode<T> extends BinaryTreeNode<T> {
    protected HeapNode<T> parent;

    HeapNode(T obj) {
        super(obj);
        parent = null;
    }

}
