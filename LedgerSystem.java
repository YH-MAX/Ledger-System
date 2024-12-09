package ledger;

import java.util.Timer;
import java.util.TimerTask;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.Scanner;
import java.sql.*;



public class LedgerSystem {
    private static String email;
    private static String password;
    public static void main(String[] args){
        Scanner input = new Scanner(System.in);
        System.out.println("== Ledger System ==\n");
        displayfirtmanu();

    }
    public static void displayfirtmanu(){
        Scanner firstmanu = new Scanner(System.in);
        System.out.println("Login or Register: \n" +
                "1. Register\n" +
                "2. Login");
        int num = firstmanu.nextInt();

        switch(num){
            case 1:
                System.out.println("== Please fill in the form ==");
                System.out.println("name: ");
                String name = firstmanu.next();
                System.out.println("Email: ");
                String email = firstmanu.next();
                System.out.println("Password: ");
                String password = firstmanu.next();
                System.out.println("Register Successful!!!");

                int user_id = registerUser(name, email, password);

                if (user_id != 0) {//////
                    System.out.println("Register Successful!");
                    enternoexistintotransactionmenu(user_id);

                } else {
                    System.out.println("Failed try again.");
                }
                break;
            case 2:
                System.out.println("== Please enter your email and password ==");
                System.out.println("Email: ");
                String loginemail = firstmanu.next();
                System.out.println("Password: ");
                String loginpassword = firstmanu.next();

                user_id = checkbysqldatabase(loginemail, loginpassword);

                if (user_id != 0) {
                    System.out.println("Login Successful!!!");
                    enternoexistintotransactionmenu(user_id);
                }else {
                    System.out.println("WRONE");
                }
                break;

        }
    }
    public static int checkbysqldatabase(String loginemail, String loginpassword){

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try{
            //注册驱动
            Class.forName("com.mysql.cj.jdbc.Driver");
            //获取连接
            conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/ledger?useUnicode=true&characterEncoding=utf8&useSSL=false", "root", "123456");
            //获取预编译的数据库操作对象
            String selectdatabase = "SELECT * FROM Users WHERE email = ? AND password = ?";


            pstmt = conn.prepareStatement(selectdatabase);
            //给问号传递信息
            pstmt.setString(1, loginemail);
            pstmt.setString(2, loginpassword);

            //执行SQL语句

            rs = pstmt.executeQuery();
            if (rs.next()){
                System.out.println("your user_id is: "+ rs.getInt(1) + "Please remember!!!!");
                return rs.getInt(1);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return -1; // 注册失败返回 -1
    }


    public static int registerUser(String name, String email, String password) {

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            //注册驱动
            Class.forName("com.mysql.cj.jdbc.Driver");
            //获取连接
            conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/ledger?useUnicode=true&characterEncoding=utf8&useSSL=false", "root", "123456");
            //获取预编译的数据库操作对象
            String insert = "INSERT INTO Users(name, email, password) VALUES (?, ?, ?)";

            String selectuser_id = "SELECT user_id FROM Users WHERE email = ?";

            pstmt = conn.prepareStatement(insert);
            //给问号传递信息
            pstmt.setString(1, name);
            pstmt.setString(2, email);
            pstmt.setString(3, password);

            //执行SQL语句
            int count = pstmt.executeUpdate();
            if (count > 0) {
                pstmt = conn.prepareStatement(selectuser_id);
                pstmt.setString(1, email);
                rs = pstmt.executeQuery();
                if (rs.next()) {
                    System.out.println("Your user_id is: " + rs.getInt(1) + "Please remember!!!");
                    String insert2 = "INSERT INTO Account_Balance (user_id, balance) VALUES (?, 0.00)";
                    pstmt = conn.prepareStatement(insert2);
                    pstmt.setInt(1, rs.getInt(1));
                    int countinsert2 = pstmt.executeUpdate();
                    if (countinsert2 > 0) {
                        System.out.println("Your Account_Balance user_id is: " + rs.getInt(1) + "Please remember!!!");
                    }
                    return rs.getInt(1);
                }

            } else {
                System.out.println("Failed try again.");
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        return -1; // 注册失败返回 -1
    }



    public static void enternoexistintotransactionmenu(int user_id) {

        Scanner menuchoice = new Scanner(System.in);
        //新用户的界面
        int USERID = user_id;

        System.out.println("== Welcome,  ==");
        System.out.println("Balance: 0.00");
        System.out.println("Savings: 0.00");
        System.out.println("Loans: 0.00");
        System.out.println("\n\n");
        System.out.println("== Transaction ==");
        System.out.println("1.Debit");
        System.out.println("2.Credit");
        System.out.println("3.History");
        System.out.println("4.Savings");
        System.out.println("5.Credit Loan");
        System.out.println("6.Deposit Interest Predictor");
        System.out.println("7.Logout");

        System.out.println("Enter your choice: ");
        int choice = menuchoice.nextInt();

        switch (choice) {
            case 1:
                System.out.println("== Debit ==");
                System.out.print("Enter amount: ");
                double amountdebit = menuchoice.nextInt();
                System.out.println("Enter description: ");
                String balancedescription = menuchoice.next();

                if (connectiontodatabase(amountdebit, balancedescription, USERID)) {
                    System.out.println("Debit Successfully Record!!!");
                    updatetoAccount_Balance(amountdebit, USERID);
                    System.out.println("== Welcome, ==");

                    System.out.println("Saving: 0.00");
                    System.out.println("Loan: 0.00");
                } else {
                    System.out.println("Failed, please try it again!");
                }

                break;
            case 2:
                System.out.println("== Credit ==");
                System.out.println("Enter amount: ");
                double credit = menuchoice.nextInt();
                System.out.println("Enter description: ");
                String creditdescription = menuchoice.next();
                if (creditconnectiondatabase(credit, creditdescription, USERID)) {
                    updatetoAccount_Balancecredit(credit, USERID);
                    System.out.println("Credit Successfully Recorded!!!");

                    System.out.println("Saving: 0.00");
                    System.out.println("Loan: 0.00");
                }
                break;
            case 3:
                System.out.println("== History ==");
                displaytransactionshistory(USERID);
                break;
            case 4:

                System.out.println("== Savings ==");
                System.out.println("Are you sure you want to activate it? (Y/N): ");
                menuchoice.nextLine();
                String activate = menuchoice.nextLine();

                if (activate.equalsIgnoreCase("Y")){
                    System.out.println("Please enter the percentage you wish to deduct from the next debit: ");
                    double savingsPercentage = menuchoice.nextDouble();
                    insertintosavings(USERID, savingsPercentage);//这里在savings里插入了percentage
                    System.out.println("\n\\nSavings Settings added successfully!!!");
                    
                } else {
                    System.out.println("Savings activation cancelled.");
                }
                break;
            case 5:
                System.out.print("Enter a specified principal amount:RM");
                double pAmount = menuchoice.nextDouble();
                System.out.print("Enter interest rate (in %): ");
                double rate = menuchoice.nextDouble();
                System.out.print("Enter repayment period (in month):");
                int period = menuchoice.nextInt();
                double totalRepayment = pAmount+(pAmount*(rate/100));
                double monthlyPayment = totalRepayment/period;

                for (int month=1; month<=period; month++) {
                    System.out.printf("Month "+month+": Pay %.2f\n", monthlyPayment);
                }
                inserintoLoans(USERID, pAmount, rate, period);
                break;
            case 6:
                Scanner sc = new Scanner(System.in);
                double rhbRate = 2.6;
                double maybankRate = 2.5;
                double hongLeongRate = 2.3;
                double allianceRate = 2.85;
                double amBankRate = 2.55;
                double standardCharteredRate = 2.65;

                System.out.println("== Deposit Interest Predictor ==");
                double accountBalance = selectfromdatabase(USERID);
                System.out.printf("Your current Account Balance: %.2f%n", accountBalance); // aligned with the variable account balance

                System.out.println("\nChoose your bank:");
                System.out.println("1. RHB (2.6%)");
                System.out.println("2. Maybank (2.5%)");
                System.out.println("3. Hong Leong (2.3%)");
                System.out.println("4. Alliance (2.85%)");
                System.out.println("5. AmBank (2.55)");
                System.out.println("6. Standard Chartered (2.65%)");

                System.out.print("Enter your choice (1-6): ");
                int bankChoice = sc.nextInt();
                double interestRate = 0;
                String selectedBank = "";
                switch (bankChoice){
                    case 1 -> {
                        interestRate = rhbRate;
                        selectedBank = "RHB";
                    }
                    case 2 -> {
                        interestRate = maybankRate;
                        selectedBank = "Maybank";
                    }
                    case 3 -> {
                        interestRate = hongLeongRate;
                        selectedBank = "Hong Leong";
                    }
                    case 4 -> {
                        interestRate = allianceRate;
                        selectedBank = "Alliance";
                    }
                    case 5 -> {
                        interestRate = amBankRate;
                        selectedBank = "AmBank";
                    }
                    case 6 -> {
                        interestRate = standardCharteredRate;
                        selectedBank = "Standard Chartered";
                    }
                    default -> {
                        System.out.println("Invalid choice. Please select a valid bank.");
                        return;
                    }
                }
                insertintoBank(interestRate, selectedBank);
                // to calculate monthly interest
                double monthlyInterest = (accountBalance * interestRate)/12/100;
                System.out.printf("Estimated Monthly Interest: %.2f%n", monthlyInterest);


                case 7:
                System.out.println("logout successfully!!!");
            default:
                System.out.println("Wrong choice. Try again!");
                break;

        }

    }


    public static boolean connectiontodatabase(double amountdebit, String balancedescription, int USERID) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/ledger?useUnicode=true&characterEncoding=utf8&useSSL=false", "root", "123456");
            if ("active".equalsIgnoreCase(confirmactivate(USERID))){
                double savingpercentage = searchpercentage(USERID);
                double amountsavings = amountdebit * (savingpercentage/100.00);
                double newdebit = amountdebit*(1 - savingpercentage/100.00);
                updatesaving_balance(USERID, amountsavings);
                updateaccount_balance(USERID, newdebit);

                amountdebit = amountdebit - amountsavings;
                String insert = "INSERT INTO Transactions(user_id, amount, description, transaction_type) VALUES (?, ?, ?, 'debit')";
                pstmt = conn.prepareStatement(insert);

                pstmt.setInt(1, USERID);
                pstmt.setDouble(2, amountdebit);
                pstmt.setString(3, balancedescription);
                int countinsert = pstmt.executeUpdate();
                if (countinsert > 0) {
                    System.out.println("Transaction Successfully Record!!!");
                }



            }



            else {
                String insert = "INSERT INTO Transactions(user_id, amount, description, transaction_type) VALUES (?, ?, ?, 'debit')";


                pstmt = conn.prepareStatement(insert);
                pstmt.setInt(1, USERID);
                pstmt.setDouble(2, amountdebit);
                pstmt.setString(3, balancedescription);
                int countInsert = pstmt.executeUpdate();

                if (countInsert > 0) {
                    return true;

                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
    public static void updatesaving_balance(int USERID, double amountsavings){
        Connection conn = null;
        PreparedStatement pstmt = null;

        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/ledger?useUnicode=true&characterEncoding=utf8&useSSL=false", "root", "123456");

            String updatesavingbalance = "UPDATE saving_balance SET savings_amount = savings_amount + ? WHERE user_id = ?";
            pstmt = conn.prepareStatement(updatesavingbalance);
            pstmt.setDouble(1, amountsavings);
            pstmt.setInt(2, USERID);


            int row = pstmt.executeUpdate();
            if (row > 0) {
                System.out.println("Updates savings_amount balance successfully!!!");
                String select = "SELECT savings_amount FROM saving_balance WHERE user_id = ?";
                pstmt = conn.prepareStatement(select);
                pstmt.setInt(1, USERID);

                ResultSet rs = pstmt.executeQuery();
                if (rs.next()){
                    System.out.println("Your savings: " + rs.getString("savings_amount"));}
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    public static void updateaccount_balance(int USERID, double amountsavings){
        Connection conn = null;
        PreparedStatement pstmt = null;

        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/ledger?useUnicode=true&characterEncoding=utf8&useSSL=false", "root", "123456");

            String updateaccountbalance = "UPDATE Account_Balance SET balance = balance + ? WHERE user_id = ?";
            pstmt = conn.prepareStatement(updateaccountbalance);

            pstmt.setDouble(1, amountsavings);
            pstmt.setInt(2, USERID);
            int row = pstmt.executeUpdate();
            if (row > 0) {
                System.out.println("Updates account balance successfully!!!" + "The savings are successfully recorded");
                String select = "SELECT balance FROM account_balance WHERE user_id = ?";
                pstmt = conn.prepareStatement(select);
                pstmt.setInt(1, USERID);

                ResultSet rs = pstmt.executeQuery();
                if (rs.next()){
                    System.out.println("Current account balance is" + rs.getString("balance"));

                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static String confirmactivate(int USERID) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/ledger?useUnicode=true&characterEncoding=utf8&useSSL=false", "root", "123456");

            String confirmactivate = "SELECT status FROM savings WHERE user_id = ?";
            pstmt = conn.prepareStatement(confirmactivate);
            pstmt.setInt(1, USERID);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getString("status");

            }


        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
    public static double searchpercentage(int USERID) {

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/ledger?useUnicode=true&characterEncoding=utf8&useSSL=false", "root", "123456");
            String searchpercent = "SELECT percentage FROM savings WHERE user_id = ?";
            pstmt = conn.prepareStatement(searchpercent);
            pstmt.setInt(1, USERID);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getDouble("percentage");
            }



        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        return 0;
    }


    public static void updatetoAccount_Balance(double amountdebit, int USERID) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/ledger?useUnicode=true&characterEncoding=utf8&useSSL=false", "root", "123456");
            if ("active".equalsIgnoreCase(confirmactivate(USERID))){
                return;
            }
            String updatebalance = "UPDATE Account_Balance SET balance = ? WHERE user_id = ?";
            pstmt = conn.prepareStatement(updatebalance);
            pstmt.setDouble(1, amountdebit);
            pstmt.setDouble(2, USERID);
            int countupdate = pstmt.executeUpdate();
            if (countupdate > 0) {
                System.out.println("Balance: " + displaybysql(USERID));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }


    }
    public static boolean creditconnectiondatabase(double credit, String creditdescription, int USERID){
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;


        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            ////获取连接
            conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/ledger?useUnicode=true&characterEncoding=utf8&useSSL=false", "root", "123456");
            //
            String insert = "INSERT INTO Transactions(user_id, amount, description, transaction_type) VALUES (?, ?, ?, 'credit')";


            pstmt = conn.prepareStatement(insert);
            pstmt.setInt(1, USERID);
            pstmt.setDouble(2, credit);
            pstmt.setString(3, creditdescription);
            int countinsert = pstmt.executeUpdate();
            if (countinsert > 0){


                return true;

            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return false;

    }
    public static void updatetoAccount_Balancecredit(double credit, int USERID){
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/ledger?useUnicode=true&characterEncoding=utf8&useSSL=false", "root", "123456");
            String updatebalancecredit = "UPDATE Account_Balance SET balance = balance - ? WHERE user_id = ?";
            pstmt = conn.prepareStatement(updatebalancecredit);
            pstmt.setDouble(1, credit);
            pstmt.setDouble(2, USERID);
            int countupdatecredit = pstmt.executeUpdate();
            if (countupdatecredit > 0){
                System.out.println("Balance: " + displaybysql(USERID));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }


    public static double displaybysql(int USERID){

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/ledger?useUnicode=true&characterEncoding=utf8&useSSL=false", "root", "123456");
            String select = "SELECT balance FROM Account_Balance WHERE user_id = ?";
            pstmt = conn.prepareStatement(select);

            pstmt.setDouble(1, USERID);
            rs = pstmt.executeQuery();
            if (rs.next()){
                return rs.getDouble("balance");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return 0.0;
    }
    public static void displaytransactionshistory(int USERID){

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/ledger?useUnicode=true&characterEncoding=utf8&useSSL=false", "root", "123456");

            String selectinitialbalance = "SELECT  balance FROM Account_Balance WHERE user_id = ?";


            pstmt = conn.prepareStatement(selectinitialbalance);
            pstmt.setInt(1, USERID);
            rs = pstmt.executeQuery();
            if (rs.next()){

                double accountbalance = rs.getDouble("balance");
                String select = "SELECT transaction_type, amount, description, date FROM Transactions WHERE user_id = ?";
                pstmt = conn.prepareStatement(select);
                pstmt.setInt(1, USERID);
                rs = pstmt.executeQuery();

                System.out.printf("%-20s %-20s %-20s %-20s %-20s", "Date", "Description", "Debit", "Credit","Balance");
                System.out.println("\n");
                String sum = "SELECT SUM(amount) AS amount FROM transactions WHERE user_id = ?";
                pstmt = conn.prepareStatement(sum);
                pstmt.setInt(1, USERID);
                ResultSet rs2 = pstmt.executeQuery();

                //double sumdebitcredit = rs2.getDouble("amount");




                while (rs.next()){
                    //double sumdebitcredit = rs2.getDouble("amount");
                    String date = rs.getString("date");
                    String description = rs.getString("description");
                    String transaction_type = rs.getString("transaction_type");
                    double amount = rs.getDouble("amount");

                    if ("debit".equalsIgnoreCase(transaction_type)&&rs2.next()){
                        double sumdebitcredit = rs2.getDouble("amount");
                        double amountdebit =  amount;
                        double amountcredit = 0.00;

                        accountbalance = accountbalance + sumdebitcredit - amountdebit;
                        System.out.printf("%-20s %-20s %-20f %-20f %-20f", date, description, amountdebit, amountcredit, accountbalance);
                    }
                    else if ("credit".equalsIgnoreCase(transaction_type)){
                        double amountcredit = rs.getDouble("amount");
                        double amountdebit = 0.00;
                        accountbalance = accountbalance - amountcredit;
                        System.out.println("\n");
                        System.out.printf("%-20s %-20s %-20f %-20f %-20f", date, description, amountdebit, amountcredit, accountbalance);}
                }

            }

            else{
                System.out.println("No transactions found");
            }


        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }


    }

    public static void  insertintosavings(int USERID, double savingspercentage){
        Connection conn = null;
        PreparedStatement pstmt = null;
        int user_id = USERID;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/ledger?useUnicode=true&characterEncoding=utf8&useSSL=false", "root", "123456");

            String insert = "INSERT INTO Savings(user_id, status, percentage) VALUES(?,'activate',?)";
            pstmt = conn.prepareStatement(insert);

            pstmt.setInt(1, user_id);
            pstmt.setDouble(2, savingspercentage);

            int countinsert = pstmt.executeUpdate();
            if (countinsert > 0){
                resettransactions(USERID, savingspercentage);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static void resettransactions(int USERID, double savingspercentage){
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/ledger?useUnicode=true&characterEncoding=utf8&useSSL=false", "root", "123456");
            String sumselecdebit = "SELECT SUM(amount) AS amount FROM Transactions WHERE user_id = ? AND transaction_type = 'debit'";
            pstmt = conn.prepareStatement(sumselecdebit);

            pstmt.setInt(1, USERID);
            rs = pstmt.executeQuery();
            double resetdebit = 0;
            if (rs.next()){
                double sumdebit = rs.getDouble("amount");
                resetdebit = sumdebit*(1-savingspercentage/100.00);
                String updatetransactions = "UPDATE Transactions SET amount = ? WHERE transaction_type = 'debit' AND user_id = ? ";
                pstmt = conn.prepareStatement(updatetransactions);
                pstmt.setDouble(1, resetdebit);
                pstmt.setInt(2, USERID);
                int countupdate = pstmt.executeUpdate();
                if (countupdate > 0){
                    insertintosaving_balance(USERID, resetdebit);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    public static void insertintosaving_balance(int USERID, double resetdebit){
        Connection conn = null;
        PreparedStatement pstmt = null;
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/ledger?useUnicode=true&characterEncoding=utf8&useSSL=false", "root", "123456");
            String insert = "INSERT INTO saving_balance (user_id, savings_amount) VALUES(?,?)";
            pstmt = conn.prepareStatement(insert);

            pstmt.setInt(1, USERID);
            pstmt.setDouble(2, resetdebit);
            int countrows = pstmt.executeUpdate();
            if (countrows > 0){
                resetaccount_balance(USERID, resetdebit);
            }


        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    public static void resetaccount_balance(int USERID, double resetdebit){

        Connection conn = null;
        PreparedStatement pstmt = null;
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/ledger?useUnicode=true&characterEncoding=utf8&useSSL=false", "root", "123456");

            String resetaccountbalance = "UPDATE Account_Balance SET balance = balance - ? WHERE user_id = ?";
            pstmt = conn.prepareStatement(resetaccountbalance);

            pstmt.setDouble(1, resetdebit);
            pstmt.setInt(2, USERID);
            int countrow = pstmt.executeUpdate();
            if (countrow > 0){
                System.out.println("Do you want to have a review?(YES/NO)");
                Scanner sc = new Scanner(System.in);
                String yourchoice = sc.nextLine();
                if (yourchoice.equals("YES")){
                    displaythereview(USERID);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    public static void displaythereview(int USERID){
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/ledger?useUnicode=true&characterEncoding=utf8&useSSL=false", "root", "123456");

            String selectfromaccountbalance = "SELECT balance FROM Account_Balance WHERE user_id = ?";
            pstmt = conn.prepareStatement(selectfromaccountbalance);

            pstmt.setInt(1, USERID);
            ResultSet rs1 = pstmt.executeQuery();
            if (rs1.next()){
                System.out.println("Balance: " + rs1.getString("balance"));
                String selectfromsavingbalance = "SELECT savings_amount FROM Saving_Balance WHERE user_id = ?";
                pstmt = conn.prepareStatement(selectfromsavingbalance);
                pstmt.setInt(1, USERID);
                ResultSet rs2 = pstmt.executeQuery();
                if (rs2.next()){
                    System.out.println("Savings: " + rs2.getDouble("savings_amount"));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
public static void inserintoLoans(int USERID, double pAmount, double rate, int period) {

    Connection conn = null;
    PreparedStatement pstmt = null;
    try{
        Class.forName("com.mysql.cj.jdbc.Driver");
        conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/ledger?useUnicode=true&characterEncoding=utf8&useSSL=false", "root", "123456");

        String insert = "INSERT INTO LOANS (user_id, principal_amount, interest_rate, repayment_period, status) VALUES(?,?,?,?,'active')";
        pstmt = conn.prepareStatement(insert);
        pstmt.setInt(1, USERID);
        pstmt.setDouble(2, pAmount);
        pstmt.setDouble(3, rate);
        pstmt.setInt(4, period);
        int countrows = pstmt.executeUpdate();
        if (countrows > 0){
            System.out.println("Your loans have been recorded!!!");
        }




    } catch (SQLException e) {
        throw new RuntimeException(e);
    } catch (ClassNotFoundException e) {
        throw new RuntimeException(e);
    }
}



public static double selectfromdatabase(int USERID){
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
    try{
        Class.forName("com.mysql.cj.jdbc.Driver");
        conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/ledger?useUnicode=true&characterEncoding=utf8&useSSL=false", "root", "123456");

        String select = "SELECT balance FROM Account_Balance WHERE user_id = ?";
        pstmt = conn.prepareStatement(select);
        pstmt.setInt(1, USERID);
        rs = pstmt.executeQuery();
        if (rs.next()){
           return rs.getDouble("balance");
        }
        } catch (SQLException e) {
        throw new RuntimeException(e);
    } catch (ClassNotFoundException e) {
        throw new RuntimeException(e);
    }


    return 0;
    }
public static void insertintoBank(double interestRate, String selectedBank){
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/ledger?useUnicode=true&characterEncoding=utf8&useSSL=false", "root", "123456");

            String insert = "INSERT INTO BANK (bank_name, interest_rate) VALUES(?,?)";
            pstmt = conn.prepareStatement(insert);
            pstmt.setString(1, selectedBank);
            pstmt.setDouble(2, interestRate);
            int countrows = pstmt.executeUpdate();
            if (countrows > 0){
                System.out.println("Your bank has been recorded!!!");
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
}}

