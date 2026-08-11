package bf.gov.matm.dto.request;

import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;

public class SearchRequest {
    
    @Size(max = 255, message = "La requête ne doit pas dépasser 255 caractères")
    private String query;
    
    private List<String> contentTypes; // ARTICLE, PROJECT, DOCUMENT, EVENT, SERVICE, FAQ, JOB
    
    private List<String> categories;
    
    private LocalDate dateFrom;
    
    private LocalDate dateTo;
    
    private String sortBy; // RELEVANCE, DATE_DESC, DATE_ASC, TITLE
    
    private Integer page = 0;
    
    private Integer size = 10;

    // Getters and Setters
    public String getQuery() { return query; }
    public void setQuery(String query) { this.query = query; }
    
    public List<String> getContentTypes() { return contentTypes; }
    public void setContentTypes(List<String> contentTypes) { this.contentTypes = contentTypes; }
    
    public List<String> getCategories() { return categories; }
    public void setCategories(List<String> categories) { this.categories = categories; }
    
    public LocalDate getDateFrom() { return dateFrom; }
    public void setDateFrom(LocalDate dateFrom) { this.dateFrom = dateFrom; }
    
    public LocalDate getDateTo() { return dateTo; }
    public void setDateTo(LocalDate dateTo) { this.dateTo = dateTo; }
    
    public String getSortBy() { return sortBy; }
    public void setSortBy(String sortBy) { this.sortBy = sortBy; }
    
    public Integer getPage() { return page; }
    public void setPage(Integer page) { this.page = page; }
    
    public Integer getSize() { return size; }
    public void setSize(Integer size) { this.size = size; }
}
