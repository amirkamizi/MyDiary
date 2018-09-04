package main.java.models;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.swing.plaf.PanelUI;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;

public class dbconnection {
    //      it checks if exists: updates
    //      if not adds this.
    Connection con;
    MyCipher cipher;

    //Info about the DataBase:
    //Table Name: diary
    //Fields:
    //ID(auto increment), date(unique text),title,story
    private String getConnectedToDB() {

        //Let's get connected
        try {
            Class.forName("org.sqlite.JDBC");
            con = DriverManager.getConnection(
                    "jdbc:sqlite:mydiarydb.db");
        } catch (Exception e) {
            System.out.println(e);
            return "Couldn't connect to the Database! " + e;
        }
        //let's start the cipher as well
        try {
            cipher = new MyCipher();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
            return "Couldn't connect to the Cipher! " + e;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "Couldn't connect to the Cipher! " + e;
        }
        return null;
    }

    public boolean createTheTables(){
        String e1 = getConnectedToDB();
        if (e1 != null) return false;

        try {
            Statement stmt = con.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS `diary` (" +
                    "`ID`INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE," +
                    "`date`TEXT NOT NULL UNIQUE," +
                    "`title`BLOB NOT NULL," +
                    "`story`BLOB NOT NULL" +
                    ");";
            stmt.executeUpdate(sql);
            con.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public String addOrUpdateDB(String date,String title,String story){
        //First Lets get Connected
        String e1 = getConnectedToDB();
        if (e1 != null) return e1;
        //first let's encrypt the data
        byte[] cipheredtitle;
        byte[] cipheredstory;
        try {
            cipheredtitle = cipher.encrypt(title);
            cipheredstory = cipher.encrypt(story);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
            return "There was a exception regarding cipher when wanted to add/update the Diary " + e;
        } catch (BadPaddingException e) {
            e.printStackTrace();
            return "There was a exception regarding cipher when wanted to add/update the Diary " + e;
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
            return "There was a exception regarding cipher when wanted to add/update the Diary " + e;
        }


        //Now that we are connected let's check if it exist or not
        try{
            PreparedStatement stmt=con.prepareStatement("select * from diary where date=?");
            stmt.setString(1,date);
            ResultSet rs = stmt.executeQuery();
            if(rs.next()){
                //it means it exists so we should update it
                try {
                    PreparedStatement stmtupdate=con.prepareStatement("update diary set title=?,story=? where date=?");
                    stmtupdate.setBytes(1,cipheredtitle);
                    stmtupdate.setBytes(2,cipheredstory);
                    stmtupdate.setString(3,date);
                    int i=stmtupdate.executeUpdate();
                    con.close();
                    return i +" diary was successfully updated.";
                } catch (SQLException e) {
                    e.printStackTrace();
                    con.close();
                    return "There was a exception when wanted to update the Diary " + e;
                }
            }else{
                //it means it doesn't exist and we should add it
                try {
                    PreparedStatement stmtadd=con.prepareStatement("insert into diary(date,title,story) values(?,?,?)");
                    stmtadd.setString(1,date);
                    stmtadd.setBytes(2,cipheredtitle);
                    stmtadd.setBytes(3,cipheredstory);
                    int i=stmtadd.executeUpdate();
                    con.close();
                    return i +" diary was successfully added.";
                } catch (SQLException e) {
                    e.printStackTrace();
                    con.close();
                    return "There was a exception when wanted to add to the Diary " + e;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            con=null;
            return "it could not get a result set and there was an exception "+ e;
        }
    }



    //a method to get and returns the diary for a specific date
    public ArrayList<String> gettheinfo(String date){
        //First Lets get Connected
        String e1 = getConnectedToDB();
        if (e1 != null) return null;
        //now let's ge the result
        try{
            PreparedStatement stmt=con.prepareStatement("select * from diary where date=?");
            stmt.setString(1,date);
            ResultSet rs = stmt.executeQuery();
            ArrayList<String> information = new ArrayList<>();
            while(rs.next()){
                //Here we should first decrypt the bytes and turn them to string then add it to the information
                //first get the bytes,then decrypt them,then turn to string, then add to information array
                //like this:
//                new String(cipher.decrypt(rs.getBytes("title")));
                try {
                    information.add( new String(cipher.decrypt(rs.getBytes("title"))));
                    information.add( new String(cipher.decrypt(rs.getBytes("story"))));
                } catch (InvalidKeyException e) {
                    e.printStackTrace();
                    System.out.println("problem in decrypting the texts");
                    return null;
                } catch (BadPaddingException e) {
                    e.printStackTrace();
                    System.out.println("problem in decrypting the texts");
                    return null;
                } catch (IllegalBlockSizeException e) {
                    e.printStackTrace();
                    System.out.println("problem in decrypting the texts");
                    return null;
                }
            }
            con.close();
            return information;
        } catch (SQLException e) {
            e.printStackTrace();
            con=null;
            return null;
        }
    }

    //get and return all the database
    public ArrayList<String> getallinfo(){
        //First Lets get Connected
        String e1 = getConnectedToDB();
        if (e1 != null) return null;
        //now let's ge the result
        try{
            PreparedStatement stmt=con.prepareStatement("select * from diary order by date ASC");
            ResultSet rs = stmt.executeQuery();
            ArrayList<String> information = new ArrayList<>();
            while(rs.next()){
                String temptitle;
                try {
                    temptitle = new String(cipher.decrypt(rs.getBytes("title")));
                } catch (InvalidKeyException e) {
                    e.printStackTrace();
                    System.out.println("problem getting the title");
                    return null;
                } catch (BadPaddingException e) {
                    e.printStackTrace();
                    System.out.println("problem getting the title");
                    return null;
                } catch (IllegalBlockSizeException e) {
                    e.printStackTrace();
                    System.out.println("problem getting the title");
                    return null;
                }
                information.add( rs.getString("date") + " | " +
                        temptitle);
            }
            con.close();
            return information;
        } catch (SQLException e) {
            e.printStackTrace();
            con=null;
            return null;
        }
    }

}
