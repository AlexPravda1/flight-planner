package planner.service.mapper;

public interface DtoResponseMapper<D, C> {
    D toDto(C object);
}
