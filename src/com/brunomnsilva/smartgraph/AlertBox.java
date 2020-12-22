package com.brunomnsilva.smartgraph;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class AlertBox {
    static void display(String title, String message){
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        window.setMinWidth(300);
        window.setMinHeight(150);

        Label label = new Label();
        label.setText(message);
        Button close_btn = new Button("close");
        close_btn.setStyle("-fx-background-color:#72bb53;");
        close_btn.setTextFill(Color.WHITE);
        close_btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                window.close();
            }
        });

        VBox layout = new VBox(20);
        layout.setStyle("-fx-alignment: center;");
        layout.getChildren().addAll(label,close_btn);
        Scene scene = new Scene(layout);

        window.setScene(scene);
        window.showAndWait();
    }
}
