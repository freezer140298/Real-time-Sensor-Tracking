package com.freezer.restfulapi;


import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.xml.bind.annotation.XmlRootElement;
import java.sql.*;

@Path("/")
public class GetLatest {
    @XmlRootElement
    protected class dbData
    {
        float temp,humi;
        String time;

        public dbData(float temp, float humi, String time) {
            this.temp = temp;
            this.humi = humi;
            this.time = time;
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

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }
    }

    @Produces(MediaType.APPLICATION_JSON)
    @GET
    @Path("/getlatest")
    public dbData getLatest()
    {
        dbData data = null;
        Connection conn = null;
        String dataQuery = "SELECT * FROM data ORDER BY ID DESC LIMIT 1";
        try
        {
            Class.forName("org.sqlite.JDBC");
            //String dbUrl = "jdbc:sqlite:C:/Users/fReezer/IdeaProjects/ProgrammableLogicDesignProject/database/db.db3"; // for Windows
            String dbUrl ="jdbc:sqlite:/opt/database/db.db3"; // for Debian
            conn = DriverManager.getConnection(dbUrl);
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(dataQuery);
            while (rs.next()) {
                String time = rs.getString("datetime");
                time = time.substring(time.indexOf(" ") + 1); // Only time
                data = new dbData(rs.getFloat("temp"), rs.getFloat("humi"), time);
            }
            conn.close();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return data;
    }
}
