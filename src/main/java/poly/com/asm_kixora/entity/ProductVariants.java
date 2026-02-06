package poly.com.asm_kixora.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "ProductVariants")
public class ProductVariants {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Thêm dòng này để tự tăng ID
    @Column(name = "Id")
    private Integer id;

    @Column(name = "Size")
    private String size;

    @Column(name = "Color")
    private String color;

    @Column(name = "Quantity")
    private Integer quantity;


    @ManyToOne
    @JoinColumn(name = "ProductId")
    private Products product;


    @Column(name = "CreatedDate")
    private LocalDateTime createdDate;
}