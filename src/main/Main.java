package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import main.java.models.dbconnection;

import java.awt.*;

public class Main extends Application {
    dbconnection db = new dbconnection();
    @Override
    public void start(Stage primaryStage) throws Exception{
        //first create the db and table
        boolean theTablesresult = db.createTheTables();
        if (theTablesresult != false) {

            Parent root = FXMLLoader.load(getClass().getResource("/main/resources/view/loginpage.fxml"));
            primaryStage.setTitle("Login to Your Diary");
            primaryStage.setScene(new Scene(root, 320, 320));
            //set the icon
            Image icon = new Image(getClass().getResourceAsStream("/main/resources/images/icon.png"));
            primaryStage.getIcons().add(icon);
            primaryStage.setResizable(false);
            primaryStage.show();
        }
    }


    public static void main(String[] args) { launch(args);
    }
}
