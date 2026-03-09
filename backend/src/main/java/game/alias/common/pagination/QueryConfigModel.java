package game.alias.common.pagination;

import org.springframework.data.domain.Sort;

import java.util.List;

public class QueryConfigModel {

    private QueryConfigModel() {}

    public enum FilterOperator {
        EQ, CONTAINS, LT, GT, IN
    }

    public enum FilterType {
        TEXT, SELECT, NUMBER
    }

    public record QueryConfig (
            SortConfig sorting,
            List<FilterConfig> filters,
            List<Integer> pageSizes
    ) {
    }

    public record FilterConfig(
            String field,
            FilterType type,
            List<String> options,
            List<FilterOperator> operators
    )
    {
    }

    public record SortConfig(
            String defaultSort,
            Sort.Direction defaultOrder,
            List<SortField> sortFields
    ) {
    }

    public record SortField(
            String field,
            String label
    ) {
    }

}
