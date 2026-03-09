package game.alias.common.pagination;

import java.util.List;

public class FilterParser {
    public static List<QueryFilter> parse(List<String> rawFilters) {
        if (rawFilters == null) return List.of();

        return rawFilters.stream()
                .map(FilterParser::parseSingle)
                .toList();
    }

    private static QueryFilter parseSingle(String filter) {
        String[] parts = filter.split(":", 3);

        if (parts.length != 3) {
            throw new IllegalArgumentException("Invalid filter format: " + filter);
        }

        String field = parts[0];

        QueryConfigModel.FilterOperator operator;
        try {
            operator = QueryConfigModel.FilterOperator.valueOf(parts[1].toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Unsupported filter operator: " + parts[1]);
        }

        String value = parts[2];

        return new QueryFilter(field, operator, value);
    }
}
