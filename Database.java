
import java.sql.*;
import java.util.*;
import java.io.*;
import java.text.*;
import java.time.LocalDateTime;  // Import the LocalDateTime class
import java.time.format.DateTimeFormatter; 

//CSE 111 DATABASE SYSTEMS
//FALL 2020
//TEAM CONSISTS OF:
//LUIS GARCIA VELASQUEZ
//JOSUE GIALIS
//SUBMITTED ON 12/13/2020 @ 9:30PM

public class Database {
    //VARIABLES AVAILABLE IN CLASS
    private Connection c = null;
    private String dbName;
    private boolean isConnected = false;
    private Scanner input = new Scanner(System.in);
    private boolean manual = false;
    private int x = 0;
    private int countMain = 1;
    private DecimalFormat df = new DecimalFormat("###############.##");


    private void openConnection(String _dbName) {
        dbName = _dbName;

        if (false == isConnected) {
            System.out.println("++++++++++++++++++++++++++++++++++");
            System.out.println("Opening database: " + _dbName);

            try {
                String connStr = new String("jdbc:sqlite:");
                connStr = connStr + _dbName;

                // STEP: Register JDBC driver
                Class.forName("org.sqlite.JDBC");

                // STEP: Open a connection
                c = DriverManager.getConnection(connStr);

                // STEP: Diable auto transactions
                c.setAutoCommit(false);

                isConnected = true;
                System.out.println("SUCCESS!");
            } catch (Exception e) {
                System.err.println(e.getClass().getName() + ": " + e.getMessage());
                System.exit(0);
            }

            System.out.println("++++++++++++++++++++++++++++++++++");
        }
    }

    public void closeConnection() {
        if (true == isConnected) {
            System.out.println("++++++++++++++++++++++++++++++++++");
            System.out.println("Closing database: " + dbName);

            try {
                // STEP: Close connection
                c.close();

                isConnected = false;
                dbName = "";
                System.out.println("SUCCESS!");
            } catch (Exception e) {
                System.err.println(e.getClass().getName() + ": " + e.getMessage());
                System.exit(0);
            }

            System.out.println("++++++++++++++++++++++++++++++++++");
        }
    }

    private void createTables(String fileName) {
        System.out.println("++++++++++++++++++++++++++++++++++");
        System.out.println("Creating tables...");

        try {
            //READS IN SQL TABLES FROM SPECIFIED TEXTFILE
            //STORES THE TABLES LINE BY LINE IN THE TABLES ARRAY.
            Statement stmt = c.createStatement();
            ArrayList<String> tables = new ArrayList<String>();
            File file = new File("Text files/" + fileName);
            Scanner read = new Scanner(file); 

            while(read.hasNextLine())
                tables.add(read.nextLine());
                
            for (int j = 0; j < tables.size(); j++) 
                stmt.execute(tables.get(j));

            c.commit();
            stmt.close();
            read.close();
            
            System.out.println("SUCCESS!");
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            try {
                c.rollback();
            } catch (Exception e1) {
                System.err.println(e1.getClass().getName() + ": " + e1.getMessage());
            }
        }
        System.out.println("++++++++++++++++++++++++++++++++++");
    }

    private void dropTables(String fileName) {
        System.out.println("++++++++++++++++++++++++++++++++++");
        System.out.println("Drop tables");

        try {
            Statement stmt = c.createStatement();

            // STEP: Execute update statement

            //READS IN SQL DROP TABLE COMMANDS FROM SPECIFIED TEXTFILE
            //STORES THE COMMANDS LINE BY LINE IN THE DROPS ARRAY.    
            ArrayList<String> drops = new ArrayList<String>();
            File file = new File("Text Files/" + fileName);
            Scanner read = new Scanner(file); 
            while(read.hasNextLine())
                drops.add(read.nextLine());
                
            for (int j = 0; j < drops.size(); j++) 
                stmt.execute(drops.get(j)); 

            c.commit();
            stmt.close();
            read.close();
            System.out.println("success");
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            try {
                c.rollback();
            } catch (Exception e1) {
                System.err.println(e1.getClass().getName() + ": " + e1.getMessage());
            }
        }
        System.out.println("++++++++++++++++++++++++++++++++++");
    }


