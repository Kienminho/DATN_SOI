package com.mid_term.springecommerce.Services;

import com.itextpdf.html2pdf.HtmlConverter;
import com.mid_term.springecommerce.DTO.InvoiceRequest;
import com.mid_term.springecommerce.Models.Entity.Invoice;
import com.mid_term.springecommerce.Models.Entity.StaffAndShipper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.UUID;

@Service
public class CommonService {
    private final TemplateEngine templateEngine;

    public CommonService(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    public String printInvoice(Invoice invoice, StaffAndShipper u, InvoiceRequest req, String outputDirectory) {
        // Print the invoice
        Context thymeleafContext = new Context();

        //add data
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        thymeleafContext.setVariable("invoice", invoice.getId());
        thymeleafContext.setVariable("customerName", req.getFullName());
        thymeleafContext.setVariable("salesStaffName", u.getFullName());
        thymeleafContext.setVariable("address", req.getAddress());
        thymeleafContext.setVariable("createdAt", formatter.format(invoice.getCreatedDate()));
        thymeleafContext.setVariable("totalPrice", req.getTotalMoney());
        thymeleafContext.setVariable("totalProducts", req.getQuantity());
        thymeleafContext.setVariable("receiveMoney", req.getReceiveMoney());
        thymeleafContext.setVariable("excessMoney", req.getMoneyBack());
        thymeleafContext.setVariable("products", req.getItems());
        String processedHtml = templateEngine.process("invoice", thymeleafContext);
        String outputPath = outputDirectory + File.separator + invoice.getInvoiceCode()+".pdf";
        try (FileOutputStream fileOutputStream = new FileOutputStream(outputPath)) {
            //fileOutputStream.write(processedHtml.getBytes(StandardCharsets.UTF_8));
            HtmlConverter.convertToPdf(processedHtml, fileOutputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return outputPath;
    }
    private static String downloadAndSaveImage(String imageUrl) {
        try {
            // Download image
            URL url = new URL(imageUrl);
            String fileName = UUID.randomUUID().toString() + ".jpg"; // Generate a unique file name
            File imageFile = new File("upload/" + fileName);

            // Create parent directories if they don't exist
            imageFile.getParentFile().mkdirs();

            // Open an input stream to the URL and copy it to the file
            try (InputStream inputStream = url.openStream()) {
                Files.copy(inputStream, imageFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            }

            // Return the path to the saved image
            return "images/" + fileName;
        } catch (IOException e) {
            // Handle the exception (log or throw a custom exception)
            e.printStackTrace();
            return null; // Return null to indicate failure
        }
    }

    private int parsePrice(String price) {
        try {
            // Remove non-numeric characters and parse to int
            String numericPrice = price.replaceAll("[^0-9]", "");
            return Integer.parseInt(numericPrice);
        } catch (NumberFormatException e) {
            // Handle the exception (log or throw a custom exception)
            e.printStackTrace();
            return 0; // Return 0 to indicate failure
        }
    }
}
