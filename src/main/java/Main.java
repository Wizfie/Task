import org.apache.log4j.Logger;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Main {

    private static final org.apache.log4j.Logger log = Logger.getLogger(Main.class);

    public static void main(String[] args) throws IOException {


        LocalDateTime time_now = LocalDateTime.now();
        LocalDateTime time_previous = LocalDateTime.now().minusMinutes(5);

        DateTimeFormatter time_format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String timenow = time_now.format(time_format) ;
        String timeprevious = time_previous.format(time_format) ;
        //String timeprevious = "2022-07-19 22:07:50" ;
        //String timenow = "2022-07-19 22:48:14" ;


        DateTimeFormatter time_format2 = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        String timenow2 = time_now.format(time_format2);
        String timeprevious2 = time_previous.format(time_format2) ;
        //String timeprevious2 = "20230101000000" ; //20220101000000
        //String timenow2 = "20230131000000"; //20220103235959


        System.out.println(timeprevious + " " + timeprevious2);
        System.out.println(timenow + " " + timenow2);

        try{
            log.info("START");


            //Sync.register_AllMember_V1(timeprevious2, timenow2);
//            Sync.item_transfer();
//            Sync.item_list();
            Sync.Warehouse_list();
//            Sync.item_list_stock();




            log.info("FINISH");
            System.exit(0);
        //} catch (SQLException e) {
        //    e.printStackTrace();
        //    log.error(e.getMessage(), e);
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage(), e);
        }


    }

}
