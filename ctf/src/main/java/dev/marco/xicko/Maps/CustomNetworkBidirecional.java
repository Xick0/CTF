package dev.marco.xicko.Maps;

import dev.marco.xicko.Collections.Grafos.NetworkBiDirectional;
import dev.marco.xicko.Collections.ListasIterador.Classes.ArrayUnorderedList;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;
import dev.marco.xicko.Collections.Grafos.NetworkBiDirectional;

public class CustomNetworkBidirecional<T> extends NetworkBiDirectional<T> implements CustomNetworkADT<T> {

    public void generateRandomGraph() {
        System.out.println("Enter the number of vertices: ");
        int numberOfVertices = new Scanner(System.in).nextInt();
        Random rand = new Random();
        boolean isConnected;

        do {
            // Resize the vertex array and the adjacency matrix
            vertices = (T[]) new Object[numberOfVertices];
            adjMatrix = new double[numberOfVertices][numberOfVertices];

            // Initialize vertices
            for (int i = 0; i < numberOfVertices; i++) {
                vertices[i] = (T) Integer.valueOf(i); // Each vertex is an Integer
            }

            // Calculate the number of edges to be added
            int numEdges = (int) ((numberOfVertices * (numberOfVertices - 1)) / 2.0 * 0.5);

            // Initialize the adjacency matrix
            for (int i = 0; i < numberOfVertices; i++) {
                for (int j = 0; j < numberOfVertices; j++) {
                    adjMatrix[i][j] = Double.POSITIVE_INFINITY;
                }
            }

            // Add random edges
            while (numEdges > 0) {
                int v1 = rand.nextInt(numberOfVertices);
                int v2 = rand.nextInt(numberOfVertices);
                if (v1 != v2 && adjMatrix[v1][v2] == Double.POSITIVE_INFINITY) {
                    double weight = 1 + rand.nextInt(15);
                    adjMatrix[v1][v2] = weight;
                    adjMatrix[v2][v1] = weight;
                    numEdges--;
                }
            }

            // Update the number of vertices
            numVertices = numberOfVertices;

            // Check if the graph is connected
            isConnected = isConnected();
        } while (!isConnected);
    }
    public void importFromJson() {
        System.out.println("Enter the filename of the map to import: ");
        String filename = new Scanner(System.in).nextLine();
        String basePath = "src/Maps/"; // Fixed path
        String fullPath = basePath + filename + ".json";
        JSONParser parser = new JSONParser();
        JSONArray graphArray = null;
        try {
            graphArray = (JSONArray) parser.parse(new FileReader(fullPath));
        } catch (IOException | ParseException e) {
            throw new RuntimeException(e);
        }
        if (!isEmpty()) {
            this.clearGraph();
        }

        // First phase: Add all vertices
        for (Object vertexObj : graphArray) {
            JSONObject vertexJson = (JSONObject) vertexObj;
            String vertexIdStr = (String) vertexJson.get("vertex");
            int vertexName = Integer.parseInt(vertexIdStr);
            T vertex = (T) Integer.valueOf(vertexName);
            this.addVertex(vertex);
        }

        // Second phase: Add all edges
        for (Object vertexObj : graphArray) {
            JSONObject vertexJson = (JSONObject) vertexObj;
            T vertex = (T) Integer.valueOf(Integer.parseInt((String) vertexJson.get("vertex")));

            JSONArray edges = (JSONArray) vertexJson.get("edges");
            for (Object edgeObj : edges) {
                JSONObject edgeJson = (JSONObject) edgeObj;
                String toVertexIdStr = (String) edgeJson.get("to");
                int toVertexName = Integer.parseInt(toVertexIdStr);
                double weight = (double) edgeJson.get("weight");

                T toVertex = (T) Integer.valueOf(toVertexName);
                this.addEdge(vertex, toVertex, weight);
            }
        }
    }
    public void exportToJson(String filename) {
        JSONArray graphArray = new JSONArray();

        // Iterate over each vertex
        for (int i = 0; i < numVertices; i++) {
            JSONObject vertexObj = new JSONObject();
            JSONArray edgesArray = new JSONArray();

            // Add vertex details
            vertexObj.put("vertex", vertices[i].toString());

            // Iterate over the adjacency matrix for edges
            for (int j = 0; j < numVertices; j++) {
                if (adjMatrix[i][j] != Double.POSITIVE_INFINITY) {
                    JSONObject edgeObj = new JSONObject();
                    edgeObj.put("to", vertices[j].toString());
                    edgeObj.put("weight", adjMatrix[i][j]);
                    edgesArray.add(edgeObj);
                }
            }

            vertexObj.put("edges", edgesArray);
            graphArray.add(vertexObj);
        }

        // Define the specific directory path
        String directoryPath = "src/Maps/";

        // Check if filename ends with .json, if not, append it
        if (!filename.endsWith(".json")) {
            filename += ".json";
        }

        // Write JSON file
        try (FileWriter file = new FileWriter(directoryPath + filename)) {
            file.write(graphArray.toJSONString());
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void clearGraph() {
        this.numVertices = 0;
        this.vertices = (T[]) new Object[this.DEFAULT_CAPACITY];
        this.adjMatrix = new double[this.DEFAULT_CAPACITY][this.DEFAULT_CAPACITY];
    }
    public Iterable<Integer> getAdjacentVertices(int vertex) {
        ArrayUnorderedList<Integer> adjacentVertices = new ArrayUnorderedList<>();
        if (indexIsValid(vertex)) {
            for (int i = 0; i < numVertices; i++) {
                if (adjMatrix[vertex][i] < Double.POSITIVE_INFINITY) {
                    adjacentVertices.addToRear(i);
                }
            }
        }
        return adjacentVertices;
    }


}
