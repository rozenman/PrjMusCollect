package ru.mycompany;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Optional;
import java.util.Properties;

public class MainFX extends Application {
    public static Controller controller;
    private  static Stage pStage;
    public static void main(String[] args) {
        launch(args);
    }
    public static Properties prop;

    @Override
    public void start(Stage primaryStage) {
        if (Controller.DEBUG_LEVEL > 0) {
            System.out.println("Application start");
        }
        prop = new Properties();
        LaodSettings();

        MainFX.pStage = primaryStage;
        primaryStage.setOnCloseRequest(event -> {
            Alert dialog = new Alert(Alert.AlertType.CONFIRMATION,
                    "Вы действительно хотите закрыть окно?");
            dialog.setTitle("Закрытие окна");
            //dialog.setHeaderText(null);
            dialog.setHeaderText("Вопрос");
            Optional<ButtonType> result = dialog.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                if (Controller.DEBUG_LEVEL > 0) {
                    System.out.println("Окно будет закрыто");
                }
            }
            else {
                if (Controller.DEBUG_LEVEL > 0) {
                    System.out.println("Окно не будет закрыто");
                }
                event.consume();
            }
        });
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/MainWinFX.fxml"));

            FXMLLoader loader = new FXMLLoader();
            controller = loader.getController();

            primaryStage.setTitle("Music Colection");
            primaryStage.setScene(new Scene(root, 1000, 800));

            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error " + e);
        }
    }

    private  void LaodSettings(){
        //System.out.println("cur DIR : " + System.getProperty("user.dir"));
        try {
            FileInputStream fi = new FileInputStream(System.getProperty("user.dir")+"/config.properties");
            prop.load(fi);
        } catch (IOException e) {
            //e.printStackTrace();
            System.out.println("ERROR "+ e);
            prop.setProperty("dir_path", "");
        }

    }

    private void SaveSettings(){
        try {
            FileOutputStream fo = new FileOutputStream(System.getProperty("user.dir")+"/config.properties");
            prop.store(fo, "");

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void stop() throws Exception {
        if (Controller.DEBUG_LEVEL > 0) {
            System.out.println("Метод stop() " + Thread.currentThread().getName());
        }

        String s;// = controller.getcurPath();
        //System.out.println(s);
        s = prop.getProperty("dir_path");
        //System.out.println("dir_path :" + s);
        SaveSettings();

        super.stop();
    }


}
