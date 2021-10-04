import java.sql.*;
import java.io.*;
import java.util.*;

//CSE 111 DATABASE SYSTEMS
//FALL 2020
//TEAM CONSISTS OF:
//LUIS GARCIA VELASQUEZ
//JOSUE GIALIS
//SUBMITTED ON 12/13/2020 @ 9:30PM

//READ ME SECTION
//HELLO I AM YOUR VEHICLE INSURANCE PROGRAM
//TO ACCESS ADMINISTRATOR MENU BRANCH TO MANAGE DATABASE, MAKE isAdmin flag = true;
//TO ACCESS USER(Customer) MENU BRANCH SET isAdmin flag = false;

public class InsuranceApp {

    public static void main(String [] args) {
        String databaseName = "data.sqlite";
        String dropTxtFile = "TablesDrop.txt";
        String tableTxtFile = "Tables.txt";
    
        boolean populate = false;
        boolean isAdmin = false;

        Database db = new Database(); 

        db.run(databaseName, dropTxtFile, tableTxtFile, populate, isAdmin);

        // https://gitlab.ucmerced.edu/jgialis/database-project
    }
    
}


