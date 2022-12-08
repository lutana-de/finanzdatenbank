package finanzdatenbank;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import org.hsqldb.Server;
import org.hsqldb.jdbc.jdbcDataSource;

public class BibDatabase {

	private static BibDatabase instance;
	
	private Server server;
	private jdbcDataSource dataSource;
	private Connection connection;
	
	public static void init() throws BibUserException {
		instance = new BibDatabase();
		instance.connect();
		instance.updateDatabase();
	}

	public static BibDatabase get() {
		return instance;
	}
	
	protected BibDatabase() {}
	
	public String getDbFolder() {
		return "data/";
	}
	
	public String getDbName() {
		return "data";
	}
	
	public String getDbFile() {
		return getDbFolder() + getDbName() + ".db";
	}
	
	protected final void connect() throws BibUserException {
		this.server = new Server();
		this.server.setDatabaseName(0, getDbName());
		this.server.setDatabasePath(0, getDbFile());
		this.server.start();

		this.dataSource = new jdbcDataSource();
		this.dataSource.setDatabase("jdbc:hsqldb:file:" + getDbFile());
		this.dataSource.setUser("sa");
		this.dataSource.setPassword("");
		try {
			this.connection = this.dataSource.getConnection();
		} catch (SQLException e) {
			throw new BibUserException(e);
		}

		try {
			Statement statement = this.connection.createStatement();
			statement.execute("SET IGNORECASE TRUE;");
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void shutdown() {
		if (this.connection == null) {
			return;
		}
		try {
			Statement statement = this.connection.createStatement();
			statement.execute("SHUTDOWN");
			statement.close();
			this.connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void export(File file) throws BibUserException {
		this.export(file.getAbsolutePath());
	}
	
	public void export(String file) throws BibUserException {
		shutdown();
		
		BibZip zip = new BibZip(getDbFolder());
		zip.saveZipFile(file);
		
		connect();
	}

	public boolean updateMonth(Integer id, int month, Integer value) {
		return this.updateField(id, "m" + month, value);
	}
	
	public boolean updateOther(Integer id, Integer value) {
		return this.updateField(id, "other", value);
	}

	public boolean updateField(Integer id, String column, Integer data) {
		if (id == null || id < 1) {
			return false;
		}
		try {
			PreparedStatement statement = this.connection.prepareStatement("UPDATE finanzen SET " + column + " = ? WHERE id = ?");
			statement.setInt(1, data);
			statement.setInt(2, id);
			statement.executeUpdate();
			statement.close();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean updateTitle(Integer id, String title) {
		try {
			PreparedStatement statement = this.connection.prepareStatement("UPDATE finanzen SET title = ? WHERE id = ?");
			statement.setString(1, title);
			statement.setInt(2, id);
			statement.executeUpdate();
			statement.close();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean deleteEntry(Integer id) {
		try {
			PreparedStatement statement = this.connection.prepareStatement("DELETE FROM finanzen WHERE id = ?");
			statement.setInt(1, id);
			statement.executeUpdate();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public List<BibEntry> readData(int year) {
		return this.readData(year, null);
	}

	public List<BibEntry> readData(int year, String query) {
		if (query == null) {
			query = "";
		}
		else {
			query = query.trim();
		}
	
		List<BibEntry> list = new ArrayList();
		try {
			String querySql = "";
			if (!query.isEmpty()) {
				querySql += " AND title LIKE  '%' || ? || '%'";
			}
			
			PreparedStatement statement = this.connection.prepareStatement(
				"SELECT * FROM finanzen WHERE year = ? " + querySql + " ORDER BY id ASC"
			);
			statement.setInt(1, year);
			if (!query.isEmpty()) {
				statement.setString(2,query);
			}
			ResultSet resultSet = statement.executeQuery();
			statement.close();
				
			while (resultSet.next()) {
				Integer[] months = new Integer[12];
				for(int i = 0; i < 12; i++) {
					months[i] = resultSet.getInt("m" + (i + 1));
					if (resultSet.wasNull()) { // Thanks Java!
						months[i] = null;
					}
				}
				Integer other = resultSet.getInt("other");
				if (resultSet.wasNull()) { // Thanks Java!
					other = null;
				}
				BibEntry be = new BibEntry(
						resultSet.getInt("id"),
						resultSet.getInt("year"),
						resultSet.getString("title"),
						months,
						other
				);
				list.add(be);
			}
			resultSet.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	public String getSetting(String key) {
		try {
			PreparedStatement statement = this.connection.prepareStatement(
				"SELECT value FROM settings WHERE title = ? LIMIT 1"
			);
			statement.setString(1, key);
			ResultSet rs = statement.executeQuery();
			statement.close();
			String value = null;
			if (rs.next()) {
				value = rs.getString("value");
			}
			return value;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public Integer getSettingAsInt(String key, Integer defaultValue) {
		String value = this.getSetting(key);
		try {
			return Integer.valueOf(value);
		} catch(Exception e) {
			return defaultValue;
		}
	}

	public boolean updateSetting(String key, String value) {
		try {
			String sql;
			if (getSetting(key) != null) {
				sql = "UPDATE settings SET value = ? WHERE title = ?";
			}
			else {
				sql = "INSERT INTO settings (value, title) VALUES (?,?)";
			}
			PreparedStatement statement = this.connection.prepareStatement(sql);
			statement.setString(1, value);
			statement.setString(2, key);
			statement.executeUpdate();
			statement.close();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public int insertData(int year, String title) {
		try {
			PreparedStatement statement = this.connection.prepareStatement(
				"INSERT INTO finanzen (year, title) VALUES (?, ?);"
			);
			statement.setInt(1, year);
			statement.setString(2, title);
			statement.executeUpdate();
			statement.close();
			
			Statement statement2 = this.connection.createStatement();
			ResultSet rs = statement2.executeQuery("call identity();");
			statement2.close();
			rs.next();
			return rs.getInt(1);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

	protected void createSettingsTable() {
		try {
			Statement statement = this.connection.createStatement();
			statement.execute("CREATE TABLE settings (title varchar(128) not null, value varchar(255) not null, CONSTRAINT titleindex UNIQUE (title))");
			statement.close();

			statement = this.connection.createStatement();
			statement.execute("INSERT INTO settings (title, value) VALUES ('font-size', '12');");
			statement.execute("INSERT INTO settings (title, value) VALUES ('db-version', '1');");
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	protected void createDataTable() {
		try {
			Statement statement = this.connection.createStatement();
			statement.execute("CREATE TABLE finanzen (id int GENERATED BY DEFAULT AS IDENTITY (START WITH 1, INCREMENT BY 1), year int not null, title varchar(255) not null, m1 int, m2 int, m3 int, m4 int, m5 int, m6 int, m7 int, m8 int, m9 int, m10 int, m11 int, m12 int, other int)");
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	protected void updateDatabase() {
		Integer dbVersion = this.getSettingAsInt("db-version", 0);
		if (dbVersion < 1) {
			this.createDataTable();
			this.createSettingsTable();
		}
	}
}
