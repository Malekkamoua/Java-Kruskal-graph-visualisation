package com.brunomnsilva.smartgraph;

import java.util.ArrayList;
import com.brunomnsilva.smartgraph.graphview.SmartGraphPanel;
import com.brunomnsilva.smartgraph.graph.Graph;
import com.brunomnsilva.smartgraph.graphview.SmartStylableNode;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import com.brunomnsilva.smartgraph.graphview.SmartPlacementStrategy;
import com.brunomnsilva.smartgraph.containers.SmartGraphDemoContainer;
import com.brunomnsilva.smartgraph.graph.Digraph;
import com.brunomnsilva.smartgraph.graph.DigraphEdgeList;
import com.brunomnsilva.smartgraph.graphview.SmartCircularSortedPlacementStrategy;

/**
 *
 * @author brunomnsilva
 */
public class Main extends Application {

    Scene home_scene,insert_nodesSize_scene,nodeName_scene,welcome_scene;

    TableView<Arete> table;

    String src, dest;
    int destination_index, source_index;
    TextField poids_textField;

    int nodes;

    ArrayList<Arete> aretes = new ArrayList<>();
    ObservableList<String> nodesArray = FXCollections.observableArrayList();

    ObservableList<Arete> minimal_arete = FXCollections.observableArrayList();

