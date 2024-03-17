package com.mid_term.springecommerce.Repositorys;

import com.mid_term.springecommerce.DTO.InvoiceResponse;
import com.mid_term.springecommerce.Models.Entity.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface InvoiceRepository extends JpaRepository<Invoice, Long> {

    @Query("SELECT new com.mid_term.springecommerce.DTO.InvoiceResponse(i.invoiceCode,u.fullName, i.nameCustomer,i.receiveMoney," +
            "i.excessMoney,i.totalMoney,i.quantity,i.createdDate) " +
            "FROM Invoice i JOIN i.salesStaff u WHERE i.createdDate >= :startDate AND i.createdDate < :endDate order by i.createdDate desc")
    List<InvoiceResponse> findDateByDate(@Param("startDate") Date startDate, @Param("endDate") Date endDate);


}
