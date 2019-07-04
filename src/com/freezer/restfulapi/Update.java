package com.freezer.restfulapi;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.io.StringReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import java.sql.*;

@Path("/")
public class Update
{
    private class UpdateData
    {
        String token;
        float temp;
        float humi;

        public UpdateData(String token,float temp, float humi) {
            this.temp = temp;
            this.humi = humi;
            this.token = token;
        }

        public float getTemp() {
            return temp;
        }

        public void setTemp(float temp) {
            this.temp = temp;
        }

        public float getHumi() {
            return humi;
        }

        public void setHumi(float humi) {
            this.humi = humi;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }
    }


    @Path("/update")
    @POST
    @Consumes("application/json")
    public Response update(String data)
    {
        StringReader stringReader = new StringReader(data);
        JsonObject json = new Gson().fromJson(data,JsonObject.class);


        // Save raw data to Update Data Object
        UpdateData updateData = new UpdateData(json.get("token").getAsString(),json.get("temp").getAsFloat(),json.get("humi").getAsFloat());

        // Get current date and time
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        String dateTimeStr = dtf.format(now);

        // Update to DB
        String tokenQuery = "SELECT token FROM tokenlist WHERE token=?";
        Connection conn = null;
        try
        {
            // Create SQLite DB connection
            Class.forName("org.sqlite.JDBC");
            //String dbUrl = "jdbc:sqlite:C:/Users/fReezer/IdeaProjects/ProgrammableLogicDesignProject/database/db.db3"; // for Windows
            String dbUrl ="jdbc:sqlite:/opt/database/db.db3"; // for Debian
            conn = DriverManager.getConnection(dbUrl);

            PreparedStatement pst = conn.prepareStatement(tokenQuery);
            pst.setString(1,updateData.getToken());


            ResultSet rs = pst.executeQuery();

            if(rs.next())
            {
                // If token exists then update new data to DB
                String updateStr = "INSERT INTO data (temp,humi,datetime) VALUES(?,?,?)";
                PreparedStatement uPst = conn.prepareStatement(updateStr);
                uPst.setFloat(1,updateData.getTemp());
                uPst.setFloat(2,updateData.getHumi());
                uPst.setString(3,dateTimeStr);
                uPst.executeUpdate();
            }
            else
            {
                return Response.status(403).build();
            }
            conn.close();
        }
        catch(SQLException e)
        {
            System.out.println(e.getMessage());
            return Response.status(501).build();
        }
        catch(ClassNotFoundException e)
        {
            System.out.println(e.getMessage());
            return  Response.status(501).build();
        }
        // Return status code 200 if update successful
        return Response.status(200).build();
    }


    @Path("/update")
    @GET
    public Response update(@QueryParam("token") String token,@QueryParam("temp") float temp,@QueryParam("humi") float humi)
    {
        // Get current date and time
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        String dateTimeStr = dtf.format(now);

        // Update to DB
        String tokenQuery = "SELECT token FROM tokenlist WHERE token=?";
        Connection conn = null;
        try
        {
            // Create SQLite DB connection
            Class.forName("org.sqlite.JDBC");
            //String dbUrl = "jdbc:sqlite:C:/Users/fReezer/IdeaProjects/ProgrammableLogicDesignProject/database/db.db3"; // for Windows
            String dbUrl ="jdbc:sqlite:/opt/database/db.db3"; // for Debian
            conn = DriverManager.getConnection(dbUrl);

            PreparedStatement pst = conn.prepareStatement(tokenQuery);
            pst.setString(1,token);


            ResultSet rs = pst.executeQuery();

            if(rs.next())
            {
                // If token exists then update new data to DB
                String updateStr = "INSERT INTO data (temp,humi,datetime) VALUES(?,?,?)";
                PreparedStatement uPst = conn.prepareStatement(updateStr);
                uPst.setFloat(1,temp);
                uPst.setFloat(2,humi);
                uPst.setString(3,dateTimeStr);
                uPst.executeUpdate();
            }
            else
            {
                return Response.status(403).build();
            }
            conn.close();
        }
        catch(SQLException e)
        {
            System.out.println(e.getMessage());
            return Response.status(501).build();
        }
        catch(ClassNotFoundException e)
        {
            System.out.println(e.getMessage());
            return  Response.status(501).build();
        }
        // Return status code 200 if update successful
        return Response.status(200).build();
    }

}

