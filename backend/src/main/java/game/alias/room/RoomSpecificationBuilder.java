package game.alias.room;

import game.alias.common.pagination.QueryFilter;
import game.alias.room.domains.Room;
import jakarta.persistence.criteria.Expression;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class RoomSpecificationBuilder {

    public Specification<Room> buildFilterSpecification(QueryFilter filter) {
        return (root, query, cb) -> {

            var path = root.get(filter.field());
            var javaType = path.getJavaType();

            return switch (filter.operator()) {
                case EQ -> cb.equal(path.as(javaType), parseValue(javaType, filter.value()));
                case CONTAINS -> {
                    Expression<String> stringPath = path.as(String.class);
                    yield cb.like(cb.lower(stringPath), "%" + filter.value().toLowerCase() + "%");
                }
                case LT -> cb.lessThan(path.as(Comparable.class), (Comparable) parseValue(javaType, filter.value()));
                case GT -> cb.greaterThan(path.as(Comparable.class), (Comparable) parseValue(javaType, filter.value()));
                case IN -> {
                    List<Object> values = Arrays.stream(filter.value().split(","))
                            .map(v -> parseValue(javaType, v))
                            .toList();
                    yield path.in(values);
                }
            };
        };
    }

    private Object parseValue(Class<?> type, String value) {
        if (type.equals(String.class)) return value;
        if (type.equals(Integer.class) || type.equals(int.class)) return Integer.parseInt(value);
        if (type.equals(Long.class) || type.equals(long.class)) return Long.parseLong(value);
        if (type.equals(Boolean.class) || type.equals(boolean.class)) return Boolean.parseBoolean(value);
        if (type.isEnum()) return Enum.valueOf((Class<Enum>) type, value);
        throw new IllegalArgumentException("Unsupported type: " + type.getName());
    }
}