package poly.com.asm_kixora.Trangchu.HomeController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import poly.com.asm_kixora.Product.BannerReponsitory;
import poly.com.asm_kixora.Product.ProductRepository;
import poly.com.asm_kixora.entity.Banners;
import poly.com.asm_kixora.entity.Products;

import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Controller
public class HomeCtrl {

    private static final int PAGE_SIZE = 9;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private BannerReponsitory bannerRepository;

    private List<Integer> parseIds(Optional<String> raw) {
        if (raw.isPresent() && !raw.get().isEmpty()) {
            return Stream.of(raw.get().split(","))
                    .map(Integer::valueOf)
                    .collect(Collectors.toList());
        }
        return null;
    }
    @ModelAttribute("banners")
    public List<Banners> getBanners() {
        return bannerRepository.findByActiveTrue();
    }

    @ModelAttribute("hotProducts")
    public List<Products> getHotProducts() {
        LocalDateTime firstDayOfMonth = LocalDateTime.now()
                .with(TemporalAdjusters.firstDayOfMonth())
                .withHour(0).withMinute(0).withSecond(0).withNano(0);
        return productRepository.findHotProductsOfMonth(firstDayOfMonth, PageRequest.of(0, 10));
    }

    @GetMapping("/home")
    public String listProducts(Model model,
                               @RequestParam("cid") Optional<String> cid,
                               @RequestParam("bid") Optional<String> bid,
                               @RequestParam("min") Optional<Double> min,
                               @RequestParam("max") Optional<Double> max,
                               @RequestParam("keyword") Optional<String> keyword,
                               @RequestParam("p") Optional<Integer> p) {

        List<Integer> brandIds = parseIds(bid);
        List<Integer> categoryIds = parseIds(cid);

        Pageable pageable = PageRequest.of(p.orElse(0), PAGE_SIZE);
        Page<Products> page = productRepository.locSanPhamTrangChu(
                categoryIds, // Truyền List
                brandIds,    // Truyền List
                min.orElse(null),
                max.orElse(null),
                keyword.orElse(null),
                pageable
        );

        return addPageToModel(model, page, "/home");
    }

    @GetMapping("/product/banchay")
    public String banChay(Model model,
                          @RequestParam("cid") Optional<String> cid,
                          @RequestParam("bid") Optional<String> bid,
                          @RequestParam("min") Optional<Double> min,
                          @RequestParam("max") Optional<Double> max,
                          @RequestParam("keyword") Optional<String> keyword,
                          @RequestParam("p") Optional<Integer> p) {
        List<Integer> brandIds = parseIds(bid);
        List<Integer> categoryIds = parseIds(cid);
        List<Integer> ids = productRepository.findTopProductIdsByFilter(
                categoryIds, brandIds,
                min.orElse(null), max.orElse(null),
                keyword.orElse(null)
        );

        Page<Products> page = buildPageFromIds(ids, p.orElse(0));
        return addPageToModel(model, page, "/product/banchay");
    }


    @GetMapping("/product/loctheogia")
    public String locTheoGia(Model model,
                             @RequestParam("cid") Optional<String> cid,
                             @RequestParam("bid") Optional<String> bid,
                             @RequestParam("min") Optional<Double> min,
                             @RequestParam("max") Optional<Double> max,
                             @RequestParam("keyword") Optional<String> keyword,
                             @RequestParam("sort") Optional<String> sort,
                             @RequestParam("p") Optional<Integer> p) {

        Pageable pageable = PageRequest.of(p.orElse(0), PAGE_SIZE);
        String sortValue = sort.orElse("asc");

        List<Integer> brandIds = parseIds(bid);
        List<Integer> categoryIds = parseIds(cid);

        Page<Products> page = "desc".equalsIgnoreCase(sortValue)
//                ? productRepository.LoctheosotienGiamdan(cid.orElse(null), bid.orElse(null), min.orElse(null), max.orElse(null), keyword.orElse(null), pageable)
//                : productRepository.LoctheosotienTangdan(cid.orElse(null), bid.orElse(null), min.orElse(null), max.orElse(null), keyword.orElse(null), pageable);
                ? productRepository.LoctheosotienGiamdan(categoryIds, brandIds, min.orElse(null), max.orElse(null), keyword.orElse(null), pageable)
                : productRepository.LoctheosotienTangdan(categoryIds, brandIds, min.orElse(null), max.orElse(null), keyword.orElse(null), pageable);

        model.addAttribute("sort", sortValue);
        return addPageToModel(model, page, "/product/loctheogia");
    }


    private String addPageToModel(Model model, Page<Products> page, String currentUri) {
        model.addAttribute("page", page);
        model.addAttribute("items", page.getContent());
        model.addAttribute("currentUri", currentUri);
        return "Home";
    }

    private Page<Products> buildPageFromIds(List<Integer> ids, int p) {
        if (ids == null || ids.isEmpty()) {
            return Page.empty();
        }

        Map<Integer, Products> productMap = productRepository.findAllById(ids)
                .stream()
                .collect(Collectors.toMap(Products::getId, pr -> pr));

        List<Products> ordered =
                ids.stream()
                .map(productMap::get)
                .filter(pr -> pr != null)
                .collect(Collectors.toList());

        Pageable pageable = PageRequest.of(p, PAGE_SIZE);
        int start = (int) pageable.getOffset();
        int end = Math.min(start + PAGE_SIZE, ordered.size());

        List<Products> slice = start < end ? ordered.subList(start, end) : List.of();
        return new PageImpl<>(slice, pageable, ordered.size());
    }
}