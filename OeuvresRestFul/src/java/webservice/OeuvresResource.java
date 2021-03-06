/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webservice;

import java.io.StringReader;
import java.util.Date;
import java.util.List;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.json.*;
import javax.ws.rs.core.Response;
import modeles.*;
import outils.Utilitaire;

@Path("webservices")
public class OeuvresResource {

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of OeuvresResource
     */
    public OeuvresResource() {
    }

    @OPTIONS
    public Response getOptions() {
        return Response.ok()
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Methods", "POST, GET, PUT, UPDATE, OPTIONS")
                .header("Access-Control-Allow-Headers", "Content-Type, Accept, X-Requested-With").build();
    }

    @GET
    @Path("getOeuvres")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Oeuvre> getOeuvres() throws Exception {
        Oeuvre oeuvre = new Oeuvre();
        return oeuvre.liste();
    }

//    @GET
//    @Path("getOeuvre/{id_oeuvre}")
//    @Produces(MediaType.APPLICATION_JSON)
//    public Oeuvre getOeuvre(@PathParam("id_oeuvre") int id_oeuvre) throws Exception {
//        Oeuvre oeuvre = new Oeuvre();
//        oeuvre = oeuvre.lire(id_oeuvre);
//        return oeuvre;
//    }
    
    /**
     * Version complète de getOeuvre retournant une Response incluant
     * soit un objet Oeuvre au format jSon et un code de retour 200, 
     * soit le message d'erreur issu de l'Exception au format jSon en cas 
     * de problèmeet un code 500
     * @param id_oeuvre : id de l'Oeuvre à lire
     * @return : Response
     * @throws Exception 
     */
    @GET
    @Path("getOeuvre/{id_oeuvre}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getOeuvre(@PathParam("id_oeuvre") int id_oeuvre) throws Exception {
        Response response = null;
        try{
            Oeuvre oeuvre = new Oeuvre();    
            oeuvre = oeuvre.lire(id_oeuvre);
            response = Response.status(Response.Status.OK).entity(oeuvre).build();
        }catch(Exception ex){
            JsonObject retour = Json.createObjectBuilder().add("message", ex.getMessage()).build();
            response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(retour).build();
        }
        return response;
    }    

    @GET
    @Path("getProprietaire/{id_proprietaire}")
    @Produces(MediaType.APPLICATION_JSON)
    public Proprietaire getProprietaire(@PathParam("id_proprietaire") int id_proprietaire) throws Exception {
        Proprietaire proprietaire = new Proprietaire();
        proprietaire.lire(id_proprietaire);
        return proprietaire;
    }

    @GET
    @Path("getReservation/{id_oeuvre}/{date_reservation}")
    @Produces(MediaType.APPLICATION_JSON)
    public Reservation getReservation(@PathParam("id_oeuvre") int id_oeuvre, @PathParam("date_reserv") String date_reserv) throws Exception {
        Reservation reservation = new Reservation();
        Date date_reservation = Utilitaire.StrToDate(date_reserv, "yyyy-MM-dd");
        reservation.lire(id_oeuvre, date_reservation);
        return reservation;
    }

    @GET
    @Path("getProprietaires")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Proprietaire> getProprietaires() throws Exception {
        Proprietaire proprietaire = new Proprietaire();
        return proprietaire.liste();
    }

    @GET
    @Path("getReservations")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Reservation> getReservations() throws Exception {
        Reservation reservation = new Reservation();
        return reservation.liste();
    }

    @GET
    @Path("getAdherents")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Adherent> getAdherents() throws Exception {
        Adherent adherent = new Adherent();
        return adherent.liste();
    }

    @GET
    @Path("getConnexion/{login}")
    @Produces(MediaType.APPLICATION_JSON)
    public Proprietaire getConnexion(@PathParam("login") String login) throws Exception {
        Proprietaire proprietaire = new Proprietaire();
        proprietaire.lire_Login(login);
        return (proprietaire);
    }

    @POST
    @Path("ajouterOeuvre")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response ajouterOeuvre(String oeuvreJson) throws Exception {
        JsonObject retour = Json.createObjectBuilder().add("message", "").build();
        Response response = null;
        int codeRetour = Response.Status.OK.getStatusCode();
        try {
            if (oeuvreJson != null) {
                Oeuvre oeuvre = new Oeuvre();
                JsonObject obj = convertJson(oeuvreJson);
                oeuvre.setPropertiesFromJson(obj);
                oeuvre.ajouter();
            }
        } catch (Exception ex) {
            String message = Utilitaire.GestException(ex);
            retour = Json.createObjectBuilder().add("message",message).build();
            codeRetour = Response.Status.INTERNAL_SERVER_ERROR.getStatusCode();
        } finally {
            response = Response.status(codeRetour).entity(retour).build();
        }
        return response;
    }

    @POST
    @Path("modifierOeuvre")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response modifierOeuvre(String oeuvreJson) throws Exception {
        Response response = null;
        try {
            if (oeuvreJson != null) {
                Oeuvre oeuvre = new Oeuvre();
                JsonObject obj = convertJson(oeuvreJson);
                oeuvre.setPropertiesFromJson(obj);
                oeuvre.modifier();
                response = Response.status(Response.Status.OK).build();
            }
        } catch (Exception ex) {
            JsonObject retour = Json.createObjectBuilder().add("message", ex.getMessage()).build();
            response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(retour).build();
        }
        return response;
    }

