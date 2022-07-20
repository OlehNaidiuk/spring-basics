package com.naidiuk.webJdbcJsonWithSpring.services;

import com.naidiuk.webJdbcJsonWithSpring.entity.User;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Component
public class ExcelServiceImpl implements ExcelService {
    @Override
    public List<User> read(MultipartFile file) {
        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {

            Sheet firstSheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = firstSheet.iterator();
            rowIterator.next();

            List<User> users = new ArrayList<>();

            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                Iterator<Cell> cellIterator = row.cellIterator();
                User user = new User();

                while (cellIterator.hasNext()) {
                    Cell cell = cellIterator.next();
                    int columnIndex = cell.getColumnIndex();

                    switch (columnIndex) {
                        case 0:
                            user.setId((int) cell.getNumericCellValue());
                            break;
                        case 1:
                            user.setName(cell.getStringCellValue());
                            break;
                        case 2:
                            user.setSurname(cell.getStringCellValue());
                            break;
                        case 3:
                            user.setSalary((int) cell.getNumericCellValue());
                            break;
                        case 4:
                            user.setWorkExperienceYears((int) cell.getNumericCellValue());
                            break;
                        case 5:
                            user.setEmail(cell.getStringCellValue());
                            break;
                        default:
                            break;
                    }
                }
                users.add(user);
            }
            return users;
        } catch (IOException e) {
            throw new RuntimeException("Can't read this file.");
        }
    }

    @Override
    public File write(List<User> users) {
        File file = new File("src/main/resources/Users_info.xlsx");

        try (OutputStream outputStream = new FileOutputStream(file)) {
            XSSFWorkbook usersWorkbook = createWorkbook(users);
            usersWorkbook.write(outputStream);
            usersWorkbook.close();
            return file;
        } catch (IOException e) {
            throw new RuntimeException("Can't write data to file", e);
        }
    }

    private XSSFWorkbook createWorkbook(List<User> users) {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Users");
        XSSFFont font = workbook.createFont();
        CellStyle cellStyle = workbook.createCellStyle();

        int rowNum = 0;
        Row headers = sheet.createRow(rowNum);

        font.setFontHeight((short) 14);

        cellStyle.setFont(font);
        cellStyle.setAlignment(HorizontalAlignment.CENTER);

        Cell cell;
        for (int index = 0; index < 6; index++) {
            sheet.autoSizeColumn(index);
            cell = headers.createCell(index);
            cell.setCellStyle(cellStyle);

            switch (index) {
                case 0:
                    cell.setCellValue("Id");
                    break;
                case 1:
                    cell.setCellValue("Name");
                    break;
                case 2:
                    cell.setCellValue("Surname");
                    break;
                case 3:
                    cell.setCellValue("Salary");
                    break;
                case 4:
                    cell.setCellValue("Work experience years");
                    break;
                case 5:
                    cell.setCellValue("Email");
                    break;
                default:
                    break;
            }
        }

        for (User user: users) {
            rowNum++;
            Row row = sheet.createRow(rowNum);

            for (int index = 0; index < 6; index++) {
                cell = row.createCell(index);
                cell.setCellStyle(cellStyle);

                switch (index) {
                    case 0:
                        cell.setCellValue(user.getId());
                        break;
                    case 1:
                        cell.setCellValue(user.getName());
                        break;
                    case 2:
                        cell.setCellValue(user.getSurname());
                        break;
                    case 3:
                        cell.setCellValue(user.getSalary());
                        break;
                    case 4:
                        cell.setCellValue(user.getWorkExperienceYears());
                        break;
                    case 5:
                        cell.setCellValue(user.getEmail());
                        break;
                    default:
                        break;
                }
            }
        }
        return workbook;
    }
}
