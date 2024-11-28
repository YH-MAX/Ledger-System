package ledger;

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
                    if (creditconnectiondatabase(credit, creditdescription, USERID)){
                        updatetoAccount_Balancecredit(credit, USERID);
                        System.out.println("Credit Successfully Recorded!!!");

                        System.out.println("Saving: 0.00");
                        System.out.println("Loan: 0.00");
                    }
                case 3:
                    System.out.println("== History ==");
                    displaytransactionshistory(USERID);
                    default:
                    System.out.println("Wrong choice. Try again!");

            }

        }

    public static boolean connectiontodatabase(double amountdebit, String balancedescription, int USERID) {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/ledger?useUnicode=true&characterEncoding=utf8&useSSL=false", "root", "123456");

            String insert = "INSERT INTO Transactions(amount, description, transaction_type) VALUES (?, ?, 'debit')";


            pstmt = conn.prepareStatement(insert);
            pstmt.setDouble(1, amountdebit);
            pstmt.setString(2, balancedescription);
            int countInsert = pstmt.executeUpdate();

            if (countInsert > 0) {
                return true;
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
   public static void updatetoAccount_Balance(double amountdebit, int USERID) {
       Connection conn = null;
       PreparedStatement pstmt = null;
       try {
           Class.forName("com.mysql.cj.jdbc.Driver");
           conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/ledger?useUnicode=true&characterEncoding=utf8&useSSL=false", "root", "123456");
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
            String insert = "INSERT INTO Transactions(amount, description, transaction_type) VALUES (?, ?, 'debit')";


            pstmt = conn.prepareStatement(insert);
            pstmt.setDouble(1, credit);
            pstmt.setString(2, creditdescription);
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
}

}


