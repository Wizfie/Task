import java.sql.*;

public class DBConnect {

    Connection local_conn = null;
    Connection local_conn2 = null;
    Statement local_stmt = null;


    public void local_connect() throws Exception {
        String local_db_name = "db_accurate";
        String local_db_user = "root";
        String local_db_pass = "Wisfie99";
        int local_port = 3306;


        String local_url = "";
        local_url = "jdbc:mysql://localhost:" + local_port + "/" + local_db_name + "?useSSL=false";
        local_conn = DriverManager.getConnection(local_url, local_db_user, local_db_pass);
        local_conn2 = DriverManager.getConnection(local_url, local_db_user, local_db_pass);
        local_stmt = local_conn.createStatement();


    }
    public void local_closeconnection() throws Exception {

        local_stmt.close();
        local_conn.close();
        local_conn2.close();

    }



}