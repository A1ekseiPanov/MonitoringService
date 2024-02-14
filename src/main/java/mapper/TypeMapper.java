package mapper;

import entity.dto.TypeMeterReadingDto;
import entity.TypeMeterReading;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface TypeMapper {
    TypeMapper INSTANCE = Mappers.getMapper(TypeMapper.class);

    TypeMeterReading typeMeterReadingDtoToTypeMeterReading(TypeMeterReadingDto dtoto);
}