    public void populate(String tableName){
        File file = new File("Data sets/"+ tableName +".csv");
        if(x == 0){
            System.out.println("++++++++++++++++++++++++++++++++++");
            System.out.
            println("Populating tables...");
        }
        x++;
        try {
            Scanner input = new Scanner(file);
            //DO FOR EVERY TABLE TO PUPULATE AND KEEP EVENLY SPACED
            while (input.hasNext()) {
                String data = input.nextLine();
                String [] split = data.split(",");
                if(tableName.equals("Discount"))
                    insertDiscount(Integer.parseInt(split[0]), split[1], split[2], Double.parseDouble(split[3]), split[4], Integer.parseInt(split[5]), split[6]);
                else if(tableName.equals("Company"))
                    insertCompany(split[0], split[1], Double.parseDouble(split[2]), Integer.parseInt(split[3]));
                else if(tableName.equals("Driver"))
                    insertDriver(split[0], split[1], split[2], split[3], Integer.parseInt(split[4]), Integer.parseInt(split[5]), Integer.parseInt(split[6]), split[7], Integer.parseInt(split[8]));
                else if(tableName.equals("Vehicle"))
                    insertVehicle(split[0], split[1], split[2], Double.parseDouble(split[3]), split[4], split[5], Integer.parseInt(split[6]));
                else if(tableName.equals("Policy"))
                    insertPolicy(split[0], split[1], split[2], split[3], split[4], Double.parseDouble(split[5]));
                else if(tableName.equals("Payment"))
                    insertPayment(Integer.parseInt(split[0]), split[1], Integer.parseInt(split[2]), Double.parseDouble(split[3]), Double.parseDouble(split[4]), split[5]);
                else if(tableName.equals("Login"))
                    insertLogin(split[0], split[1], Integer.parseInt(split[2]));
                else if(tableName.equals("Customer"))
                    insertCustomer(split[0], split[1], split[2], Double.parseDouble(split[3]), split[4], Integer.parseInt(split[5]));
                else if(tableName.equals("Vehicle_Driver_Has"))
                    insertVehicle_Driver_Has(Integer.parseInt(split[0]), split[1], split[2]);
                else if(tableName.equals("CompanyOwnsPolicy"))
                    insertCompanyOwnsPolicy(split[0], split[1], Double.parseDouble(split[2]), Integer.parseInt(split[3]));  
            }
            if(x == 9){
                System.out.println("SUCCESS!");
                System.out.println("++++++++++++++++++++++++++++++++++");
            }

            input.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
// @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
// @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
// @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
// @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
// @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
// @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
// @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
// @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
// @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
// @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
// @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
// @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
//      Repetitive Functions used for data insertion using
//     scanner to read in data from csv files or allowing
//       user to overwrite functions for manual entry.
// @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
// @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
// @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
// @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
// @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
// @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
// @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
// @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
// @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
// @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
// @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
// @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    public void insertCustomer(String fName, String lName, String DOB, double balance, String pNum, int cKey){
        try {
            String sql = "INSERT INTO Customer(first_name, last_name, DOB, balance, policy_num, customer_key) " +
                "VALUES(?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = c.prepareStatement(sql);
        
        if(manual){
            System.out.println("INSERT INTO Customer(first_name, last_name, DOB, balance, policy_num, customer_key");
            System.out.print("Enter first name: ");
            fName = input.next();
            System.out.print("Enter last name: ");
            lName = input.next();
            System.out.print("Enter DOB (MM/DD/YYYY): ");
            DOB = input.next();
            System.out.print("Enter balance: ");
            balance = input.nextDouble();
            pNum = System.console().readLine("Enter policy number: ");
            System.out.print("Enter customer key: ");
            cKey = input.nextInt();

            FileWriter fw = new FileWriter("Data sets/Customer.csv", true); //Set to true to append data, rather than overwrite preexisting.
            BufferedWriter br = new BufferedWriter(fw);
            PrintWriter pw = new PrintWriter(fw);
            pw.println(fName + "," + lName + "," + DOB + "," + balance + "," + pNum + "," + cKey);
            pw.flush();
            pw.close();
            System.out.println("Inserting data...");
            pause(1000);
            System.out.println("Success!");
            pause(1000);
            manual = false;
        }

            stmt.setString(1, fName);
            stmt.setString(2, lName);
            stmt.setString(3, DOB);
            stmt.setDouble(4, balance);
            stmt.setString(5, pNum);
            stmt.setInt(6, cKey);

            stmt.executeUpdate();
            c.commit();
            stmt.close();
            
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            try {
                c.rollback();
            } catch (Exception e1) {
                System.err.println(e1.getClass().getName() + ": " + e1.getMessage());
            }
        }
    }


public void insertLogin(String username, String password, int customer_key){
    try {
        
        String sql = "INSERT INTO Login(username, password, customer_key) " +
            "VALUES(?, ?, ?)";
        PreparedStatement stmt = c.prepareStatement(sql);

        if(manual){
            FileWriter fw = new FileWriter("Data sets/Login.csv", true); //Set to true to append data, rather than overwrite preexisting.
            BufferedWriter br = new BufferedWriter(fw);
            PrintWriter pw = new PrintWriter(fw);
            System.out.println("INSERT INTO Login(username, password, customer key)");
            System.out.print("Enter username: ");
            username = input.next();
            System.out.print("Enter password: ");
            password = input.next();
            System.out.print("Enter customer key: ");
            customer_key = input.nextInt();
            pw.println(username + "," + password + "," + customer_key);
            pw.flush();
            pw.close();
            System.out.println("Inserting data...");
            pause(1000);
            System.out.println("Success!");
            pause(1000);
            manual = false;
        } 
        
        stmt.setString(1, username);
        stmt.setString(2, password);
        stmt.setInt(3, customer_key);

        stmt.executeUpdate();
        c.commit();

        stmt.close();
        
    } catch (Exception e) {
        System.err.println(e.getClass().getName() + ": " + e.getMessage());
        try {
            c.rollback();
        } catch (Exception e1) {
            System.err.println(e1.getClass().getName() + ": " + e1.getMessage());
        }
    }
}


public void insertPayment(int pID, String pMethod, int pCID, double pAmt, double pFee, String pDate){
    try {
        String sql = "INSERT INTO Payment(payment_id, method, customer_key, payment_amt, transaction_fee, date) " +
            "VALUES(?, ?, ?, ?, ?, ?)";
        PreparedStatement stmt = c.prepareStatement(sql);
        
        if(manual){
            FileWriter fw = new FileWriter("Data sets/Payment.csv", true); //Set to true to append data, rather than overwrite preexisting.
            BufferedWriter br = new BufferedWriter(fw);
            PrintWriter pw = new PrintWriter(fw);
            System.out.println("INSERT INTO Payment(payment_id, method, customer_key, payment_amt, transaction_fee, date)");
            System.out.print("Enter payment id: ");
            pID = input.nextInt();
            System.out.print("Enter payment method: ");
            pMethod = input.nextLine();
            System.out.print("Enter customer id: ");
            pCID = input.nextInt();
            System.out.print("Enter payment amount: ");
            pAmt = input.nextDouble();
            System.out.print("Enter payment fee: ");
            pFee = input.nextDouble();
            System.out.print("Enter payment date (MM/DD/YYYY): ");
            pDate = input.next();
            pw.println(pID + "," + pMethod + "," + pCID + "," + pAmt + "," + pFee + "," + pDate);
            pw.flush();
            pw.close();
            System.out.println("Inserting data...");
            pause(1000);
            System.out.println("Success!");
            pause(1000);
            manual = false;
        }

        stmt.setInt(1, pID);
        stmt.setString(2, pMethod);
        stmt.setInt(3, pCID);
        stmt.setDouble(4, pAmt);
        stmt.setDouble(5, pFee);
        stmt.setString(6, pDate);
        // STEP: Execute batch
        stmt.executeUpdate();

        // STEP: Commit transaction
        c.commit();
        stmt.close();
        
    } 
    catch (Exception e) {
        System.err.println(e.getClass().getName() + ": " + e.getMessage());
        try {
            c.rollback();
        } catch (Exception e1) {
            System.err.println(e1.getClass().getName() + ": " + e1.getMessage());
        }
    }
}

public void insertPolicy(String pFName, String pLName, String pdate, String pvin, String pNum, double pRate){
    try {
        
        String sql = "INSERT INTO Policy(insured_fname, insured_lname, insured_DOB, insured_VIN, policy_num, policy_rate) " +
            "VALUES(?, ?, ?, ?, ?, ?)";
        PreparedStatement stmt = c.prepareStatement(sql);
    if(manual){
        FileWriter fw = new FileWriter("Data sets/Policy.csv", true); //Set to true to append data, rather than overwrite preexisting.
        BufferedWriter br = new BufferedWriter(fw);
        PrintWriter pw = new PrintWriter(fw);
        System.out.print("INSERT INTO Policy(insured_fname, insured_lname, insured_DOB, insured_VIN, policy_num, policy_rate)");
        System.out.print("Enter first name: ");
        pFName = input.next();
        System.out.print("Enter last name: ");
        pLName = input.next();
        System.out.print("Enter date (MM/DD/YYYY): ");
        pdate = input.next();
        System.out.print("Enter vin: ");
        pvin = input.next();
        pNum = System.console().readLine("Enter policy number: ");
        System.out.print("Enter policy rate: ");
        pRate = input.nextDouble();    
        pw.println(pFName + "," + pLName + "," + pdate + "," + pvin + "," + pNum + "," + pRate);
        pw.flush();
        pw.close();
        System.out.println("Inserting data...");
        pause(1000);
        System.out.println("Success!");
        pause(1000);
        manual = false;
    }
   
        // STEP: Execute batch

        stmt.setString(1, pFName);
        stmt.setString(2, pLName);
        stmt.setString(3, pdate);
        stmt.setString(4, pvin);
        stmt.setString(5, pNum);
        stmt.setDouble(6, pRate);
        stmt.executeUpdate();

        // STEP: Commit transaction
        c.commit();

        stmt.close();
        
    } catch (Exception e) {
        System.err.println(e.getClass().getName() + ": " + e.getMessage());
        try {
            c.rollback();
        } catch (Exception e1) {
            System.err.println(e1.getClass().getName() + ": " + e1.getMessage());
        }
    }
}
public void insertVehicle(String year, String make, String model, double price, String color, String vin, int mileage){
    try {
        
        String sql = "INSERT INTO Vehicle(year, make, model, price, color, VIN, mileage) " +
        "VALUES(?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement stmt = c.prepareStatement(sql);
        
        if(manual){
            FileWriter fw = new FileWriter("Data sets/Vehicle.csv", true); //Set to true to append data, rather than overwrite preexisting.
            BufferedWriter br = new BufferedWriter(fw);
            PrintWriter pw = new PrintWriter(fw);
            System.out.println("INSERT INTO Vehicle(year, make, model, type, usage, vin, mileage) ");
            System.out.print("Enter car year: ");
            year = input.next();
            System.out.print("Enter car make: ");
            make = input.next();
            System.out.print("Enter car model: ");
            model = input.next();
            System.out.print("Enter car price: ");
            price = input.nextDouble();
            System.out.print("Enter car color: ");
            color = input.next();
            System.out.print("Enter car vin: ");
            vin = input.next();
            System.out.print("Enter car mileage: ");
            mileage = input.nextInt();
            
            pw.println(year + "," + make + "," + model + "," + price + "," + color + "," + vin + "," + mileage);
            pw.flush();
            pw.close();
            System.out.println("Inserting data...");
            pause(1000);
            System.out.println("Success!");
            pause(1000);
            manual = false;
        }
        // STEP: Execute batch

        stmt.setString(1, year);
        stmt.setString(2, make);
        stmt.setString(3, model);
        stmt.setDouble(4, price);
        stmt.setString(5, color);
        stmt.setString(6, vin);

        //////////////CHANGE TO INT BECAUSE ODOMETERS DONT GIVE A FUCK ABOUT 0.1 miles
        stmt.setInt(7, mileage);
        stmt.executeUpdate();

        // STEP: Commit transaction
        c.commit();

        stmt.close();
        
    } catch (Exception e) {
        System.err.println(e.getClass().getName() + ": " + e.getMessage());
        try {
            c.rollback();
        } catch (Exception e1) {
            System.err.println(e1.getClass().getName() + ": " + e1.getMessage());
        }
    }
}
public void insertDriver(String fn, String ln, String dob, String lid, int age, int exp, int vPoints, String vDescr, int dKey){
    try {
        String sql = "INSERT INTO Driver(first_name, last_name, DOB, license_ID, age, experience, violation_pts, violation_des, driver_key) " +
            "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement stmt = c.prepareStatement(sql);
        if(manual){
            FileWriter fw = new FileWriter("Data sets/Driver.csv", true); //Set to true to append data, rather than overwrite preexisting.
            BufferedWriter br = new BufferedWriter(fw);
            PrintWriter pw = new PrintWriter(fw);
            System.out.println("INSERT INTO Driver(first_name, last_name, DOB, license_ID, age, experience, violation_pts, violation_des, driver_key) ");
            System.out.print("Enter first name: ");
            fn = input.next();
            System.out.print("Enter last name: ");
            ln = input.next();
            System.out.print("Enter DOB (MM/DD/YYYY): ");
            dob = input.next();
            System.out.print("Enter license ID: ");
            lid = input.next();
            System.out.print("Enter age: ");
            age = input.nextInt();

            while (age<18){
                System.err.println("Driver must be 18 or older to drive and get an insurane policy with VIP");
                System.out.print("Enter reenter appropiate age: ");
                age = input.nextInt();
            }
            System.out.print("Enter experience: ");
            exp = input.nextInt();
            System.out.print("Enter number of violation points: ");
            vPoints = input.nextInt();
            System.out.print("Enter violation description: ");
            vDescr = input.nextLine();
            System.out.print("Enter driver key: ");
            dKey = input.nextInt();

            pw.println(fn + "," + ln + "," + dob + "," + lid + "," + age + "," + exp + "," + vPoints + "," + vDescr + "," + dKey);
            pw.flush();
            pw.close();
            System.out.println("Inserting data...");
            pause(1000);
            System.out.println("Success!");
            pause(1000);
            manual = false;
        }

        stmt.setString(1, fn);
        stmt.setString(2, ln);
        stmt.setString(3, dob);
        stmt.setString(4, lid);
        stmt.setInt(5,age);
        stmt.setInt(6, exp);
        stmt.setInt(7, vPoints);
        stmt.setString(8, vDescr);
        stmt.setInt(9, dKey);
        // STEP: Execute batch
        stmt.executeUpdate();

        // STEP: Commit transaction
        c.commit();

        stmt.close();
        
    } catch (Exception e) {
        System.err.println(e.getClass().getName() + ": " + e.getMessage());
        try {
            c.rollback();
        } catch (Exception e1) {
            System.err.println(e1.getClass().getName() + ": " + e1.getMessage());
        }
    }
}
public void insertCompany(String name, String pNum, double cBal, int cKey){
    try {
        String sql = "INSERT INTO Company(name, policy_num, customer_bal, customer_key) " +
            "VALUES(?, ?, ?, ?)";
        PreparedStatement stmt = c.prepareStatement(sql);
        
        if(manual){
            FileWriter fw = new FileWriter("Data sets/Company.csv", true); //Set to true to append data, rather than overwrite preexisting.
            BufferedWriter br = new BufferedWriter(fw);
            PrintWriter pw = new PrintWriter(fw);
            System.out.println("INSERT INTO Company(name, policy_num, customer_bal, customer_key) ");
            name = System.console().readLine("Enter company name: "); 
            pNum = System.console().readLine("Enter policy number: ");
            System.out.print("Enter customer balance: ");
            cBal = input.nextDouble();
            System.out.print("Enter customer key: ");
            cKey = input.nextInt();
    
            pw.println(name + "," + pNum + "," + cBal + "," + cKey);
            pw.flush();
            pw.close();
            System.out.println("Inserting data...");
            pause(1000);
            System.out.println("Success!");
            pause(1000);
            manual = false;
        }
        

        stmt.setString(1, name);
        stmt.setString(2, pNum);
        stmt.setDouble(3, cBal);
        stmt.setInt(4, cKey);
        // STEP: Execute batch
        stmt.executeUpdate();

        // STEP: Commit transaction
        c.commit();

        stmt.close();
        
        
    } catch (Exception e) {
        System.err.println(e.getClass().getName() + ": " + e.getMessage());
        try {
            c.rollback();
        } catch (Exception e1) {
            System.err.println(e1.getClass().getName() + ": " + e1.getMessage());
        }
    }
}
public void insertDiscount(int dKey, String student, String military, double GPA, String disability, int goodTimeDriving, String mS){
    try {
        String sql = "INSERT INTO Discount(driver_key, student, military, GPA, disability, goodTimeDriving, marital_status) " +
            "VALUES(?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement stmt = c.prepareStatement(sql);

        if(manual){
            FileWriter fw = new FileWriter("Data sets/Discount.csv", true); //Set to true to append data, rather than overwrite preexisting.
            BufferedWriter br = new BufferedWriter(fw);
            PrintWriter pw = new PrintWriter(fw);
            System.out.println("INSERT INTO Discount(driver_key, student, military, GPA, disability, goodTimeDriving, marital_status) ");

            System.out.print("Enter driver key: ");
            dKey = input.nextInt();
            System.out.print("Are you a student? (Y/N): ");
            student = input.next();
            System.out.print("Are you in military? (Y/N): ");
            military = input.next();
            System.out.print("Enter GPA: ");
            GPA = input.nextDouble();
            System.out.print("Enter driver key: ");
            disability = input.next();
            System.out.print("Enter driver key: ");
            goodTimeDriving = input.nextInt();
            System.out.print("Enter driver key: ");
            mS = input.next();
            pw.println(dKey + "," + student + "," + military + "," + GPA + "," + disability + "," + goodTimeDriving);
            pw.flush();
            pw.close();
            System.out.println("Inserting data...");
            pause(1000);
            System.out.println("Success!");
            pause(1000);
            manual = false;
        }

        stmt.setInt(1, dKey);
        stmt.setString(2, student);
        stmt.setString(3, military);
        stmt.setDouble(4, GPA);
        stmt.setString(5, disability);
        stmt.setInt(6, goodTimeDriving);
        stmt.setString(7, mS);
        // STEP: Execute batch
        stmt.executeUpdate();

        // STEP: Commit transaction
        c.commit();

        stmt.close();
        
    } catch (Exception e) {
        System.err.println(e.getClass().getName() + ": " + e.getMessage());
        try {
            c.rollback();
        } catch (Exception e1) {
            System.err.println(e1.getClass().getName() + ": " + e1.getMessage());
        }
    }

    
}




public void insertCompanyOwnsPolicy(String pol_num, String comp_name, double rate, int cust_key){
    try {
        
        String sql = "INSERT INTO CompanyOwnsPolicy(policy_num, company_name, customer_bal, customer_key) " +
            "VALUES(?, ?, ?, ?)";
        PreparedStatement stmt = c.prepareStatement(sql);

        if(manual){
            FileWriter fw = new FileWriter("Data sets/CompanyOwnsPolicy.csv", true); //Set to true to append data, rather than overwrite preexisting.
            BufferedWriter br = new BufferedWriter(fw);
            PrintWriter pw = new PrintWriter(fw);
            System.out.println("INSERT INTO CompanyOwnsPolicy(pol_num, comp_name, bal, cust_key)");
            pol_num = System.console().readLine("Enter policy number: ");
            System.out.print("Enter company name: ");
            comp_name = input.nextLine();
            System.out.print("Enter policy bal: ");
            rate = input.nextDouble();
	        System.out.print("Enter customer key: ");
            cust_key = input.nextInt();
            pw.println(pol_num + "," + comp_name + "," + rate +"," +cust_key);
            pw.flush();
            pw.close();
            System.out.println("Inserting data...");
            pause(1000);
            System.out.println("Success!");
            pause(1000);
            manual = false;
        } 
        stmt.setString(1, pol_num);
        stmt.setString(2, comp_name);
	    stmt.setDouble(3, rate);
        stmt.setInt(4, cust_key);
        //Add data to CSV file.

        // STEP: Execute batch
        stmt.executeUpdate();

        // STEP: Commit transaction
        c.commit();
        stmt.close();
        
    } catch (Exception e) {
        System.err.println(e.getClass().getName() + ": " + e.getMessage());
        try {
            c.rollback();
        } catch (Exception e1) {
            System.err.println(e1.getClass().getName() + ": " + e1.getMessage());
        }
    }
}


public void insertVehicle_Driver_Has(int drive_key, String VIN, String licence_num){
    try {
        String sql = "INSERT INTO Vehicle_Driver_Has(driver_key, VIN, license_ID)" +
            "VALUES(?, ?, ?)";
        PreparedStatement stmt = c.prepareStatement(sql);
        if(manual){
            FileWriter fw = new FileWriter("Data sets/Vehicle_Driver_Has.csv", true); //Set to true to append data, rather than overwrite preexisting.
            BufferedWriter br = new BufferedWriter(fw);
            PrintWriter pw = new PrintWriter(fw);
            System.out.println("INSERT INTO Vehicle_Driver_Has(drive_key, VIN, license ID)");
            System.out.print("Enter Driver key: ");
            drive_key = input.nextInt();
            System.out.print("Enter Licence Number: ");
            licence_num = input.next();
            System.out.print("Enter VIN: ");
            VIN = input.next();
         
            pw.println(drive_key + "," + VIN + "," + licence_num);
            pw.flush();
            pw.close();
            System.out.println("Inserting data...");
            pause(1000);
            System.out.println("Success!");
            pause(1000);
            manual = false;
        }

        stmt.setInt(1, drive_key);
        stmt.setString(2, VIN);
        stmt.setString(3, licence_num);
        // STEP: Execute batch
        stmt.executeUpdate();

        // STEP: Commit transaction
        c.commit();

        stmt.close();
        
    } catch (Exception e) {
        System.err.println(e.getClass().getName() + ": " + e.getMessage());
        try {
            c.rollback();
        } catch (Exception e1) {
            System.err.println(e1.getClass().getName() + ": " + e1.getMessage());
        }
    }
}




// @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
// @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
// @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
// @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
// @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
// @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
// @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
// @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
// @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
// @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
// @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
// @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
//            General Sequential Program Functions
// @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
// @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
// @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
// @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
// @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
// @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
// @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
// @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
// @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
// @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
// @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
// @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@


    //LOGIN STARUP FOR CUSTOMER (!isAdmin) OR DATA MANAGEMENT IF(ISADMIN)
    public void printLogin(Boolean isAdmin){
        clearScreen();
        if(isAdmin == true){
            adminMenu();
        }
        else{
            System.out.println("                                 Welcome to VIP!");
            System.out.println("            Insurance that treats you and your vehicle with extra care");
            System.out.println("****************************************************************************************");
     
            System.out.print("\n1) Login \n2) Create Account\n\n Option #: ");
     
            int choice = input.nextInt();
     
            System.out.println("----------------------------------------------------------");
            
            if(choice==1){
                clearScreen();
                System.out.println("                  Greetings, please sign in.");
                System.out.println("******************************************************************");
                String user = System.console().readLine("username :\t");
                String psw = System.console().readLine("password :\t");
                //CALL TO LOGIN VERIFICATION
                login(user, psw);
     
            }else if (choice==2){
                //OTHERWISE COLLECT USER DATA FOR NEW ACCOUNT
                createAccount();
            }
        }
        
    }
    
    
    public void login(String user, String psw){
        try{
            String sql = "SELECT COUNT(username), customer_key FROM Login WHERE username = '" + user +"' AND password = '" + psw  +"';";
            PreparedStatement stmt = c.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            int exists = 0;
            int cust = -1;
            while(rs.next()){   
                exists = rs.getInt(1);
                cust = rs.getInt(2);
                if(exists == 1){
                    //IF CORRECT INPUT PROVIDE CUSTOMER MENU
                    custMenu(cust);
                }
                else{
                    //ERROR CASE WHEN INPUT WRONG USERNAME OR PASSWORD, REPRINT LOGIN OPTIONS
                    System.out.println("Wrong username or Password, Please Try Again or Create Account");
                    pause(1500);
                    printLogin(false);
                }
            }
            c.commit();
            rs.close();
            stmt.close();
            
        }catch(Exception e){
            System.err.println(e.getClass().getName() + ":" + e.getMessage());
        }
    }
    

    public void createAccount(){
        clearScreen();
        System.out.println("                       Welcome to VIP Online Insurance! ");
        System.out.println("                  To proceed, answer the following prompts");
        System.out.println("***************************************************************************************");
            //COLLECT BASIC CUSTOMER INFO
            String fName = System.console().readLine("First Name :\t");
            String lName = System.console().readLine("Last Name :\t");
            String DOB = System.console().readLine("DOB MM/DD/YY:\t");
            Double bal = 0.0;
            String user = System.console().readLine("email :\t\t");
            String pass = System.console().readLine("password :\t");

            clearScreen();
            System.out.println("----------------------------------------------------------------------------------");
            System.out.println("                             Thank You!                              ");
            System.out.println("To proceed, answer the following prompts regarding your driving records");
            System.out.println("----------------------------------------------------------------------------------");
            //COLLECT DRIVING RECORD
            String [] Descriptions = {"Nothing", "Speeding", "Bad Stop", "Red Light", "DUI", "Crash"};
            
            String license = System.console().readLine("license:\t");
            System.out.print("Age :\t\t");
            int age = input.nextInt();
            System.out.print("Experience :\t");
            int exp = input.nextInt();
            System.out.print("Violation Points :\t");
            int pts = input.nextInt();
            String des = " ";
            if(pts>=0 && pts<=5){
                des = Descriptions[pts];
            }
            //AUTO ASSIGNMENT OF DESCRIPTION BASED ON PTS
            System.out.print("Description :\t" + des);
            int currentCustNum = 0;
            int currentDriver = 0;
            try{
                //CREATE NEW CUSTOMER KEY
                String sql = "SELECT COUNT(customer_key) FROM Customer;";
                PreparedStatement stmt = c.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery();

                while(rs.next()){   
                    currentCustNum = rs.getInt(1);
                }
                currentCustNum+=1;
                //CREATE NEW DRIVER KEY
                String sql2 = "SELECT MAX(driver_key) FROM Driver;";
                 stmt = c.prepareStatement(sql2);
                 rs = stmt.executeQuery();

                 while(rs.next()){   
                    currentDriver= rs.getInt(1);
                }
                currentDriver+=1;

            //NEW CUSTOMER=DEFAULT DRIVER DISCOUNST TABLE
            System.out.println("\n\n\nAnswer the following prompts to qualify for discounts");
            String studyDisc = System.console().readLine("Are you a student? (Y/N) : \t");
            Double gpa = 0.0;
            if(studyDisc.equalsIgnoreCase("Y")){
                System.out.print("Enter GPA :\t");
                gpa = input.nextDouble();
            }
            String armyDisc = System.console().readLine("Are you a veteran? (Y/N) : \t");
            String disability = System.console().readLine("Do you have a disability parking permit? (Y/N): \t");
            System.out.print("Years driving without trafic points?: \t");
            int drivingYrs = input.nextInt();
            String marital = System.console().readLine("Are you a married (Y/N): \t");

            //END OF DISCOUNT TO DRIVER
            rs.close();
            stmt.close();
            c.commit();
            clearScreen();
            
            System.out.println("----------------------------------------------------------------------------------");
            System.out.println("                              Thank You!                              ");
            System.out.println("   To proceed, add the following information about your vehicle.");
            System.out.println("----------------------------------------------------------------------------------");
            //COLLECT DATA FOR DEFAULT VEHICLE/DRIVER/CUSTOMER
            String year = System.console().readLine("Enter year :\t");
            String make = System.console().readLine("Enter make :\t");
            String model = System.console().readLine("Enter model :\t");
            System.out.print("Enter car price: ");
            Double carPrice = input.nextDouble();
            String color = System.console().readLine("Enter color :\t");
            String VIN = System.console().readLine("Enter VIN :\t");
            System.out.print("Enter car mileage: ");
            int mileage = input.nextInt();
            
            //INSERTION OF NEW DATA INTO DATABASE
            insertLogin(user, pass, currentCustNum);
            insertDiscount(currentDriver, studyDisc, armyDisc, gpa, disability, drivingYrs, marital);
            insertCustomer(fName, lName, DOB, bal, "ABCD 123456789", currentCustNum);
            insertDriver(fName, lName, DOB, license, age, exp, pts, des, currentDriver);
            insertPolicy(fName, lName, DOB, VIN, "ABCD 123456789", bal);
            insertVehicle(year, make, model, carPrice, color, VIN, mileage);
            insertVehicle_Driver_Has(currentDriver, VIN, license);

            System.out.println("Thank you "+fName+" for joining Vehicle Insurance Program");
            pause(1000);
            clearScreen();
            //PROVIDE QUOTES
            selectInsurers(currentCustNum, carPrice);

            }catch (Exception e) {
                System.err.println(e.getClass().getName() + ": " + e.getMessage());
                try {
                    c.rollback();
                } catch (Exception e1) {
                    System.err.println(e1.getClass().getName() + ": " + e1.getMessage());
                }
            }
            
    }

    
    public void selectInsurers(int cust, double cost){
        try{
            int i = 0;
            String sql = "SELECT DISTINCT company_name FROM CompanyOwnsPolicy;";
            double x = discounts(cust);
            cost = cost - x * cost;
            
            PreparedStatement stmt = c.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            ArrayList<String> company_names = new ArrayList<String>();
            ArrayList<String> policyNums = new ArrayList<String>();
            ArrayList<Double> rates = new ArrayList<Double>();

            System.out.println("       Company Name          PolicyNums          Rate");
            System.out.println("    --------------------------------------------------------");
            while(rs.next()){   
               int random = (int)(Math.random() * (22 - 15 + 1) + 15);
               company_names.add(rs.getString(1));
               policyNums.add(generateRandom(4, 9));
               //INSURANCE GENERAL COST COMPUTATION
               rates.add(cost/random);
               System.out.printf((i + 1) + "%20s %20s %10s\n", company_names.get(i), policyNums.get(i), df.format(rates.get(i)/6));
               i++;
            }
            //INSURANCE DISCOUNTS APPLIED TO GENERAL COST
            System.out.print("\n\nOption #: ");
            int choice = input.nextInt();
            insertCompany(company_names.get(choice - 1), policyNums.get(choice - 1), rates.get(choice - 1), cust);
            insertCompanyOwnsPolicy(policyNums.get(choice - 1), company_names.get(choice - 1), rates.get(choice - 1), cust);
            //UPDATE POLICY NUM FROM CompanyOwnsPolicy TABLE BASED ON USER POLICY SELECTION
            sql = "UPDATE CompanyOwnsPolicy SET policy_num = ? WHERE customer_key = '"+ cust+"';";
            stmt = c.prepareStatement(sql);
            stmt.setString(1,policyNums.get(choice - 1));
            stmt.executeUpdate();
            //UPDATE POLICY NUM FROM CUSTOMER TABLE BASED ON USER POLICY SELECTION
            sql = "UPDATE Customer SET policy_num = ? WHERE customer_key = '"+ cust+"';";
            stmt = c.prepareStatement(sql);
            stmt.setString(1,policyNums.get(choice - 1));
            stmt.executeUpdate();
            //UPDATE BALANCE IN CUSTOMER TABLE WITH DISCOUNTS
            sql = "UPDATE Customer SET balance = ? WHERE customer_key = '"+ cust+"';";
            stmt = c.prepareStatement(sql);
            stmt.setDouble(1,rates.get(choice - 1));
            stmt.executeUpdate();
            sql = "UPDATE Customer SET policy_num = ? WHERE customer_key = '"+ cust+"';";
            //UPDATE policy_rate  FROM CompanyOwnsPolicy TABLE BASED ON USER POLICY SELECTION
            sql = "UPDATE Policy SET policy_rate = ? WHERE policy_num = 'ABCD 123456789';";
            stmt = c.prepareStatement(sql);
            stmt.setDouble(1,rates.get(choice - 1));
            stmt.executeUpdate();
            ////UPDATE policy_num FROM CompanyOwnsPolicy TABLE BASED ON USER POLICY SELECTION
            sql = "UPDATE Policy SET policy_num = ? WHERE policy_num = 'ABCD 123456789';";
            stmt = c.prepareStatement(sql);
            stmt.setString(1,policyNums.get(choice - 1));
            stmt.executeUpdate();
            c.commit();
            stmt.close();

            System.out.println("\nPlease use your login Information, thank you for joining VIP!");
            pause(3000);
            //RETURN TO CUSTOMER MENU
            custMenu(cust);

            rs.close();
            stmt.close();
        }catch(Exception e){
            System.err.println(e.getClass().getName() + ":" + e.getMessage());
        }
    }
   

    void managePolicy(int cust_key, String pNum, Double bal){
        try {
            clearScreen();
            System.out.println("Policy #: " + pNum);
            System.out.println("--------------------");
            //MENU FOR VIEW, REMOVAL AND ADDITION OF DRIVERS & VEHICLES
            String choice = System.console().readLine("\n\n(1) View driver " + 
                                                      "\n(2) Add Driver " +
                                                      "\n(3) Remove Driver " +
                                                      "\n(4) View Vehicles " + 
                                                      "\n(5) Add Vehicles " +
                                                      "\n(6) Remove Vehicle " +
                                                      "\n\nOption #: "); 
            if(choice.equals("1"))
                viewDriver(pNum, cust_key);
            else if(choice.equals("2"))
                addDriver(pNum, cust_key);
            else if(choice.equals("3"))
                removeDriver(cust_key, pNum);
            else if(choice.equals("4"))
                viewVehicles(pNum, cust_key);
            else if(choice.equals("5"))
                addVehicle(pNum, cust_key, bal);
            else if(choice.equals("6"))
                removeVehicle(pNum, cust_key);
        } 
        catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            try {
                c.rollback();
            } 
            catch (Exception e1) {
                System.err.println(e1.getClass().getName() + ": " + e1.getMessage());
            }
        }
    }
    

