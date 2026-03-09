package game.alias.common.pagination;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class QueryValidator {

    public static Pageable validatePageable(Pageable pageable, QueryConfigModel.QueryConfig config) {
        int page = Math.max(pageable.getPageNumber(), 0);

        int size = config.pageSizes().contains(pageable.getPageSize())
                ? pageable.getPageSize()
                : config.pageSizes().getFirst();

        List<String> allowedFields = config.sorting().sortFields()
                .stream()
                .map(QueryConfigModel.SortField::field)
                .toList();

        Sort sort = pageable.getSort();

        if (sort.isUnsorted()) {
            return PageRequest.of(
                    page,
                    size,
                    Sort.by(config.sorting().defaultOrder(), config.sorting().defaultSort())
            );
        }

        for (Sort.Order order : sort) {
            if (!allowedFields.contains(order.getProperty())) {
                return PageRequest.of(
                        page,
                        size,
                        Sort.by(config.sorting().defaultOrder(), config.sorting().defaultSort())
                );
            }
        }

        return PageRequest.of(page, size, sort);
    }

    public static void validateFilters(
            List<QueryFilter> filters,
            QueryConfigModel.QueryConfig config
    ) {

        Map<String, QueryConfigModel.FilterConfig> allowedFilters =
                config.filters()
                        .stream()
                        .collect(Collectors.toMap(
                                QueryConfigModel.FilterConfig::field,
                                f -> f
                        ));

        for (QueryFilter f : filters) {

            QueryConfigModel.FilterConfig filterConfig = allowedFilters.get(f.field());

            if (filterConfig == null) {
                throw new IllegalArgumentException("Filter not allowed: " + f.field());
            }

            if (!filterConfig.operators().contains(f.operator())) {
                throw new IllegalArgumentException("Operator " + f.operator() + " not allowed for field " + f.field());
            }
        }
    }
}