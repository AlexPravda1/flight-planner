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

    public static <FromEntity, ToEntity> List<ToEntity> mapToList(List<FromEntity> fromList,
                                                                  final Class<ToEntity> toClass) {
        return fromList
                .stream()
                .map(from -> mapper.map(from, toClass))
                .collect(Collectors.toList());
    }

    public static <FromEntity, ToEntity> ToEntity map(FromEntity fromObject, final Class<ToEntity> toClass) {
        return mapper.map(fromObject, toClass);
    }
}