    public void makePayment(String fName, String lName, int cust_key, Double bal, String pNum){
        clearScreen();
    
        System.out.println("\tGreetings " + lName + ", " + fName + "!");
        System.out.println("*******************************************");
        System.out.println("Your current Balance is: \t$" + bal);
        System.out.println("*******************************************");

        //NO PAYMENT NECESSARY
        if(bal<=0){
            System.out.println("Your payments are up to date! Thank you for your preference.");
            custMenu(cust_key);
        }
        System.out.println("*******************************************");
        
        //USER CHOOSES PAYMENT METHOD
        System.out.print("Choose payment method: \n(1) cash \n(2) check \n(3) paypal \n(4) bitcoin \n(5) debit card \n(6) credit card\n \noption\t#");
        int payChoice = input.nextInt();
        System.out.print("Enter amount to pay:\t$");
        Double amt = input.nextDouble();
        Double updatedBal = bal - amt;
        
        try{
            //UPDATE BALANCE FROM CUSTOMER BASED ON NEW PAYMENT
            String sql = "UPDATE Customer SET balance = ? WHERE customer_key = '"+ cust_key +"';";
            PreparedStatement stmt = c.prepareStatement(sql);
            stmt.setDouble(1,updatedBal);
            stmt.executeUpdate();
            //UPDATE POLICY_RATE FROM POLICY BASED ON NEW PAYMENT
            sql = "UPDATE Policy SET policy_rate = ? WHERE policy_num = '"+ pNum +"';";
            stmt = c.prepareStatement(sql);
            stmt.setDouble(1,updatedBal);
            stmt.executeUpdate();
            //UPDATE customer_bal FROM COMPANYOWNSPOLICY BASED ON NEW PAYMENT
            sql = "UPDATE CompanyOwnsPolicy SET customer_bal = ? WHERE customer_key = '"+ cust_key +"';";
            stmt = c.prepareStatement(sql);
            stmt.setDouble(1,updatedBal);
            stmt.executeUpdate();            
            //UPDATE customer_bal FROM COMPANY BASED ON NEW PAYMENT
            sql = "UPDATE Company SET customer_bal = ? WHERE policy_num = '"+ pNum +"';";
            stmt = c.prepareStatement(sql);
            stmt.setDouble(1,updatedBal);
            stmt.executeUpdate();
            
            c.commit();
            stmt.close();
            //AUTO FEE METHOD AND ASSIGNMENT FOR DISCOUNTS TABLE
            String [] methods = {"cash", "check", "paypal", "bitcoin", "debit card", "credit card", "pre-paid card"};
            Double [] fee = {0.00, 0.01, 0.03, 0.04, 0.05, 0.025, 0.025, 0.03};
            LocalDateTime myDateObj = LocalDateTime.now();  
            DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("MM/dd/yyyy"); 
            String currentDate = myDateObj.format(myFormatObj);

            Random rnd = new Random();
            int paymentID = (rnd.nextInt((999999999 - 100000000) + 1) + 100000000);
            pause(2500);
            Double transFee = amt*(fee[payChoice-1]);
            //INSERT PAYMENT IN DATABASE
            insertPayment(paymentID, methods[payChoice - 1], cust_key, amt, transFee, currentDate);

            clearScreen();
            //AUTOMATIC RECEIPT AFTER PAYMENT COMPLETION & DATABASE UPDATE
            System.out.println("---------------------------------------------------------------");
            System.out.println("Thank you for your payment of :\t$" + df.format(amt) +" Dollar");
            System.out.println("There was a transaction fee of :\t$" + df.format(transFee) +" Dollar");
            System.out.println("Your new balance is :\t$" + df.format(updatedBal));
            System.out.println("Payment ID :\t" + paymentID);
            System.out.println("Payment received on:\t" + currentDate);
            System.out.println("---------------------------------------------------------------");
            System.out.print("Press 1 to return to main menu\t:");

            //RETURN TO CUSTOMER MENU
            int num = input.nextInt();
                if(num==1){
                    custMenu(cust_key);
                }

        }catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            try {
                c.rollback();
            } catch (Exception e1) {
                System.err.println(e1.getClass().getName() + ": " + e1.getMessage());
            }            
        }
    }
    



