package bcd;

import java.awt.im.InputContext;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.sql.DriverManager.getConnection;

public abstract class ConnectionFactory {
    private static final String DB_PROPERTIES_FILE = "database.properties";
    private static Connection cnx;

    /**
     * Para carregar o arquivo properties
     * @return
     */
    private static InputStream getInputStream(){
        // Para pegar o arquivo quando estiver dentro um .jar
        InputStream is = ConnectionFactory.class.getResourceAsStream("/resources/"+DB_PROPERTIES_FILE);
        // Para pegar o arquivo quando estiver executando pela IDE
        if (is == null){
            is = ConnectionFactory.class.getClassLoader().getResourceAsStream(DB_PROPERTIES_FILE);
        }
        return is;
    }

    /**
     * Faz a conexão em um banco de dados MySQL e retorna o objeto Connection
     *
     * @return conexão com um banco MySQL
     */
    public static synchronized Connection getDBConnection(){
        Properties properties = new Properties();
        // carregando as propriedades user, password e useSSL do arquivo database.properties
        try {
            properties.load(getInputStream());
            // obtendo valores para host, port e dbname do arquivo properties
            String host = properties.getProperty("host");
            String port = properties.getProperty("port");
            String dbname = properties.getProperty("database");

            String url = "jdbc:mysql://" + host + ":" + port + "/" + dbname;
            cnx = getConnection(url, properties);

        } catch (IOException ex) {
            Logger.getLogger(ConnectionFactory.class.getName()).log(Level.SEVERE, "arquivo properties não encontrado", ex);
        } catch (SQLException ex) {
            Logger.getLogger(ConnectionFactory.class.getName()).log(Level.SEVERE, "erro com instrução SQL", ex);
        }
        return cnx;
    }
}
