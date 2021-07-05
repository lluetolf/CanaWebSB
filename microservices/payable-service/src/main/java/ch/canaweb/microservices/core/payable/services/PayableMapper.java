package ch.canaweb.microservices.core.payable.services;

import ch.canaweb.api.core.Payable.Payable;
import ch.canaweb.microservices.core.payable.persistence.PayableEntity;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface PayableMapper {

    @Mappings({})
    Payable entityToApi(PayableEntity entity);

    @Mappings({
        @Mapping(target = "id", ignore = true),
        @Mapping(target = "version", ignore = true)
    })
    PayableEntity apiToEntity(Payable api);


    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "version", ignore = true)
    })
    void updateEntityWithApi(@MappingTarget PayableEntity entity, Payable api);
}


