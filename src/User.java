import java.sql.*;

public class User {
	private int userid;
	private String username;
	private String gender;
	private String email;

	public User(int userid, String username, String gender, String email) {
		this.userid = userid;
		this.username = username;
		this.gender = gender;
		this.email = email;
	}
	public static void main(String [] args){
		
		User.login("asdf", "sdf");
	}

	public static boolean register(String username, String password1,
			String password2, String gender, String email) {
		// check if username duplicated
		// check if two passwords are same
		// encode password
		// insert user into database

		// if success
		return true;
		// else false
	}

	public static boolean login(String username, String password) {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager
					.getConnection(
							"jdbc:mysql://ec2-34-195-151-200.compute-1.amazonaws.com:3306/landlord",
							"landlord", "admin");
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("select * from users");
			while (rs.next())
				System.out.println(rs.getInt(1) + "  " + rs.getString(2) + "  "
						+ rs.getString(3));
			con.close();
		} catch (Exception e) {
			System.out.println(e);
		}
		// select password from database
		// if selected successfully
		// encode password parameter
		// check if password match
		// if match

		return true;
		// else
		// return false;
	}

	public static User getUser(String username) {
		// get user fromm db
		return new User(1, "username", "male", "user@email.com");
	}

}
