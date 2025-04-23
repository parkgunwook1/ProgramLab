package api.store.datalab;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import okhttp3.OkHttpClient;
import okhttp3.Request;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataLabContextServer {

    private String apiId;
    private String apiPwd;

    public DataLabContextServer(String apiId, String apiPwd) {
        this.apiId = apiId;
        this.apiPwd = apiPwd;
    }

}
