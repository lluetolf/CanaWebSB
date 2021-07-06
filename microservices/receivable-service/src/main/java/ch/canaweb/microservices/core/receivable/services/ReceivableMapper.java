package ch.canaweb.microservices.core.receivable.services;

import ch.canaweb.api.core.Receivable.Receivable;
import ch.canaweb.microservices.core.receivable.persistence.ReceivableEntity;
import org.mapstruct.*;


@Mapper(componentModel = "spring")
public interface ReceivableMapper {
    @Mappings({})
    Receivable entityToApi(ReceivableEntity entity);

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "version", ignore = true)
    })
    ReceivableEntity apiToEntity(Receivable api);

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "version", ignore = true)
    })
    void updateEntityWithApi(@MappingTarget ReceivableEntity entity, Receivable api);
}
