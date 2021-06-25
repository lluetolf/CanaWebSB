package ch.canaweb.microservices.core.field.services;

import ch.canaweb.api.core.Field.Field;
import ch.canaweb.microservices.core.field.persistence.FieldEntity;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface FieldMapper {

    @Mappings({})
    Field entityToApi(FieldEntity entity);

    @Mappings({
        @Mapping(target = "id", ignore = true),
        @Mapping(target = "version", ignore = true)
    })
    FieldEntity apiToEntity(Field api);


    void updateEntityWithApi(@MappingTarget FieldEntity entity, Field api);
}
