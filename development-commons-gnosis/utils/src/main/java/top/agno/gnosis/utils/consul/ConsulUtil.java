package top.agno.gnosis.utils.consul;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpStatus;
import top.agno.gnosis.utils.http.HttpUtil;
import top.agno.gnosis.utils.http.RequestResult;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

/**
 * @Description:
 * @Author: gaojing [01381583@yto.net.cn]
 * @Date: Created in 19:08 2019/8/22
 * @Modified:
 */
public class ConsulUtil {

    private static String CONSUL_URL = "";

    private static String GET_KEY_PREFIX = "v1/kv/";

    public ConsulUtil(String url) {
        if (url == null || "".equals(url)) {
            throw new RuntimeException("consul url is blank");
        }
        CONSUL_URL = url.endsWith("/") ? CONSUL_URL : url.concat("/");
    }

    public static String getValue(String key) {
        checkUrlIsNotBlank();
        RequestResult result = HttpUtil.doGet(CONSUL_URL.concat(GET_KEY_PREFIX).concat(key));
        int status = result.getStatusCode();
        if (status == HttpStatus.SC_OK) {
            JSONArray jsonArray = JSONArray.parseArray(result.getResult());
            if (jsonArray == null || jsonArray.size() == 0) {
                return null;
            }
            JSONObject jsonObject = JSONObject.parseObject(String.valueOf(jsonArray.get(0)));
            return new String(Base64.getDecoder().decode(jsonObject.getString("Value")));

        }
        return null;
    }

    public static List<String> getValues() {
        checkUrlIsNotBlank();
        RequestResult result = HttpUtil.doGet(CONSUL_URL.concat(GET_KEY_PREFIX).concat("?recurse"));
        int status = result.getStatusCode();
        if (status == HttpStatus.SC_OK) {
            JSONArray jsonArray = JSONArray.parseArray(result.getResult());
            if (jsonArray == null || jsonArray.size() == 0) {
                return null;
            }
            List<String> resultList = new ArrayList<>();
            jsonArray.stream().forEach(e -> {
                JSONObject jsonObject = JSONObject.parseObject(String.valueOf(e));
                resultList.add(new String(Base64.getDecoder().decode(jsonObject.getString("Value"))));
            });
            return resultList;

        }
        return null;
    }

    public static List<String> getValues(String keyPrefix) {
        checkUrlIsNotBlank();
        RequestResult result = HttpUtil.doGet(CONSUL_URL.concat(GET_KEY_PREFIX).concat("?recurse"));
        int status = result.getStatusCode();
        if (status == HttpStatus.SC_OK) {
            JSONArray jsonArray = JSONArray.parseArray(result.getResult());
            if (jsonArray == null || jsonArray.size() == 0) {
                return null;
            }
            List<String> resultList = new ArrayList<>();
            jsonArray.stream().forEach(e -> {
                JSONObject jsonObject = JSONObject.parseObject(String.valueOf(e));
                if (jsonObject.getString("Key").startsWith(keyPrefix)) {
                    resultList.add(new String(Base64.getDecoder().decode(jsonObject.getString("Value"))));
                }
            });
            return resultList == null || resultList.size() == 0 ? null : resultList;
        }
        return null;
    }

    public static List<String> getKeys() {
        checkUrlIsNotBlank();
        RequestResult result = HttpUtil.doGet(CONSUL_URL.concat(GET_KEY_PREFIX).concat("?recurse"));
        int status = result.getStatusCode();
        if (status == HttpStatus.SC_OK) {
            JSONArray jsonArray = JSONArray.parseArray(result.getResult());
            if (jsonArray == null || jsonArray.size() == 0) {
                return null;
            }
            List<String> resultList = new ArrayList<>();
            jsonArray.stream().forEach(e -> {
                JSONObject jsonObject = JSONObject.parseObject(String.valueOf(e));
                resultList.add(jsonObject.getString("Key"));
            });
            return resultList;

        }
        return null;
    }

    public static boolean insertOrUpdateKey(String key, String value) {
        checkUrlIsNotBlank();
        RequestResult result = HttpUtil.doPut(CONSUL_URL.concat(GET_KEY_PREFIX).concat(key), value);
        int status = result.getStatusCode();
        if (status != HttpStatus.SC_OK) {
            return true;
        }
        return false;
    }

