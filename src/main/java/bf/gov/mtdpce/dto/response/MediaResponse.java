package bf.gov.mtdpce.dto.response;

import java.time.LocalDateTime;
import java.util.List;

public class MediaResponse {
    
    private Long id;
    private String title;
    private String description;
    private String fileUrl;
    private String thumbnailUrl;
    private String mediaType;
    private Long albumId;
    private String albumName;
    private String category;
    private List<String> tags;
    private Boolean isPublic;
    private Integer displayOrder;
    private Long viewCount;
    private Long downloadCount;
    private Long fileSize;
    private String mimeType;
    private Integer duration; // For video/audio in seconds
    private Integer width; // For images/videos
    private Integer height; // For images/videos
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy;

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
    
    public String getMediaType() { return mediaType; }
    public void setMediaType(String mediaType) { this.mediaType = mediaType; }
    
    public Long getAlbumId() { return albumId; }
    public void setAlbumId(Long albumId) { this.albumId = albumId; }
    
    public String getAlbumName() { return albumName; }
    public void setAlbumName(String albumName) { this.albumName = albumName; }
    
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    
    public List<String> getTags() { return tags; }
    public void setTags(List<String> tags) { this.tags = tags; }
    
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
    
    public String getCreatedBy() { return createdBy; }
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }
}
