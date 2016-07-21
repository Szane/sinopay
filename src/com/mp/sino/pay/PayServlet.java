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

/**
 * Created by xueling on 16/7/20.
 */
@WebServlet(name = "PayServlet", urlPatterns = "/paySign")
public class PayServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {


    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        StringBuffer params = new StringBuffer();
        params.append("amount=");
        params.append(String.valueOf(request.getParameter("amount")));
        params.append("&bizSysFlag=");
        params.append(String.valueOf(request.getParameter("bizSysFlag")));
        params.append("&orderNo=");
        params.append(String.valueOf(request.getParameter("orderNo")));
        params.append("&orgId=");
        params.append(String.valueOf(request.getParameter("orgId")));
        params.append("&payAcctNo=");
        params.append(String.valueOf(request.getParameter("payAcctNo")));
        params.append("&payCode=");
        params.append(String.valueOf(request.getParameter("payCode")));
        params.append("&payer=");
        params.append(String.valueOf(request.getParameter("payer")));
        params.append("&receptAcctName=");
        params.append(String.valueOf(request.getParameter("receptAcctName")));
        params.append("&receptAcctNo=");
        params.append(String.valueOf(request.getParameter("receptAcctNo")));
        String input = new String(params.toString().getBytes("iso8859-1"), "UTF8");//FIXME
        String result = getMd5(input);
        response.setCharacterEncoding("GBK");
        response.setContentType("application/json; charset=GBK");
        PrintWriter out = response.getWriter();
        out.println("{sign:" + result + "}");
        out.close();
    }

    private String getMd5(String strSrc) {
        String key = "epay";
        String charSet = "GBK";
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

}
