package entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "Reviews")
public class Reviews {
    @Id
    @Column(name = "Id")
    private Integer id;

    @Column(name = "Rating")
    private Integer rating;

    @Column(name = "Content")
    private String content;

    @Column(name = "ReviewDate")
    private LocalDateTime reviewDate;

    @Column(name = "UserId")
    private Integer userId;

    @Column(name = "ProductId")
    private Integer productId;

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getRating() {
        return this.rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getReviewDate() {
        return this.reviewDate;
    }

    public void setReviewDate(LocalDateTime reviewDate) {
        this.reviewDate = reviewDate;
    }

    public Integer getUserId() {
        return this.userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getProductId() {
        return this.productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }
}
