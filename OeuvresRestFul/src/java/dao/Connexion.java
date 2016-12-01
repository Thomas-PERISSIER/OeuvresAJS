/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.sql.Connection;
import javax.naming.*;
import javax.sql.DataSource;

/**
 *
 * @author alain
 */
public class Connexion {

    /**
     * Constructeur par défaut
     */
    public Connexion() {
    }

    /**
     * Etablit une connexion à la base de données en prenant les paramètres
     * de connexion dans un fichier properties
     * @return Connexion à la base de données
     * @throws Exception
     */
    public Connection connecter() throws Exception {
        Context initCtx, envCtx;
        DataSource ds;
        Connection connection;
        try {
            initCtx = new InitialContext();
            envCtx = (Context) initCtx.lookup("java:comp/env");
            ds = (DataSource) envCtx.lookup("jdbc/Oeuvres");
            connection = ds.getConnection();
            return (connection);
        } catch (Exception e) {
            throw e;
        }
    }
}