package main.java.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import main.java.models.dbconnection;

import java.io.IOException;
import java.util.ArrayList;

public class HomepageController {

    @FXML
    private BorderPane borderpaneid;

    @FXML
    private DatePicker diarydatepicker;

    @FXML
    private TextField diarytitletxt;

    @FXML
    private Label statuslbl;

    @FXML
    private Button savebtn;

    @FXML
    private TextArea diarytextarea;

    dbconnection dbconn = new dbconnection();

    @FXML
    void closeTheProgram(ActionEvent event) {
        // get the stage so we can close the window
        Stage stage = (Stage) diarytextarea.getScene().getWindow();
        // close the stage
        stage.close();
    }

    @FXML
    void openAboutMeWindow(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About us");
        alert.setHeaderText(null);
        alert.setGraphic(null);
        //to clear all the buttons and the functionality of close buton you should do this
        //but for now we don't need that.
        //alert.getButtonTypes().clear();
        alert.setContentText("Write your about me here!");
        alert.showAndWait();
    }

    //get the date and if the diary already exists show the story
    //if not set the story to empty
    @FXML
    void showTheEntriesifExist(ActionEvent event) {
        statuslbl.setText("");
        String date = diarydatepicker.getValue().toString();
        ArrayList<String> result = dbconn.gettheinfo(date);
        if(result.isEmpty()) {
            diarytitletxt.setText("");
            diarytextarea.setText("");
            statuslbl.setText("ready for a new story");
        }else{
            diarytitletxt.setText(result.get(0));
            diarytextarea.setText(result.get(1));
            statuslbl.setText("");
        }
    }

    @FXML
    void openAllEntryWindow(ActionEvent event) {
        //get all the data from DB and Show in a New Window As a Text form Not Table or List
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getClassLoader().getResource("main/resources/view/veiwallpage.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("My's Diary|All Stories");
            stage.setScene(new Scene(root,320,480));
            Image icon = new Image(getClass().getResourceAsStream("/main/resources/images/icon.png"));
            stage.getIcons().add(icon);
            stage.setMinWidth(320);
            stage.setMinHeight(240);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //save the diary to db
    @FXML
    void saveTheDiaryToDB(ActionEvent event) {
        statuslbl.setText("");
        if(diarydatepicker.getValue()!=null){
            statuslbl.setText("");
            String date = diarydatepicker.getValue().toString();
            String title = diarytitletxt.getText();
            if (diarytextarea.getText().isEmpty()){
                statuslbl.setText("The Story is Empty! You should write your Diary...");
            }else {
                String story = diarytextarea.getText();
                String status = dbconn.addOrUpdateDB(date, title, story);
                statuslbl.setText(status);
            }
        }else {
            statuslbl.setText("You should choose a date first!");
        }

    }

    //set to focusmode and fullscreen
    @FXML
    void startTheFocusMode(ActionEvent event) {
        //TODO: MAYBE LATER HIDE EVERYTHING AND CHANGE THE COLOR OF THE BG
        Stage currstage = (Stage) diarytextarea.getScene().getWindow();
        if (currstage.isFullScreen()){
            currstage.setFullScreen(false);
        }else{
            currstage.setFullScreen(true);
        }
    }

}