    @POST
    @Path("modifierProprietaire")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response modifierProprietaire(String proprietaireJson) {
        JsonObject retour = Json.createObjectBuilder().add("message", "").build();
        Response response = null;
        int codeRetour = Response.Status.OK.getStatusCode();
        try {
            Proprietaire proprietaire = new Proprietaire();
            JsonObject obj = convertJson(proprietaireJson);
            proprietaire.setPropertiesFromJson(obj);
            proprietaire.modifier();
        } catch (Exception ex) {
            retour = Json.createObjectBuilder().add("message", ex.getMessage()).build();
            codeRetour = Response.Status.INTERNAL_SERVER_ERROR.getStatusCode();
        } finally {
            response = Response.status(codeRetour).entity(retour).build();
            return response;
        }
    }

    @POST
    @Path("ajouterProprietaire")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response ajouterProprietaire(String proprietaireJson) {
        JsonObject retour = Json.createObjectBuilder().add("message", "").build();
        Response response = null;
        int codeRetour = Response.Status.OK.getStatusCode();
        try {
            Proprietaire proprietaire = new Proprietaire();
            JsonObject obj = convertJson(proprietaireJson);
            proprietaire.setPropertiesFromJson(obj);
            proprietaire.ajouter();
        } catch (Exception ex) {
            retour = Json.createObjectBuilder().add("message", ex.getMessage()).build();
            codeRetour = Response.Status.INTERNAL_SERVER_ERROR.getStatusCode();
        } finally {
            response = Response.status(codeRetour).entity(retour).build();
            return response;
        }
    }

    @POST
    @Path("ajouterReservation")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response ajouterReservation(String reservationJson) {
        JsonObject retour = Json.createObjectBuilder().add("message", "").build();
        Response response = null;
        int codeRetour = Response.Status.OK.getStatusCode();
        try {
            Reservation reservation = new Reservation();
            JsonObject obj = convertJson(reservationJson);
            reservation.setId_adherent(obj.getInt("id_adherent"));
            reservation.setDate_reservation(Utilitaire.StrToDate(obj.getString("date_reservation"), "yyyy-MM-dd"));
            reservation.setId_oeuvre(obj.getInt("id_oeuvre"));
            reservation.ajouter();
        } catch (Exception ex) {
            retour = Json.createObjectBuilder().add("message", ex.getMessage()).build();
            codeRetour = Response.Status.INTERNAL_SERVER_ERROR.getStatusCode();
        } finally {
            response = Response.status(codeRetour).entity(retour).build();
            return response;
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("confirmerReservation/{id_oeuvre}-{date_reservation}")
    public Response confirmerReservation(@PathParam("id_oeuvre") int id_oeuvre, @PathParam("date_reservation") String date_reservation) {
        JsonObject retour = Json.createObjectBuilder().add("message", "").build();
        Response response = null;
        int codeRetour = Response.Status.OK.getStatusCode();
        try {
            Reservation reservation = new Reservation();
            reservation.setId_oeuvre(id_oeuvre);
            reservation.setDate_reservation(Utilitaire.StrToDate(date_reservation, "yyyy-MM-dd"));            
            reservation.modifier();
        } catch (Exception ex) {
            retour = Json.createObjectBuilder().add("message", ex.getMessage()).build();
            codeRetour = Response.Status.INTERNAL_SERVER_ERROR.getStatusCode();
        } finally {
            response = Response.status(codeRetour).entity(retour).build();
            return response;
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("supprimerReservation/{id_oeuvre}-{date_reservation}")
    public Response supprimerReservation(@PathParam("id_oeuvre") int id_oeuvre, @PathParam("date_reservation") String date_reservation) {
        JsonObject retour = Json.createObjectBuilder().add("message", "").build();
        Response response = null;
        int codeRetour = Response.Status.OK.getStatusCode();
        try {
            Reservation reservation = new Reservation();
            reservation.setId_oeuvre(id_oeuvre);
            reservation.setDate_reservation(Utilitaire.StrToDate(date_reservation, "yyyy-MM-dd"));   
            reservation.supprimer();
        } catch (Exception ex) {
            retour = Json.createObjectBuilder().add("message", ex.getMessage()).build();
            codeRetour = Response.Status.INTERNAL_SERVER_ERROR.getStatusCode();
        } finally {
            response = Response.status(codeRetour).entity(retour).build();
            return response;
        }
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("supprimerOeuvre/{id_oeuvre}")
    public Response supprimerOeuvre(@PathParam("id_oeuvre") int id_oeuvre) {
        JsonObject retour = Json.createObjectBuilder().add("message", "").build();
        Response response = null;
        int codeRetour = Response.Status.OK.getStatusCode();
        try {
            Oeuvre oeuvre = new Oeuvre(); 
            oeuvre.supprimer(id_oeuvre);
        } catch (Exception ex) {
            retour = Json.createObjectBuilder().add("message", ex.getMessage()).build();
            codeRetour = Response.Status.INTERNAL_SERVER_ERROR.getStatusCode();
        } finally {
            response = Response.status(codeRetour).entity(retour).build();
            return response;
        }
    }
    
    /**
     * Convertit une chaîne au format jSon en un JsonObject
     * @param s chaîne à convertir
     * @return JsonObject
     */
    private JsonObject convertJson(String s) {
        JsonReader jsonReader = Json.createReader(new StringReader(s));
        JsonObject jsonObject = jsonReader.readObject();
        jsonReader.close();
        return jsonObject;
    }
}