package com.sthwin.webflux;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by User
 * Date: 2020. 6. 12. 오전 9:06
 */
public class testJdbc {

    public static void main(String[] args) throws Exception {
        Connection conn = null;
        try {
            String user = "heimdall";
            String pw = "heimdall";
            String url = "jdbc:oracle:thin:@localhost:50329:otadev";

            Class.forName("oracle.jdbc.driver.OracleDriver");
            conn = DriverManager.getConnection(url, user, pw);

            System.out.println("Database에 연결되었습니다.\n");

        } catch (ClassNotFoundException cnfe) {
            System.out.println("DB 드라이버 로딩 실패 :" + cnfe.toString());
        } catch (SQLException sqle) {
            System.out.println("DB 접속실패 : " + sqle.toString());
        } catch (Exception e) {
            System.out.println("Unkonwn error");
            e.printStackTrace();
        }


    }
}
