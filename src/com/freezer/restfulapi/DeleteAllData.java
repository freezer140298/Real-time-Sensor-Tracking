package com.freezer.restfulapi;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import java.sql.*;

@Path("/")
public class DeleteAllData {
    @GET
    @Path("/deletealldata/{token}")
    @Produces(MediaType.TEXT_PLAIN)
    public String deleteAllData(@PathParam("token") String tokenVal)
    {
        String tokenQuery = "SELECT token FROM tokenlist WHERE token=?";
        Connection conn = null;
        try {
            // Create SQLite DB connection
            Class.forName("org.sqlite.JDBC");

            //String dbUrl = "jdbc:sqlite:C:/Users/fReezer/IdeaProjects/ProgrammableLogicDesignProject/database/db.db3"; // for Windows
            String dbUrl ="jdbc:sqlite:/opt/database/db.db3"; // for Debian
            conn = DriverManager.getConnection(dbUrl);

            PreparedStatement pst = conn.prepareStatement(tokenQuery);
            pst.setString(1, tokenVal);


            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                String deleteDataStr = "DELETE FROM data";
                Statement st = conn.createStatement();
                st.executeUpdate(deleteDataStr);
            } else {
                return "Token invalid";
            }

        } catch (SQLException e) {

        } catch (ClassNotFoundException e) {

        }
        return "OK";
    }
}
