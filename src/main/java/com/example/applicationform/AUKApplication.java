package com.example.applicationform;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.geometry.Insets;
import javafx.scene.paint.Color;
import java.io.*;

public class AUKApplication extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("AUK Bachelor's Program Application");

        TextField nameField = new TextField();
        nameField.setPromptText("Enter your first name");

        TextField surnameField = new TextField();
        surnameField.setPromptText("Enter your surname");

        TextField emailField = new TextField();
        emailField.setPromptText("Enter your email");

        TextField phoneField = new TextField();
        phoneField.setPromptText("Enter your phone number");

        DatePicker dobPicker = new DatePicker();
        dobPicker.setPromptText("Select your date of birth");

        RadioButton bgm = new RadioButton("BGM");
        RadioButton bba = new RadioButton("BBA");
        RadioButton bms = new RadioButton("BMS");
        RadioButton bse = new RadioButton("BSE");
        RadioButton bds = new RadioButton("BDS");

        ToggleGroup specialtyGroup = new ToggleGroup();
        bgm.setToggleGroup(specialtyGroup);
        bba.setToggleGroup(specialtyGroup);
        bms.setToggleGroup(specialtyGroup);
        bse.setToggleGroup(specialtyGroup);
        bds.setToggleGroup(specialtyGroup);

        RadioButton maleRadioButton = new RadioButton("Male");
        RadioButton femaleRadioButton = new RadioButton("Female");

        ToggleGroup genderGroup = new ToggleGroup();
        maleRadioButton.setToggleGroup(genderGroup);
        femaleRadioButton.setToggleGroup(genderGroup);

        HBox genderBox = new HBox(20, maleRadioButton, femaleRadioButton);
        genderBox.setAlignment(Pos.CENTER);

        HBox specialtyBox = new HBox(25, bse, bds, bba, bms, bgm);
        specialtyBox.setAlignment(Pos.CENTER);

        Button submitButton = new Button("Submit");

        TextArea reportArea = new TextArea();
        reportArea.setEditable(false);

        Label nameErrorLabel = new Label();
        Label surnameErrorLabel = new Label();
        Label emailErrorLabel = new Label();
        Label phoneErrorLabel = new Label();
        Label dobErrorLabel = new Label();

        VBox inputVBox = new VBox(
                10,
                new VBox(5, new Label("Name:"), nameField, nameErrorLabel),
                new VBox(5, new Label("Surname:"), surnameField, surnameErrorLabel),
                new VBox(5, new Label("Email:"), emailField, emailErrorLabel),
                new VBox(5, new Label("Phone Number:"), phoneField, phoneErrorLabel),
                new VBox(5, new Label("Date of Birth:"), dobPicker, dobErrorLabel),
                new Label("Gender:"), genderBox,
                new Label("Specialty:"), specialtyBox,
                submitButton,
                new Label("Report:"),
                reportArea
        );

        VBox root = new VBox(20, inputVBox);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(10));

        Scene scene = new Scene(root, 400, 800);
        primaryStage.setScene(scene);
        primaryStage.show();

        submitButton.setOnAction(e -> {
            boolean valid = true;

            if (nameField.getText().trim().isEmpty()) {
                nameErrorLabel.setText("* Name cannot be empty.");
                nameErrorLabel.setTextFill(Color.RED); // Установка красного цвета
                valid = false;
            } else {
                nameErrorLabel.setText("");
            }

            if (surnameField.getText().trim().isEmpty()) {
                surnameErrorLabel.setText("* Surname cannot be empty.");
                surnameErrorLabel.setTextFill(Color.RED); // Установка красного цвета
                valid = false;
            } else {
                surnameErrorLabel.setText("");
            }

            if (!emailField.getText().contains("@")) {
                emailErrorLabel.setText("* Invalid email address.");
                emailErrorLabel.setTextFill(Color.RED); // Установка красного цвета
                valid = false;
            } else {
                emailErrorLabel.setText("");
            }


            if (!phoneField.getText().matches("\\d+")) {
                phoneErrorLabel.setText("* Phone number must be numeric.");
                phoneErrorLabel.setTextFill(Color.RED);
                valid = false;
            } else {
                phoneErrorLabel.setText("");
            }

            if (dobPicker.getValue() == null) {
                dobErrorLabel.setText("* Please select your date of birth.");
                dobErrorLabel.setTextFill(Color.RED);
                valid = false;
            } else {
                dobErrorLabel.setText("");
            }


            RadioButton selectedSpecialty = (RadioButton) specialtyGroup.getSelectedToggle();
            if (selectedSpecialty == null) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Missing Information");
                alert.setHeaderText("Please select a specialty.");
                alert.showAndWait();
                valid = false;
            }

            RadioButton selectedGender = (RadioButton) genderGroup.getSelectedToggle();
            if (selectedGender == null) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Missing Information");
                alert.setHeaderText("Please select your gender.");
                alert.showAndWait();
                valid = false;
            }

            if (valid) {
                String report = String.format(
                        "Application Report:%n" +
                                "Name: %s%n" +
                                "Surname: %s%n" +
                                "Email: %s%n" +
                                "Phone: %s%n" +
                                "Date of Birth: %s%n" +
                                "Gender: %s%n" +
                                "Specialty: %s",
                        nameField.getText(),
                        surnameField.getText(),
                        emailField.getText(),
                        phoneField.getText(),
                        dobPicker.getValue(),
                        selectedGender.getText(),
                        selectedSpecialty.getText()
                );
                reportArea.setText(report);

                saveApplicationInfo(nameField.getText(), surnameField.getText(), report);
            }
        });

        Button saveButton = new Button("Save to File");
        Button loadButton = new Button("Load from File");

        HBox buttonBox = new HBox(10, saveButton, loadButton);
        buttonBox.setAlignment(Pos.CENTER);

        root.getChildren().add(buttonBox);

        saveButton.setOnAction(e -> saveToFile(reportArea.getText()));

        loadButton.setOnAction(e -> loadFromFile(reportArea));
    }

    private void saveToFile(String report) {
        if (!report.isEmpty()) {
            try {
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Save Application Report");
                File file = fileChooser.showSaveDialog(null);
                if (file != null) {
                    FileWriter writer = new FileWriter(file);
                    writer.write(report);
                    writer.close();
                    System.out.println("Report saved to: " + file.getAbsolutePath());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Nothing to save.");
        }
    }

    private void loadFromFile(TextArea reportArea) {
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open Application Report");
            File file = fileChooser.showOpenDialog(null);
            if (file != null) {
                FileReader reader = new FileReader(file);
                BufferedReader bufferedReader = new BufferedReader(reader);
                String line;
                StringBuilder content = new StringBuilder();
                while ((line = bufferedReader.readLine()) != null) {
                    content.append(line).append("\n");
                }
                reportArea.setText(content.toString());
                bufferedReader.close();
                reader.close();
                System.out.println("Report loaded from: " + file.getAbsolutePath());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveApplicationInfo(String firstName, String lastName, String report) {
        try {
            String fileName = lastName + "_" + firstName + "_form.txt";
            FileWriter writer = new FileWriter(fileName);
            writer.write(report);
            writer.close();
            System.out.println("Application information saved to: " + fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