// @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
// @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
// @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
// @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
// @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
// @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
// @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
// @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
// @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
// @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
// @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
// @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
//               Modification Functions
// @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
// @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
// @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
// @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
// @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
// @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
// @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
// @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
// @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
// @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
// @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
// @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@




// @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
//               Delete Functions
// @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@

    public void removeDriver(int custKey, String pNum){
        try {
            clearScreen();
            int totalDrivers = 1;
            int i = 0;
            //CALCULATES TOTAL NUMBER OF DRIVERS FOR A GIVEN POLICY NUMBER
            String sql = "SELECT COUNT(*) " +
            "FROM Driver D, Vehicle V, Vehicle_Driver_Has VV, Policy P " +
            "WHERE " +
                "VV.VIN = V.VIN AND " +
                "VV.driver_key = D.driver_key AND " +
                "P.insured_VIN = VV.VIN AND " +
                "P.policy_num = '"+pNum+"'; ";
            PreparedStatement stmt = c.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while (rs.next())
                totalDrivers = rs.getInt(1);
            //CASE ONLY DEFAULT DRIVER
            if(totalDrivers == 1){
                System.out.println("ERROR: Cannot remove driver from policy containing only 1 driver.");
                System.out.println("REDIRECTING...");
                pause(3000);
                custMenu(custKey);
            }
            //CASE MULTIPLE DRIVERS
            if(totalDrivers > 1){
                //RETRIEVES ALL OF THE FIRST NAMES, LAST NAMES, AND DRIVER KEYS
                //FOR A SPECIFIED POLICY, STORES IN RESPECTIVE ARRAYS.
                sql = "SELECT D.first_name, D.last_name, D.driver_key " +
                "FROM Driver D, Vehicle V, Vehicle_Driver_Has VV, Policy P " +
                "WHERE " +
                "VV.VIN = V.VIN AND " +
                "VV.driver_key = D.driver_key AND " +
                "P.insured_VIN = VV.VIN AND " +
                "P.policy_num = '"+pNum+"';";
                ArrayList<String> first = new ArrayList<String>();
                ArrayList<String> last = new ArrayList<String>();
                ArrayList<Integer> key = new ArrayList<Integer>();

                System.out.println("        First                Last               dKey");
                System.out.println("-----------------------------------------------------");    
                stmt = c.prepareStatement(sql);
                rs = stmt.executeQuery();

                while (rs.next()){
                    first.add(rs.getString(1));
                    last.add(rs.getString(2));
                    key.add(rs.getInt(3));
                }

                first.remove(0);
                last.remove(0);
                key.remove(0);

                for (String f : first) {
                    System.out.printf("%20s %20s %10s\n", first.get(i), last.get(i), key.get(i));
                    i++;
                }

                rs.close();
                System.out.print("\n\nEnter driver key of person to remove: ");
                int num = input.nextInt();
                //DELETE DRIVER FROM DATABASE
                sql = "DELETE FROM Driver WHERE driver_key  = "+num+"";
                stmt = c.prepareStatement(sql);
                stmt.executeUpdate();
                //DELETE DRIVER-VEHICLE LINK FROM DATABASE
                sql = "DELETE FROM Vehicle_Driver_Has WHERE driver_key = "+num+"";
                stmt = c.prepareStatement(sql);
                stmt.executeUpdate();
                // STEP: Commit transaction
                c.commit();
                stmt.close();
                System.out.println("\n\n Successfully removed driver key: " + num);
                pause(2000);
                custMenu(custKey);
            }
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            try {
                c.rollback();
            } catch (Exception e1) {
                System.err.println(e1.getClass().getName() + ": " + e1.getMessage());
            }
        }
    }

    //REMOVAL OF ACCOUNT FROM DATABASE
    void removeAccountInfo(int custKey){

        try {
            //REMOVAL FROM LOGIN
            String sql = "DELETE FROM Login WHERE customer_key = "+custKey+"";
            PreparedStatement stmt = c.prepareStatement(sql);
            stmt.executeUpdate();
            //REMOVAL FROM CUSTOMER
            sql = "DELETE FROM Customer WHERE customer_key = "+custKey+"";
            stmt = c.prepareStatement(sql);
            stmt.executeUpdate();
            //REMOVAL FROM COMPANY TO WHOM CUSTOMER MADE PAYMENTS TO
            sql = "DELETE FROM Company WHERE customer_key = "+custKey+"";
            stmt = c.prepareStatement(sql);
            stmt.executeUpdate();
            //REMOVAL OF POLICY FROM COMPANY
            sql = "DELETE FROM CompanyOwnsPolicy WHERE customer_key = "+custKey+"";
            stmt = c.prepareStatement(sql);
            stmt.executeUpdate();

            // STEP: Commit transaction
            c.commit();
            stmt.close();

            System.out.println("\n\nDeleting...");
            pause(1000);
            System.out.println("Successful!");
            pause(1000);
            //RETURN TO LOGIN MENU
            printLogin(false);

        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            try {
                c.rollback();
            } catch (Exception e1) {
                System.err.println(e1.getClass().getName() + ": " + e1.getMessage());
            }
        }

    }


    public void removeVehicle(String pNum, int cust_key){
    try{
        clearScreen();
        int i = 0;
        System.out.println("                Remove Vehicle");
        System.out.println("**************************************************");

        //RETRIEVES THE FIRST NAME, LAST NAME, MAKER, MODEL, YEAR, COLOR, VIN, MILEAGE OF ALL CARS FOR A GIVEN POLICY
        String sql = "SELECT D.first_name, D.last_name, V.make, V.model, V.year, V.color, V.VIN, V.mileage, P.policy_num " +
        "FROM Driver D, Vehicle_Driver_Has VDH, Policy P, Vehicle V, Customer C " +
        "WHERE " +
        "D.driver_key = VDH.driver_key  " +
            "AND VDH.VIN = V.VIN " +
            "AND V.VIN = P.insured_VIN " +
            "AND C.policy_num = P.policy_num " +
            "AND P.policy_num = '"+pNum+"'; ";
        
        PreparedStatement stmt = c.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();
        //PRINT EACH VEHICLE LINKED TO POLICY X
        while (rs.next()) {
            i++;
            System.out.println("\nVehicle " + i + ": ");
            System.out.println("------------------");  
            System.out.println("Make: " + rs.getString(3) + "\nModel: " + rs.getString(4) + "\nYear: " + rs.getString(5) + "\nColor: " + rs.getString(6) + "\nVIN: " + rs.getString(7) + "\nMileage: " + rs.getInt(8) + "\nPolicy Number: " + rs.getString(9));
            System.out.println("------------------");
        }
        rs.close();
        String vin = System.console().readLine("Enter vehicle VIN you would like to remove: ");
        //DELETE VEHICLE FROM Vehicle_Driver_Has
        sql = "DELETE FROM Vehicle_Driver_Has WHERE VIN = '"+vin+"';";
        stmt = c.prepareStatement(sql);
        stmt.executeUpdate();
        //DELETE VEHICLE FROM Policy
        sql = "DELETE FROM Policy WHERE insured_VIN = '"+vin+"';";
        stmt = c.prepareStatement(sql);
        stmt.executeUpdate();
        //DELETE VEHICLE FROM VEHICLE
        sql = "DELETE FROM Vehicle WHERE VIN = '"+vin+"';";
        stmt = c.prepareStatement(sql);
        stmt.executeUpdate(); 
        
        c.commit();
        stmt.close();
        System.out.println("Successfuly deleted vehicle.");
        System.out.println("Redirecting...");
        pause(3000);
        //TAKE BACK TO CUSTMENU PASSES IN CUST_KEY
        custMenu(cust_key);
    }
    catch (Exception e) {
        System.err.println(e.getClass().getName() + ": " + e.getMessage());
        try {
            c.rollback();
        } catch (Exception e1) {
            System.err.println(e1.getClass().getName() + ": " + e1.getMessage());
        }
    }
}


