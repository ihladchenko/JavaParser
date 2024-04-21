package com.example.olxphoneparser;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileOutputStream;
import java.io.IOException;

@RestController
public class WebScraperController {

    @GetMapping("/scrape")
    public String scrape(@RequestParam String searchTerm) throws IOException {
        Document doc = Jsoup.connect("https://www.olx.ua/elektronika/telefony-i-aksesuary/mobilnye-telefony-smartfony/q-" + searchTerm).get();
        Elements items = doc.select("a.detailsLink");

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Smartphones");

        int rowNum = 0;
        Row row = sheet.createRow(rowNum++);

        row.createCell(0).setCellValue("Посилання на товар");
        row.createCell(1).setCellValue("Внутрішній номер товара");
        row.createCell(2).setCellValue("Лінк на товар на сайті");
        row.createCell(3).setCellValue("Короткий опис");
        row.createCell(4).setCellValue("Ціна");

        for (Element item : items) {
            String link = item.absUrl("href");
            String title = item.attr("title");
            String id = item.attr("data-id");
            String price = item.select(".price").text();

            row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(link);
            row.createCell(1).setCellValue(id);
            row.createCell(2).setCellValue(title);
            row.createCell(3).setCellValue(searchTerm);
            row.createCell(4).setCellValue(price);
        }

        try (FileOutputStream fos = new FileOutputStream("smartphones.xlsx")) {
            workbook.write(fos);
        }

        return "Data scraped and saved to smartphones.xlsx";
    }
}
