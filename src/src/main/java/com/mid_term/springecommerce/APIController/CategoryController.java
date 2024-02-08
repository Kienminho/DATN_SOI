package com.mid_term.springecommerce.APIController;

import com.mid_term.springecommerce.Models.Entity.Category;
import com.mid_term.springecommerce.Models.Entity.Product;
import com.mid_term.springecommerce.Models.RequestModel.CategoryRequest;
import com.mid_term.springecommerce.Models.ResponseModel.CategoryResponse;
import com.mid_term.springecommerce.Repositorys.CategoryRepository;
import com.mid_term.springecommerce.Repositorys.ProductRepository;
import com.mid_term.springecommerce.Services.ExcelExportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {
    @Autowired
    private ExcelExportService excelExportService;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductRepository productRepository;

    @GetMapping("get-list-category")
    public Object getAllCategory() {
        List<CategoryResponse> data = categoryRepository.getCategoryResponses();
        return Response.createSuccessResponseModel(data.size(), data);
    }

    @GetMapping("export-category-excel")
    public Object exportCategory() {
        List<CategoryResponse> categories = categoryRepository.getCategoryResponses();
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            excelExportService.exportCategoryToExcel(categories, outputStream);
            byte[] excelBytes = outputStream.toByteArray();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
            headers.setContentDispositionFormData("attachment", "categories.xlsx");
            headers.setContentLength(excelBytes.length);

            return new ResponseEntity<>(excelBytes, headers, HttpStatus.OK);
        } catch (IOException e) {
            System.out.println("CategoryController -> exportCategory: " + e.getMessage());
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value = "add-category",consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public Object addCategory(@ModelAttribute CategoryRequest req) {
        try {
            Category c = new Category();
            c.setName(req.getName());
            c.setDescription(req.getDescription());
            categoryRepository.save(c);
            return Response.createSuccessResponseModel(0, c);
        }
        catch (Exception e) {
            System.out.println("CategoryController -> addCategory: " + e.getMessage());
            return Response.createErrorResponseModel(e.getMessage(), false);
        }
    }

    @PutMapping(value = "update-category",consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public Object updateCategory(@ModelAttribute CategoryRequest req) {
        try {
            Category c = categoryRepository.getReferenceById(req.getId());
            c.setName(req.getName());
            c.setDescription(req.getDescription());
            categoryRepository.save(c);
            return Response.createSuccessResponseModel(0, true);
        }
        catch (Exception e) {
            System.out.println("CategoryController -> updateCategory: " + e.getMessage());
            return Response.createErrorResponseModel(e.getMessage(), false);
        }
    }

    @DeleteMapping("delete-category/{id}")
    public Object deleteCategory(@PathVariable Long id) {
        try {
            Category c = categoryRepository.getReferenceById(id);
            List<Product> listProduct = productRepository.getProductsByCategory(c);
            if (!listProduct.isEmpty()) {
                return Response.createErrorResponseModel("Danh mục đang có chứa sản phẩm, không thể xoá", false);
            }
            c.set_deleted(true);
            categoryRepository.save(c);
            return Response.createSuccessResponseModel(0, true);
        }
        catch (Exception e) {
            System.out.println("CategoryController -> deleteCategory: " + e.getMessage());
            return Response.createErrorResponseModel(e.getMessage(), false);
        }
    }

}

