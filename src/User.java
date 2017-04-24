import java.sql.*;
import java.util.ArrayList;

public class User {
	private int userid;
	private String username;
	private String email;
	private String phone;
	private String myMove;
	private ArrayList myCards;
	private int myIndex;
	private int money;
	

	//constructor
	public User(int userid, String username, String email, String phone, int money) {
		this.userid = userid;
		this.username = username;
		this.phone = phone;
		this.email = email;
		this.money = money;
	}
	
	//getters for user attributes
	public int getUserid(){
		return this.userid;
	}
	public String getUsername(){
		return this.username;
	}
	public String getEmail(){
		return this.email;
	}
	public String getMyMove() {
		return myMove;
	}
	public int getMyIndex(){
		return myIndex;
	}
	public void setMyIndex(int myIndex){
		this.myIndex = myIndex;
	}

	public void setMyMove(String myMove) {
		this.myMove = myMove;
	}
	public int getNextUserIndex(){
		if (myIndex ==2){
			return 0;
		}
		return myIndex+=1;
	}

	public ArrayList getMyCards() {
		return myCards;
	}

	public void setMyCards(ArrayList myCards) {
		this.myCards = myCards;
	}
	public int getMoney(){
		return this.money;
	}
	public void setMoney(int money){
		this.money = money;
	}

	//add user into database
	public static boolean register(String username, String password1,
			String password2, String email, String phone) {
		//encrypt password using bcrypt
		String hashed = BCrypt.hashpw(password1, BCrypt.gensalt());
		// insert user into database
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager
					.getConnection(
							"jdbc:mysql://ec2-34-195-151-200.compute-1.amazonaws.com:3306/landlord",
							"landlord", "admin");
			String query = "INSERT INTO users (username,password,email,phone) VALUES(?,?,?,?)";
			PreparedStatement stmt = con.prepareStatement(query);
			stmt.setString(1,username);
			stmt.setString(2,hashed);
			stmt.setString(3,email);
			stmt.setString(4,phone);
			int count = stmt.executeUpdate();
			if (count>0){ //succesfully added user into database
				con.close();
				return true;
			}else{
				con.close();
				return false;
			}
		} catch (Exception e) {
			return false;
		}
	}
	
	//check if username already exist in database user list
	public static boolean dupUser(String username){ 
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
			if (res.next()){ //found same username in database
				con.close();
				return true;
			}else{ //username is not used
				con.close();
				return false;
			}
		} catch (Exception e) {
			return false;
		}
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
			if (res.next()){ //user found, get user's password hash
				hashed = res.getString("password");
			}else{//user not found, login fail
				con.close();
				return false;
			}
			con.close();
		} catch (Exception e) {
			return false;
		}
		//check if password and saved hash match using bcrypt
		if (!hashed.equals("") && !password.equals("") && BCrypt.checkpw(password, hashed)){
			return true;//password match, login success
		}
		return false;
	}

	public static int getUser(String username) {
		// get user fromm db
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager
					.getConnection(
							"jdbc:mysql://ec2-34-195-151-200.compute-1.amazonaws.com:3306/landlord",
							"landlord", "admin");
			String query = "select id,username from users where username = ?";
			PreparedStatement stmt = con.prepareStatement(query);
			stmt.setString(1,username);
			ResultSet res = stmt.executeQuery();
			if (res.next()){ //user found
				int userid = res.getInt("id");
				con.close();
				return userid;
			}else{ //user not found - return 0
				con.close();
				return 0;
			}
			
		} catch (Exception e) { //error - return 0
			return 0;
		}
		
	}
	
	public static User getUser(int userid) {
		// get user fromm db by id
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager
					.getConnection(
							"jdbc:mysql://ec2-34-195-151-200.compute-1.amazonaws.com:3306/landlord",
							"landlord", "admin");
			String query = "select * from users where id = ?";
			PreparedStatement stmt = con.prepareStatement(query);
			stmt.setInt(1,userid);
			ResultSet res = stmt.executeQuery();
			if (res.next()){ //user found
				int id = res.getInt("id");
				String username = res.getString("username");
				String email = res.getString("email");
				String phone = res.getString("phone");
				int money = res.getInt("money");
				User user = new User(id,username,email,phone,money);
				con.close();
				return user;
			}else{ //user not found - return 0
				con.close();
				return null;
			}
			
		} catch (Exception e) { //error - return 0
			return null;
		}
		
	}

}
