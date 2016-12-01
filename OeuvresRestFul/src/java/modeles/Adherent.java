package modeles;

import dao.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class Adherent {
    private int id_adherent;
    private String nom_adherent;
    private String prenom_adherent;

    public Adherent() {
    }
    // <editor-fold desc="Propriétés">
    public int getId_adherent() {
        return id_adherent;
    }
    public void setId_adherent(int id_adherent) {
        this.id_adherent = id_adherent;
    }
    public String getNom_adherent() {
        return nom_adherent;
    }
    public void setNom_adherent(String nom_adherent) {
        this.nom_adherent = nom_adherent;
    }
    public String getPrenom_adherent() {
        return prenom_adherent;
    }
    public void setPrenom_adherent(String prenom_adherent) {
        this.prenom_adherent = prenom_adherent;
    }
    // </editor-fold>
    /**
     * Lecture d'un adhérent dans la base de données
     * @param id : Id de l'adhérent à lire
     * @return Adherent
     * @throws Exception
     */
    public Adherent lire(int id) throws Exception {
        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection connection = null;
        try {
            Connexion cnx = new Connexion();
            connection = cnx.connecter();
            ps = connection.prepareStatement("select * from adherent where id_adherent = ?");
            ps.setInt(1, id);
            rs = ps.executeQuery();
            if (rs.next()) {
                setId_adherent(rs.getInt("id_adherent"));
                setNom_adherent(rs.getString("nom_adherent"));
                setPrenom_adherent(rs.getString("prenom_adherent"));
            } else {
                throw new Exception("Adhérent inconnu !");
            }
            return this;
        } catch (Exception e) {
            throw e;
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * liste des adhérents
     * @return List<Adherent> Collection d'adhérents
     * @throws Exception
     */
    public List<Adherent> liste() throws Exception {
        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection connection = null;
        Adherent unAdherent;
        List<Adherent> lAdherents = new ArrayList<Adherent>();
        try {
            Connexion cnx = new Connexion();
            connection = cnx.connecter();
            ps = connection.prepareStatement("select * from adherent");
            rs = ps.executeQuery();
            while (rs.next()) {
                unAdherent = new Adherent();
                unAdherent.setId_adherent(rs.getInt("id_adherent"));
                unAdherent.setNom_adherent(rs.getString("nom_adherent"));
                unAdherent.setPrenom_adherent(rs.getString("prenom_adherent"));
                lAdherents.add(unAdherent);
            }
            return(lAdherents);
        } catch (Exception e) {
            throw e;
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}