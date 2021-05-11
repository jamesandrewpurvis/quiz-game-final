package sample;

import java.math.BigInteger;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

//this class contains management for our DB

public class DatabaseManager {
	//fields
	private String mIP = null;
	private int mPort = 0;
	private String mConnectionString = null;
	private Connection mConnection = null;
	private String mUsername = null;
	private String mPassword = null;

	//properties (getters/setters)
	public String getIP() {
		return mIP;
	}

	public int getPort() {
		return mPort;
	}

	public String getConnectionString() {
		return mConnectionString;
	}

	public String getUsername() {
		return mUsername;
	}

	public String getPassowrd() {
		return mPassword;
	}

	//constructor
	public DatabaseManager(String IP, int port, String username, String password) {
		//a little bit of input checking
		if (IP.isBlank() == false && username.isBlank() == false && password.isBlank() == false && port > 0) {
			this.mIP = IP;
			this.mPort = port;
			this.mUsername = username;
			this.mPassword = password;
		}

		//our connection string
		mConnectionString = "jdbc:mysql://" + mIP + ":" + mPort + "/game";

		//this method returns a connection object for us to use 
		mConnection = createDatabaseConnection();

	}

	private Connection createDatabaseConnection() {
		//defining our connection var
		Connection testConnection = null;

		//error handling
		try {
			//use the driveManager get connection method to grab a connection using our connection string.
			testConnection = DriverManager.getConnection(mConnectionString, mUsername, mPassword);
			System.out.println("Connected to mySQL[" + mConnectionString + "]");
			return testConnection; //return our connection and let the user know!
		} catch (SQLException e) //darn! an error occurred let the user know!
		{
			System.out.println(e.getMessage());
		}

		//return null, we couldn't get a connection
		System.out.println("Unable to connect to mySQL[" + mConnectionString + "]");
		return null;
	}

	//this method creates a databases on first bootup, the params are a string which is the query.
	public void createDatabase(String query) {
		//error handling
		try {
			//our statement variable, we use this and our connection to create a statement
			Statement mStatement = mConnection.createStatement();
			mStatement.executeUpdate(query);
			//lets execute our query

			//it worked! let the user know
			System.out.println("A new databased named the_world has been created!");
		}
		//darn a error!
		catch (SQLException e) {
			//print the error
			System.out.println(e.getMessage());
		}
	}

	//this method creates a table, it takes in a string which is the query.
	public void createTable(String query) {
		//error handling
		try {
			//use our connection and statement var to create a statement
			Statement mStatement = mConnection.createStatement();
			//execute our statement using our query.
			mStatement.executeUpdate(query);

			//it worked! let the user know!
			System.out.println("A new table named player_scores has been created!");
		} catch (SQLException e) //darn a error!
		{
			//print the error!
			System.out.println(e.getMessage());
		}
	}

	//this method allows us to select data from the DB, it returns a resultset
	public ResultSet SELECT(String query) {
		try {
			Statement mStatement = mConnection.createStatement();

			ResultSet mResult = mStatement.executeQuery(query);

			return mResult;

		} catch (SQLException ex) {
			System.out.println(ex.getMessage());
			return null;
		}
	}

	//this method lets us update a record, it takes in a query as a parameter
	public void UPDATE(String query) {
		try {
			Statement mStatement = mConnection.createStatement();

			mStatement.executeUpdate(query);


		} catch (SQLException ex) {
			System.out.println(ex.getMessage());
		}
	}

	//this method lets us insert into our database, it uses a query as well as a parameter.
	public void INSERT(String query) {
		try {
			Statement mStatement = mConnection.createStatement();

			mStatement.executeUpdate(query);

			System.out.println(query);


		} catch (SQLException ex) {
			System.out.println(ex.getMessage());
		}
	}

	//this method lets us delete from our DB
	public void DELETE(String query) {
		try {
			Statement mStatement = mConnection.createStatement();

			mStatement.executeUpdate(query);

			System.out.println(query);


		} catch (SQLException ex) {
			System.out.println(ex.getMessage());
		}
	}

}