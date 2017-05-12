package org.LoadData;

import org.tools.MysqlConn;

import java.beans.Statement;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;


public class LoadCsv {
    private static void LoadFileData(String path) throws IOException {
        // get file list where the path has   
        File file = new File(path);

        // get the folder list   
        File[] array = file.listFiles();
        java.sql.Statement stmt = null;

        Properties prop = new Properties();
        InputStream in = MysqlConn.class.getClassLoader()
                                        .getResourceAsStream("org//tools//db.properties");
        prop.load(in);

        String ip = prop.getProperty("ip");

        //        System.out.println(ip);
        String username = prop.getProperty("username");
        String password = prop.getProperty("passwd");
        String database = prop.getProperty("database");
        String url = "jdbc:mysql://" + ip + ":3306/" + database + "?user=" +
            username + "&password=" + password +
            "&useServerPrepStmts=false&rewriteBatchedStatements=true";
        System.out.println(url);

        Connection conn = null;

       
        try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        try {
			conn = DriverManager.getConnection(url);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
       
        try {
			conn.setAutoCommit(false);
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} 
        
        for (int i = 0; i < array.length; i++) {
            if (array[i].isFile()) {
                // only take file name  
                String result = new String();
                FileInputStream fis = new FileInputStream("shares_data//" +
                        array[i].getName());
                InputStreamReader isr = new InputStreamReader(fis, "GBK");
                BufferedReader br = new BufferedReader(isr);
                String s = null;
                int rows = 1;
                
                

                while ((s = br.readLine()) != null) { //??readLine????????
                    result = (System.lineSeparator() + s);

                    String value = result.toString().replace("\'", "")
                                         .replace(",", "\',\'");

                    String sql = "INSERT INTO getdata VALUES ('" + value +
                        "\');";
                    System.out.println(sql);

                    if (rows == 1) {
                    	rows++;
                        continue;
                    }

                    
                try {
					stmt = conn.createStatement();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            		  
                try {
					stmt.executeUpdate(sql);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
                }
                try {
					conn.commit();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
                rows = 1;
              
            }
        }
    }

    //	private static int insert(String student) {
    //	    Connection conn = getConn();
    //	    int i = 0;
    //	    String sql = "insert into students (Name,Sex,Age) values(?,?,?)";
    //	    PreparedStatement pstmt;
    //	    try {
    //	        pstmt = (PreparedStatement) conn.prepareStatement(sql);
    //	        pstmt.setString(1, student.getName());
    //	        pstmt.setString(2, student.getSex());
    //	        pstmt.setString(3, student.getAge());
    //	        i = pstmt.executeUpdate();
    //	        pstmt.close();
    //	        conn.close();
    //	    } catch (SQLException e) {
    //	        e.printStackTrace();
    //	    }
    //	    return i;
    //	}
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        try {
            LoadFileData("shares_data//");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