// @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
//               Update Functions
// @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@

//UPDATE ACCOUNT MENU
public void updateAccount(int cust_key, String pNum){
    System.out.println("                                \nUpdate Account");
    System.out.println("*****************************************************************************");
    String choice = System.console().readLine("1) Change Name \n2) Update Login \n\toption\t#");
    
    if(choice.equals("1")){
        //SHOW NAME CHANGE OPTIONS
        clearScreen();
        changeName(cust_key, pNum);
    }
    else if(choice.equals("2")){
        //SHOW LOGIN INFO CHANGE OPTIONS
        clearScreen();
        changeLogin(cust_key);
    }
    System.out.println("*****************************************************************************");
}

//CASE NAME CHANGE
public void changeName(int cust_key, String pNum){
    clearScreen();

    System.out.println("*****************************************************************************");
    String choice = System.console().readLine("1) Change First Name \n2) Change Last Name \n3) Change Full Name \n\toption\t#");
    try{
        String sql;
        PreparedStatement stmt;
        if(choice.equals("1")){
            String fName = System.console().readLine("Enter new First Name :\t");
            //UPDATE first_Name on Customer
            sql = "UPDATE Customer SET first_name = ? WHERE customer_key = " + cust_key + ";"; 
            stmt = c.prepareStatement(sql);
            stmt.setString(1,fName);
            stmt.executeUpdate();
            //UPDATE first_Name on Policy
            sql = "UPDATE Policy SET insured_fname = ? WHERE policy_num= '" + pNum + "';"; 
            stmt = c.prepareStatement(sql);
            stmt.setString(1,fName);
            stmt.executeUpdate();
            //UPDATE first_Name on Driver
            int offset = 1000 +cust_key;
            sql = "UPDATE Driver SET first_name = ? WHERE driver_key= " + offset + ";"; 
            stmt = c.prepareStatement(sql);
            stmt.setString(1,fName);
            stmt.executeUpdate();

            c.commit();
            stmt.close();
            System.out.println("Your first name has been updated to: \t"+fName);
            pause(1500);
            custMenu(cust_key);

        }else if (choice.equals("2")){
            String lName = System.console().readLine("Enter new Last Name: \t");
            //UPDATE last_Name on Customer
            sql = "UPDATE Customer SET last_name = ? WHERE customer_key = " + cust_key + ";"; 
            stmt = c.prepareStatement(sql);
            stmt.setString(1,lName);
            stmt.executeUpdate();
            //UPDATE last_Name on Policy
            sql = "UPDATE Policy SET insured_lname = ? WHERE policy_num= '" + pNum + "';"; 
            stmt = c.prepareStatement(sql);
            stmt.setString(1,lName);
            stmt.executeUpdate();
            //UPDATE first_Name on Driver
            int offset = 1000 +cust_key;
            sql = "UPDATE Driver SET last_name = ? WHERE driver_key= " + offset + ";"; 
            stmt = c.prepareStatement(sql);
            stmt.setString(1,lName);
            stmt.executeUpdate();
            c.commit();
            stmt.close();
            System.out.println("Your last name has been updated to\t:"+lName);
            pause(1500);
            custMenu(cust_key);
        }
        else if (choice.equals("3")){
            String f_Name = System.console().readLine("Enter new First Name :\t");
            String l_Name = System.console().readLine("Enter new Last Name :\t");
            //UPDATE first_Name on Customer
            sql = "UPDATE Customer SET first_name = ? WHERE customer_key = " + cust_key + ";"; 
            stmt = c.prepareStatement(sql);
            stmt.setString(1,f_Name);
            stmt.executeUpdate();
            //UPDATE first_Name on Policy
            sql = "UPDATE Policy SET insured_fname = ? WHERE policy_num= '" + pNum + "';"; 
            stmt = c.prepareStatement(sql);
            stmt.setString(1,f_Name);
            stmt.executeUpdate();
            //UPDATE first_Name on Driver
            int offset = 1000 +cust_key;
            sql = "UPDATE Driver SET first_name = ? WHERE driver_key= " + offset + ";"; 
            stmt = c.prepareStatement(sql);
            stmt.setString(1,f_Name);
            stmt.executeUpdate();

            //UPDATE last_Name on Customer
            sql = "UPDATE Customer SET last_name = ? WHERE customer_key = " + cust_key + ";"; 
            stmt = c.prepareStatement(sql);
            stmt.setString(1,l_Name);
            stmt.executeUpdate();
            //UPDATE last_Name on Policy
            sql = "UPDATE Policy SET insured_lname = ? WHERE policy_num= '" + pNum + "';"; 
            stmt = c.prepareStatement(sql);
            stmt.setString(1,l_Name);
            stmt.executeUpdate();
            //UPDATE first_Name on Driver
            sql = "UPDATE Driver SET last_name = ? WHERE driver_key= " + offset + ";"; 
            stmt = c.prepareStatement(sql);
            stmt.setString(1,l_Name);
            stmt.executeUpdate();

            c.commit();
            stmt.close();
            System.out.println("Your name has been updated to\t:" + l_Name + "," + f_Name);
            pause(1500);
            custMenu(cust_key);
        }
        custMenu(cust_key);
    }catch(Exception e){
        System.err.println(e.getClass().getName() + ": " + e.getMessage());
        try {
            c.rollback();
        } catch (Exception e1) {
            System.err.println(e1.getClass().getName() + ": " + e1.getMessage());
        }
    }

    System.out.println("*****************************************************************************");

}

