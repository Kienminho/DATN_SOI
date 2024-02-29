package com.mid_term.springecommerce.Repositorys;

import com.mid_term.springecommerce.DTO.InvoiceItemResponse;
import com.mid_term.springecommerce.Models.Entity.InvoiceItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface InvoiceItemRepository extends JpaRepository<InvoiceItem, Long> {
    @Query("SELECT new com.mid_term.springecommerce.DTO.InvoiceItemResponse(inv.invoiceCode,p.name,i.quantity,i.unitPrice, i.createdDate) FROM InvoiceItem i JOIN i.invoice inv JOIN i.product p WHERE inv.invoiceCode=:id")
    List<InvoiceItemResponse> getDetailInvoice(@Param("id") String id);
}
