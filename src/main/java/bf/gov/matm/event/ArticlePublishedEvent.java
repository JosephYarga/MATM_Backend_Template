package bf.gov.matm.event;

import bf.gov.matm.entity.Article;
import lombok.Getter;

@Getter
public class ArticlePublishedEvent {

    private final Article article;

    public ArticlePublishedEvent(Article article) {
        this.article = article;
    }
}
