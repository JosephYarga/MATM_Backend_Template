package bf.gov.mtdpce.service;
import java.util.UUID;

import bf.gov.mtdpce.dto.request.ArticleCategoryRequest;
import bf.gov.mtdpce.dto.response.ArticleCategoryResponse;
import bf.gov.mtdpce.entity.ArticleCategory;
import bf.gov.mtdpce.exception.BadRequestException;
import bf.gov.mtdpce.exception.ResourceNotFoundException;
import bf.gov.mtdpce.repository.ArticleCategoryRepository;
import bf.gov.mtdpce.repository.ArticleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ArticleCategoryService {

    private final ArticleCategoryRepository articleCategoryRepository;
    private final ArticleRepository articleRepository;

    public ArticleCategoryService(ArticleCategoryRepository articleCategoryRepository,
                                  ArticleRepository articleRepository) {
        this.articleCategoryRepository = articleCategoryRepository;
        this.articleRepository = articleRepository;
    }

    @Transactional(readOnly = true)
    public List<ArticleCategoryResponse> getAll() {
        return articleCategoryRepository.findAllByOrderByDisplayOrderAscLabelAsc()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public ArticleCategoryResponse getById(UUID id) {
        return mapToResponse(findOrThrow(id));
    }

    @Transactional
    public ArticleCategoryResponse create(ArticleCategoryRequest dto) {
        String code = normalizeCode(dto.getCode());
        if (articleCategoryRepository.existsByCode(code)) {
            throw new BadRequestException("Une catégorie avec ce code existe déjà");
        }
        ArticleCategory category = ArticleCategory.builder()
                .code(code)
                .label(dto.getLabel())
                .description(dto.getDescription())
                .displayOrder(dto.getDisplayOrder() != null ? dto.getDisplayOrder() : 0)
                .build();
        return mapToResponse(articleCategoryRepository.save(category));
    }

    @Transactional
    public ArticleCategoryResponse update(UUID id, ArticleCategoryRequest dto) {
        ArticleCategory category = findOrThrow(id);
        String code = normalizeCode(dto.getCode());

        if (!category.getCode().equals(code) && articleCategoryRepository.existsByCode(code)) {
            throw new BadRequestException("Une catégorie avec ce code existe déjà");
        }

        category.setCode(code);
        category.setLabel(dto.getLabel());
        category.setDescription(dto.getDescription());
        if (dto.getDisplayOrder() != null) {
            category.setDisplayOrder(dto.getDisplayOrder());
        }
        return mapToResponse(articleCategoryRepository.save(category));
    }

    @Transactional
    public void delete(UUID id) {
        ArticleCategory category = findOrThrow(id);
        long used = articleRepository.countByCategoryCode(category.getCode());
        if (used > 0) {
            throw new BadRequestException(
                    "Impossible de supprimer : " + used + " article(s) utilisent cette catégorie.");
        }
        articleCategoryRepository.delete(category);
    }

    private ArticleCategory findOrThrow(UUID id) {
        return articleCategoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Catégorie d'article", "id", id));
    }

    private String normalizeCode(String code) {
        return code == null ? null : code.trim().toUpperCase().replaceAll("\\s+", "_");
    }

    private ArticleCategoryResponse mapToResponse(ArticleCategory c) {
        return ArticleCategoryResponse.builder()
                .id(c.getId())
                .code(c.getCode())
                .label(c.getLabel())
                .description(c.getDescription())
                .displayOrder(c.getDisplayOrder())
                .articleCount(articleRepository.countByCategoryCode(c.getCode()))
                .createdAt(c.getCreatedAt())
                .updatedAt(c.getUpdatedAt())
                .build();
    }
}
