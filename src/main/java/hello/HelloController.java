package hello;

import db.DatabaseConnection;
import org.json.JSONArray;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

@RestController
public class HelloController {

    @RequestMapping("/search")
    public String index(@RequestParam("action") String action, @RequestParam(value = "startDate", required = false) String startDate, @RequestParam(value = "endDate", required = false) String endDate) {

        ArrayList result = new ArrayList<>();
        result.add("---GREETINGS---");
        result.add("ACTION EXECUTED: " + action);

        switch (action) {
            case "READ":
                result.add(executeTestAction());
                break;
            case "SEARCH":
                result.add(searchBetweenDates(startDate, endDate));
                break;
        }

        return new JSONArray(result).toString();
    }

    @RequestMapping("/test")
    public String index() {
        return "HELLO WORLD";
    }


    public ArrayList executeTestAction() {
        ArrayList result = new ArrayList<>();
        try {
            ResultSet rs = DatabaseConnection.getInstance().query("SELECT * FROM ida.room;");
            while (rs.next()) {
                String instruction = rs.getString("instructions");
                String idroom = rs.getString("idroom");
                String floor = rs.getString("floor");
                result.add("INSTRUCTION :" + instruction + " for ROOM " + idroom + " on FLOOR " + floor + "\t");
            }
        } catch (SQLException e) {
            result = new ArrayList<>();
            result.add("CAN NOT CONNECT TO DATABASE");
        }
        return result;
    }

    /**
     * @param startDate in format 2018/12/23
     * @param endDate   in format 2018/12/23
     * @return
     */
    public ArrayList searchBetweenDates(String startDate, String endDate) {
        ArrayList result = new ArrayList<>();
        try {
            ResultSet rs = DatabaseConnection.getInstance().query("SELECT * FROM ida.reservationcalendar as rc where rc.date between '" + startDate + "'and '" + endDate + "';");
            while (rs.next()) {
                String date = rs.getString("date");
                String idroom = rs.getString("idroom");
                String idreservation = rs.getString("idreservation");
                result.add("DATE :" + date + " for ROOM " + idroom + " =>> RESERVATIONID " + idreservation + "\t");
            }
        } catch (SQLException e) {
            System.out.println("exception shit happened");
        }
        return result;
    }
}