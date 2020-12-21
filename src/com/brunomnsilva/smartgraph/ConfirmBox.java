package com.brunomnsilva.smartgraph;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ConfirmBox {

    static boolean answer;

    static boolean display(String title, String message){
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        window.setMinWidth(250);

        Label label = new Label();
        label.setText(message);

        Button yes_btn = new Button("Yes");
        yes_btn.setOnAction(e -> {
            answer = true;
            window.close();
        });

        Button no_btn = new Button("No");
        no_btn.setOnAction(e -> {
            answer = false;
            window.close();
        });

        VBox layout = new VBox(20);
        layout.getChildren().addAll(label, yes_btn,no_btn);

        Scene scene = new Scene(layout);

        window.setScene(scene);
        window.showAndWait();

        return answer;
    }
}