    public static boolean deleteKey(String key) {
        checkUrlIsNotBlank();
        RequestResult result = HttpUtil.doDelete(CONSUL_URL.concat(GET_KEY_PREFIX).concat(key));
        int status = result.getStatusCode();
        if (status != HttpStatus.SC_OK) {
            return true;
        }
        return false;
    }

    public static boolean deleteAllOfKeys() {
        checkUrlIsNotBlank();
        List<String> keys = getKeys();
        if (keys == null || keys.size() == 0) {
            return true;
        }
        try {
            keys.stream().forEach(ConsulUtil::deleteKey);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    private static void checkUrlIsNotBlank() {
        if (CONSUL_URL == null || "".equals(CONSUL_URL)) {
            throw new RuntimeException("consul url is blank");
        }
    }

    public static void main(String[] args) {
        // Ly8lMjAlMjAlMjAlMjAlMjAlMjAlMjAlMjBJbnRTdHJlYW0ucmFuZ2VDbG9zZWQlMjgxJTJDJTIwMTAwMDAlMjkuZm9yRWFjaCUyOGUlMjAtJTNFJTIwJTdCJTBBLy8lMjAlMjAlMjAlMjAlMjAlMjAlMjAlMjAlMjAlMjAlMjAlMjBDb25zdWxVdGlsJTIwY29uc3VsVXRpbCUyMCUzRCUyMG5ldyUyMENvbnN1bFV0aWwlMjglMjJodHRwJTNBLy8xOTIuMTY4LjIwNy4xNDAlM0E4NTAxJTIyJTI5JTNCJTBBLy8lMjAlMjAlMjAlMjAlMjAlMjAlMjAlMjAlMjAlMjAlMjAlMjBpbnNlcnRPclVwZGF0ZUtleSUyOFN5c3RlbS5jdXJyZW50VGltZU1pbGxpcyUyOCUyOSUyMCslMjAlMjIlMjIlMkMlMjBTeXN0ZW0uY3VycmVudFRpbWVNaWxsaXMlMjglMjklMjArJTIwJTIyJTIyJTI5JTNCJTBBLy8lMjAlMjAlMjAlMjAlMjAlMjAlMjAlMjAlMjAlMjAlMjAlMjBTeXN0ZW0ub3V0LnByaW50bG4lMjhnZXRLZXlzJTI4JTI5JTI5JTNCJTBBLy8lMjAlMjAlMjAlMjAlMjAlMjAlMjAlMjAlN0QlMjklM0IlMEElMEElMjAlMjAlMjAlMjAlMjAlMjAlMjAlMjBDb25zdWxVdGlsJTIwY29uc3VsVXRpbCUyMCUzRCUyMG5ldyUyMENvbnN1bFV0aWwlMjglMjJodHRwJTNBLy8xOTIuMTY4LjIwNy4xNDAlM0E4NTAxJTIyJTI5JTNCJTBBJTBBLy8lMjAlMjAlMjAlMjAlMjAlMjAlMjAlMjBTeXN0ZW0ub3V0LnByaW50bG4lMjhjb25zdWxVdGlsLmdldFZhbHVlcyUyOCUyOS5zaXplJTI4JTI5JTI5JTNCJTBBLy8lMjAlMjAlMjAlMjAlMjAlMjAlMjAlMjBTeXN0ZW0ub3V0LnByaW50bG4lMjhjb25zdWxVdGlsLmdldFZhbHVlcyUyOCUyMjE1NjY0Nzc0MTg4JTIyJTI5LnNpemUlMjglMjklMjklM0IlMEEvLyUyMCUyMCUyMCUyMCUyMCUyMCUyMCUyMFN5c3RlbS5vdXQucHJpbnRsbiUyOGNvbnN1bFV0aWwuZ2V0VmFsdWVzJTI4JTIyMTU2NjQ3NzQxODQlMjIlMjkuc2l6ZSUyOCUyOSUyOSUzQiUwQSUwQS8vJTIwJTIwJTIwJTIwJTIwJTIwJTIwJTIwY29uc3VsVXRpbC5kZWxldGVBbGxPZktleXMlMjglMjklM0I=
    }
}