    @Override
    public void start(Stage primaryStage) {

        primaryStage.setTitle("New window");

        Button home_scene_btn = new Button("Please insert your nodes' number");
        home_scene_btn.setOnAction(e -> primaryStage.setScene(insert_nodesSize_scene));
        TextField node_textField = new TextField();
        TextField nodeName_textField = new TextField();
        Label label_insert_nodesSize_scene = new Label("Nodes' size", node_textField);
        Button insert_nodesSize_scene_btn = new Button("Nodes' names");

        insert_nodesSize_scene_btn.setOnAction(e -> {
            System.out.println(node_textField.getText());
            boolean result = is_int(node_textField.getText());
            if (!result) {
                AlertBox.display("Invalid input", "Please enter a valid number");
            } else {
                primaryStage.setScene(nodeName_scene);
            }
        });

        Label label_nodeName_scene = new Label("Nodes' Name");
        Button nodeName_add_item = new Button("Ajouter");

        Button nodeName_scene_btn = new Button("kruskal");
        nodeName_scene_btn.setDisable(true);

        // node size text field and the event control
        node_textField.setPromptText("Node num");

        // node names textfield and the even control
        nodeName_textField.setPromptText("Node name");
        nodeName_add_item.setOnAction(e -> {
            if (nodesArray.size() < nodes) {
                if (nodeName_textField.getText() != "") {
                    if (!nodesArray.contains(nodeName_textField.getText())) {
                        nodesArray.add(nodeName_textField.getText());
                        System.out.println(nodeName_textField.getText() + "  " + nodes + "  " + nodesArray.size());
                    } else {
                        AlertBox.display("Alert box", "Attention the node *** " + nodeName_textField.getText() + " *** exists");
                    }
                }
            } else {
                if (nodesArray.size() == nodes) {
                    nodeName_add_item.setDisable(true);
                    nodeName_scene_btn.setDisable(false);
                }
            }

            nodeName_textField.setText("");
        });

        nodeName_scene_btn.setOnAction(e -> primaryStage.setScene(home_scene));

        //Create Table
        //Source Column
        TableColumn<Arete, String> sourceColumn = new TableColumn<>("Source");
        sourceColumn.setMaxWidth(200);
        sourceColumn.setCellValueFactory(new PropertyValueFactory<>("Source"));
        //Destination Column
        TableColumn<Arete, String> destinationColumn = new TableColumn<>("Destination");
        destinationColumn.setMaxWidth(200);
        destinationColumn.setCellValueFactory(new PropertyValueFactory<>("Destination"));
        //Destination Column
        TableColumn<Arete, String> poidsColumn = new TableColumn<>("Poids");
        poidsColumn.setMaxWidth(200);
        poidsColumn.setCellValueFactory(new PropertyValueFactory<>("Poids"));

        Label source = new Label("Source");
        Label destination = new Label("Destination");

        poids_textField = new TextField();
        poids_textField.setPromptText("Poids entre les deux sommets");

        //Supposedly dynamic array
        ChoiceBox<String> nodesList_source = new ChoiceBox<>();
        nodesList_source.setItems(nodesArray);
        //Fill list dynamically

        nodesList_source.getSelectionModel().selectedItemProperty().addListener((v, oldValue, newValue) -> {
            src = newValue;
            int index = nodesArray.indexOf(newValue);
            System.out.println("Source " + index);
        });

        ChoiceBox<String> nodesList_destination = new ChoiceBox<>();

        //Fill list dynamically
        nodesList_destination.setItems(nodesArray);
        //Get selected Item
        nodesList_destination.getSelectionModel().selectedItemProperty().addListener((v, oldValue, newValue) -> {
            dest = newValue;
            destination_index = nodesArray.indexOf(newValue);
            System.out.println("Destination " + destination_index);
        });

        Button add_btn = new Button("Add item");
        Button delete_btn = new Button("Delete selected");

        add_btn.setOnAction(e -> addArete());
        delete_btn.setOnAction(e -> deleteArete());
        //Finishing the table
        table = new TableView<>();
        table.setItems(getAretes());
        table.getColumns().addAll(sourceColumn, destinationColumn, poidsColumn);

        Button run_algo = new Button("Run");
        run_algo.setOnAction(e -> {

            int result = Kruskal(aretes);
            AlertBox.display("Alert box", "Poids minimal : " + result);

            Graph<String, String> g_min = build_sample_digraph();
            Graph<String, String> g = build_graph();

            SmartPlacementStrategy strategy = new SmartCircularSortedPlacementStrategy();
            SmartGraphPanel<String, String> graphView_min = new SmartGraphPanel<>(g_min, strategy);

            Scene scene = new Scene(new SmartGraphDemoContainer(graphView_min), 1024, 768);

            Stage stage = new Stage(StageStyle.DECORATED);
            stage.setTitle("Poids minimal"+result);
            stage.setMinHeight(600);
            stage.setMinWidth(800);
            stage.setScene(scene);
            stage.show();

            graphView_min.init();

            graphView_min.setVertexDoubleClickAction(graphVertex -> {
                if( !graphVertex.removeStyleClass("myVertex") ) {
                    graphVertex.addStyleClass("myVertex");
                }

            });
            graphView_min.setEdgeDoubleClickAction(graphEdge -> {
                graphEdge.setStyle("-fx-stroke: black; -fx-stroke-width: 2;");

            });

            SmartGraphPanel<String, String> graphView = new SmartGraphPanel<>(g, strategy);

            Scene scene2 = new Scene(new SmartGraphDemoContainer(graphView), 1024, 768);

            Stage stage2 = new Stage(StageStyle.DECORATED);
            stage2.setTitle("Graph initial");
            stage2.setMinHeight(600);
            stage2.setMinWidth(800);
            stage2.setScene(scene2);
            stage2.show();

            graphView.init();

            graphView.setVertexDoubleClickAction(graphVertex -> {
                if( !graphVertex.removeStyleClass("myVertex") ) {
                    graphVertex.addStyleClass("myVertex");
                }

            });
            graphView.setEdgeDoubleClickAction(graphEdge -> {
                graphEdge.setStyle("-fx-stroke: black; -fx-stroke-width: 2;");

            });
        });
        //welcome_scene + layout + button+ label
        Label label_welcome_scene = new Label("Kruskal's Algorithme");
        Button begin_button_welcome_scene = new Button("Begin");
        VBox layout_welcome_scene = new VBox(20);
        layout_welcome_scene.getChildren().addAll(label_welcome_scene,begin_button_welcome_scene);
        welcome_scene = new Scene(layout_welcome_scene, 512,340);
        VBox layout_scene1 = new VBox(20);
        layout_scene1.getChildren().addAll(table, source, nodesList_source, destination, nodesList_destination, poids_textField, add_btn, delete_btn, run_algo);
        home_scene = new Scene(layout_scene1, 800, 600);

        VBox layout_insert_nodesSize_scene = new VBox(20);
        layout_insert_nodesSize_scene.getChildren().addAll(label_insert_nodesSize_scene, node_textField, insert_nodesSize_scene_btn);
        insert_nodesSize_scene = new Scene(layout_insert_nodesSize_scene, 800, 600);

        VBox layout_nodeName_scene = new VBox(20);
        layout_nodeName_scene.getChildren().addAll(label_nodeName_scene, nodeName_textField, nodeName_add_item, nodeName_scene_btn);
        nodeName_scene = new Scene(layout_nodeName_scene, 800, 600);

        primaryStage.setScene(welcome_scene);
        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    private Graph<String, String> build_sample_digraph() {

        Digraph<String, String> g = new DigraphEdgeList<>();

        for (String value : nodesArray) {
            g.insertVertex(value);
        }

        for (Arete ar : minimal_arete) {
            g.insertEdge(ar.getSource(), ar.getDestination(), ar.getSource()+""+ar.getDestination());

        }

        return g;
    }

    private Graph<String, String> build_graph() {

        Digraph<String, String> g = new DigraphEdgeList<>();

        for (String value : nodesArray) {
            g.insertVertex(value);
        }

        for (Arete ar : aretes) {
            g.insertEdge(ar.getSource(), ar.getDestination(), ar.getSource()+""+ar.getDestination());

        }

        return g;
    }


    private void deleteArete() {

        ObservableList<Arete> allAretes, selectedArete;

        allAretes = table.getItems();
        selectedArete = table.getSelectionModel().getSelectedItems();
        selectedArete.forEach(allAretes::remove);
    }

    private void addArete() {
        Arete arete = new Arete();

        arete.setSource(src);
        arete.setSource_index(source_index);

        arete.setDestination(dest);
        arete.setDestination_index(destination_index);

        arete.setPoids(Integer.parseInt(poids_textField.getText()));

        aretes.add(arete);
        table.getItems().add(arete);

        for (Arete value : aretes) {
            System.out.println(value.getSource()+" -> "+value.getDestination()+" Poids :"+value.getPoids());
        }
    }

    private boolean is_int(String message) {
        try{
            nodes = Integer.parseInt(message);
            System.out.println("user age is "+nodes);
            return true;
        }catch (NumberFormatException e){
            System.out.println("Error "+ message);
            return false;
        }
    }

    public ObservableList<Arete> getAretes(){

        return FXCollections.observableArrayList();

    }

    private int Kruskal(ArrayList<Arete>aretes) {

        int[][] matrice = new int[nodes][nodes];
        int[] parent = new int[nodes];
        int min;
        int u = 0;
        int v = 0;
        int noOfEdges = 1;
        int poids = 0;

        for(int i = 0; i < nodes; i++){

            for(int j = 0; j < nodes; j++){
                matrice[i][j] = 999999999;
            }
        }

        for (Arete ar : aretes) {
            System.out.println("ddd"+ar.getSource_index() + " "+ar.getDestination_index());

            parent[ar.getSource_index()] = 0;

            matrice[ar.getSource_index()][ar.getDestination_index()] = ar.getPoids();

            if(matrice[ar.getSource_index()][ar.getDestination_index()]==0){
                matrice[ar.getSource_index()][ar.getDestination_index()] = 999999999;
            }

        }

        //find
        while(noOfEdges < nodes){
            min = 999999999;
            for(int i = 0; i < nodes; i++){
                for(int j = 0; j < nodes; j++){
                    if(matrice[i][j] < min){
                        min = matrice[i][j];
                        u = i;
                        v = j;
                    }
                }
            }
            while(parent[u]!=0){
                u = parent[u];
            }
            while(parent[v]!=0){
                v = parent[v];
            }

            //union
            if(v!=u){

                noOfEdges++;
                System.out.println("Arete trouvee: " + nodesArray.get(u) + "->" + nodesArray.get(v)+" Min : " + min);
                Arete ar = new Arete(nodesArray.get(u),nodesArray.get(v),u,v,min);
                minimal_arete.add(ar);

                if (min < 999999999) {
                    poids += min;
                    parent[v] = u;
                }
            }

            //pour ne plus visiter ces arètes
            matrice[u][v] = 999999999;
            matrice[v][u] = 999999999;
        }

        System.out.println(poids);

        return poids;
    }

}
