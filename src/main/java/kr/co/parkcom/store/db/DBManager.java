package kr.co.parkcom.store.db;

import kr.co.parkcom.store.Application;
import kr.co.parkcom.store.domain.keyword.dto.KeywordTrendResult;
import kr.co.parkcom.store.util.ConfigMapReader;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DBManager implements IDBManager{

    private static final Logger log = LoggerFactory.getLogger(Application.class);

    private Connection conn;

    public DBManager(Connection conn) {
        this.conn = conn;
    }
    @Override
    public int insertKeywordTrend(KeywordTrendResult keywordTrendResult) {
        String sql = "INSERT INTO year_keyword_trend(keyword , q1_avg , q2_avg , q3_avg , q4_avg )" +
                " VALUES(?,?,?,?,?)";

        int result = 0;

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1 , keywordTrendResult.getKeyword());
            pstmt.setInt(2 , keywordTrendResult.getQ1Avg());
            pstmt.setInt(3 , keywordTrendResult.getQ2Avg());
            pstmt.setInt(4 , keywordTrendResult.getQ3Avg());
            pstmt.setInt(5 , keywordTrendResult.getQ4Avg());

            result = pstmt.executeUpdate();

            System.out.println(result);
        }catch (Exception e) {
            log.error(e.getMessage() , e);
        }

        return result;
    }

    @Override
    public int selectKeywordTrend() {
        return 0;
    }

    public static void main(String[] args) {
        ConfigMapReader config = new ConfigMapReader();
        config.loadConfig("config.xml");

        IDBManager idbManager = null;
        String dbType = config.get("dbType");

        String dbUrl = config.get("db.url");
        String dbUser = config.get("db.user");
        String dbPwd = config.get("db.password");

        System.out.println(dbUrl + ", " + dbUser  + ","+ dbPwd);
        Connection dbCon = null;
        try {
            dbCon = DriverManager.getConnection(dbUrl , dbUser , dbPwd);

            if (dbCon != null) {
                log.info("db connection success");
                if (dbType.equals("Y")) {
                    idbManager = new DBManager(dbCon);
                }else {
                    idbManager = new MockDBManager();
                }
            }
        }catch (SQLException e) {
            log.error("connection : {} " ,e.getMessage());
        }

        KeywordTrendResult result = new KeywordTrendResult("선풍기" , 1 , 2 , 3 , 4);
        int dbResult = idbManager.insertKeywordTrend(result);
        System.out.println(dbResult);
    }
}
