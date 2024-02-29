package com.mid_term.springecommerce.APIController;

import com.mid_term.springecommerce.DTO.CartRequest;
import com.mid_term.springecommerce.DTO.InvoiceItemResponse;
import com.mid_term.springecommerce.DTO.InvoiceRequest;
import com.mid_term.springecommerce.DTO.InvoiceResponse;
import com.mid_term.springecommerce.Models.Entity.*;
import com.mid_term.springecommerce.Repositorys.*;
import com.mid_term.springecommerce.Services.CommonService;
import com.mid_term.springecommerce.Utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    @Autowired
    private OrderRepository  orderRepository;

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private StaffAndShipperRepository staffAndShipperRepository;

    @Autowired
    private InvoiceItemRepository invoiceItemRepository;

    @Autowired
    private CommonService commonService;

    @Value("${pdf.outputDirectory}")
    private String outputDirectory;

    @GetMapping("/get-list")
    public Object getListOrder(@RequestParam(value = "keyword", defaultValue = "") String keyword){
        try {
            List<Order> data = orderRepository.getAll(keyword);
            return Response.createSuccessResponseModel(data.size(), data);
        } catch (Exception e) {
            return Response.createErrorResponseModel("Vui lòng thử lại sau!", e.getMessage());
        }
    }

    @PostMapping("/get-list-filter")
    public Object getListFilter(@RequestBody Map<String, Date> req) {
        try {
            Date fromDate = req.get("fromDate");
            Date toDate = req.get("toDate");
            List<InvoiceResponse> data = invoiceRepository.findDateByDate(fromDate, toDate);
            return Response.createSuccessResponseModel(data.size(), data);
        }
        catch (Exception ex) {
            System.out.println(ex.getMessage());
            return Response.createErrorResponseModel("Vui lòng thử lại.", ex);
        }
    }

    @GetMapping("/get-list-invoice/{id}")
    public Object getListInvoice(@PathVariable String id) {
        try {
            List<InvoiceItemResponse> data = invoiceItemRepository.getDetailInvoice(id);
            return Response.createSuccessResponseModel(data.size(), data);
        } catch (Exception e) {
            return Response.createErrorResponseModel("Vui lòng thử lại sau!", e.getMessage());
        }
    }

    @GetMapping("/export-invoice-by-order/{id}")
    public Object exportInvoiceByOrder(@PathVariable Long id) {
        try {
            Order order = orderRepository.getDetailById(id);
            StaffAndShipper staffAndShipper = staffAndShipperRepository.getUserById(Utils.idUserLogin);
            List<OrderItem> items = orderItemRepository.getDetailByOrderId(id);
            //create invoice
            String invoiceCode = String.valueOf(Utils.generateSixDigitNumber());
            Invoice invoice = new Invoice(invoiceCode, order.getName(), staffAndShipper, order.getTotalPrice(), 0, order.getTotalPrice(), items.size(), new Date());
            invoiceRepository.save(invoice);

            //create invoice items
            List<CartRequest> data = new ArrayList<>();
            for (OrderItem item : items) {
                InvoiceItem i = new InvoiceItem(invoice, item.getProduct(), item.getQuantity(), item.getUnitPrice(), new Date());
                CartRequest c = new CartRequest(item.getProduct().getName(), item.getQuantity(), item.getProduct().getSalePrice(), item.getUnitPrice());
                data.add(c);
                invoiceItemRepository.save(i);
            }

            //print invoice
            InvoiceRequest req = new InvoiceRequest(order.getPhoneNumber(), order.getName(), order.getAddress(), items.size(), order.getTotalPrice(), order.getTotalPrice(), 0, data);
            String output = commonService.printInvoice(invoice, staffAndShipper,req, outputDirectory);
            if (output != null) {
                order.setIssueInvoice(true);
                orderRepository.save(order);
            }

            return Response.createSuccessResponseModel(0, "Xuất hóa đơn thành công! Mã hoá đơn: " + invoiceCode);
        } catch (Exception e) {
            return Response.createErrorResponseModel("Vui lòng thử lại sau!", e.getMessage());
        }
    }

    @GetMapping("/get-detail-order/{id}")
    public Object getDetailOrder(@PathVariable Long id) {
        Order data  = orderRepository.getDetailById(id);
        return Response.createSuccessResponseModel(1, data);
    }
}
