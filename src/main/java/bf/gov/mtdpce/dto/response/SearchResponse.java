package bf.gov.mtdpce.dto.response;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class SearchResponse {
    
    private String query;
    private long totalResults;
    private int page;
    private int size;
    private int totalPages;
    private List<SearchResultItem> results;
    private Map<String, Long> facets; // Category counts
    private List<String> suggestions;
    private long searchDurationMs;

    public static class SearchResultItem {
        private Long id;
        private String type; // ARTICLE, PROJECT, DOCUMENT, EVENT, SERVICE, FAQ, JOB
        private String title;
        private String excerpt;
        private String url;
        private String imageUrl;
        private String category;
        private Double relevanceScore;
        private LocalDateTime date;
        private Map<String, String> highlights; // Field -> highlighted text

        // Getters and Setters
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        
        public String getType() { return type; }
        public void setType(String type) { this.type = type; }
        
        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }
        
        public String getExcerpt() { return excerpt; }
        public void setExcerpt(String excerpt) { this.excerpt = excerpt; }
        
        public String getUrl() { return url; }
        public void setUrl(String url) { this.url = url; }
        
        public String getImageUrl() { return imageUrl; }
        public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
        
        public String getCategory() { return category; }
        public void setCategory(String category) { this.category = category; }
        
        public Double getRelevanceScore() { return relevanceScore; }
        public void setRelevanceScore(Double relevanceScore) { this.relevanceScore = relevanceScore; }
        
        public LocalDateTime getDate() { return date; }
        public void setDate(LocalDateTime date) { this.date = date; }
        
        public Map<String, String> getHighlights() { return highlights; }
        public void setHighlights(Map<String, String> highlights) { this.highlights = highlights; }
    }

    // Getters and Setters
    public String getQuery() { return query; }
    public void setQuery(String query) { this.query = query; }
    
    public long getTotalResults() { return totalResults; }
    public void setTotalResults(long totalResults) { this.totalResults = totalResults; }
    
    public int getPage() { return page; }
    public void setPage(int page) { this.page = page; }
    
    public int getSize() { return size; }
    public void setSize(int size) { this.size = size; }
    
    public int getTotalPages() { return totalPages; }
    public void setTotalPages(int totalPages) { this.totalPages = totalPages; }
    
    public List<SearchResultItem> getResults() { return results; }
    public void setResults(List<SearchResultItem> results) { this.results = results; }
    
    public Map<String, Long> getFacets() { return facets; }
    public void setFacets(Map<String, Long> facets) { this.facets = facets; }
    
    public List<String> getSuggestions() { return suggestions; }
    public void setSuggestions(List<String> suggestions) { this.suggestions = suggestions; }
    
    public long getSearchDurationMs() { return searchDurationMs; }
    public void setSearchDurationMs(long searchDurationMs) { this.searchDurationMs = searchDurationMs; }
}
