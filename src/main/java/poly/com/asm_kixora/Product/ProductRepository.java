package poly.com.asm_kixora.Product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import poly.com.asm_kixora.entity.Brand;
import poly.com.asm_kixora.entity.Products;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Products, Integer> {

 @Query("SELECT DISTINCT p FROM Products p WHERE " +
         "(:cids IS NULL OR p.category.id IN :cids) AND " +
         "(:bids IS NULL OR p.brand.id IN :bids) AND " +
         "(:min IS NULL OR p.price >= :min) AND " +
         "(:max IS NULL OR p.price <= :max) AND " +
         "(:kw IS NULL OR p.name LIKE %:kw%) AND p.available = true")
 Page<Products> locSanPhamTrangChu(
         @Param("cids") List<Integer> cids,
         @Param("bids") List<Integer> bids,
         @Param("min") Double min,
         @Param("max") Double max,
         @Param("kw") String kw,
         Pageable pageable
 );

 // 2. Hàm Bán Chạy
 @Query("SELECT p.id FROM Products p " +
         "LEFT JOIN ProductVariants pv ON p.id = pv.product.id " +
         "LEFT JOIN OrderDetails d ON pv.id = d.productVariant.id " +
         "WHERE (:cids IS NULL OR p.category.id IN :cids) " +
         "AND (:bids IS NULL OR p.brand.id IN :bids) " +
         "AND (:min IS NULL OR p.price >= :min) " +
         "AND (:max IS NULL OR p.price <= :max) " +
         "AND (:kw IS NULL OR p.name LIKE %:kw%) " +
         "AND p.available = true " +
         "GROUP BY p.id " +
         "ORDER BY SUM(d.quantity) DESC")
 List<Integer> findTopProductIdsByFilter(
         @Param("cids") List<Integer> cids,
         @Param("bids") List<Integer> bids,
         @Param("min") Double min,
         @Param("max") Double max,
         @Param("kw") String kw);


 @Query("SELECT DISTINCT p FROM Products p WHERE " +
         "(:cids IS NULL OR p.category.id IN :cids) AND " +
         "(:bids IS NULL OR p.brand.id IN :bids) AND " +
         "(:min IS NULL OR p.price >= :min) AND " +
         "(:max IS NULL OR p.price <= :max) AND " +
         "(:kw IS NULL OR p.name LIKE %:kw%) AND p.available = true " +
         "ORDER BY p.price ASC")
 Page<Products> LoctheosotienTangdan(
         @Param("cids") List<Integer> cids,
         @Param("bids") List<Integer> bids,
         @Param("min") Double min,
         @Param("max") Double max,
         @Param("kw") String kw,
         Pageable pageable);


 @Query("SELECT DISTINCT p FROM Products p WHERE " +
         "(:cids IS NULL OR p.category.id IN :cids) AND " +
         "(:bids IS NULL OR p.brand.id IN :bids) AND " +
         "(:min IS NULL OR p.price >= :min) AND " +
         "(:max IS NULL OR p.price <= :max) AND " +
         "(:kw IS NULL OR p.name LIKE %:kw%) AND p.available = true " +
         "ORDER BY p.price DESC")
 Page<Products> LoctheosotienGiamdan(
         @Param("cids") List<Integer> cids,
         @Param("bids") List<Integer> bids,
         @Param("min") Double min,
         @Param("max") Double max,
         @Param("kw") String kw,
         Pageable pageable);

 @Query("SELECT p FROM Products p " +
         "JOIN ProductVariants pv ON p.id = pv.product.id " +
         "JOIN OrderDetails d ON pv.id = d.productVariant.id " +
         "JOIN Orders o ON d.orderId = o.id " +
         "WHERE o.orderDate >= :startDate " +
         "AND p.available = true " +
         "GROUP BY p.id, p.name, p.price, p.image, p.available, p.category, p.brand, " +
         "p.createdDate, p.material, p.gender " +
         "ORDER BY SUM(d.quantity) DESC")
 List<Products> findHotProductsOfMonth(@Param("startDate") java.time.LocalDateTime startDate, Pageable pageable);

 List<Products> findTop6ByCategory_IdOrderByCreatedDateDesc(Integer cid);




 // Trang cho tat ca san pham


 @Query("SELECT DISTINCT p FROM Products p " +
         "LEFT JOIN p.productVariants v " +
         "WHERE (:kw IS NULL OR p.name LIKE %:kw%) " +
         "AND (:sizes IS NULL OR v.size IN :sizes) " +
         "AND (:colors IS NULL OR v.color IN :colors) " +
         "AND (:genders IS NULL OR p.gender IN :genders) " +
         "AND (:materials IS NULL OR p.material IN :materials) " +
         "AND (:cids IS NULL OR p.category.id IN :cids) " + // Thêm cids
         "AND (:bids IS NULL OR p.brand.id IN :bids) " +    // Thêm bids
         "AND (:minPrice IS NULL OR p.price >= :minPrice) " +
         "AND (:maxPrice IS NULL OR p.price <= :maxPrice) " +
         "AND p.available = true")
 Page<Products> findFilteredProducts(
         @Param("sizes") List<String> sizes,
         @Param("colors") List<String> colors,
         @Param("genders") List<String> genders,
         @Param("materials") List<String> materials,
         @Param("kw") String kw,
         @Param("cids") List<Integer> cids,  // Mới thêm
         @Param("bids") List<Integer> bids,  // Mới thêm
         @Param("minPrice") Double minPrice,
         @Param("maxPrice") Double maxPrice,
         Pageable pageable
 );
 @Query("SELECT DISTINCT p.brand FROM Products p WHERE p.brand IS NOT NULL")
 List<Brand> findAllBrands();
 // cái này là product Variable
 @Query("SELECT DISTINCT v.size FROM ProductVariants v WHERE v.size IS NOT NULL ORDER BY v.size ASC")
 List<String> findAllExistingSizes();

 @Query("SELECT DISTINCT v.color FROM ProductVariants v WHERE v.color IS NOT NULL")
 List<String> findAllExistingColors();
 // cái này là của product
 @Query("SELECT DISTINCT p.material FROM Products p WHERE p.material IS NOT NULL")
 List<String> findAllExistingMaterials();

 @Query("SELECT DISTINCT p.gender FROM Products p WHERE p.gender IS NOT NULL")
 List<String> findAllExistingGenders();

}