package poly.com.asm_kixora.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "Products")
public class Products {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    private String material;
    private String gender;
    private BigDecimal price;
    private String image;
    private Boolean available;

    @Column(name = "CreatedDate")
    private LocalDateTime createdDate;

    // Quan hệ với Category (Bạn đã làm)
    @ManyToOne
    @JoinColumn(name = "CategoryId")
    private Categories category;

    // Quan hệ với Brand (Mới dựa trên DB)
    @ManyToOne
    @JoinColumn(name = "BrandId")
    private Brand brand;
}