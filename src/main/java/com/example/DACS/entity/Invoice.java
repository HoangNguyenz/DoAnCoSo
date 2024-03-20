package com.example.DACS.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
import lombok.*;
import org.hibernate.Hibernate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "invoices")
public class Invoice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) @Column(name = "id")
    private Long id;
    @Column(name = "invoice_date")
    private Date invoiceDate = new Date();
    @Column(name = "total")
    @Positive(message = "Total must be positive")
    private Double price;
    @OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL) @ToString.Exclude
    private List<ItemInvoice> itemInvoices = new ArrayList<>();

    @Column(name = "buyer_name")
    private String buyerName;

    @Column(name = "buyer_address")
    private String buyerAddress;

    @Column(name = "buyer_phone")
    private String buyerPhone;

    @Column(name = "buyer_note")
    private String buyerNote;

    @Column(name = "buyer_email")
    private String buyerEmail;

    @Override
    public String toString() {
        return "Invoice{" +
                "id=" + id +
                ", invoiceDate=" + invoiceDate +
                ", price=" + price +
                ", itemInvoices=" + itemInvoices +
                ", buyerName='" + buyerName + '\'' +
                ", buyerAddress='" + buyerAddress + '\'' +
                ", buyerPhone='" + buyerPhone + '\'' +
                ", buyerNote='" + buyerNote + '\'' +
                ", buyerEmail='" + buyerEmail + '\'' +
                '}';
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) !=
                Hibernate.getClass(o)) return false; Invoice invoice = (Invoice) o;
        return getId() != null && Objects.equals(getId(), invoice.getId());
    }
    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
