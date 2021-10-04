-- Basic INSERTION modification statements allowing 
-- for non-static and static input into program during runtime. 
-- Question marks included as implemented in java.
    -- INSERT INTO Customer(first_name, last_name, DOB, balance, policy_num, customer_key) VALUES(?, ?, ?, ?, ?, ?)
    -- INSERT INTO Login(username, password, customer_key) VALUES(?, ?, ?)
    -- INSERT INTO Payment(payment_id, method, customer_key, payment_amt, transaction_fee, date) VALUES(?, ?, ?, ?, ?, ?)
    -- INSERT INTO Policy(insured_fname, insured_lname, insured_DOB, insured_VIN, policy_num, policy_rate) VALUES(?, ?, ?, ?, ?, ?)
    -- INSERT INTO Vehicle(year, make, model, price, color, VIN, mileage) VALUES(?, ?, ?, ?, ?, ?, ?)
    -- INSERT INTO Driver(first_name, last_name, DOB, license_ID, age, experience, violation_pts, violation_des, driver_key) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)
    -- INSERT INTO Company(name, policy_num, customer_bal, customer_key) VALUES(?, ?, ?, ?)
    -- INSERT INTO Discount(driver_key, student, military, GPA, disability, goodTimeDriving, marital_status) VALUES(?, ?, ?, ?, ?, ?, ?)
    -- INSERT INTO CompanyOwnsPolicy(policy_num, company_name, policy_rate, customer_key) VALUES(?, ?, ?, ?)
    -- INSERT INTO Vehicle_Driver_Has(driver_key, VIN, license_ID) VALUES(?, ?, ?)

-- Basic UPDATE modification statements allowing 
-- for non-static and static update of database during runtime, as
-- implemented in Java. 'valuen's and 'attributen's serve as place holders for 
    -- UPDATE Customer SET attribute1 = value1, attribute2 = value2, ... WHERE attribute1 = value1, attribute2 = value2, ...  
    -- UPDATE Login SET attribute1 = value1, attribute2 = value2, ... WHERE attribute1 = value1, attribute2 = value2, ...  
    -- UPDATE Payment SET attribute1 = value1, attribute2 = value2, ... WHERE attribute1 = value1, attribute2 = value2, ...  
    -- UPDATE Policy SET attribute1 = value1, attribute2 = value2, ... WHERE attribute1 = value1, attribute2 = value2, ...  
    -- UPDATE Vehicle SET attribute1 = value1, attribute2 = value2, ... WHERE attribute1 = value1, attribute2 = value2, ...  
    -- UPDATE Driver SET attribute1 = value1, attribute2 = value2, ... WHERE attribute1 = value1, attribute2 = value2, ...  
    -- UPDATE Company SET attribute1 = value1, attribute2 = value2, ... WHERE attribute1 = value1, attribute2 = value2, ...  
    -- UPDATE Discount SET attribute1 = value1, attribute2 = value2, ... WHERE attribute1 = value1, attribute2 = value2, ...  
    -- UPDATE CompanyOwnsPolicy SET attribute1 = value1, attribute2 = value2, ... WHERE attribute1 = value1, attribute2 = value2, ...  
    -- UPDATE Vehicle_Driver_Has SET attribute1 = value1, attribute2 = value2, ... WHERE attribute1 = value1, attribute2 = value2, ...  


-- Basic DELETE modification statements allowing 
-- for non-static and static deletion of database 
-- tuples during runtime, as implemented in Java.
    -- DELETE FROM Customer WHERE attribute1 = value1, attribute2 = value2, ... ;
    -- DELETE FROM Login WHERE attribute1 = value1, attribute2 = value2, ... ;
    -- DELETE FROM Payment WHERE attribute1 = value1, attribute2 = value2, ... ;
    -- DELETE FROM Policy WHERE attribute1 = value1, attribute2 = value2, ... ;
    -- DELETE FROM Vehicle WHERE attribute1 = value1, attribute2 = value2, ... ;
    -- DELETE FROM Driver WHERE attribute1 = value1, attribute2 = value2, ... ;
    -- DELETE FROM Company WHERE attribute1 = value1, attribute2 = value2, ... ;
    -- DELETE FROM Discount WHERE attribute1 = value1, attribute2 = value2, ... ;
    -- DELETE FROM CompanyOwnsPolicy WHERE attribute1 = value1, attribute2 = value2, ... ;
    -- DELETE FROM Vehicle_Driver_Has WHERE attribute1 = value1, attribute2 = value2, ... ;

    
