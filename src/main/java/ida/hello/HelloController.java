package ida.hello;

import ch.qos.logback.core.util.FileUtil;
import ida.db.DatabaseConnection;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.json.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Map;

@RestController
public class HelloController {

    String TEST_JSON_RESPONSE_FILE = "test/response.json";

    @RequestMapping("/search")
    public String index(@RequestParam("action") String action, @RequestParam(value = "startDate", required = false) String startDate, @RequestParam(value = "endDate", required = false) String endDate) {
        ArrayList result = new ArrayList<>();
        result.add("---GREETINGS---");
        result.add("ACTION EXECUTED: " + action);

        switch (action.toLowerCase().trim()) {
            case "read":
                result.add(executeTestAction());
                break;
            case "search":
                result.add(searchBetweenDates(startDate, endDate));
                break;
            case "heroku":
                result.add(executeHerokuAction());
                break;
        }

        return new JSONArray(result).toString();
    }

    @RequestMapping("/test")
    public String index() {
        return "HELLO WORLD";
    }


    @PostMapping("/bookApt")
    @ResponseBody
    public String saveData(HttpServletRequest request,
                           HttpServletResponse response, Model model) throws IOException {

        System.out.println("post request received");

        StringBuffer jb = new StringBuffer();
        String line = null;
        try {
            BufferedReader reader = request.getReader();
            while ((line = reader.readLine()) != null)
                jb.append(line);
        } catch (Exception e) { /*report an error*/ }

        System.out.println("jb.toString(): " + jb.toString());
        try {
            JSONObject jsonObject = HTTP.toJSONObject(jb.toString());
            System.out.println("json object: " + jsonObject);
        } catch (JSONException e) {
            // crash and burn
            throw new IOException("Error parsing JSON request string");
        }


        Enumeration<String> parameterMap = request.getAttributeNames();
        System.out.println("parameters map: " + parameterMap);
//        for (Map.Entry<String, String[]> entry : parameterMap.entrySet())
//        {
//            System.out.println(entry.getKey() + "->" + entry.getValue());
//        }

        System.out.println(request.getParameter("json"));
        String jsonString = request.getParameter("json");
        return org.apache.commons.io.FileUtils.readFileToString(new File(getClass().getClassLoader().getResource(TEST_JSON_RESPONSE_FILE).getFile()));
    }

    public ArrayList executeTestAction() {
        System.out.println("EXECUTING TEST ACTION");
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

    public ArrayList executeHerokuAction() {
        System.out.println("EXECUTING HEROKU POSTGRATE SQL CONNECTION ACTION");
        ArrayList result = new ArrayList<>();
        try {
            ResultSet rs = DatabaseConnection.getInstance().query("SELECT * FROM public.\"testTable\";");
            while (rs.next()) {
                int id = rs.getInt("testID");
                String name = rs.getString("testName");
                result.add("id :" + id + " for test table with name " + name + ".\t");
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