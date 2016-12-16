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
import java.util.ArrayList;
import java.util.List;

public class Main extends Application {

    private List<String> lines;

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
        Integer linesSupportingTLS = checkLinesForTLS();

        Integer linesSupportingSSL = checkLinesForSSL();

        tfOutput.setText("TLS: " + linesSupportingTLS.toString() + " SSL: " + linesSupportingSSL);
    }

    private int checkLinesForSSL() {
        int sslSupportiveLines = 0;

        for (String line : lines) {
            if (doesThisLineSupportSSL(line)) {
                sslSupportiveLines++;
            }
        }
        return sslSupportiveLines;

    }

    private boolean doesThisLineSupportSSL(String line) {
        String[] splitString = line.split("\\[|\\]");
        List<String> listWithABA = new ArrayList<>();
        List<String> listWithBAB = new ArrayList<>();

        for (int i = 0; i < splitString.length; i++) {
            if (i % 2 == 0) {
                listWithABA.add(splitString[i]);
            } else {
                listWithBAB.add(splitString[i]);
            }
        }

        List<String> aba = extractABA(listWithABA);
        List<String> bab = extractABA(listWithBAB);

        for (String a : aba) {
            for (String b : bab) {
                if (isAbaBabMatch(a, b)) {
                    return true;
                }
            }

        }
        return false;
    }

    private boolean isAbaBabMatch(String aba, String bab) {
        if(aba == null || bab == null){
            return false;
        }
        return aba.charAt(0) == bab.charAt(1) && aba.charAt(1) == bab.charAt(0);
    }

    private List<String> extractABA(List<String> abaList) {
        List<String> abas = new ArrayList<>();
        for (String s : abaList) {
            if (s.length() < 3) {
                continue;
            }
            for (int i = 0; i < s.length() - 2; i++) {
                char c1 = s.charAt(i);
                char c2 = s.charAt(i + 1);
                char c3 = s.charAt(i + 2);
                if (c1 == c3 && c1 != c2) {
                    abas.add(new StringBuilder(c1).append(c2).append(c3).toString());
                }
            }
        }
        return abas;
    }

    private int checkLinesForTLS() {
        int tlsSupportiveLines = 0;

        for (String line : lines) {
            if (doesThisLineSupportTLS(line)) {
                tlsSupportiveLines++;
            }
        }

        return tlsSupportiveLines;
    }

    private boolean doesThisLineSupportTLS(String line) {
        String[] splitLine = line.split("\\[|\\]");

        List<String> shouldSupportTLS = new ArrayList<>();
        List<String> shouldNotSupportTLS = new ArrayList<>();

        for (int i = 0; i < splitLine.length; i++) {
            if (i % 2 == 0) {
                shouldSupportTLS.add(splitLine[i]);
            } else {
                shouldNotSupportTLS.add(splitLine[i]);
            }
        }

        boolean supportTLS = false;

        // if any does -> true so far
        for (String s : shouldSupportTLS) {
            supportTLS = supportTLS || doesThisStringSupportTLS(s);
        }

        // if any does -> false
        for (String s : shouldNotSupportTLS) {
            supportTLS = supportTLS && !doesThisStringSupportTLS(s);
        }

        return supportTLS;
    }

    private boolean doesThisStringSupportTLS(String s) {
        if (s.length() < 5) {
            return false;
        }
        for (int i = 0; i < s.length() - 4; i++) {
            char c1 = s.charAt(i);
            char c2 = s.charAt(i + 1);
            char c3 = s.charAt(i + 2);
            char c4 = s.charAt(i + 3);
            if (c1 == c4 && c2 == c3) {
                return true;
            }
        }
        return false;
    }

    private void parseInput() {
        String line;
        lines = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(new File("input.txt")))) {
            line = br.readLine();
            while (line != null) {
                lines.add(line);
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
