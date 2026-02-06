package poly.com.asm_kixora.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Categories")
public class Categories {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Tự động tăng ID theo DB
    @Column(name = "Id")
    private Integer id;

    @Column(name = "Name")
    private String name;

    @Column(name = "CreatedDate")
    private LocalDateTime createdDate = LocalDateTime.now();

    // (Tùy chọn) Nếu bạn muốn từ 1 Category lấy được danh sách Products thuộc về nó
    @OneToMany(mappedBy = "category")
    private List<Products> products;
}