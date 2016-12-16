package com.adiko;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.*;

public class Main extends Application {

    public static void main(String[] args) {
    launch(args);
    }

    Stage window;
    TextField tfOutput;

    @Override
    public void start(Stage primaryStage) throws Exception {
        window = primaryStage;

        VBox layout = new VBox(10);
        layout.setAlignment(Pos.CENTER);

        tfOutput = new TextField();
        Button btnGo = new Button("GO");
        btnGo.setOnAction(e -> processInput());

        layout.getChildren().addAll(btnGo, tfOutput);

        StackPane root = new StackPane();
        root.getChildren().add(layout);
        window.setScene(new Scene(root, 300, 300));
        window.show();
    }

    private void processInput() {
        parseInput();
        // TODO magic
    }

    private void parseInput() {
        String line;
        try (BufferedReader br = new BufferedReader(new FileReader(new File("input.txt")))) {
            line = br.readLine();
            while (line != null) {
                // TODO parsing
                line = br.readLine();
            }
        } catch (FileNotFoundException e) {
            // e.printStackTrace();
            System.out.println("FILE NOT FOUND: " + e.getMessage());
        } catch (IOException e) {
            // e.printStackTrace();
            System.out.println("IOEXCEPTION: " + e.getMessage());
        }
    }
}
