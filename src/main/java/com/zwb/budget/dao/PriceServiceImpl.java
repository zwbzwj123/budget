package com.zwb.budget.dao;

import com.zwb.budget.entity.ItemPrice;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.sql.*;
import java.text.DecimalFormat;
import java.util.*;

/**
 * Created by zwb
 * Time: 2019/10/6
 */
@Service
public class PriceServiceImpl implements PriceService{

    @Autowired
    private PriceRepository priceRepository;

    @Override
    public List<ItemPrice> findByIndex(String queryIndex) {
        Connection conn = null;
        Statement statement = null;
        ResultSet rs = null;
        List<ItemPrice> lists = new ArrayList<ItemPrice>();
        // 连接数据库的url
        String url = "jdbc:mysql://localhost:3306/price?useSSL=false&serverTimezone=UTC";
        // 数据库用户名
        String user = "root";
        // 数据库密码
        String password = "zwbzwj123";
        try {
            // 1加载驱动
            Class.forName("com.mysql.jdbc.Driver");
            // 2创建数据连接对象
            conn = DriverManager.getConnection(url, user, password);
            // 3.创建Statement对象
            statement = conn.createStatement();
            // 4.获取ResultSet对象
            String sql = "select * from cost where title_index like '"+ queryIndex + "%'";
            rs = statement.executeQuery(sql);
            while (rs.next()) {// 判断是否有下一个
                ItemPrice p = new ItemPrice();
                String titleIndex = rs.getString("title_index");
                p.setIndex(titleIndex);
                String factor = rs.getString("factor");
                p.setFactor(factor);
                String name = rs.getString("name");
                p.setName(name);
                String measure = rs.getString("measure");
                p.setMeasure(measure);
                Integer cost = rs.getInt("cost");
                p.setCost(cost);
                Integer setupCost = rs.getInt("setup_cost");
                p.setSetupCost(setupCost);
                Integer laborCost = rs.getInt("labor_cost");
                p.setLaborCost(laborCost);
                Integer extraCost = rs.getInt("extra_cost");
                p.setExtraCost(extraCost);
                Integer machineryCost = rs.getInt("machinery_cost");
                p.setMachineryCost(machineryCost);
                lists.add(p);
            }
        } catch (ClassNotFoundException e) {
            // TODO 自动生成的 catch 块
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // 5.关闭数据库
            // 注意关闭顺序
            try {
                if (rs != null)
                    rs.close();
                if (statement != null)
                    statement.cancel();
                if (conn != null)
                    conn.close();
            } catch (SQLException e) {
                // TODO 自动生成的 catch 块
                e.printStackTrace();
            }
        }
        return lists;
    }

    @Override
    public ResponseEntity<byte[]> sendExcel(String indexArray, String username) {
        Connection conn = null;
        Statement statement = null;
        ResultSet rs = null;
        List<String> indexList = Arrays.asList(indexArray.split(","));
        List<ItemPrice> itemLists = new ArrayList<ItemPrice>();

        String url = "jdbc:mysql://localhost:3306/price?useSSL=false&serverTimezone=UTC";
        String user = "root";
        String password = "zwbzwj123";
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(url, user, password);
            statement = conn.createStatement();
            for (String queryIndex : indexList) {
                String sql = "select * from cost where title_index like '"+ queryIndex + "%'";
                rs = statement.executeQuery(sql);
                while (rs.next()) {// 判断是否有下一个
                    ItemPrice p = new ItemPrice();
                    String titleIndex = rs.getString("title_index");
                    p.setIndex(titleIndex);
                    String name = rs.getString("name");
                    p.setName(name);
                    String measure = rs.getString("measure");
                    p.setMeasure(measure);
                    Integer cost = rs.getInt("cost");
                    p.setCost(cost);
                    String factor = rs.getString("factor");
                    p.setFactor(factor);
                    Integer setupCost = rs.getInt("setup_cost");
                    p.setSetupCost(setupCost);
                    Integer laborCost = rs.getInt("labor_cost");
                    p.setLaborCost(laborCost);
                    Integer extraCost = rs.getInt("extra_cost");
                    p.setExtraCost(extraCost);
                    Integer machineryCost = rs.getInt("machinery_cost");
                    p.setMachineryCost(machineryCost);
                    itemLists.add(p);
                }
            }
        } catch (ClassNotFoundException e) {
            // TODO 自动生成的 catch 块
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // 5.关闭数据库
            // 注意关闭顺序
            try {
                if (rs != null)
                    rs.close();
                if (statement != null)
                    statement.cancel();
                if (conn != null)
                    conn.close();
            } catch (SQLException e) {
                // TODO 自动生成的 catch 块
                e.printStackTrace();
            }
        }

