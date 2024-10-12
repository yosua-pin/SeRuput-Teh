package util;
import java.sql.*;

public class Connect {

    private final String USERNAME = "root";
    private final String PASSWORD = "";
    private final String DATABASE = "seruput_teh";
    private final String HOST = "localhost:3306";
    private final String CONNECTION = String.format("jdbc:mysql://%s/%s", HOST, DATABASE);

    private Connection con;
    private Statement st;
    private static Connect connect;
    public ResultSet rs;
    public ResultSetMetaData rsmd;
    public static Connect getInstance() {
        if (connect == null) return new Connect();
        return connect;
    }

    private Connect() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(CONNECTION, USERNAME, PASSWORD);
            st = con.createStatement();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        return con; 
    }
    
    public static Connect getConnect() {
        return connect = (connect == null) ? new Connect() : connect;
    }
    
    public ResultSet execQuery(String query) {
    	try {
			rs=  st.executeQuery(query);
			rsmd = rs.getMetaData();
		} catch (Exception e) {
			
		}
    	return rs;
    }
    public ResultSet preparedStatement(String name) {
    	
    	return null;
    }
    public boolean execUpdate(String query) {
    	boolean success;
    	try {
			st.executeUpdate(query);
//			System.out.println(query+"\n^ run successfuly");
			success=true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
//			System.out.println(query);
//			System.out.println("^execUpdate failed");
			success=false;
		}
    	return success;
    }
    public void insertData(String userID,String username, String password,String role, String phone_num, String address, String gender) {
        String query = "INSERT INTO user(userID, username, password, role, address, phone_num, gender) VALUES (?,?, ?, ?, ?, ?,?)";

        try {
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, userID);
            ps.setString(2, username);
            ps.setString(3, password);
            ps.setString(4, role);
            ps.setString(5, address);
            ps.setString(6, phone_num);
            ps.setString(7, gender);

            ps.executeUpdate();

//            System.out.println("Data inserted successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public String createID() {
    	  String query = "SELECT COUNT(userID) FROM user WHERE userID LIKE 'CU%'";
    	  try {
//    	   System.out.println(query);
    	   ResultSet countID = st.executeQuery(query);
    	   
    	         if (countID.next()) {
    	             int totalRow = countID.getInt(1) + 4;
    	             String userID = "CU" + String.format("%03d", totalRow);
    	             return userID;
    	         } else {
    	             // Handle the case when there are no rows in the result set
    	             return null;
    	         }
    	    
    	  } catch (SQLException e) {
    	   // TODO Auto-generated catch block
    	   e.printStackTrace();
    	   return null;
    	  }
    	 
    	 }
    public boolean isUsernameUnique(String username) {
        String query = "SELECT * FROM user WHERE username = ?";
        try {
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, username);
            ResultSet resultSet = ps.executeQuery();
            return !resultSet.next(); // Return true if username is unique
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Handle the exception appropriately
        }
    }
    public boolean validateLogin(String username, String password) {
        String query = "SELECT * FROM user WHERE username = ? AND password = ?";
        try {
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet resultSet = ps.executeQuery();
            return resultSet.next(); // Return true if username and password match
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Handle the exception appropriately
        }
    }
    
    public boolean isCustomer(String username) {
        String query = "SELECT COUNT(*) FROM user WHERE username = ? AND userID LIKE 'CU%'";
        try {
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int count = rs.getInt(1);
                return count > 0;
            } else {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}