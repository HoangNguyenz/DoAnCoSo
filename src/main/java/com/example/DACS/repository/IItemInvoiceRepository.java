package com.example.DACS.repository;
import com.example.DACS.entity.ItemInvoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IItemInvoiceRepository extends  JpaRepository<ItemInvoice, Long>{
}
