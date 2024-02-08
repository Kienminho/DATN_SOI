package com.mid_term.springecommerce.Services;

import com.mid_term.springecommerce.Models.Entity.Category;
import com.mid_term.springecommerce.Models.Entity.Product;
import com.mid_term.springecommerce.Models.ResponseModel.CategoryResponse;
import org.apache.poi.ooxml.POIXMLProperties;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

@Service
public class ExcelExportService {
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    public void exportProductsToExcel(List<Product> products, ByteArrayOutputStream outputStream) {
        try (Workbook workbook = new XSSFWorkbook()) {
            POIXMLProperties properties = ((XSSFWorkbook) workbook).getProperties();
            POIXMLProperties.CoreProperties coreProperties = properties.getCoreProperties();
            coreProperties.setTitle("Category statistics");
            coreProperties.setCreator("SOI");
            coreProperties.setDescription("Đồ án tốt nghiệp của SOI.");
            Sheet sheet = workbook.createSheet("Products");
            // Create header row
            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("Mã");
            headerRow.createCell(1).setCellValue("Tên sản phẩm");
            headerRow.createCell(2).setCellValue("Mô tả");
            headerRow.createCell(3).setCellValue("Ngày nhập");
            headerRow.createCell(4).setCellValue("Giá nhập");
            headerRow.createCell(5).setCellValue("Giá bán");
            headerRow.createCell(6).setCellValue("Số lượng hiện có");
            headerRow.createCell(7).setCellValue("Số lượng bán ra");
            headerRow.createCell(8).setCellValue("Loại sản phẩm");

            // Set specific width for header columns
            //sheet.setColumnWidth(0, 3000);
            sheet.setColumnWidth(1, 6000);
            sheet.setColumnWidth(3, 5500);
            sheet.setColumnWidth(4, 5500);
            sheet.setColumnWidth(2, 10000);
            sheet.setColumnWidth(6, 6000);
            sheet.setColumnWidth(7, 6000);
            sheet.setColumnWidth(8, 6000);

            customStyle(workbook, headerRow);
            // Populate data rows
            int rowNum = 1;
            for (Product product : products) {
                String formattedDate = "";
                if (product.getCreatedDate() != null) {
                    formattedDate = dateFormat.format(product.getCreatedDate());
                }
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(product.getId());
                row.createCell(1).setCellValue(product.getName());
                row.createCell(2).setCellValue(product.getDescription());
                row.createCell(3).setCellValue(formattedDate);
                row.createCell(4).setCellValue(product.getImportPrice());
                row.createCell(5).setCellValue(product.getSalePrice());
                row.createCell(6).setCellValue(product.getCurrentQuantity());
                row.createCell(7).setCellValue(product.getSaleNumber());
                row.createCell(8).setCellValue(product.getCategory().getName());
                // Add other columns as needed
            }

            formatFile(sheet, workbook, 8);
            // Write workbook to ByteArrayOutputStream
            workbook.write(outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void exportCategoryToExcel(List<CategoryResponse> categories, ByteArrayOutputStream outputStream) {
        try (Workbook workbook = new XSSFWorkbook()) {
            POIXMLProperties properties = ((XSSFWorkbook) workbook).getProperties();
            POIXMLProperties.CoreProperties coreProperties = properties.getCoreProperties();
            coreProperties.setTitle("Category statistics");
            coreProperties.setCreator("SOI");
            coreProperties.setDescription("Đồ án tốt nghiệp của SOI.");

            Sheet sheet = workbook.createSheet("Categories");
            // Create header row
            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("Mã");
            headerRow.createCell(1).setCellValue("Tên loại sản phẩm");
            headerRow.createCell(2).setCellValue("Mô tả");
            headerRow.createCell(3).setCellValue("Số lượng sản phẩm");
            headerRow.createCell(4).setCellValue("Ngày tạo");

            sheet.setColumnWidth(2, 15000);
            sheet.setColumnWidth(3, 6000);
            sheet.setColumnWidth(4, 4000);

            // Set specific width for header columns
            sheet.setColumnWidth(1, 6000);
            customStyle(workbook, headerRow);
            // Populate data rows
            int rowNum = 1;
            for (CategoryResponse category : categories) {
                String formattedDate = "";
                if (category.getCreatedDate() != null) {
                    formattedDate = dateFormat.format(category.getCreatedDate());
                }
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(category.getId());
                row.createCell(1).setCellValue(category.getCategoryName());
                row.createCell(2).setCellValue(category.getDescription());
                row.createCell(3).setCellValue(category.getTotalProducts());
                row.createCell(4).setCellValue(formattedDate);
            }

            formatFile(sheet, workbook ,7);
            // Write workbook to ByteArrayOutputStream
            workbook.write(outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void customStyle(Workbook workbook, Row headerRow) {

        // Create cell style
        // Apply cell styles to header cells
        CellStyle headerCellStyle = workbook.createCellStyle();
        headerCellStyle.setFillForegroundColor(IndexedColors.ORANGE.getIndex());
        headerCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        Font headerFont = workbook.createFont();
        headerFont.setColor(IndexedColors.BLACK.getIndex());
        headerFont.setFontHeightInPoints((short) 14);
        headerFont.setBold(true);
        headerCellStyle.setFont(headerFont);

        for (int i = 0; i < headerRow.getLastCellNum(); i++) {
            headerRow.getCell(i).setCellStyle(headerCellStyle);
        }

        // Set specific height for header row
        headerRow.setHeightInPoints(30);
    }

    private void formatFile(Sheet sheet, Workbook workbook, int index) {
        // Shift rows down by 3 positions
        sheet.shiftRows(0, sheet.getLastRowNum(), 4, true, false);

        // Create rows with larger font size and bold text
        CellStyle boldStyle = workbook.createCellStyle();
        Font boldFont = workbook.createFont();
        boldFont.setBold(true);
        boldFont.setFontHeightInPoints((short) 11); // Set the font size to 14
        boldStyle.setFont(boldFont);

        Row companyNameRow = sheet.createRow(0);
        Cell companyNameCell = companyNameRow.createCell(0);
        companyNameCell.setCellValue("Company name");
        companyNameCell.setCellStyle(boldStyle);

        Row addressRow = sheet.createRow(1);
        Cell addressCell = addressRow.createCell(0);
        addressCell.setCellValue("Địa chỉ");
        addressCell.setCellStyle(boldStyle);

        Row phoneNumberRow = sheet.createRow(2);
        Cell phoneNumberCell = phoneNumberRow.createCell(0);
        phoneNumberCell.setCellValue("Số điện thoại");
        phoneNumberCell.setCellStyle(boldStyle);

        Row taxCodeRow = sheet.createRow(3);
        Cell taxCodeCell = taxCodeRow.createCell(0);
        taxCodeCell.setCellValue("Số hiệu thuế");
        taxCodeCell.setCellStyle(boldStyle);


        // Merge cells for signature and date
        sheet.addMergedRegion(new CellRangeAddress(sheet.getLastRowNum() + 1, sheet.getLastRowNum() + 1, 0, 2));
        sheet.addMergedRegion(new CellRangeAddress(sheet.getLastRowNum() + 1, sheet.getLastRowNum() + 1, 3, 5));

        // Add additional rows at the end for signing
        CellStyle centerAlignStyle = workbook.createCellStyle();
        centerAlignStyle.setAlignment(HorizontalAlignment.CENTER);

        Row signatureRow = sheet.createRow(sheet.getLastRowNum() + 3);
        signatureRow.createCell(index).setCellValue("Hà Nội, ngày ... tháng ... năm ....");
        Row fullNameRow = sheet.createRow(sheet.getLastRowNum() + 1);
        fullNameRow.createCell(8).setCellValue("Họ và tên");
        fullNameRow.getCell(8).setCellStyle(centerAlignStyle);
        Row fullNameBracketRow = sheet.createRow(sheet.getLastRowNum() + 1);
        fullNameBracketRow.createCell(8).setCellValue("(Ký, ghi rõ họ tên)");
        fullNameBracketRow.getCell(8).setCellStyle(centerAlignStyle);
    }
}
