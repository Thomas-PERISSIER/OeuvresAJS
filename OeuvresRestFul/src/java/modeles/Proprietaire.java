package modeles;

import dao.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import javax.json.JsonObject;

public class Proprietaire {
    private int id_proprietaire;
    private String nom_proprietaire;
    private String prenom_proprietaire;
    private String login;
    private String pwd;    

    public Proprietaire() {
    }
    // <editor-fold desc="Propriétés">  
    public int getId_proprietaire() {
        return id_proprietaire;
    }

    public void setId_proprietaire(int id_proprietaire) {
        this.id_proprietaire = id_proprietaire;
    }
    
    public String getNom_proprietaire() {
        return nom_proprietaire;
    }

    public void setNom_proprietaire(String nom_proprietaire) {
        this.nom_proprietaire = nom_proprietaire;
    }
      
    public String getPrenom_proprietaire() {
        return prenom_proprietaire;
    }

    public void setPrenom_proprietaire(String prenom_proprietaire) {
        this.prenom_proprietaire = prenom_proprietaire;
    }
    public void setLogin(String login) {
        this.login = login;
    }

    public String getLogin() {
        return this.login;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getPwd() {
        return this.pwd;
    }    
    // </editor-fold>  
    /**
     * Lecture d'un Propriétaire dans la base de données
     * @param id Id du Propriétaire à lire
     * @throws Exception
     */
    public Proprietaire lire_Id(int id) throws Exception {
        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection connection = null;
        try {
            Connexion cnx = new Connexion();
            connection = cnx.connecter();
            ps = connection.prepareStatement("select * from proprietaire where id_proprietaire = ?");
            ps.setInt(1, id);
            rs = ps.executeQuery();
            if (rs.next()) {
                setProprietes(rs);
            } else {
                throw new Exception("Proprietaire inconnu !");
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
     * Lecture du propriétaire sur son login
     * @param login Login du propriétaire
     * @return classe Proprietaire
     * @throws Exception 
     */
    public Proprietaire lire_Login(String login) throws Exception {
        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection connection = null;
        try {
            Connexion cnx = new Connexion();
            connection = cnx.connecter();
            ps = connection.prepareStatement("select * from proprietaire where login = ?");
            ps.setString(1, login);
            rs = ps.executeQuery();
            if (rs.next()) {
                setProprietes(rs);
            } else {
                throw new Exception("Utilisateur inconnu !");
            }
            return (this);
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

    public boolean connecter(String login, String pwd) throws Exception {
        boolean retour = false;
        try {
            lire_Login(login);
            if (pwd.equals(getPwd())) {
                retour = true;
            }
            return retour;
        } catch (Exception e) {
            throw e;
        }
    }
    

    /**
     * liste des Propriétaires
     * @return List<Proprietaire> Collection de Propriétaires
     * @throws Exception
     */
    public List<Proprietaire> liste() throws Exception {
        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection connection = null;
        List<Proprietaire> lProprietaires = new ArrayList<Proprietaire>();
        try {
            Connexion cnx = new Connexion();
            connection = cnx.connecter();
            ps = connection.prepareStatement("select * from proprietaire");
            rs = ps.executeQuery();
            while (rs.next()) {
                Proprietaire unProprietaire = new Proprietaire();
                unProprietaire.setProprietes(rs);
                lProprietaires.add(unProprietaire);
            }
            return(lProprietaires);
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
    
    public void modifier() throws Exception {
        PreparedStatement ps = null;
        Connection connection = null;
        try {
            Connexion cnx = new Connexion();
            connection = cnx.connecter();
            ps = connection.prepareStatement("update proprietaire set nom_proprietaire = ?, prenom_proprietaire = ? where id_proprietaire = ?");
            ps.setString(1, getNom_proprietaire());
            ps.setString(2, getPrenom_proprietaire());
            ps.setInt(3, getId_proprietaire());
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
     * Insert un propriétaire dans la base de données
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
            setId_proprietaire(db_outils.getIdentifiant(connection, "PROPRIETAIRE"));
            String requete = "insert into proprietaire (id_proprietaire, nom_proprietaire, prenom_proprietaire)";
            requete += " values (?, ?, ?)";
            ps = connection.prepareStatement(requete);
            ps.setInt(1, getId_proprietaire());
            ps.setString(2, getNom_proprietaire());
            ps.setString(3, getPrenom_proprietaire());
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
    
    private void setProprietes(ResultSet rs) throws Exception {
        try {
            setId_proprietaire(rs.getInt("id_proprietaire"));
            setNom_proprietaire(rs.getString("nom_proprietaire"));
            setPrenom_proprietaire(rs.getString("prenom_proprietaire"));
            setLogin(rs.getString("login"));
            setPwd(rs.getString("pwd"));
        } catch (Exception e) {
            throw e;
        }
    }    
    
    public void setPropertiesFromJson(JsonObject jsonObject) {
        setId_proprietaire(jsonObject.getInt("id_proprietaire"));
        setNom_proprietaire(jsonObject.getString("nom_proprietaire"));
        setPrenom_proprietaire(jsonObject.getString("prenom_proprietaire"));        
    }    
}
