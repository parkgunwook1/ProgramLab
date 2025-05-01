package kr.co.parkcom.store.db;

import kr.co.parkcom.store.Application;
import kr.co.parkcom.store.domain.keyword.dto.KeywordMonth;
import kr.co.parkcom.store.domain.keyword.dto.KeywordStatisticsTrendResult;
import kr.co.parkcom.store.util.ConfigMapReader;
import okhttp3.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DBManager implements IDBManager{

    private static final Logger log = LoggerFactory.getLogger(Application.class);

    private Connection conn;

    public DBManager(Connection conn) {
        this.conn = conn;
    }
    @Override
    public int insertKeywordStatisticsTrend(KeywordStatisticsTrendResult keywordTrendResult) {
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
    public int insertKeywordMonth(KeywordMonth keywordMonth) {
        String sql = "INSERT INTO keyword_monthly (keyword, jan_search, feb_search, mar_search, apr_search, may_search, jun_search, " +
                "jul_search, aug_search, sep_search, oct_search, nov_search, dec_search)"
              +  "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?)";

        int result = 0;

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1 , keywordMonth.getKeyword());
            pstmt.setInt(2 , keywordMonth.getMonth1());
            pstmt.setInt(3 , keywordMonth.getMonth2());
            pstmt.setInt(4 , keywordMonth.getMonth3());
            pstmt.setInt(5 , keywordMonth.getMonth4());
            pstmt.setInt(6 , keywordMonth.getMonth5());
            pstmt.setInt(7 , keywordMonth.getMonth6());
            pstmt.setInt(8 , keywordMonth.getMonth7());
            pstmt.setInt(9 , keywordMonth.getMonth8());
            pstmt.setInt(10 , keywordMonth.getMonth9());
            pstmt.setInt(11 , keywordMonth.getMonth10());
            pstmt.setInt(12 , keywordMonth.getMonth11());
            pstmt.setInt(13 , keywordMonth.getMonth12());

            result = pstmt.executeUpdate();

            System.out.println(result);
        }catch (Exception e) {
            log.error(e.getMessage() , e);
        }

        return result;
    }

    @Override
    public List<String> selectKeywordTrend() {
        List<String> keywords = new ArrayList<>();
        String sql = "SELECT keyword FROM year_keyword_trend";

        try (PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                keywords.add(rs.getString("keyword"));
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return keywords;
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

        KeywordStatisticsTrendResult result = new KeywordStatisticsTrendResult("선풍기" , 1 , 2 , 3 , 4);
        int dbResult = idbManager.insertKeywordStatisticsTrend(result);
        System.out.println(dbResult);
    }
}
