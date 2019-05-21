package fun.Vyse.cloud.orika.converter;

import ma.glasnost.orika.MappingContext;
import ma.glasnost.orika.converter.BidirectionalConverter;
import ma.glasnost.orika.metadata.Type;

import java.time.LocalDate;

public class LocalDateConverter extends BidirectionalConverter<LocalDate, LocalDate>{

    @Override
    public LocalDate convertTo(LocalDate source, Type<LocalDate> type, MappingContext mappingContext) {
        return LocalDate.from(source);
    }

    @Override
    public LocalDate convertFrom(LocalDate source, Type<LocalDate> type, MappingContext mappingContext) {
        return LocalDate.from(source);
    }
}
