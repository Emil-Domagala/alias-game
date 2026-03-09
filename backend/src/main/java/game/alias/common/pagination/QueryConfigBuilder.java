package game.alias.common.pagination;

import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;

public class QueryConfigBuilder {

    private String defaultSort;
    private Sort.Direction defaultOrder;

    private final List<QueryConfigModel.SortField> sortFields = new ArrayList<>();
    private final List<QueryConfigModel.FilterConfig> filters = new ArrayList<>();
    private List<Integer> pageSizes = List.of(10, 20, 50);

    public QueryConfigBuilder defaultSort(String field, Sort.Direction direction) {
        this.defaultSort = field;
        this.defaultOrder = direction;
        return this;
    }

    public QueryConfigBuilder sortField(String field, String label) {
        sortFields.add(new QueryConfigModel.SortField(field, label));
        return this;
    }

    public QueryConfigBuilder filter(String field, QueryConfigModel.FilterType type, List<String> options, List<QueryConfigModel.FilterOperator> operators) {
        filters.add(new QueryConfigModel.FilterConfig(field, type, options, operators));
        return this;
    }

    public QueryConfigBuilder pageSizes(Integer... sizes) {
        this.pageSizes = List.of(sizes);
        return this;
    }

    public QueryConfigModel.QueryConfig build() {

        if (defaultSort == null) {
            throw new IllegalStateException("Default sort must be defined");
        }

        return new QueryConfigModel.QueryConfig(
                new QueryConfigModel.SortConfig(defaultSort, defaultOrder, List.copyOf(sortFields)),
                List.copyOf(filters),
                List.copyOf(pageSizes)
        );
    }
}