//CASE UPDATE LOGIN
public void changeLogin(int cust_key){
    clearScreen();
    System.out.println("*****************************************************************************");
    String choice = System.console().readLine("1) Change Username \n2) Change password \n3) Change Username and Password\n\toption\t#");
    try{
        String sql;
        PreparedStatement stmt;
        if(choice.equals("1")){
            String usr = System.console().readLine("Enter new email :\t");
            //UPDATE USERNAME ONLY
            sql = "UPDATE Login SET username = ? WHERE customer_key = " + cust_key + ";"; 
            stmt = c.prepareStatement(sql);
            stmt.setString(1,usr);
            stmt.executeUpdate();
            c.commit();
            stmt.close();

            System.out.println("Your username has been updated to\t:" + usr);
            pause(1500);
            printLogin(false);

        }
        else if (choice.equals("2")){
            String psw = System.console().readLine("Enter new password :\t");
            //UPDATE PASSWORD ONLY
            sql = "UPDATE Login SET password = ? WHERE customer_key = " + cust_key + ";"; 
            stmt = c.prepareStatement(sql);
            stmt.setString(1,psw);
            stmt.executeUpdate();
            c.commit();
            stmt.close();

            System.out.println("Your password has been updated to\t:" + psw);
            pause(1500);
            printLogin(false);
        }
        else if (choice.equals("3")){
            //UPDATE USERNAME & PASSWORD 
            String user = System.console().readLine("Enter new email :\t");
            String pass = System.console().readLine("Enter new password :\t");

            sql = "UPDATE Login SET username = ? WHERE customer_key = " + cust_key + ";"; 
            stmt = c.prepareStatement(sql);
            stmt.setString(1,user);
            stmt.executeUpdate();

            sql = "UPDATE Login SET password = ? WHERE customer_key = " + cust_key + ";";  
            stmt = c.prepareStatement(sql);
            stmt.setString(1,pass);
            stmt.executeUpdate();
            
            c.commit();
            stmt.close();
            //RETURN TO LOGIN TO VERIRY NEW INFO
            System.out.println("***********************************************************************");
            System.out.println("              Your login information has been updated                 ");
            System.out.println("         please login with your new username and password             ");
            pause(3000);
            printLogin(false);
        }

    }catch(Exception e){
        System.err.println(e.getClass().getName() + ": " + e.getMessage());
        try {
            c.rollback();
        } catch (Exception e1) {
            System.err.println(e1.getClass().getName() + ": " + e1.getMessage());
        }
    }
    System.out.println("*****************************************************************************");
}



// @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
//               Insert Functions
// @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@

public void addDriver(String pNum, int cust_key){
    try{
        int driverKeyCount = 0;
        int custKey = 0;
        String VIN = "";
        clearScreen();

        System.out.println("Adding driver to Policy #: " + pNum);
        System.out.println("Required (*)");
        System.out.println("-----------------------------");
        //DATABASE QUERY TO SELECT ALL DRIVERS LINKED TO CUSTMER X
        String sql = "SELECT D.first_name, D.last_name, D.DOB, D.license_ID, D.age, D.experience, D.violation_pts, D.violation_des, D.driver_key, P.insured_VIN , C.customer_key " +
        "FROM Driver D, Vehicle_Driver_Has VDH, Policy P, Vehicle V, Customer C " +
        "WHERE " +
        "D.driver_key = VDH.driver_key  " +
            "AND VDH.VIN = V.VIN " +
            "AND V.VIN = P.insured_VIN " +
            "AND C.policy_num = P.policy_num " +
            "AND P.policy_num = '"+pNum+"';";
        PreparedStatement stmt = c.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            VIN = rs.getString(10);
        }

        sql = "SELECT MAX(driver_key) FROM Driver;";
        stmt = c.prepareStatement(sql);
        rs = stmt.executeQuery();
        while (rs.next()) {
            driverKeyCount = rs.getInt(1);
        }
        rs.close();
        
        //COLLECT NEW DRIVER INFROMATION
        String fn = System.console().readLine("* Enter first name: ");
        String ln = System.console().readLine("* Enter last name: ");
        String dob = System.console().readLine("* Enter DOB(MM/DD/YY): ");
        String lid = System.console().readLine("* Enter license ID: ");
        System.out.print("* Enter age: ");
        int age = input.nextInt();
        while (age<18){
            System.err.println("Driver must be 18 or older to drive and get an insurane policy with VIP");
            System.out.print("Enter reenter appropiate age: ");
            age = input.nextInt();
        }
        System.out.print("* Enter experience: ");
        int exp = input.nextInt();
        System.out.print("* Enter violation points: ");
        int vPoints = input.nextInt();
        String vDescr = System.console().readLine("* Enter violation description: ");
        stmt.close();

        driverKeyCount++;
        //INSERTION INTO DRIVER AND VEHICLE_DRIVER_HAS TABLES FROM DATABASE
        insertDriver(fn, ln, dob, lid, age, exp, vPoints, vDescr, driverKeyCount);
        insertVehicle_Driver_Has(driverKeyCount, VIN, lid);
        String choice2 = System.console().readLine("Press 1 to return to main menu: ");
        if(choice2.equals("1"))
            custMenu(cust_key);
    }
    catch (Exception e) {
        System.err.println(e.getClass().getName() + ": " + e.getMessage());
        try {
            c.rollback();
        } catch (Exception e1) {
            System.err.println(e1.getClass().getName() + ": " + e1.getMessage());
        }
    }
    
}


public void addVehicle(String pNum, int cust_key, Double bal){
        try{
            clearScreen();
            int driverKeyCount = 0;
            ArrayList<String> first = new ArrayList<String>();
            ArrayList<String> last = new ArrayList<String>();
            ArrayList<String> lid = new ArrayList<String>();
            ArrayList<String> DOB = new ArrayList<String>();
            ArrayList<Integer> key = new ArrayList<Integer>();
            int i = 0;
            
            System.out.println("Adding Vehicle to Policy #: " + pNum);
            System.out.println("Required (*)");
            System.out.println("-----------------------------");

            String sql = "SELECT MAX(driver_key) FROM Driver;";
            PreparedStatement stmt = c.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()){
                driverKeyCount = rs.getInt(1);
            }
            //DATA FOR VEHICLE FROM USER INPUT
            String year = System.console().readLine("* Enter year: ");
            String make = System.console().readLine("* Enter make: ");
            String model = System.console().readLine("* Enter model: ");
            System.out.print("* Enter price: ");
            Double price = input.nextDouble();
            String color = System.console().readLine("* Enter color: ");
            String vin = System.console().readLine("* Enter vin: ");
            System.out.print("* Enter mileage : ");
            int mileage = input.nextInt();
            insertVehicle(year, make, model, price, color, vin, mileage);

           
            sql = "SELECT first_name, last_name, D.license_ID, D.driver_key, make, model, VV.VIN, VV.license_ID, D.DOB " +
                        "FROM Driver D, Vehicle V, Vehicle_Driver_Has VV, Policy P " +
                        "WHERE " +
                        "VV.VIN = V.VIN AND " +
                            "VV.driver_key = D.driver_key AND " +
                            "P.insured_VIN = VV.VIN AND " +
                            "P.policy_num = '"+pNum+"'; ";
            stmt = c.prepareStatement(sql);
            rs = stmt.executeQuery();

            //Needs to perform two or more function insertions.
            while (rs.next()) {
                first.add(rs.getString(1));
                last.add(rs.getString(2));
                lid.add(rs.getString(3));
                key.add(rs.getInt(4));
                DOB.add(rs.getString(9));


                insertPolicy(first.get(i), last.get(i), DOB.get(i), vin, pNum, price * 0.01);
                insertVehicle_Driver_Has(key.get(i), vin, lid.get(i));
                i++;
            }

            rs.close();
            
            //UPDATING policy_rate ON TABLE Policy
            sql = "UPDATE Policy SET policy_rate = ? WHERE policy_num = '"+pNum+"';";
            stmt = c.prepareStatement(sql);
            stmt.setDouble(1, bal + 100);
            stmt.executeUpdate();
            //UPDATING balance ON TABLE Customer 
            sql = "UPDATE Customer SET balance = ? WHERE policy_num = '"+ pNum+"';";
            stmt = c.prepareStatement(sql);
            stmt.setDouble(1, bal + 100);
            stmt.executeUpdate();
            //UPDATING customer_bal ON TABLE CompanyOwnsPolicy
            sql = "UPDATE CompanyOwnsPolicy SET customer_bal = ? WHERE policy_num = '"+ pNum+"';";
            stmt = c.prepareStatement(sql);
            stmt.setDouble(1, bal + 100);
            stmt.executeUpdate();
            //UPDATING customer_bal ON TABLE CompanyOwnsPolicy
            sql = "UPDATE Company SET customer_bal = ? WHERE policy_num = '"+pNum+"';";
            stmt = c.prepareStatement(sql);
            stmt.setDouble(1, bal + 100);
            stmt.executeUpdate();

            c.commit();
         
            rs.close();
            stmt.close();
                
            String choice4 = System.console().readLine("Press 1 to return to main menu: ");
            if(choice4.equals("1"))
                custMenu(cust_key);

        }
        catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            try {
                c.rollback();
            } catch (Exception e1) {
                System.err.println(e1.getClass().getName() + ": " + e1.getMessage());
            }
        }
    }





// @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
// @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
// @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
// @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
// @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
// @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
// @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
// @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
// @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
// @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
// @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
// @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
//                   View Functions
// @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
// @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
// @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
// @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
// @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
// @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
// @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
// @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
// @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
// @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
// @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
// @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@


