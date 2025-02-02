package earlybird.earlybird.error.exception;

import earlybird.earlybird.error.ErrorCode;

public class ArticleNotFoundException extends NotFoundException {
    public ArticleNotFoundException() {
        super(ErrorCode.ARTICLE_NOT_FOUND);
    }
}
