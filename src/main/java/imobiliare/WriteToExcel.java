package imobiliare;

import imobiliare.enums.PageType;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Deque;

import static imobiliare.result.Result.*;

public class WriteToExcel {

    public static final String PATH_TO_OUTPUT_FILE = "C:\\Users\\vancer\\Desktop\\Docs\\ImobiliarePret.xlsx";

    public static void writeToExcel(Deque<Object> newEntry, PageType pageType) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd - HH:mm");
        String currentTime = LocalDateTime.now().format(formatter);

        newEntry.push(currentTime);
        try {
            FileInputStream inputStream = new FileInputStream(new File(PATH_TO_OUTPUT_FILE));
            Workbook workbook = WorkbookFactory.create(inputStream);

            Sheet sheet = workbook.getSheetAt(pageType.ordinal());

            final Object[] arrayOfNewEntry = newEntry.toArray();

            ANUNTURI_PROCESATE = ANUNTURI_PROCESATE + (Integer) arrayOfNewEntry[2];

            Object[][] bookData = {arrayOfNewEntry};

            int rowCount = sheet.getLastRowNum();

            for (Object[] aBook : bookData) {
                Row row = sheet.createRow(++rowCount);

                int columnCount = 0;

                Cell cell = row.createCell(columnCount);
                cell.setCellValue(rowCount);

                for (Object field : aBook) {
                    cell = row.createCell(++columnCount);
                    if (field instanceof String) {
                        cell.setCellValue((String) field);
                    } else if (field instanceof Integer) {
                        cell.setCellValue((Integer) field);
                    } else if (field instanceof Double) {
                        cell.setCellValue((double) field);
                    }
                }
            }

            inputStream.close();

            FileOutputStream outputStream = new FileOutputStream(PATH_TO_OUTPUT_FILE);
            workbook.write(outputStream);
            workbook.close();
            outputStream.close();

        } catch (IOException | EncryptedDocumentException | InvalidFormatException ex) {
            ex.printStackTrace();
        }
    }
}