-- Use case 1: Create account and Login.
    --1a. System needs to verify username does not already exist. 
    -- Print out a set of usernames from data base and store in
    -- array to perform linear search on it.
    SELECT username FROM Login;

    --1b. System creates a new username to insert into the Login table,
    -- which is then stored in the data base using the following query.
    -- Dummy Scenario: 
        --username: jsmith123
        --password: earth123
        --customer_key will be determined as follows
            --1b.1 Run a query to find the total count of customers, and store that value in an integer.age
                int count stores result query value -> SELECT COUNT(customer_key) FROM Login;
            --1b.2 Pass count as a paramter for insert SQL function    
                INSERT INTO Login(username, password, customer_key) VALUES(jsmith123, earth123, count + 1)


-- Use case 2: Select from insurers list.
    --2a. Customers will be presented with a list of names of
    -- available insurance companies offering policy contracts.
    -- This selection will be stored into a string variable 
    -- called choice, in our java program.
    String distinct company names from result query value -> SELECT DISTINCT name FROM Company;


-- Use case 3: View driver records
    --3a. Customers/Drivers will be able to view their records by the following SQL statement.
    -- Dummy Scenario:
    SELECT 
        Driver.driver_key, 
        violation_des, 
        violation_pts, 
        student, 
        military, 
        GPA, 
        disability, 
        experience, 
        marital_status
    FROM 
        Driver,
        Discount
    WHERE 
        Driver.driver_key = Discount.driver_key AND
        Driver.driver_key = 1001;

-- Use case 4: View car statistics history,
    --4a. Insurance companies that sign up with the 
    --Insurance App will be able to view general 
    --information about cars stored in the database, 
    --using the following queries.
        --4a.1 View general information about a car by model.
        SELECT 
            model, 
            make, 
            price
        FROM 
            Vehicle
        WHERE
            make = 'chevrolet'
        GROUP BY model
        ORDER BY make;

        --4a.2 View average price of cars insured on Insurance App by maker.
        SELECT
            make, 
            AVG(price) AS average_cost
        FROM 
            Vehicle
        GROUP BY make
        ORDER BY average_cost DESC;


        --4a.2 View ranking of car makes on Insurance App.
        SELECT make, COUNT(make)
        FROM Vehicle
        GROUP BY make
        ORDER BY COUNT(make) DESC;


-- Managing Policy
    -- View general policy information
    SELECT name, Company.policy_num, Policy.policy_rate, make, model
    FROM Company, Vehicle_Driver_Has, Vehicle, Policy
    WHERE
    Vehicle_Driver_Has.VIN = Vehicle.VIN AND
    Policy.policy_num = Company.policy_num AND
    Policy.insured_VIN = Vehicle.VIN;


