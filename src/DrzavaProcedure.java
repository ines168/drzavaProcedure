import com.microsoft.sqlserver.jdbc.SQLServerDataSource;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;

public class DrzavaProcedure {
    public static void main(String[] args) {
        DataSource dataSource = createDataSource();
        try(Connection connection = dataSource.getConnection()) {
            spremiDrzave(connection);
            //obrisiDrzave(connection, 18);
            ispisiDrzave(connection);
        } catch (SQLException e) {
            System.err.println("Greška prilikom spajanja.");
            e.printStackTrace();
        }

    }

    private static void spremiDrzave(Connection connection) throws SQLException {
        ArrayList <Drzava> drzave = new ArrayList<>();
        for (int i = 1; i<=10; i++) {
            drzave.add(new Drzava("Drzava"+i));
        }

        PreparedStatement stmt;
        stmt = connection.prepareStatement("INSERT INTO Drzava (Naziv) VALUES (?)");
        for (Drzava d : drzave) {
            stmt.setString(1, d.getNaziv());
            stmt.executeUpdate();
        }

    }

    private static void obrisiDrzave(Connection connection , int id) throws SQLException{
        CallableStatement cs = connection.prepareCall("{call ObrisiDrzave(?)}");
        cs.setInt(1, id);
        cs.executeUpdate();
    }

    private static void ispisiDrzave(Connection connection) throws SQLException {
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT IDDrzava, Naziv FROM Drzava ");
        System.out.println("\nPopis država:");

        while (rs.next()) {
            int id = rs.getInt("IDDrzava");
            String naziv = rs.getString("Naziv");
            System.out.println(naziv + ", ID: " + id );
        }
        rs.close();
        stmt.close();
    }

    private static DataSource createDataSource() {
        SQLServerDataSource ds = new SQLServerDataSource();
        ds.setServerName("localhost");
        ds.setPortNumber(1433);
        ds.setDatabaseName("AdventureWorksOBP");
        ds.setUser("sa");
        ds.setPassword("SQL");
        ds.setEncrypt(false);
        return ds;
    }
}
