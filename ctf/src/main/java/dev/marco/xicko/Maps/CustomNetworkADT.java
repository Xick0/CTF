package dev.marco.xicko.Maps;

import dev.marco.xicko.Collections.Grafos.NetworkADT;

public interface CustomNetworkADT<T> extends NetworkADT<T> {
    void importFromJson();
    void generateRandomGraph();
    void exportToJson(String filename);
    public Iterable<Integer> getAdjacentVertices(int vertex);

}
