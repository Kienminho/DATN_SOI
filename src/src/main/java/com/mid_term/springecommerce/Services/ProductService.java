package com.mid_term.springecommerce.Services;

import com.mid_term.springecommerce.Repositorys.CategoryRepository;
import com.mid_term.springecommerce.Repositorys.ProductRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    /*public void saveProductsFromJson(String filePath) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            List<ProductDTO> productDTOList = objectMapper.readValue(new File(filePath), new TypeReference<List<ProductDTO>>() {});

            for (ProductDTO productDTO : productDTOList) {
                Product product = new Product();
                product.setName(productDTO.getName());
                product.setDescription("Cam kết chính hãng 100%");
                product.setSalePrice(parsePrice(productDTO.getSalePrice()));
                product.setCurrentQuantity(0);
                product.set_activated(true);
                product.set_deleted(false);

                String imagePath = downloadAndSaveImage(productDTO.getImage());
                product.setImageUrl(imagePath);

                // Check if category already exists or create a new one
                Category category = categoryRepository.findById(3L).orElse(null);

                product.setCategory(category);

                productRepository.save(product);
            }
        } catch (IOException e) {
            // Handle the exception (log or throw a custom exception)
            e.printStackTrace();
        }
    }*/



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