-- Use case 6: Creating a policy
    --6a. Insurers will be able to create a 
    --policy from viewing several different information 
    --from drivers, their records and the car value.
    -- We will first begin by looking at the discounts 
    -- table and the drivers table to gather as much information about
    -- the driver as we can. Our goal is to check values such as 
    -- student, military, married, violation points,
    -- experience etc and create boolean variables to store those
    -- values in our java program. 
    --      boolean married \
    --      boolean exp ------> Values will be stored in the result queries.
    --      boolean student /
    -- Result Query 1: 
    SELECT student, military, GPA, marital_status, disability, goodTimeDriving
    FROM Discount;
    SELECT driver_key, age, experience, violation_pts FROM Driver;

    --We will need a function in Java to generate a random unique policy number.
        -- We will call this randomly generated value pNum
    --We will then need to know more information about the car that is to be insured, via the following query:
   -- which will also gather general information to prepare insert statement:
    SELECT 
        first_name, last_name, DOB, 
        Vehicle.VIN, model, make, 
        price, marital_status, military, 
        student, GPA, disability, goodTimeDriving, 
        age, experience, violation_pts
    FROM
        Driver,
        Vehicle_Driver_Has,
        Vehicle, 
        Discount
    WHERE   
        Driver.driver_key = Vehicle_Driver_Has.driver_key AND
        Vehicle.VIN = Vehicle_Driver_Has.VIN AND 
        Driver.license_id = Vehicle_Driver_Has.license_ID AND
        Discount.driver_key = Driver.driver_key;


    SELECT * FROM Vehicle ORDER BY price ASC;

    -- This query will store several local variables in Java that will have a series of 
    -- thresholds and constraints applied to it with certain conditions that if met/not met
    -- will all have an impact on the policy rate.
    -- Insurer then inserts a contract tailored to the information gathered from the
    -- previous queries.
    -- Once the insurer has decided on a policy rate, the program will prompt the user to accept or delcine 
    -- the insert statement into the policy table through an example:
    -- Lets say John is a single student, in his 30s, single and with a good driving record and GPA, looking to insure his 2020 Honda Civic
    -- The insurer decides that after taking these factors into account, the monthly rate will be 120 dollars.
    INSERT INTO Policy(insured_fname, insured_lname, insured_DOB, insured_VIN, policy_num, policy_rate) VALUES(John, Smith, 12/15/1986, 82j4383h4234 , 2384 JDHJSD, 120);


    


--This addresses the login case with two strings 
--from userInput for username & password
SELECT username, password FROM Login
WHERE username = 'kristoffer.moore@gmail.com'

AND password = 'iscf9te';

--This query addresses the ability to add a driver to a car
--given 
INSERT INTO Vehicle_Driver_Has(driver_key, vin, licence)
VALUES (1969, '19xfb2f81fe252000', 'ncct0jmg');

--This query presents you with the ability to make a payment
--and adjust the customer balance 
INSERT INTO Payment(payment_ID, method, customer_key, payment_amt, transaction_fee, date);
VALUES (156067761, 'debit card', 865,84.46,0.025,'11/5/17');


SELECT * FROM Vehicle ORDER BY price ASC;


UPDATE Customer SET balance = balance -84.46 WHERE customer_key = 865;

--This query allows us to view the average experience and violation of drivers
--and their possible correlation to policy pricing based on the same
SELECT AVG(experience) , violation_des FROM Driver
    GROUP BY violation_des;


--This query allows us to see the average pricing of policy rates for 
--Drivers of violation description/points 
SELECT violation_des, AVE(policy_rate) FROM ((Customer
    INNER JOIN Driver ON Customer.first_name = Driver.first_name
    AND Customer.last_name = Driver.last_name
    AND Customer.DOB = Driver.DOB)
    INNER JOIN Policy ON Customer.policy_num = Policy.policy_num)

    GROUP BY violation_des ORDER BY violation_pts DESC;


--This query allows us to see which Company has the most customers
SELECT name, COUNT(customer_key) AS custCount FROM Company 
GROUP BY name;


--This query allows us to see the customers that haven't done any payments;
SELECT balance FROM Customer 
WHERE customer_key NOT IN(
    SELECT customer_key FROM Payment);


--To be used as max price for indulging in expensive autos
SELECT * FROM VEHICLES WHERE price IN (
    SELECT MAX(price) FROM VEHICLES)


--To be used as min price for indulging in expensive autos
SELECT * FROM VEHICLES WHERE price IN (
    SELECT MIN(price) FROM VEHICLES)
--
--




SELECT COUNT(username), customer_key FROM Login WHERE username = 'edgarsoccer14@hotmail.com' AND password = 'samasama34';

SELECT * FROM Vehicle GROUP BY make;
SELECT * FROM Vehicle;


SELECT D.first_name, D.last_name, D.DOB, D.license_ID, D.age, 
D.experience, D.violation_pts, D.violation_des, D.driver_key, 
P.insured_VIN , C.customer_key
FROM Driver D, Vehicle_Driver_Has VDH, Policy P, Vehicle V, Customer C
WHERE
D.driver_key = VDH.driver_key 
AND VDH.VIN = V.VIN
AND V.VIN = P.insured_VIN
AND C.policy_num = P.policy_num
AND P.policy_num = 1;