package bf.gov.mtdpce.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "medias")
public class Media {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, length = 255)
    private String title;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    @Column(name = "file_url", nullable = false, length = 500)
    private String fileUrl;
    
    @Column(name = "thumbnail_url", length = 500)
    private String thumbnailUrl;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "media_type", nullable = false, length = 20)
    private MediaType mediaType;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "album_id")
    private Album album;
    
    @Column(length = 100)
    private String category;
    
    @Column(columnDefinition = "TEXT")
    private String tags;
    
    @Column(name = "is_public")
    private Boolean isPublic = true;
    
    @Column(name = "display_order")
    private Integer displayOrder = 0;
    
    @Column(name = "view_count")
    private Long viewCount = 0L;
    
    @Column(name = "download_count")
    private Long downloadCount = 0L;
    
    @Column(name = "file_size")
    private Long fileSize;
    
    @Column(name = "mime_type", length = 100)
    private String mimeType;
    
    private Integer duration;
    
    private Integer width;
    
    private Integer height;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    private User createdBy;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public String getFileUrl() { return fileUrl; }
    public void setFileUrl(String fileUrl) { this.fileUrl = fileUrl; }
    
    public String getThumbnailUrl() { return thumbnailUrl; }
    public void setThumbnailUrl(String thumbnailUrl) { this.thumbnailUrl = thumbnailUrl; }
    
    public MediaType getMediaType() { return mediaType; }
    public void setMediaType(MediaType mediaType) { this.mediaType = mediaType; }
    
    public Album getAlbum() { return album; }
    public void setAlbum(Album album) { this.album = album; }
    
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    
    public String getTags() { return tags; }
    public void setTags(String tags) { this.tags = tags; }
    
    public Boolean getIsPublic() { return isPublic; }
    public void setIsPublic(Boolean isPublic) { this.isPublic = isPublic; }
    
    public Integer getDisplayOrder() { return displayOrder; }
    public void setDisplayOrder(Integer displayOrder) { this.displayOrder = displayOrder; }
    
    public Long getViewCount() { return viewCount; }
    public void setViewCount(Long viewCount) { this.viewCount = viewCount; }
    
    public Long getDownloadCount() { return downloadCount; }
    public void setDownloadCount(Long downloadCount) { this.downloadCount = downloadCount; }
    
    public Long getFileSize() { return fileSize; }
    public void setFileSize(Long fileSize) { this.fileSize = fileSize; }
    
    public String getMimeType() { return mimeType; }
    public void setMimeType(String mimeType) { this.mimeType = mimeType; }
    
    public Integer getDuration() { return duration; }
    public void setDuration(Integer duration) { this.duration = duration; }
    
    public Integer getWidth() { return width; }
    public void setWidth(Integer width) { this.width = width; }
    
    public Integer getHeight() { return height; }
    public void setHeight(Integer height) { this.height = height; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    
    public User getCreatedBy() { return createdBy; }
    public void setCreatedBy(User createdBy) { this.createdBy = createdBy; }
}
