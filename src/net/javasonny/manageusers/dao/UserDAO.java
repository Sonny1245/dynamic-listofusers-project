package net.javasonny.manageusers.dao;

import java.sql.*;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import net.javasonny.manageusers.model.User;

public class UserDAO {
	
	String jdbcURL = "jdbc:mysql://localhost:3306/demo?"
	+ "useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
	String jdbcUsername = "hbstudent";
	String jdbcPassword = "hbstudent";
	
	private static final String INSERT_USERS_SQL = "INSERT INTO users"
			             + "(name, email, country)VALUES(?,?,?)";
	
	private static final String SELECT_USER_BY_ID = "select id, name, email, country"
						 + "from users where id = ? ";
	
	private static final String SELECT_ALL_USERS = "SELECT * FROM users";
	
	private static final String DELETE_USERS_SQL = "DELETE FROM users WHERE id = ?";
	
	private static final String UPDATE_USERS_SQL = "UPDATE users "
			+ "SET name = ?, email = ?, country = ? WHERE id = ? ";
	
	protected Connection getConnection() {
		
		Connection connection = null;
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);		 
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return connection;
		
	}
	
	//InsertUser()
	public void insertUser(User user) throws SQLException {	
		try {		
			Connection connection = getConnection();
			PreparedStatement preparedStatement =  connection.prepareStatement(INSERT_USERS_SQL);
			preparedStatement.setString(1, user.getName());
			preparedStatement.setString(2, user.getEmail());
			preparedStatement.setString(3, user.getCountry());
			preparedStatement.executeUpdate();
					
		} catch(Exception e) {
			e.printStackTrace();
		}
			
	}
	
	//UpdateUser()
	public boolean updateUser(User user) throws SQLException {
		
		boolean rowUpdated = false;	
		try {
			Connection connection = getConnection();
			PreparedStatement statement = connection.prepareStatement(UPDATE_USERS_SQL);
			statement.setString(1, user.getName());
			statement.setString(2, user.getEmail());
			statement.setString(3, user.getCountry());
			statement.setInt(4, user.getId());
			
			rowUpdated = statement.executeUpdate() > 0;
					
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return rowUpdated;		
	}
	
	//Select the user by id:
	public User selectUser(int id) throws SQLException {
		
		User user = null;
		
		try {
			
			Connection connection = getConnection();
			PreparedStatement preparedStatement = connection.prepareStatement(SELECT_USER_BY_ID);
			preparedStatement.setInt(1, id);
			System.out.println(preparedStatement);
			ResultSet rs = preparedStatement.executeQuery();
			
			while(rs.next()) {
				
				String name = rs.getString("name");
				String email = rs.getString("email");
				String country = rs.getString("country");
				user = new User(id, name, email, country);					
			}
			
		}catch(Exception e) {
			e.printStackTrace();		
		}
			
		return user;		
	}
	
	//Select all users:
		public List<User> selectAllUsers() throws SQLException {
			
			List<User> users = new ArrayList<>();
			
			try {
				
				Connection connection = getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_USERS);
				System.out.println(preparedStatement);
				ResultSet rs = preparedStatement.executeQuery();
				
				while(rs.next()) {
					
					int id = rs.getInt("id");
					String name = rs.getString("name");
					String email = rs.getString("email");
					String country = rs.getString("country");
					users.add(new User(id, name, email, country));					
				}
				
			}catch(Exception e) {
				e.printStackTrace();		
			}
				
			return users;		
		}
		
		//Delete user by id:
		public boolean deleteUser(int id) throws SQLException{
			
			boolean rowDeleted = false;
			
			try {
				
				Connection connection = getConnection();
				PreparedStatement statement = connection.prepareStatement(DELETE_USERS_SQL);
				statement.setInt(1, id);
				
				rowDeleted = statement.executeUpdate() > 0;
				
				
			}catch(Exception e) {
				e.printStackTrace();
			}
			
			return rowDeleted;			
		}
		
		@SuppressWarnings("unused")
		private void printSQLException(SQLException ex) {
	        for (Throwable e: ex) {
	            if (e instanceof SQLException) {
	                e.printStackTrace(System.err);
	                System.err.println("SQLState: " + ((SQLException) e).getSQLState());
	                System.err.println("Error Code: " + ((SQLException) e).getErrorCode());
	                System.err.println("Message: " + e.getMessage());
	                Throwable t = ex.getCause();
	                while (t != null) {
	                    System.out.println("Cause: " + t);
	                    t = t.getCause();
	                }
	            }
	        }
	    }
	
}
