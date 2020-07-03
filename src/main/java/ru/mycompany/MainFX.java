package ru.mycompany;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;

public class MainFX extends Application {
    public static Controller controller;
    private  static Stage pStage;
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {





        MainFX.pStage = primaryStage;
        primaryStage.setOnCloseRequest(event -> {
            Alert dialog = new Alert(Alert.AlertType.CONFIRMATION,
                    "Вы действительно хотите закрыть окно?");
            dialog.setTitle("Закрытие окна");
            //dialog.setHeaderText(null);
            dialog.setHeaderText("Вопрос");
            Optional<ButtonType> result = dialog.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                System.out.println("Окно будет закрыто");
            }
            else {
                System.out.println("Окно не будет закрыто");
                event.consume();
            }
        });
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/MainWinFX.fxml"));
            FXMLLoader loader = new FXMLLoader();
            controller = loader.getController();
            primaryStage.setTitle("Music Colection");
            primaryStage.setScene(new Scene(root, 812, 400));
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error " + e);
        }
    }
}