        HSSFWorkbook workbook = null;
        FileInputStream fis = null;
        File f = new File("template.xls");
        try {
            fis = new FileInputStream(f);
            workbook = new HSSFWorkbook(fis);
        } catch (Exception e) {
            System.out.println(e);
            return null;
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        HSSFSheet sheet = workbook.getSheet("Sheet1");
        HSSFRow rows = null;
        HSSFCellStyle cellStyle = workbook.createCellStyle();
        HSSFCellStyle cellStyle2 = workbook.createCellStyle();
        HSSFCellStyle cellStyle3 = workbook.createCellStyle();
        DataFormat format = workbook.createDataFormat();

        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setBorderLeft(BorderStyle.THIN);
        cellStyle.setBorderTop(BorderStyle.THIN);
        cellStyle.setBorderRight(BorderStyle.THIN);
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        cellStyle.setDataFormat(format.getFormat("0.00"));

        cellStyle2.setBorderBottom(BorderStyle.THIN);
        cellStyle2.setBorderLeft(BorderStyle.THIN);
        cellStyle2.setBorderTop(BorderStyle.THIN);
        cellStyle2.setBorderRight(BorderStyle.THIN);
        cellStyle2.setVerticalAlignment(VerticalAlignment.CENTER);
        cellStyle2.setAlignment(HorizontalAlignment.LEFT);
        cellStyle2.setWrapText(true);

        cellStyle3.setBorderBottom(BorderStyle.THIN);
        cellStyle3.setBorderLeft(BorderStyle.THIN);
        cellStyle3.setBorderTop(BorderStyle.THIN);
        cellStyle3.setBorderRight(BorderStyle.THIN);
        cellStyle3.setVerticalAlignment(VerticalAlignment.CENTER);
        cellStyle3.setAlignment(HorizontalAlignment.CENTER);
        cellStyle3.setDataFormat(format.getFormat("0"));

        HSSFFont font=workbook.createFont();
        font.setFontHeightInPoints((short)9);
        font.setFontName("宋体");
        cellStyle.setFont(font);
        cellStyle2.setFont(font);
        cellStyle3.setFont(font);

        for (int rowIndex = 5; rowIndex < itemLists.size() + 5; rowIndex++){
            if (sheet.getRow(rowIndex) != null) {
                int lastRowNo = sheet.getLastRowNum();
                sheet.shiftRows(rowIndex, lastRowNo, 1);
            }
            rows = sheet.createRow(rowIndex);
            rows.setHeightInPoints(35);
            int i = rowIndex - 5;
            for (int colIndex = 0; colIndex < 18; colIndex++) {
                HSSFCell cell = rows.createCell(colIndex);
                cell.setCellStyle(cellStyle);
                String sql = null;
                String factorDate = null;
                List<String> factorWithDate = null;
                Float factor = 0.0f;
                switch(colIndex){
                    case 1:
                        cell.setCellStyle(cellStyle2);
                        cell.setCellValue(itemLists.get(i).getIndex());
                        break;
                    case 2:
                        cell.setCellStyle(cellStyle2);
                        if (itemLists.get(i).getFactor() == null) {
                            cell.setCellValue(itemLists.get(i).getName());
                            break;
                        }
                        try {
                            Class.forName("com.mysql.jdbc.Driver");
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                        try {
                            conn = DriverManager.getConnection(url, user, password);
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        try {
                            statement = conn.createStatement();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        sql = "select * from user where user_name like '"+ username + "%'";
                        try {
                            rs = statement.executeQuery(sql);
                            while (rs.next()) {
                                factorDate = rs.getString("factor_date");
                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }

                        factorWithDate = Arrays.asList(itemLists.get(i).getFactor().split("/"));
                        for (int index = 0; index < factorWithDate.size(); index++){
                            assert factorDate != null;
                            if (factorWithDate.get(index).contains(factorDate)) {
                                factor = new Float(factorWithDate.get(index).split("__")[1]);
                                break;
                            }
                        }
                        if (factor != 0){
                            cell.setCellValue(itemLists.get(i).getName() +
                                    "（主材*" + factor + "不含税系数）");
                        } else {
                            cell.setCellValue(itemLists.get(i).getName());
                        }
                        break;
                    case 3:
                        cell.setCellValue(itemLists.get(i).getMeasure());
                        break;
                    case 4:
                        cell.setCellStyle(cellStyle3);
                        cell.setCellValue(0);
                        break;
                    case 9:
                        if (itemLists.get(i).getFactor() == null){
                            cell.setCellValue(itemLists.get(i).getCost());
                            break;
                        } else {
                            try {
                                Class.forName("com.mysql.jdbc.Driver");
                            } catch (ClassNotFoundException e) {
                                e.printStackTrace();
                            }
                            try {
                                conn = DriverManager.getConnection(url, user, password);
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                            try {
                                statement = conn.createStatement();
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                            sql = "select * from user where user_name like '"+ username + "%'";
                            try {
                                rs = statement.executeQuery(sql);
                                while (rs.next()) {
                                    factorDate = rs.getString("factor_date");
                                }
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }

                            factorWithDate = Arrays.asList(itemLists.get(i).getFactor().split("/"));
                            for (int index = 0; index < factorWithDate.size(); index++){
                                assert factorDate != null;
                                if (factorWithDate.get(index).contains(factorDate)) {
                                    factor = new Float(factorWithDate.get(index).split("__")[1]);
                                    break;
                                }
                            }
                            String costWithFactor = new DecimalFormat("0.00").format(factor * itemLists.get(i).getCost());
                            cell.setCellValue(Float.valueOf(costWithFactor));
                        }
                        break;
                    case 10:
                        cell.setCellValue(itemLists.get(i).getSetupCost());
                        break;
                    case 11:
                        cell.setCellValue(itemLists.get(i).getLaborCost());
                        break;
                    case 12:
                        cell.setCellValue(itemLists.get(i).getExtraCost());
                        break;
                    case 14:
                        cell.setCellStyle(cellStyle3);
                        cell.setCellType(CellType.FORMULA);
                        cell.setCellFormula("E" + (rowIndex + 1) + "*J" + (rowIndex + 1));
                        break;
                    case 15:
                        cell.setCellStyle(cellStyle3);
                        cell.setCellType(CellType.FORMULA);
                        cell.setCellFormula("E" + (rowIndex + 1) + "*K" + (rowIndex + 1));
                        break;
                    case 16:
                        cell.setCellStyle(cellStyle3);
                        cell.setCellType(CellType.FORMULA);
                        cell.setCellFormula("E" + (rowIndex + 1) + "*L" + (rowIndex + 1));
                        break;
                    case 17:
                        cell.setCellStyle(cellStyle3);
                        cell.setCellType(CellType.FORMULA);
                        cell.setCellFormula("E" + (rowIndex + 1) + "*M" + (rowIndex + 1));
                        break;
                }
            }
        }

        HSSFRow row = null;
        HSSFCell cell = null;
        int subtotal = itemLists.size() + 5;
        row =  sheet.getRow(subtotal);
        for (int index = 14; index < 18; index++) {
            cell = row.createCell(index);
            cell.setCellStyle(cellStyle3);
            cell.setCellType(CellType.FORMULA);
            switch(index) {
                case 14:
                    cell.setCellFormula("SUM(O" + 6 + ":O" + (5 + itemLists.size()) + ")");
                    break;
                case 15:
                    cell.setCellFormula("SUM(P" + 6 + ":P" + (5 + itemLists.size()) + ")");
                    break;
                case 16:
                    cell.setCellFormula("SUM(Q" + 6 + ":Q" + (5 + itemLists.size()) + ")");
                    break;
                case 17:
                    cell.setCellFormula("SUM(R" + 6 + ":R" + (5 + itemLists.size()) + ")");
                    break;
            }
        }

        int subtotal_real = subtotal + 1;
        for (int index = subtotal + 3, j = 0; index < subtotal + 18; index++, j++) {
            row =  sheet.getRow(index);
            switch(j) {
                case 0 :
                    cell = row.createCell(14);
                    cell.setCellStyle(cellStyle3);
                    cell.setCellType(CellType.FORMULA);
                    cell.setCellFormula("O" + subtotal_real + "*E" + (subtotal_real + 3) + "/100");
                    break;
                case 1:
                    cell = row.createCell(14);
                    cell.setCellStyle(cellStyle3);
                    cell.setCellType(CellType.FORMULA);
                    cell.setCellFormula("O" + subtotal_real + "*E" + (subtotal_real + 4) + "/100");
                    break;
                case 2:
                    cell = row.createCell(15);
                    cell.setCellStyle(cellStyle3);
                    cell.setCellType(CellType.FORMULA);
                    cell.setCellFormula("Q" + subtotal_real + "*E" + (subtotal_real + 5) + "/100");
                    break;
                case 3:
                    cell = row.createCell(14);
                    cell.setCellStyle(cellStyle3);
                    cell.setCellType(CellType.FORMULA);
                    cell.setCellFormula("SUM(O" + subtotal_real + ":O" + (5 + subtotal_real) + ")");

                    cell = row.createCell(15);
                    cell.setCellStyle(cellStyle3);
                    cell.setCellType(CellType.FORMULA);
                    cell.setCellFormula("SUM(P" + subtotal_real + ":P" + (5 + subtotal_real) + ")");
                    break;
                case 5:
                    cell = row.createCell(14);
                    cell.setCellStyle(cellStyle3);
                    cell.setCellType(CellType.FORMULA);
                    cell.setCellFormula("O" + (subtotal_real + 6));

                    cell = row.createCell(15);
                    cell.setCellStyle(cellStyle3);
                    cell.setCellType(CellType.FORMULA);
                    cell.setCellFormula("P" + (subtotal_real + 6));
                    break;
                case 8:
                    cell = row.createCell(14);
                    cell.setCellStyle(cellStyle3);
                    cell.setCellType(CellType.FORMULA);
                    cell.setCellFormula("O" + subtotal_real);
                    break;
                case 11:
                    cell = row.createCell(14);
                    cell.setCellStyle(cellStyle3);
                    cell.setCellType(CellType.FORMULA);
                    cell.setCellFormula("O" + (subtotal_real + 3));
                    break;
                case 12:
                    cell = row.createCell(14);
                    cell.setCellStyle(cellStyle3);
                    cell.setCellType(CellType.FORMULA);
                    cell.setCellFormula("O" + (subtotal_real + 4));
                    break;
                case 14:
                    cell = row.createCell(14);
                    cell.setCellStyle(cellStyle3);
                    cell.setCellType(CellType.FORMULA);
                    cell.setCellFormula("O" + subtotal_real +"*0.16+O" + (subtotal_real + 3) + "*0.1+O" + (subtotal_real + 4) + "*0.06");

                    cell = row.createCell(15);
                    cell.setCellStyle(cellStyle3);
                    cell.setCellType(CellType.FORMULA);
                    cell.setCellFormula("P" + (subtotal_real + 6) + "*0.1");
                    break;
            }
        }

        FileOutputStream fileOut;
        try {
            fileOut = new FileOutputStream("result.xls");
            workbook.write(fileOut);
            fileOut.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        File file = new File("result.xls");
        HttpHeaders headers = new HttpHeaders();
        //下载显示的文件名，解决中文名称乱码问题
        String downloadFielName = "result.xls";
//        String downloadFielName = new String(filename.getBytes("UTF-8"),"iso-8859-1");
        //通知浏览器以attachment（下载方式）打开图片
        headers.setContentDispositionFormData("attachment", downloadFielName);
        //application/octet-stream ： 二进制流数据（最常见的文件下载）。
        headers.setContentType(MediaType.parseMediaType("application/vnd.ms-excel;charset=UTF-8"));
        try {
            return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(file),
                    headers, HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void upload(MultipartFile file) {
        String fileRealName = file.getOriginalFilename();//获得原始文件名;
        int pointIndex =  fileRealName.lastIndexOf(".");//点号的位置
        String fileSuffix = fileRealName.substring(pointIndex);//截取文件后缀
        String fileNewName = "factor";//新文件名,时间戳形式yyyyMMddHHmmssSSS
        String saveFileName = fileNewName.concat(fileSuffix);//新文件完整名（含后缀）
        File savedFile = new File(saveFileName);
        try {
            FileUtils.copyInputStreamToFile(file.getInputStream(),savedFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        FileInputStream fis = null;
        File f = new File(saveFileName);
        if (fileSuffix.equals(".xls")) {
            HSSFWorkbook workbook = null;
            try {
                fis = new FileInputStream(f);
                workbook = new HSSFWorkbook(fis);
            } catch (Exception e) {
                System.out.println(e);
            } finally {
                if (fis != null) {
                    try {
                        fis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            Sheet sheet = workbook.getSheetAt(0);
            int rowNum = sheet.getLastRowNum();
            List<ItemPrice> itemLists = new ArrayList<ItemPrice>();

            Connection conn = null;
            Statement statement = null;
            ResultSet rs = null;
            String url = "jdbc:mysql://localhost:3306/price?useSSL=false&serverTimezone=UTC";
            String user = "root";
            String password = "zwbzwj123";

            String factor_data = sheet.getRow(1).getCell(1).toString() + "__";
            for (int rowIndex = 1; rowIndex < rowNum + 1; rowIndex++) {
                Row row = sheet.getRow(rowIndex);
                String startEndIndex = row.getCell(0).toString();
                String factor = row.getCell(1).toString();
                try {
                    Class.forName("com.mysql.jdbc.Driver");
                    conn = DriverManager.getConnection(url, user, password);
                    statement = conn.createStatement();
                    String sql = null;
                    if (startEndIndex.contains("___")) {
                        String startIndex = startEndIndex.split("___")[0];
                        String endIndex = startEndIndex.split("___")[1];
                        sql = "update cost set factor=concat(IFNULL(factor,''),'" + factor_data + new Float(factor) + "/') where title_index between '" + startIndex + "' and '"+ endIndex +"'";
                    } else if (startEndIndex.split("-").length == 2 || startEndIndex.split("-").length == 3) {
                        sql = "update cost set factor=concat(IFNULL(factor,''),'" + factor_data + new Float(factor) + "/') where title_index='" + startEndIndex + "'";
                    } else if (rowIndex == 1) {
                        sql = "update cost set factor=concat(IFNULL(factor,''),'" + factor_data + factor + "/') where title_index='" + startEndIndex + "'";
                    } else {
                        ArrayIndexOutOfBoundsException  exception = new ArrayIndexOutOfBoundsException();
                        throw exception;
                    }
                    statement.execute(sql);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (SQLException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (rs != null)
                            rs.close();
                        if (statement != null)
                            statement.cancel();
                        if (conn != null)
                            conn.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        } else {
            XSSFWorkbook workbook = null;
            try {
                fis = new FileInputStream(f);
                workbook = new XSSFWorkbook(fis);
            } catch (Exception e) {
                System.out.println(e);
            } finally {
                if (fis != null) {
                    try {
                        fis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            Sheet sheet = workbook.getSheetAt(0);
            int rowNum = sheet.getLastRowNum();
            List<ItemPrice> itemLists = new ArrayList<ItemPrice>();

            Connection conn = null;
            Statement statement = null;
            ResultSet rs = null;
            String url = "jdbc:mysql://localhost:3306/price?useSSL=false&serverTimezone=UTC";
            String user = "root";
            String password = "zwbzwj123";

            String factor_data = sheet.getRow(1).getCell(1).toString() + "__";
            for (int rowIndex = 1; rowIndex < rowNum + 1; rowIndex++) {
                Row row = sheet.getRow(rowIndex);
                String startEndIndex = row.getCell(0).toString();
                String factor = row.getCell(1).toString();
                try {
                    Class.forName("com.mysql.jdbc.Driver");
                    conn = DriverManager.getConnection(url, user, password);
                    statement = conn.createStatement();
                    String sql = null;
                    if (startEndIndex.contains("___")) {
                        String startIndex = startEndIndex.split("___")[0];
                        String endIndex = startEndIndex.split("___")[1];
                        sql = "update cost set factor=concat(IFNULL(factor,''),'" + factor_data + new Float(factor) + "/') where title_index between '" + startIndex + "' and '"+ endIndex +"'";
                    } else if (startEndIndex.split("-").length == 2 || startEndIndex.split("-").length == 3) {
                        sql = "update cost set factor=concat(IFNULL(factor,''),'" + factor_data + new Float(factor) + "/') where title_index='" + startEndIndex + "'";
                    } else if (rowIndex == 1) {
                        sql = "update cost set factor=concat(IFNULL(factor,''),'" + factor + "/') where title_index='" + startEndIndex + "'";
                    } else {
                        ArrayIndexOutOfBoundsException  exception = new ArrayIndexOutOfBoundsException();
                        throw exception;
                    }
                    statement.execute(sql);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (SQLException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (rs != null)
                            rs.close();
                        if (statement != null)
                            statement.cancel();
                        if (conn != null)
                            conn.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    @Override
    public void setFactorDate(String factorDate, String username) {
        Connection conn = null;
        Statement statement = null;
        ResultSet rs = null;
        String url = "jdbc:mysql://localhost:3306/price?useSSL=false&serverTimezone=UTC";
        String user = "root";
        String sqlPassword = "zwbzwj123";

        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try {
            conn = DriverManager.getConnection(url, user, sqlPassword);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            statement = conn.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        String sql = "update user set factor_date='" + factorDate + "' where user_name='" + username + "'";
        try {
            statement.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            statement.cancel();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void removeFactorDate(String factorDate) {
        Connection conn = null;
        Statement statement1 = null;
        Statement statement2 = null;
        ResultSet rs1 = null;
        String url = "jdbc:mysql://localhost:3306/price?useSSL=false&serverTimezone=UTC";
        String user = "root";
        String sqlPassword = "zwbzwj123";

        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try {
            conn = DriverManager.getConnection(url, user, sqlPassword);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            statement1 = conn.createStatement();
            statement2 = conn.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        String sql1 = "select factor from cost";
        String factorDateNew = null;
        try {
            rs1 = statement1.executeQuery(sql1);
            while (rs1.next()) {
                String factorOld = rs1.getString("factor");
                String factorNew = null;
                String regex = factorDate + ".*?" + "/";
                if (factorOld != null) {
                    factorNew = factorOld.replaceFirst(regex, "");
                    String sql2 = "update cost set factor='" + factorNew + "' where factor='" + factorOld + "'";
                    assert statement2 != null;
                    statement2.execute(sql2);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            rs1.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            statement1.cancel();
            statement2.cancel();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public String login(String username, String password) {
        Connection conn = null;
        Statement statement = null;
        ResultSet rs = null;
        String url = "jdbc:mysql://localhost:3306/price?useSSL=false&serverTimezone=UTC";
        String user = "root";
        String sqlPassword = "zwbzwj123";

        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try {
            conn = DriverManager.getConnection(url, user, sqlPassword);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            statement = conn.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        String sql = "select * from user where user_name like '"+ username + "%'";
        String factorDate = null;
        try {
            rs = statement.executeQuery(sql);
            while (rs.next()) {
                if (password.equals(rs.getString("password"))) {
                    factorDate = rs.getString("factor_date");
                    if (username.equals("yssh")) {
                        return factorDate + ";true";
                    } else {
                        return factorDate + ";false";
                    }

                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            statement.cancel();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "false";
    }

    @Override
    public String register(String username, String password, String code) {
        Connection conn = null;
        Statement statement = null;
        ResultSet rs = null;
        String url = "jdbc:mysql://localhost:3306/price?useSSL=false&serverTimezone=UTC";
        String user = "root";
        String sqlPassword = "zwbzwj123";

        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try {
            conn = DriverManager.getConnection(url, user, sqlPassword);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            statement = conn.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (code.equals("yxz")) {
            if (queryUserName(username, conn)){
                return "用户名已存在！";
            }
            String sql = "insert into user (user_name,password) select '"+
                    username + "','" + password + "' from dual" +
                    " where not exists (select user_name from user where user_name='" + username + "' )";
            try {
                statement.execute(sql);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                statement.cancel();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return "true";
        }
        return "false";
    }

    private Boolean queryUserName (String username, Connection conn) {
        Statement statement = null;
        ResultSet rs = null;
        try {
            statement = conn.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        String sql = "select user_name from user where user_name='" + username + "'";
        try {
            rs = statement.executeQuery(sql);
            while(rs.next()) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            statement.cancel();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
