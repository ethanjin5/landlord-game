import java.sql.*;

public class dbCon {
	private Connection con;

	public dbCon() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			this.con = DriverManager.getConnection(
					"jdbc:mysql://ec2-34-195-151-200.compute-1.amazonaws.com:3306/landlord", "landlord", "admin");
		} catch (Exception e) {
			System.out.println(e);
		}

	}
	
	public Connection getConnection(){
		return this.con;
	}
	
	public void closeConnection() throws SQLException{
		this.con.close();
	}

}
