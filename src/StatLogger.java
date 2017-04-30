import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.*;

public class StatLogger {
	public static void log(int gameid, String playerMove){
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager
					.getConnection(
							"jdbc:mysql://ec2-34-195-151-200.compute-1.amazonaws.com:3306/landlord",
							"landlord", "admin");
			String query = "INSERT INTO game_stat (gameid,move) VALUES(?,?)";
			PreparedStatement stmt = con.prepareStatement(query);
			stmt.setInt(1,gameid);
			stmt.setString(2,playerMove);
			stmt.executeUpdate(); 
			stmt.close();
			con.close();
		} catch (Exception e) {
			System.out.println(e);
		}
		
		
	}
}