public void viewData(){
    try {
        clearScreen();
        int i = 0;
        System.out.println("(1) Company\n" + "(2) Customer\n" + "(3) Discount\n" + "(4) Driver\n" + "(5) Login\n" + "(6) Payment\n" + "(7) Policy\n" + "(8) Vehicle\n" + "(9) VehicleDriverHas\n" + "(10) Company_Owns_Policy\n");
        System.out.print("Enter table name to view data: ");
        String tableName = input.next();
        ArrayList<String> tables = new ArrayList<String>();
        File file = new File("Text files/VIew Tables/"+tableName+".txt");
        Scanner read = new Scanner(file); 
        while(read.hasNextLine())
            tables.add(read.nextLine());
        // STEP: Commit transaction
        
        PreparedStatement stmt = c.prepareStatement(tables.get(2));
        ResultSet rs = stmt.executeQuery();
        c.commit();
        read.close();

        clearScreen();
        System.out.println("Displaying information for " + tableName + "\n\n\n");

        while (rs.next()) {
            i++;
            if(tableName.toLowerCase().equals("company")){
                if(i == 1)
                    System.out.println(tables.get(0) + "\n" + tables.get(1));
                System.out.printf("%20s %10s %10f %10d\n", rs.getString("name"), rs.getString("policy_num"), rs.getDouble("customer_bal"), rs.getInt("customer_key"));
            } 
            else if(tableName.toLowerCase().equals("customer")){
                if(i == 1)
                    System.out.println(tables.get(0) + "\n" + tables.get(1));
                System.out.printf("%20s %20s %10s %10f %10s %10d\n", rs.getString("first_name"), rs.getString("last_name"), rs.getString("DOB"), rs.getDouble("balance"), rs.getString("policy_num"), rs.getInt("customer_key"));
            }
            else if(tableName.toLowerCase().equals("discount")){
                if(i == 1)
                    System.out.println(tables.get(0) + "\n" + tables.get(1));
                System.out.printf("%10d %5s %10s %10f %10s %10d %10s\n", rs.getInt("driver_key"), rs.getString("student"), rs.getString("military"), rs.getDouble("GPA"), rs.getString("disability"), rs.getInt("goodTimeDriving"),  rs.getString("marital_status"));
            }
            else if(tableName.toLowerCase().equals("driver")){
                if(i == 1)
                    System.out.println(tables.get(0) + "\n" + tables.get(1)); 
                System.out.printf("%10s %10s %5s %10s %5d %10d %5d %30s %10d\n", rs.getString("first_name"), rs.getString("last_name"), rs.getString("DOB"), rs.getString("license_id"), rs.getInt("age"), rs.getInt("experience"), rs.getInt("violation_pts"), rs.getString("violation_des"), rs.getInt("driver_key"));
            }
            else if(tableName.toLowerCase().equals("login")){
                if(i == 1)
                    System.out.println(tables.get(0) + "\n" + tables.get(1));
                System.out.printf("%30s %30s %30d\n", rs.getString("username"), rs.getString("password"), rs.getInt("customer_key"));
            }
            else if(tableName.toLowerCase().equals("payment")){
                if(i == 1)
                    System.out.println(tables.get(0) + "\n" + tables.get(1));
                System.out.printf("%10d %10s %10d %10f %10f %10s\n", rs.getInt("payment_id"), rs.getString("method"),
                                                                rs.getInt("customer_key"), rs.getDouble("payment_amt"),
                                                                rs.getDouble("transaction_fee"), rs.getString("date"));
            }   
             else if(tableName.toLowerCase().equals("policy")){
                 if(i == 1)
                     System.out.println(tables.get(0) + "\n" + tables.get(1));
                 System.out.printf("%10s %10s %10s %10s %10s %10f\n", rs.getString("insured_fname"), 
                    rs.getString("insured_lname"), rs.getString("insured_DOB"),
                    rs.getString("insured_VIN"), rs.getString("policy_num"), rs.getDouble("policy_rate"));
             }
             else if(tableName.toLowerCase().equals("vehicle")){
                 if(i == 1)
                     System.out.println(tables.get(0) + "\n" + tables.get(1));
                 System.out.printf("%10d %10s %10s %10f %10s %10s %10d\n", rs.getInt("year"), rs.getString("make"),
                 rs.getString("model"), rs.getDouble("price"), rs.getString("color"), rs.getString("VIN"),rs.getInt("mileage"));
             }
             else if(tableName.toLowerCase().equals("vehicle_driver_has")){
                if(i == 1)
                    System.out.println(tables.get(0) + "\n" + tables.get(1));
                    System.out.printf("%30d %30s %30s\n", rs.getInt(1), rs.getString(2), rs.getString(3));
             }
            else if(tableName.toLowerCase().equals("companyownspolicy")){
                if(i == 1)
                    System.out.println(tables.get(0) + "\n" + tables.get(1));
                System.out.printf("%20s %30s %20f %20d\n", rs.getString("company_name"), rs.getString("policy_num"), rs.getDouble(3), rs.getInt("customer_key"));
            }
            }
        rs.close();
        stmt.close();
    } catch (Exception e) {
        System.err.println(e.getClass().getName() + ": " + e.getMessage());
    }
    System.out.print("\n\n\n\n Press '1' to return to main menu: ");
    int xxy = input.nextInt();
    if(xxy==1){
        adminMenu();
    }
}


public void viewDriver(String pNum, int cust_key){
try{
    clearScreen();
    int i = 0;
    System.out.println("Driver records for Policy #: " + pNum);
    System.out.println("-----------------------------\n\n");
    String sql =  "SELECT D.first_name, D.last_name, D.DOB, D.license_ID, D.age, D.experience, D.violation_pts, D.violation_des, D.driver_key, P.insured_VIN , C.customer_key " +
    "FROM Driver D, Vehicle_Driver_Has VDH, Policy P, Vehicle V, Customer C " +
    "WHERE " +
    "D.driver_key = VDH.driver_key  " +
        "AND VDH.VIN = V.VIN " +
        "AND V.VIN = P.insured_VIN " +
        "AND C.policy_num = P.policy_num " +
        "AND P.policy_num = '"+pNum+"';";
    PreparedStatement stmt = c.prepareStatement(sql);
    ResultSet rs = stmt.executeQuery();
    while (rs.next()) {
        i++;
        System.out.println("Driver " + i + ": ");
        System.out.println("------------------");
        System.out.println("First name: " + rs.getString(1) + "\nLast name: " + rs.getString(2) + "\nDOB: " + rs.getString(3) + "\nLicense ID: " + rs.getString(4) + "\nAge: " + rs.getInt(5) + "\nExperience: " + rs.getInt(6) + "\nViolation Points: " + rs.getInt(7) + "\nViolation Description: " + rs.getString(8) + "\nDriver key: " + rs.getInt(9));
        System.out.println("------------------");
        
    }
    rs.close();
    stmt.close();

    String choice1 = System.console().readLine("Press 1 to return to main menu: ");
    if(choice1.equals("1"))
        custMenu(cust_key);
}
catch (Exception e) {
        System.err.println(e.getClass().getName() + ": " + e.getMessage());
        try {
            c.rollback();
        } catch (Exception e1) {
            System.err.println(e1.getClass().getName() + ": " + e1.getMessage());
        }
    }
}


public void viewVehicles(String pNum, int cust_key){
try{
    int i = 0;
    clearScreen();
    System.out.println("Vehicles under customer key: " + cust_key);
    System.out.println("******************************************************\n\n");
    String sql = "SELECT D.first_name, D.last_name, V.make, V.model, V.year, V.color, V.VIN, V.mileage, P.policy_num, V.price " +
    "FROM Driver D, Vehicle_Driver_Has VDH, Policy P, Vehicle V, Customer C " +
    "WHERE " +
    "D.driver_key = VDH.driver_key  " +
        "AND VDH.VIN = V.VIN " +
        "AND V.VIN = P.insured_VIN " +
        "AND C.policy_num = P.policy_num " +
        "AND P.policy_num = '"+pNum+"'; ";
    
    PreparedStatement stmt = c.prepareStatement(sql);
    ResultSet rs = stmt.executeQuery();
    while (rs.next()) {
        i++;
        System.out.println("\nVehicle " + i + ": ");
        System.out.println("------------------");  
        System.out.println("Make: " + rs.getString(3) + "\nModel: " + rs.getString(4) + "\nYear: " + rs.getString(5) + "\nColor: " + rs.getString(6) + "\nVIN: " + rs.getString(7) + "\nMileage: " + rs.getInt(8) + "\nPolicy Number: " + rs.getString(9) + "\nVehicle Price: " + rs.getDouble(10));
        System.out.println("------------------");
    }
    rs.close();
    stmt.close();

    String choice3 = System.console().readLine("\n\nPress 1 to return to main menu: ");
    if(choice3.equals("1"))
        custMenu(cust_key);

}
catch (Exception e) {
        System.err.println(e.getClass().getName() + ": " + e.getMessage());
        try {
            c.rollback();
        } catch (Exception e1) {
            System.err.println(e1.getClass().getName() + ": " + e1.getMessage());
        }
    }
}

//VIEW EVERY PAYMENT OF X CUSTOMER
public void viewPayments(String fName, String lName, int cust_key, Double bal){
clearScreen();
System.out.println("\tGreetings " + lName + ", " + fName + "!");

System.out.println("*******************************************");
System.out.println("Your current Balance is: \t$" + df.format(bal));
System.out.println("*******************************************");

try{
    //GET PAYMENT FROM DATABASE
    String sql = "SELECT payment_id, method, customer_key, payment_amt, transaction_fee, date FROM Payment WHERE customer_key =" + cust_key +";";
    PreparedStatement stmt = c.prepareStatement(sql);
    ResultSet rs = stmt.executeQuery();

    while(rs.next()){
        //PRINT EVERY PAYMENT BY CUSTOMER X
        System.out.println("\n*******************************************");
        System.out.println("Payment ID :\t\t" + rs.getInt(1));
        System.out.println("Payment method :\t\t" + rs.getString(2));
        System.out.println("Custumer ID :\t\t" + rs.getInt(3));

        // Double prevPaymt =  rs.getDouble(4) +  rs.getDouble(4)*rs.getDouble(5);
        Double feeX = (rs.getDouble(4))*(rs.getDouble(5));
        System.out.println("Thank you for your payment of :\t$" + df.format(rs.getDouble(4)) +" Dollar");
        System.out.println("Transaction fee :\t$" + feeX);
        System.out.println("Payment received on:\t" + rs.getString(6));
        System.out.println("*******************************************\n");
    }
    c.commit();
    stmt.close();

    //RETURN TO CUSTOMER MENU
    System.out.print("Press 1 to return to main menu: ");
    int num = input.nextInt();
        if(num==1){custMenu(cust_key);}

}catch(Exception e){
        System.err.println(e.getClass().getName() + ": " + e.getMessage());
        try {
            c.rollback();
        } catch (Exception e1) {
            System.err.println(e1.getClass().getName() + ": " + e1.getMessage());
        }
    }
}



    

