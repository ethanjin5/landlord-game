import java.sql.*;

public class User {
	private int userid;
	private String username;
	private String email;
	private String phone;

	public User(int userid, String username, String email, String phone) {
		this.userid = userid;
		this.username = username;
		this.phone = phone;
		this.email = email;
	}
	
	public int getUserid(){
		return this.userid;
	}
	public String getUsername(){
		return this.username;
	}
	public String getEmail(){
		return this.email;
	}

	public static boolean register(String username, String password1,
			String password2, String email, String phone) {
		// check if username duplicated
		// check if two passwords are same
		// encode password
		String hashed = BCrypt.hashpw(password1, BCrypt.gensalt());
		// insert user into database

		// if success
		return true;
		// else false
	}

	public static boolean login(String username, String password) {
		String hashed="";
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager
					.getConnection(
							"jdbc:mysql://ec2-34-195-151-200.compute-1.amazonaws.com:3306/landlord",
							"landlord", "admin");
			String query = "select * from users where username = ?";
			PreparedStatement stmt = con.prepareStatement(query);
			stmt.setString(1,username);
			ResultSet res = stmt.executeQuery();
			if (res.next()){
				hashed = res.getString("password");
			}else{
				return false;
			}
			con.close();
		} catch (Exception e) {
			System.out.println(e);
		}
		if (!hashed.equals("") && !password.equals("") && BCrypt.checkpw(password, hashed)){
			return true;
		}
		return false;
	}

	public static User getUser(String username) {
		// get user fromm db
		return new User(1, "username", "user@email.com", "7987998989");
	}

}
