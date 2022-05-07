package planner.util;

import java.util.List;
import java.util.stream.Collectors;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public final class MapperUtil {
    private static Mapper mapper;

    @Autowired
    public MapperUtil(Mapper mapper) {
        MapperUtil.mapper = mapper;
    }

    public static <FromT, T> List<T> getMappedDtoList(List<FromT> fromList,
                                                      final Class<T> toClass) {
        return fromList
                .stream()
                .map(from -> mapper.map(from, toClass))
                .collect(Collectors.toList());
    }

    public static <FromT, T> T getMappedDtoEntity(FromT fromClass, final Class<T> toClass) {
        return mapper.map(fromClass, toClass);
    }
}
