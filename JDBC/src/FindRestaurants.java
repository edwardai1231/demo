
import java.io.IOException;
import java.sql.*;
import java.util.Scanner;

public class FindRestaurants {
	public static void main(String args[]) {
		String dbSys = null;
		Scanner in = null;
		String aptnum = null; 
		
		try {
			in = new Scanner(System.in);
			System.out
					.println("Please enter information to test connection to the database");
			dbSys = readEntry(in, "Using Oracle (o), MySql (m) or HSQLDB (h)? ");

		} catch (IOException e) {
			System.out.println("Problem with user input, please try again\n");
			System.exit(1);
		}

		String user = null;
		String password = null;
		String connStr = null;
		String yesNo;
		try {
			if (dbSys.equals("o")) {
				user = readEntry(in, "user: ");
				password = readEntry(in, "password: ");
				yesNo = readEntry(in,
						"use canned Oracle connection string (y/n): ");
				if (yesNo.equals("y")) {
					String host = readEntry(in, "host: ");
					String port = readEntry(in, "port (often 1521): ");
					String sid = readEntry(in, "sid (site id): ");
					aptnum = readEntry(in, "Enter apartment Listing Number: ");
					connStr = "jdbc:oracle:thin:@" + host + ":" + port + ":"
							+ sid;
				} else {
					connStr = readEntry(in, "connection string: ");
				}
			} else if (dbSys.equals("m")) {// MySQL--
				user = readEntry(in, "user: ");
				password = readEntry(in, "password: ");
				yesNo = readEntry(in,
						"use canned MySql connection string (y/n): ");
				if (yesNo.equals("y")) {
					String host = readEntry(in, "host: ");
					String port = readEntry(in, "port (often 3306): ");
					String db = user + "db";
					connStr = "jdbc:mysql://" + host + ":" + port + "/" + db;
					aptnum = readEntry(in, "Enter apartment Listing Number: ");
				} else {
					connStr = readEntry(in, "connection string: ");
				}
			} else if (dbSys.equals("h")) { // HSQLDB (Hypersonic) db
				yesNo = readEntry(in,
						"use canned HSQLDB connection string (y/n): ");
				if (yesNo.equals("y")) {
					String db = readEntry(in, "db or <CR>: ");
					aptnum = readEntry(in, "Enter apartment Listing Number: ");
					connStr = "jdbc:hsqldb:hsql://localhost/" + db;
				} else {
					connStr = readEntry(in, "connection string: ");
				}
				user = "sa";
				password = "";
			} else {
				user = readEntry(in, "user: ");
				password = readEntry(in, "password: ");
				connStr = readEntry(in, "connection string: ");
			}
		} catch (IOException e) {
			System.out.println("Problem with user input, please try again\n");
			System.exit(3);
		}
		System.out.println("using connection string: " + connStr);
		System.out.print("Connecting to the database...");
		System.out.flush();
		Connection conn = null;
		// Connect to the database
		// Use finally clause to close connection
		try {
			conn = DriverManager.getConnection(connStr, user, password);
			System.out.println("connected.");
			findrestuarant(conn, aptnum);
		} catch (SQLException e) {
			System.out.println("Problem with JDBC Connection\n");
			printSQLException(e);
			System.exit(4);
		} finally {
			// Close the connection, if it was obtained, no matter what happens
			// above or within called methods
			if (conn != null) {
				try {
					conn.close(); // this also closes the Statement and
									// ResultSet, if any
				} catch (SQLException e) {
					System.out
							.println("Problem with closing JDBC Connection\n");
					printSQLException(e);
					System.exit(5);
				}
			}
		}
	}
	
	static void findrestuarant(Connection conn, String aptnum) throws SQLException {
		Statement stmt = conn.createStatement();
		ResultSet rset = null;
		try {
			/*String q = "SELECT e.eid, e.salary FROM employees1 e" + " WHERE NOT EXISTS ("
					+ "(SELECT a.aid FROM aircraft1 a WHERE a.cruisingrange < 2000 and a.aid <> 16)" + " MINUS "
					+ "(SELECT c.aid FROM certified1 c WHERE c.eid = e.eid)" + ")";*/
			String q = "SELECT b.name, b.stars, b.review_count  " + 
					   "FROM yelp_db.apartments a, yelp_db.business b, yelp_db.category c " + 
					   "WHERE a.listing = '" + aptnum + "' "
					   		+ " AND 200 > (select sdo_geom.sdo_distance (" + 
					"         sdo_geometry (2001, 4326, null, sdo_elem_info_array(1, 1, 1)," + 
					"             sdo_ordinate_array(a.longitude, a.latitude))," + 
					"	      sdo_geometry (2001, 4326, null, sdo_elem_info_array(1, 1, 1)," + 
					"             sdo_ordinate_array(b.longitude, b.latitude)" + 
					"          ),  1,  'unit=M'" + 
					"       ) distance_m from dual) AND b.review_count > 9 " + 
					"         AND b.city = 'Las Vegas' AND b.state = 'NV' AND c.business_id = b.id " + 
					"         AND c.category = 'Restaurants'" ; 
					
			
			System.out.println(q);
			rset = stmt.executeQuery(q);
			while (rset.next()) {
			    System.out.println("Name: " + rset.getString(1));
			    System.out.println("Stars: " + rset.getString(2));
			    System.out.println("Review Count: " + rset.getString(3));
			    System.out.println();
			}
			System.out.println("Done");
		} finally { // Note: try without catch: let the caller handle
					// any exceptions of the "normal" db actions.
			stmt.close(); // clean up statement resources, incl. rset
		}
	}

	// print out all exceptions connected to e by nextException or getCause
	static void printSQLException(SQLException e) {
		// SQLExceptions can be delivered in lists (e.getNextException)
		// Each such exception can have a cause (e.getCause, from Throwable)
		while (e != null) {
			System.out.println("SQLException Message:" + e.getMessage());
			Throwable t = e.getCause();
			while (t != null) {
				System.out.println("SQLException Cause:" + t);
				t = t.getCause();
			}
			e = e.getNextException();
		}
	}

	// super-simple prompted input from user
	public static String readEntry(Scanner in, String prompt)
			throws IOException {
		System.out.print(prompt);
		return in.nextLine().trim();
	}
	

}
