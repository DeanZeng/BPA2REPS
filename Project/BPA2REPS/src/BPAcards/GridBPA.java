/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BPAcards;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author zfd
 */
public class GridBPA {

    public List<ACcard> accardArray;
    public List<Bcard> bcardArray;
    public List<Lcard> lcardArray;
    public List<Tcard> tcardArray;

    public GridBPA() {
        accardArray = new ArrayList<>();
        bcardArray = new ArrayList<>();
        lcardArray = new ArrayList<>();
        tcardArray = new ArrayList<>();
    }

    public void transBPA(String bpaFile) {
        transBPA(bpaFile,"B,AC,L,T");
    }
    
    public void transBPA(String bpaFile,String cards){
        /**
         * cards：需要解析的卡片类型，用逗号隔开，例如“AC,B,L”
        */
        String[] cardTypes= cards.split(",");
        Boolean Bflag  = false;
        Boolean ACflag = false;
        Boolean Lflag  = false;
        Boolean Tflag  = false;
        for (int i = 0; i < cardTypes.length; i++) {
            if (cardTypes[i].compareTo("AC")==0){
                ACflag=true;
            }
            if (cardTypes[i].compareTo("B")==0){
                Bflag=true;
            }
            if (cardTypes[i].compareTo("L")==0){
                Lflag=true;
            }
            if (cardTypes[i].compareTo("T")==0){
                Tflag=true;
            }
        }
        // 开始解析文件
        FileReader q = null;
        try {
            q = new FileReader(bpaFile);
            try (BufferedReader b = new BufferedReader(q)) {
                String str;
                int line=0;
                //List<Acard> accardArray = new ArrayList<Acard>();
                int aci = 0;
                //List<Bcard> bcardArray = new ArrayList<Bcard>();
                int bi = 0;
                //List<Lcard> lcardArray = new ArrayList<Lcard>();
                int li = 0;
                //List<Tcard> tcardArray = new ArrayList<Tcard>();
                int ti = 0;
                //<editor-fold> read BPA file
                while ((str = b.readLine()) != null) {
                    line++;
                    try {
                        if (ACflag) {
                            if (ACcard.isAcard(str)) {
                                ACcard acc = new ACcard();
                                acc.CharToCard(str);
                                this.accardArray.add(acc);
                                aci++;
                            }
                        }
                        if (Bflag) {
                            if (Bcard.isBcard(str)) {
                                Bcard bc = new Bcard();
                                bc.CharToCard(str);
                                bcardArray.add(bc);
                                bi++;
                            }
                        }
                        if (Lflag) {
                            if (Lcard.isLcard(str)) {
                                Lcard lc = new Lcard();
                                lc.CharToCard(str);
                                lcardArray.add(lc);
                                li++;
                            }
                        }
                        if (Tflag) {
                            if (Tcard.isTcard(str)) {
                                Tcard tc = new Tcard();
                                tc.CharToCard(str);
                                tcardArray.add(tc);
                                ti++;
                            }
                        }
                    } catch (BPAtrsException ex) {
                        System.out.println("读取第"+line+"行: '" + str + "'时发生错误: "+ex.getMessage());
                        //Logger.getLogger(GridBPA.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            } catch (IOException ex) {
                Logger.getLogger(GridBPA.class.getName()).log(Level.SEVERE, null, ex);
            }
            System.out.println("读取BPA文件完毕!");
            //</editor-fold>
        } catch (FileNotFoundException ex) {
            Logger.getLogger(GridBPA.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                q.close();
            } catch (IOException ex) {
                Logger.getLogger(GridBPA.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        
    }

}

class SelfFunc {
    /**
     * BPAcards 包中用到的自定义函数
    */
    protected static int min(int a, int b) {
        return a < b ? a : b;
    }

    protected static String bpadate(char month, int year) {
        /**
         * 返回日期，暂时返回"null"
        */
        return "null";
    }

    protected static double strToD(String str, int m, int n) throws BPAtrsException {
        //convert from string to double
        //for BPAtoCard conversion 
        //Fm.n
        //problem:1)" -18" 2)"    " (3)" . "(4)"  1137"F6,5
        while (m > str.length()) {
            str = str + ' ';
        }
        char[] c = (str + ' ').toCharArray();

        double f = 0;
        if (str.indexOf('.') == -1) {
            //无小数点
            for (int i = m - 1; i >= 0; i--) {
                //插入小数点
                if ((c[i] >= '0') && (c[i] <= '9')) {
                    if (m - i - 1 >= n) {
                        c[i + 1] = '.';
                    } else {
                        for (int j = m; j > m - n; j--) {
                            c[j] = c[j - 1];
                        }
                        c[m - n] = '.';
                    }
                    break;
                }
            }
        }
        // 有小数点
        int begin = 0, end = 0, l = c.length;
        for (begin = 0; begin < l; begin++) {
            if (c[begin] != ' ') {
                break;
            }
        }
        for (end = l - 1; end >= 0; end--) {
            if (c[end] != ' ') {
                break;
            }
        }
        if (begin == l) {
            return 0;
        }
        if ((begin == end) && ((c[begin] < '0') && (c[begin] < '9'))) {
            return 0;
        }
        boolean negative = false;
        if (str.charAt(begin) == '-') {
            negative = true;
            begin++;
        }
        for (int i = begin; i <= end; i++) {
            //其他字符（包括空格）用0替代
            if ((c[i] >= '0') && (c[i] <= '9')) {
            } else if (c[i] == '.') {
            } else if (c[i] == ' ') {
                c[i] = '0';
            } else {
             
                throw new BPAtrsException("String:'" + str + "'无法转换为Double");
            }
        }
        String s = new String(c, begin, end - begin + 1);
        try {
            f = Double.parseDouble(s);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BPAtrsException("String:'" + str + "'无法转换为Double");
        }
        f = negative ? -f : f;
        if ((Math.abs(f) < 0.0000001) || (Math.abs(f) > 1000000)) {
            f = 0;
        }
        return f;
    }

    protected static double strToDtest(String str, int m, int n) {
        //convert from string to double
        //for BPAtoCard conversion 
        //Fm.n
        //problem:1)" -18" 2)"   " 3) "456" F(3,2)
        int f = 0;
        int l = str.length();
        int begin = 0;
        int end = 0;
        boolean negative = false;
        for (int i = 0; i < l; i++) {
            if (str.charAt(i) != ' ') {
                begin = i;
                break;
            }
        }
        for (int i = l - 1; i >= 0; i--) {
            if (str.charAt(i) != ' ') {
                end = i;
                break;
            }
        }
        if (begin == end) {
            return 0;
        }
        if (str.charAt(begin) == '-') {
            negative = true;
            begin++;
        }
        for (int i = begin; i <= end; i++) {
            if ((str.charAt(i) >= '0') && (str.charAt(i) <= '9')) {
                f = f + str.charAt(i) * 10;
            } else if (str.charAt(i) == '.') {
                break;
            } else {
                System.out.println("String:'" + str + "' 格式不对，输出结果为0");
                return 0;
            }
        }
        if (negative) {
            f = -f;
        }
        return f;
    }

    protected static double strToD(String str) throws BPAtrsException {
        //convert from string to double
        //for BPAtoCard conversion 
        //Fm.n n=0
        int m = str.length();
        return strToD(str, m, 0);
    }

    protected static int strToI(String str) {
        //convert string to integer
        //for BPA card conversion
        //required formation: "  (-)XXXX。    "
        //X:0-9
        int f = 0;
        int l = str.length();
        int begin = 0;
        int end = 0;
        boolean negative = false;
        for (int i = 0; i < l; i++) {
            if (str.charAt(i) != ' ') {
                begin = i;
                break;
            }
        }
        for (int i = l - 1; i >= 0; i--) {
            if (str.charAt(i) != ' ') {
                end = i;
                break;
            }
        }
        if (begin == end) {
            return 0;
        }
        if (str.charAt(begin) == '-') {
            negative = true;
            begin++;
        }
        for (int i = begin; i <= end; i++) {
            if ((str.charAt(i) >= '0') && (str.charAt(i) <= '9')) {
                f = f + str.charAt(i) * 10;
            } else if (str.charAt(i) == '.') {
                break;
            } else {
                System.out.println("String:'" + str + "' 格式不对，输出结果为0");
                return 0;
            }
        }
        if (negative) {
            f = -f;
        }
        return f;
    }

    protected static String substring(String str, int beginIn, int endIn) {

        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) > 255) {
                if (i < beginIn) {
                    beginIn--;
                }
                if (i < endIn) {
                    endIn--;
                }
            }
        }

        return str.substring(beginIn, endIn);

    }

    protected static int strlen(String str) {
        //计算字符串长度
        //针对含有汉字的字符串，
        //汉字占2个字符长度
        int l = str.length();
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) > 255) {
                l++;
            }
        }
        return l;
    }

    protected static char charAt(String str, int index) {
        //返回第n个字符
        //针对含有汉字的字符串，汉字占两个字符长度
        for (int i = 0; i < index; i++) {
            if (str.charAt(i) > 255) {
                index--;
            }
        }
        char c = str.charAt(index);
        return c;
    }
}

class BPAtrsException extends Exception {
    /**
     * BPA解析 
    */
    private String errorMsg;
    public BPAtrsException(String s) {
        this.errorMsg=s;
    }
    public BPAtrsException() {
    }
    @Override
    public String getMessage(){
        return errorMsg;
    }
}
