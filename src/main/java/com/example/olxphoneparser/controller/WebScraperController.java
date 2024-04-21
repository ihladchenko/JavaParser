package com.example.olxphoneparser.controller;

import com.example.olxphoneparser.model.ExcelFile;
import com.example.olxphoneparser.repository.ExcelFileRepository;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Controller
public class WebScraperController {

    @Autowired
    private ExcelFileRepository excelFileRepository;

    @GetMapping("/scrape")
    public ModelAndView scrape(@RequestParam(name = "searchTerm", defaultValue = "смартфон") String searchTerm) throws IOException {
        // Парсинг olx.ua
        String url = "https://www.olx.ua/elektronika/telefony-i-aksesuary/mobilnye-telefony-smartfony/q-" + searchTerm;
        Document document = Jsoup.connect(url).get();

        // Створення Excel файла
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Phones");

        // Додавання заголовків
        Row headerRow = sheet.createRow(0);
        String[] columns = {"Link", "Internal Number", "Link on Site", "Description", "Price"};
        for (int i = 0; i < columns.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(columns[i]);
        }

        // Заповнення даних
        Elements phoneElements = document.select(".offer-wrapper");
        int rowNum = 1;
        for (Element phoneElement : phoneElements) {
            Element linkElement = phoneElement.selectFirst(".detailsLink");
            String link = linkElement.absUrl("href");

            Element idElement = phoneElement.selectFirst(".bottom-cell");
            String id = idElement.attr("data-id");

            Element descriptionElement = phoneElement.selectFirst(".title-cell");
            String description = descriptionElement.text();

            Element priceElement = phoneElement.selectFirst(".price");
            String price = priceElement.text();

            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(link);
            row.createCell(1).setCellValue(id);
            row.createCell(2).setCellValue(link);
            row.createCell(3).setCellValue(description);
            row.createCell(4).setCellValue(price);
        }

        // Конвертація в ByteArrayOutputStream
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        byte[] fileContent = outputStream.toByteArray();

        // Зберігання в базі даних
        ExcelFile excelFile = new ExcelFile();
        excelFile.setFileName("phones.xlsx");
        excelFile.setData(fileContent);
        excelFileRepository.save(excelFile);

        ModelAndView modelAndView = new ModelAndView("index");
        modelAndView.addObject("searchTerm", searchTerm);
        return modelAndView;
    }
}