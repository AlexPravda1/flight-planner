package planner.service.mapper;

public interface DtoRequestMapper<D, C> {
    C toEntity(D dto);
}
