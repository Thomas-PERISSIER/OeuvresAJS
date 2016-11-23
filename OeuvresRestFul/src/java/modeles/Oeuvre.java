package modeles;

import dao.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import javax.json.JsonNumber;
import javax.json.JsonObject;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

public class Oeuvre {

    private int id_oeuvre;
    private int id_proprietaire;
    private String titre;
    private double prix;
    private Proprietaire proprietaire;

    public Oeuvre() {
        setProprietaire(new Proprietaire());
    }

    /**
     * Initialise le Propriétaire d'une oeuvre
     *
     * @param id_proprietaire Id du propriétaire de l'oeuvre
     * @throws Exception
     */
    public Oeuvre(int id_proprietaire) throws Exception {
        setProprietaire(new Proprietaire().lire_Id(id_proprietaire));

    }
    // <editor-fold desc="Propriétés">
    public int getId_oeuvre() {
        return id_oeuvre;
    }
    public void setId_oeuvre(int id_oeuvre) {
        this.id_oeuvre = id_oeuvre;
    }
    public int getId_proprietaire() {
        return id_proprietaire;
    }
    public void setId_proprietaire(int id_proprietaire) {
        this.id_proprietaire = id_proprietaire;
    }
    public Proprietaire getProprietaire() {
        return proprietaire;
    }
    public void setProprietaire(Proprietaire proprietaire) {
        this.proprietaire = proprietaire;
    }
    public String getTitre() {
        return titre;
    }
    public void setTitre(String titre) {
        this.titre = titre;
    }
    public double getPrix() {
        return prix;
    }
    public void setPrix(double prix) {
        this.prix = prix;
    }
    // </editor-fold>
    /**
     * Lecture d'une Oeuvre dans la base de données
     *
     * @param id Id de l'oeuvre à lire
     * @throws Exception
     */
    public Oeuvre lire_Id(int id) throws Exception {
        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection connection = null;
        try {
            Connexion cnx = new Connexion();
            connection = cnx.connecter();
            ps = connection.prepareStatement("select * from oeuvre where id_oeuvre = ?");
            ps.setInt(1, id);
            rs = ps.executeQuery();
            if (rs.next()) {
                setProperties(rs);
            } else {
                throw new Exception("Oeuvre inconnue !");
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
     * liste des oeuvre
     *
     * @return List<Oeuvre> Collection d'oeuvres
     * @throws Exception
     */
    public List<Oeuvre> liste() throws Exception {
        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection connection = null;
        List<Oeuvre> lOeuvres = new ArrayList<Oeuvre>();
        try {
            Connexion cnx = new Connexion();
            connection = cnx.connecter();
            ps = connection.prepareStatement("select * from oeuvre");
            rs = ps.executeQuery();
            while (rs.next()) {
                Oeuvre uneOeuvre = new Oeuvre();
                uneOeuvre.setProperties(rs);
                lOeuvres.add(uneOeuvre);
            }
            return (lOeuvres);
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
    
    public void supprimer(int id_oeuvre) throws Exception {
        PreparedStatement ps = null;
        Connection connection = null;
        try {
            Connexion cnx = new Connexion();
            connection = cnx.connecter();
            ps = connection.prepareStatement("delete from oeuvre where id_oeuvre = ?");
            ps.setInt(1, id_oeuvre);
            ps.executeUpdate();
        } catch (Exception e) {
            throw e;
        } finally {
            try {
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
     * Met à jour une oeuvre dans la base de données
     *
     * @throws Exception
     */
    public void modifier() throws Exception {
        PreparedStatement ps = null;
        Connection connection = null;
        try {
            Connexion cnx = new Connexion();
            connection = cnx.connecter();
            ps = connection.prepareStatement("update oeuvre set titre = ?, prix = ?, id_proprietaire = ? where id_oeuvre = ?");
            ps.setString(1, getTitre());
            ps.setDouble(2, getPrix());
            ps.setInt(3, getId_proprietaire());
            ps.setInt(4, getId_oeuvre());
            ps.executeUpdate();
        } catch (Exception e) {
            throw e;
        } finally {
            try {
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
     * Insert une oeuvre dans la base de données
     *
     * @throws Exception
     */
    public void ajouter() throws Exception {
        PreparedStatement ps = null;
        Connection connection = null;
        Db_outils db_outils;
        try {
            Connexion cnx = new Connexion();
            connection = cnx.connecter();
            db_outils = new Db_outils();
            setId_oeuvre(db_outils.getIdentifiant(connection, "OEUVRE"));
            String requete = "insert into oeuvre (id_oeuvre, titre, prix, id_proprietaire)";
            requete += " values (?, ?, ?, ?)";
            ps = connection.prepareStatement(requete);
            ps.setInt(1, getId_oeuvre());
            ps.setString(2, getTitre());
            ps.setDouble(3, getPrix());
            ps.setInt(4, getId_proprietaire());
            ps.executeUpdate();
        } catch (Exception e) {
            throw e;
        } finally {
            try {
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

    private void setProperties(ResultSet rs) throws Exception {
        try {
            setId_oeuvre(rs.getInt("id_oeuvre"));
            setId_proprietaire(rs.getInt("id_proprietaire"));
            setTitre(rs.getString("titre"));
            setPrix(Double.parseDouble(rs.getString("prix")));
            setProprietaire(getProprietaire().lire_Id(this.getId_proprietaire()));
        } catch (Exception e) {
            throw e;
        }
    }

    public void setPropertiesFromJson(JsonObject jsonObject) {
        this.setId_proprietaire(jsonObject.getInt("id_proprietaire"));
        this.setId_oeuvre(jsonObject.getInt("id_oeuvre"));
//        Proprietaire proprietaire = new Proprietaire();
//        proprietaire.setPropertiesFromJson(jsonObject.getJsonObject("proprietaire"));
//        this.setProprietaire(proprietaire);
        JsonNumber jsonNumber = jsonObject.getJsonNumber("prix");
        this.setPrix(jsonNumber.doubleValue());
        this.setTitre(jsonObject.getString("titre"));
    }
}
