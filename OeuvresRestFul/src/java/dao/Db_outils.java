/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Types;

/**
 *
 * @author alain
 */
public class Db_outils {

    public Db_outils() {
    }

    public int getIdentifiant(Connection connection, String id) throws Exception {
        CallableStatement cs = null;
        try {
            cs = connection.prepareCall("{? = call inc_parametre(?)}");
            cs.registerOutParameter(1,Types.INTEGER);
            cs.setString(2,id);
            cs.execute();
            return (cs.getInt(1));
        }catch(Exception e){
            throw e;
        }finally{
            try{
                if (cs != null) cs.close();
            }catch(Exception e){
                throw e;
            }
        }
    }
}
