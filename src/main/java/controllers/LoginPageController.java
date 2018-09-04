package main.java.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginPageController {

    @FXML
    private Label messagelbl;

    @FXML
    private TextField usernametxt;

    @FXML
    private PasswordField passwordpsw;

    @FXML
    void checkAndLogin(ActionEvent event) {
        //Check the user name and password and if correct try to Login and open a new window
        if((usernametxt.getText().toLowerCase().equals("usernam"))&&(passwordpsw.getText().equals("pass123wor#"))){
            try {
                messagelbl.setText("Successful!");
                Stage currstage = (Stage) messagelbl.getScene().getWindow();
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getClassLoader().getResource("main/resources/view/homepage.fxml"));
                Parent root = loader.load();
                Stage stage = new Stage();
                stage.setTitle("Amir's Diary");
                stage.setScene(new Scene(root, 640, 480));
                Image icon = new Image(getClass().getResourceAsStream("/main/resources/images/icon.png"));
                stage.getIcons().add(icon);
                stage.setMinWidth(650);
                stage.setMinHeight(480);
                //Close the current window before opening the other one
                currstage.close();
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
                messagelbl.setText("There was an Error Please "+ e +" check again");
            }
        }else {
            //User name or password is not correct
            messagelbl.setText("Username or Password is Wrong!");

        }

    }

}
