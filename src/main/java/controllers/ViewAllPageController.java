package main.java.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import main.java.models.dbconnection;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ViewAllPageController implements Initializable {


    @FXML
    private ListView<String> liststoriesid;

    dbconnection dbconn = new dbconnection();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ArrayList<String> reslutlist = dbconn.getallinfo();
        for (int i = 0; i <reslutlist.size(); i++) {
            liststoriesid.getItems().add(reslutlist.get(i));
        }
    }
}

