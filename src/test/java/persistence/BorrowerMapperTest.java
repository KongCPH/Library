package persistence;

import entities.Borrower;
import exceptions.DatabaseException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.*;

class BorrowerMapperTest {

    private final static String USER = "postgres";
    private final static String PASSWORD = "postgres";
    private final static String URL = "jdbc:postgresql://localhost:5432/bibliotek?currentSchema=test";

    private static DatabaseConnector connector;
    private static BorrowerMapper borrowerMapper;

    @BeforeAll
    public static void setUpClass() {
        try {
            connector = new DatabaseConnector(USER, PASSWORD, URL);
            // dependency injection
            borrowerMapper = new BorrowerMapper(connector);
            try (Connection testConnection = connector.getConnection())
            {
                try (Statement stmt = testConnection.createStatement())
                {
                    // The test schema is already created, so we only need to delete/create test tables
                    stmt.execute("DROP TABLE IF EXISTS test.bog CASCADE");
                    stmt.execute("DROP TABLE IF EXISTS test.forfatter CASCADE");
                    stmt.execute("DROP TABLE IF EXISTS test.laaner CASCADE");
                    stmt.execute("DROP TABLE IF EXISTS test.postnummer CASCADE");
                    stmt.execute("DROP TABLE IF EXISTS test.udlaan CASCADE");

                    // Removes sequenceobjects which controls autoincrement of keys
                    stmt.execute("DROP SEQUENCE IF EXISTS test.bog_bog_id_seq CASCADE;");
                    stmt.execute("DROP SEQUENCE IF EXISTS test.forfatter_forfatter_id_seq CASCADE;");
                    stmt.execute("DROP SEQUENCE IF EXISTS test.laaner_laaner_id_seq CASCADE;");

                    // Create tables as copy of original public schema structure
                    stmt.execute("CREATE TABLE test.bog AS (SELECT * from public.bog) WITH NO DATA");
                    stmt.execute("CREATE TABLE test.forfatter AS (SELECT * from public.forfatter) WITH NO DATA");
                    stmt.execute("CREATE TABLE test.laaner AS (SELECT * from public.laaner) WITH NO DATA");
                    stmt.execute("CREATE TABLE test.postnummer AS (SELECT * from public.postnummer) WITH NO DATA");
                    stmt.execute("CREATE TABLE test.udlaan AS (SELECT * from public.udlaan) WITH NO DATA");

                    // Create sequences for auto generating id's
                    stmt.execute("CREATE SEQUENCE test.bog_bog_id_seq");
                    stmt.execute("ALTER TABLE test.bog ALTER COLUMN bog_id SET DEFAULT nextval('test.bog_bog_id_seq')");
                    stmt.execute("CREATE SEQUENCE test.forfatter_forfatter_id_seq");
                    stmt.execute("ALTER TABLE test.forfatter ALTER COLUMN forfatter_id SET DEFAULT nextval('test.forfatter_forfatter_id_seq')");
                    stmt.execute("CREATE SEQUENCE test.laaner_laaner_id_seq");
                    stmt.execute("ALTER TABLE test.laaner ALTER COLUMN laaner_id SET DEFAULT nextval('test.laaner_laaner_id_seq')");
                }
            }

            catch (SQLException e)
            {
                System.out.println(e.getMessage());
                fail("Database connection failed");
            }

        }
        catch (DatabaseException e){
            System.out.println(e.getMessage());
            fail("Database connection failed");
        }
    }

    @BeforeEach
    void setUp() {
        try (Connection testConnection = connector.getConnection()) {
            try (Statement stmt = testConnection.createStatement()) {
                // Delete all rows to start from scratch
                stmt.execute("DELETE FROM test.udlaan CASCADE");
                stmt.execute("DELETE FROM test.laaner CASCADE");
                stmt.execute("DELETE FROM test.bog CASCADE");
                stmt.execute("DELETE FROM test.forfatter CASCADE");

                // Set all sequence objects to start autoincrementing from 1
                stmt.execute("SELECT setval('test.forfatter_forfatter_id_seq', 1, false)");
                stmt.execute("SELECT setval('test.bog_bog_id_seq', 1, false)");
                stmt.execute("SELECT setval('test.laaner_laaner_id_seq', 1, false)");

                // Insert test data into forfatter-tabel
                stmt.execute("INSERT INTO test.forfatter (forfatter_id, navn) VALUES " +
                        "(DEFAULT, 'Karen Blixen'), " +
                        "(DEFAULT, 'Jussi Adler-Olsen'), " +
                        "(DEFAULT, 'H.C. Andersen')");

                // Insert test data into bog-tabel
                stmt.execute("INSERT INTO test.bog (bog_id, titel, udgivelsesaar, forfatter_id) VALUES " +
                        "(DEFAULT, 'Babettes Gæstebud', 1950, 1), " +
                        "(DEFAULT, 'Kvinden i buret', 2007, 2), " +
                        "(DEFAULT, 'Den lille Havfrue', 1837, 3)");

                // Insert test data into laaner-tabel
                stmt.execute("INSERT INTO test.laaner (laaner_id, navn, adresse, postnr) VALUES " +
                        "(DEFAULT, 'Anders Sørensen', 'Nørregade 10', 8000), " +
                        "(DEFAULT, 'Sofie Hansen', 'Vestergade 23', 9000), " +
                        "(DEFAULT, 'Emil Rasmussen', 'Østergade 5', 5000), " +
                        "(DEFAULT, 'Laura Mikkelsen', 'Søndergade 7', 4000), " +
                        "(DEFAULT, 'Mads Pedersen', 'Kirkestræde 14', 6000)");

                // Insert test data into udlaan-tabel
                stmt.execute("INSERT INTO test.udlaan (bog_id, laaner_id, dato) VALUES " +
                        "(1, 1, '2025-02-10'), " +
                        "(2, 1, '2025-02-15'), " +
                        "(3, 2, '2025-02-12'), " +
                        "(1, 3, '2025-02-18'), " +
                        "(2, 4, '2025-02-14'), " +
                        "(3, 5, '2025-02-20')");

                // More sequence object stuff
                stmt.execute("SELECT setval('test.forfatter_forfatter_id_seq', COALESCE((SELECT MAX(forfatter_id) FROM test.forfatter)+1, 1), false)");
                stmt.execute("SELECT setval('test.bog_bog_id_seq', COALESCE((SELECT MAX(bog_id) FROM test.bog)+1, 1), false)");
                stmt.execute("SELECT setval('test.laaner_laaner_id_seq', COALESCE((SELECT MAX(laaner_id) FROM test.laaner)+1, 1), false)");
            }
        } catch (SQLException throwables) {
            fail("Database connection failed");
        }
    }

    @Test
    void testConnection() throws SQLException {
        assertNotNull(connector.getConnection());
    }

    @Test
    void testGetBorrowerById() throws DatabaseException{
        assertEquals(new Borrower("Anders Sørensen", "Nørregade 10" + " " + 8000, 1), borrowerMapper.getBorrowerById(1));

    }



}

