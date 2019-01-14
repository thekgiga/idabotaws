package hello;

import db.DatabaseConnection;
import org.json.JSONArray;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

@RestController
public class HelloController {

    @RequestMapping("/")
    public String index() {

        ArrayList result = new ArrayList<>();
        result.add("---GREETINGS---");
        try {
            ResultSet rs = DatabaseConnection.getInstance().query("SELECT * FROM ida.room;");
            while (rs.next()) {
                String instruction = rs.getString("instructions");
                String idroom = rs.getString("idroom");
                String floor = rs.getString("floor");
                result.add("INSTRUCTION :" + instruction + " for room " + idroom + " on floor " + floor + "\t");
            }

        } catch (SQLException e) {
            System.out.println("exception shit happened");
        }

        return new JSONArray(result).toString();
    }

}