package game.alias.common.pagination;

public record QueryFilter(
        String field,
        QueryConfigModel.FilterOperator operator,
        String value
) {
}
