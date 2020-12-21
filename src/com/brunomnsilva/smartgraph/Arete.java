package com.brunomnsilva.smartgraph;

public class Arete {
    String source;
    String destination;
    int poids, destination_index, source_index;

    public Arete() {
        this.source = "";
        this.destination = "";
        this.poids = 0;
    }

    public Arete(String source, String destination, int poids) {
        this.source = source;
        this.destination = destination;
        this.poids = poids;
    }

    public Arete(String source, String destination, int source_index, int destination_index, int poids) {
        this.source = source;
        this.destination = destination;
        this.source_index = source_index;
        this.destination_index = destination_index;
        this.poids = poids;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public int getPoids() {
        return poids;
    }

    public void setPoids(int poids) {
        this.poids = poids;
    }

    public int getDestination_index() {
        return destination_index;
    }

    public void setDestination_index(int destination_index) {
        this.destination_index = destination_index;
    }

    public int getSource_index() {
        return source_index;
    }

    public void setSource_index(int source_index) {
        this.source_index = source_index;
    }
}