// @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
// @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
// @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
// @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
// @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
// @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
// @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
// @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
// @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
// @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
// @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
// @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
//                   Menu Functions
// @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
// @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
// @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
// @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
// @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
// @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
// @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
// @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
// @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
// @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
// @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
// @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@


//This admin menu can be toggeled ON and OFF depending on the truth value of the isAdmin variable.
public void adminMenu(){
    int nChoice = 0;
    while(nChoice != 3){
            clearScreen();
            System.out.println("(1) Insert data");
            System.out.println("(2) View Data");
            System.out.println("(3) Exit");
            System.out.print("\n\n\nChoice: ");
            nChoice = input.nextInt();
        if (nChoice == 1)
            menuInsert();
        else if (nChoice == 2){
            do
                viewData();  
            while(input.nextInt() != 1);
            clearScreen();
            }
        else
            System.out.println("Exiting...");  
    }
}


public void custMenu(int cust_key){
    clearScreen();
    try {
        String fName = "";
        String lName = "";
        String DOB = "";
        Double bal = 0.0;
        String pNum = " ";
        String user = " ";
        String pass = " ";
        String sql = "SELECT Cu.first_name, Cu.last_name, Cu.DOB, Cu.balance, Cu.policy_num, L.username, L.password " +
        "FROM " +
        "Customer Cu, Login L " +
        "WHERE " +
        "Cu.customer_key = L.customer_key " +
        "AND Cu.customer_key = "+cust_key+"; ";
        
        PreparedStatement stmt = c.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();
    
        while (rs.next()) {
            fName = rs.getString(1);
            lName = rs.getString(2);
            DOB = rs.getString(3);
            bal = rs.getDouble(4);
            pNum = rs.getString(5);
            user = rs.getString(6);
            pass = rs.getString(7);
        }
        if(countMain == 1){
            System.out.println("Welcome Back " + fName + " " + lName + " to VIP Insurance Online App ! Cust key ID: " + cust_key + "\n\n");
            pause(2000);
        } 
        countMain++;
        clearScreen();
        System.out.println("Customer Account Summary");
        System.out.println("------------------------------------------------------------");
        System.out.print("username:\t" + user + "\n" + "First Name:\t" + fName + "\n" + "Last Name:\t" + lName + "\n" + 
        "DOB:\t\t" + DOB + "\n" + "Balance:\t" + df.format(bal) + "\n"  + "Policy Number:\t" + pNum + "\n");
        System.out.println("------------------------------------------------------------");

        String choice = System.console().readLine("\n\n(1) Payments \n(2) Manage Policy \n(3) Account settings \n(4) Logout  \n\nOption #: ");
        System.out.println("------------------------------------------------------------");
        //CASE PAYMENTS
        if(choice.equals("1")){
            clearScreen();
            System.out.println("\n                                Payments");
            System.out.println("**************************************************************************");
            String selection = System.console().readLine("\n(1) Make payment \n(2) View payments \n\nOption #\t");
            if(selection.equals("1")){
                //MAKE A PAYMENT
                makePayment(fName, lName, cust_key, bal, pNum);    
            }else if(selection.equals("2")){
                //VIEW ALL PREVIOUS PAYMENTS
                viewPayments(fName, lName, cust_key, bal);
            }
        }
        //CASE MANAGE POLICY
        if(choice.equals("2")){
            clearScreen();
            System.out.println();
            managePolicy(cust_key, pNum, bal);
        }
        //CASE ACCOUNT SETTINGS
        if(choice.equals("3")){
            
            clearScreen();
            System.out.println("                                \nAccount Settings");
            System.out.println("***************************************************************************************");
            String selection = System.console().readLine("\n \n(1) Update account information \n(2) Remove account information \n\nOption\t#:");
            if(selection.equals("1")){
                //CALL TO UPDATE ACCOUNT 
                updateAccount(cust_key, pNum);
            }
            if(selection.equals("2")){
                if(bal == 0){
                    //CALL TO REMOVE ACCOUNT ONLY IF BALANCE IS PAID OFF
                    removeAccountInfo(cust_key);
                } else{
                    //OTHERWISE PROMPT PAYMENT
                    System.out.println("\n\nCurrent balance: " + bal + "\n\nPlease ensure your account balance is paid in full prior to account deletion!");
                    pause(2500);
                    makePayment(fName, lName, cust_key, bal, pNum);
                }
            }
        }
        //CASE LOGOUT
        else if(choice.equals("4")){
            clearScreen();
            System.out.println("**********  Thank you for using VIP online services  **********");
            pause(3000);
            printLogin(false);
        }
    }catch (Exception e) {
        System.err.println(e.getClass().getName() + ": " + e.getMessage());
        try {
            c.rollback();
        } catch (Exception e1) {
            System.err.println(e1.getClass().getName() + ": " + e1.getMessage());
        }
    }
}


    public void menuInsert(){
        clearScreen();
        manual = true;
        String sChoice = System.console().readLine("Enter name of table to insert data: ");
        if(sChoice.equalsIgnoreCase("customer"))
            insertCustomer("", "", "", 0, "", 0);
        else if(sChoice.equalsIgnoreCase("login"))
            insertLogin("", "", 0);
        else if(sChoice.equalsIgnoreCase("payment"))
            insertPayment(0, "", 0, 0, 0, "");
        else if(sChoice.equalsIgnoreCase("policy"))
            insertPolicy("", "", "", "", "", 0);
        else if(sChoice.equalsIgnoreCase("vehicle"))
            insertVehicle("", "", "", 0, "", "", 0);
        else if(sChoice.equalsIgnoreCase("driver"))
            insertDriver("", "", "", "", 0, 0, 0, "", 0);
        else if(sChoice.equalsIgnoreCase("company"))
            insertCompany("", "", 0, 0);
        else if(sChoice.equalsIgnoreCase("discount") || sChoice.equalsIgnoreCase("discounts"))
            insertDiscount(0, "", "",0, "", 0, "");
        else if(sChoice.equalsIgnoreCase("companyownspolicy"))
            insertCompanyOwnsPolicy("", "",0,0);
        else if(sChoice.equalsIgnoreCase("vehicle_driver_has"))
            insertVehicle_Driver_Has(0,"","");
    }


// @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
// @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
// @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
// @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
// @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
// @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
// @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
// @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
// @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
// @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
// @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
// @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
//                   Utility Functions
// @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
// @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
// @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
// @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
// @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
// @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
// @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
// @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
// @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
// @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
// @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
// @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    

    //Allows for the program to pause momentarily for a parameterized time.
    public void pause(int time){
        try {
            Thread.sleep(time);
        } catch(InterruptedException ex) {
            Thread.currentThread().interrupt();
        }    }
    
    
        public static void clearScreen() {  
        System.out.print("\033[H\033[2J");  
        System.out.flush();  
    }  


    //Function that runs the program, to encapsulate the data base object in the main driver method.
    public void run(String dataBaseFile, String dropTxtFile, String tableTxtFile, boolean populate, boolean isAdmin){
        
        clearScreen();
        openConnection(dataBaseFile);
       if(populate){
        dropTables(dropTxtFile);
        createTables(tableTxtFile);
        populateTables();
       }
        printLogin(isAdmin);
        closeConnection();
    }


    public String generateRandom(int charSize, int numSize){
        Random rnd = new Random();
        String letters = "";
        for(int i=0; i<charSize; i++){
            char temp = (char)(rnd.nextInt((90 - 65) + 1) + 65);
            letters+=temp;
        }
        letters+=" ";
        for(int i=0; i<numSize; i++){
            int temp = rnd.nextInt(9);
            letters+=temp;
        }
        return letters;
    }


    //This discount function takes into account a given customer key, and then checks the discounts table to see what information
    //in the tuple populated is able to apply discount. For every given value, the returned x is being incremented or remains
    //the same to return that value and use it later.
    public Double discounts(int cust){
        double x = 0.0;
        try{
            String student = "";
            String military = "";
            Double GPA = 0.0;
            String disability = "";
            int goodTimeDriving = 0;
            String married = "";
    
            String sql = "SELECT * FROM Discount WHERE driver_key = "+(1000 + cust)+"";
            
            PreparedStatement stmt = c.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            
            while(rs.next()){
                student = rs.getString(2);
                military = rs.getString(3);
                GPA = rs.getDouble(4);
                disability = rs.getString(5);
                goodTimeDriving = rs.getInt(6);
                married = rs.getString(7);
            }
           
            System.out.println("   Discounts applied");
            System.out.println("----------------------");
            if(student.equalsIgnoreCase("Y")){
                x = x + 0.01;
                System.out.println("Student \t[x]");
            }
            if(student.equalsIgnoreCase("n")){
                System.out.println("Student \t[ ]");
            }
    
            if(military.equalsIgnoreCase("Y")){
                x = x + 0.02;
                System.out.println("Military \t[x]");
            }
            if(military.equalsIgnoreCase("n")){
                System.out.println("Military \t[ ]");
            }
    
            if(disability.equalsIgnoreCase("Y")){
                x = x + 0.015;
                System.out.println("Disability \t[x]");
            }
            if(disability.equalsIgnoreCase("n")){
                System.out.println("Disability \t[ ]");
            }
    
            if(married.equalsIgnoreCase("Y")){
                x = x + 0.01;
                System.out.println("Married \t[x]");
            }
            if(married.equalsIgnoreCase("n")){
                System.out.println("Married \t[ ]");
            }
            
            
    
            if(goodTimeDriving >= 4 && goodTimeDriving <= 7){
                x = x + 0.01;
                System.out.println("GTD \t\t[x]");
    
            }
            if(goodTimeDriving < 4){
                System.out.println("GTD \t\t[ ]");
            }
           
    
            if(goodTimeDriving > 7 && goodTimeDriving <= 10){
                x = x + 0.02;
                System.out.println("GTD \t\t[x]");
            }
           
            if(goodTimeDriving > 10){
                x = x + 0.03;
                System.out.println("GTD \t\t[x]");
    
            }
            
    
    
            if(GPA < 2){
                x = x + 0.01;
                System.out.println("GPA \t\t[ ]");
    
            }
            
            if(GPA >= 2 && GPA <= 3){
                x = x + 0.01;
                System.out.println("GPA \t\t[x]");
            }
            if(GPA > 3 && GPA <= 3.5){
                x = x + 0.02;
                System.out.println("GPA \t\t[x]");
            }
            if(GPA > 3.5 && GPA <= 4.0){
                x = x + 0.03;
                System.out.println("GPA \t\t[x]");
            }
            
            System.out.println("\n\n\n\n");
        }
        catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            try {
                c.rollback();
            } catch (Exception e1) {
                System.err.println(e1.getClass().getName() + ": " + e1.getMessage());
            }
        }
        return x;
    }


    public void populateTables(){
        populate("Customer");
        populate("Company");
        populate("Discount");
        populate("Driver");
        populate("Login");
        populate("Payment");
        populate("Policy");
        populate("Vehicle");
        populate("CompanyOwnsPolicy");
        populate("Vehicle_Driver_Has");
        System.out.println("\n\n\n Preparing program...");
        pause(1000);
    }
}