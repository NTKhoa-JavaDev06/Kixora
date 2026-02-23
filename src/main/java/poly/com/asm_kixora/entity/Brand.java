package poly.com.asm_kixora.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "Brand")
public class Brand {
    @Id
    @Column(name = "Id")
    private Integer id;

    @Column(name = "Name")
    private String name;

    @Column(name = "Logo")
    private String logo;

    @Column(name = "IsActive")
    private Boolean isActive;

    @Column(name = "DisplayOrder")
    private Integer displayOrder;

    @Column(name = "CreatedDate")
    private LocalDateTime createdDate;
}
