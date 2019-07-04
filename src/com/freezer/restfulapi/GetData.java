package com.freezer.restfulapi;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.xml.bind.annotation.XmlRootElement;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Path("/")
public class GetData {

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
    @Path("/getdata/{amount}")
    @GET
    public List<dbData> getListData(@PathParam("amount") int amount)
    {
        List<dbData> dbDataList = new ArrayList<dbData>();
        Connection conn = null;
        String dataQuery = "SELECT * FROM ( SELECT * FROM data ORDER BY id DESC LIMIT " + String.valueOf(amount) + ") ORDER BY id ASC";
        try {
            Class.forName("org.sqlite.JDBC");
            //String dbUrl = "jdbc:sqlite:C:/Users/fReezer/IdeaProjects/ProgrammableLogicDesignProject/database/db.db3"; // for Windows
            String dbUrl ="jdbc:sqlite:/opt/database/db.db3"; // for Debian

            conn = DriverManager.getConnection(dbUrl);
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(dataQuery);

            while (rs.next()) {
                String time = rs.getString("datetime");
                time = time.substring(time.indexOf(" ") + 1); // Only time
                dbDataList.add(new dbData(rs.getFloat("temp"), rs.getFloat("humi"), time));
            }
            conn.close();

        }
        catch (ClassNotFoundException e) {
            e.printStackTrace();
            return dbDataList;
        }
        catch (SQLException e) {
            e.printStackTrace();
            return dbDataList;
        }
        return dbDataList;
    }
}
