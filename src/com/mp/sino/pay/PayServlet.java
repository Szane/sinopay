/**
 * Created by xueling on 16/7/20.
 */
package com.mp.sino.pay;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "PayServlet", urlPatterns = "/paySign")
public class PayServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("payer", request.getParameter("payer"));
        map.put("amount", request.getParameter("amount"));
        map.put("bizSysFlag", request.getParameter("bizSysFlag"));
        map.put("orgId", request.getParameter("orgId"));
        map.put("orderNo", request.getParameter("orderNo"));
        map.put("payCode", request.getParameter("payCode"));
        map.put("payAcctNo", request.getParameter("payAcctNo"));
        map.put("receptAcctNo", request.getParameter("receptAcctNo"));
        map.put("receptAcctName", request.getParameter("receptAcctName"));

        String valuesBySort = getValuesByAscSort(map);
        valuesBySort = new String(valuesBySort.getBytes("ISO-8859-1"), "GBK");
        String digest = getKeyedDigest(valuesBySort, "ShanDongXML");

        response.setCharacterEncoding("GBK");
        response.setContentType("application/json; charset=GBK");
        PrintWriter out = response.getWriter();
        out.write("{sign:'" + digest + valuesBySort + "'}");
        out.close();
    }

    //默认字符串编码
    private static final String defCharSet = "GBK";
    //默认的拼接字符串的连接方式
    private static final String defLink = "&";

    /**
     * 获取字符串的 md5 签名值
     *
     * @param strSrc 签名原串
     * @param key    签名key
     * @return
     */
    public String getKeyedDigest(String strSrc, String key) {
        String charSet = defCharSet;
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(strSrc.getBytes(charSet));
            StringBuffer result = new StringBuffer();
            byte[] temp = md5.digest(key.getBytes(charSet));
            for (int i = 0; i < temp.length; i++) {
                result.append(Integer.toHexString(
                        (0x000000ff & temp[i]) | 0xffffff00).substring(6));
            }
            return result.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private String getValuesBySort(Map<String, String> map,
                                   String rejectKey, String link) {
        String result = null;
        try {
            link = (isEmpty(link) ? defLink : link);
            result = getValuesBySort_asc(map, rejectKey, link);
        } catch (Exception e) {
        }
        return result;
    }

    /**
     * 判断输入的字符串是否是null或""
     *
     * @param str
     * @return
     */
    private boolean isEmpty(String str) {
        if (str == null || "".equals(str.trim()))
            return true;
        return false;
    }

    private String getValuesBySort_asc(Map<String, String> map,
                                       String rejectKey, String link) {
        if (map == null || map.size() < 1) {
            return null;
        }
        String[] keyArray = map.keySet().toArray(new String[0]);
        Arrays.sort(keyArray);
        StringBuilder result = new StringBuilder();
        for (String key : keyArray) {
            if (key != null && key.equals(rejectKey)) {
                continue;
            }
            result.append(key).append("=").append(map.get(key)).append(link);
        }
        return (result.length() > 0 ? result.subSequence(0,
                result.length() - link.length()).toString() : null);
    }

    /**
     * map中的 key-value 按一定顺序和规则排列后生成新的字符串
     *
     * @param @param  map  参与排序的 map
     * @param @return 按 map中key的 assic 码顺序，以 & 连接 方式返回字符串
     * @return String
     * @Title: getValuesByAscSort
     */
    public String getValuesByAscSort(Map<String, String> map) {
        return getValuesBySort(map, null, null);
    }

}
