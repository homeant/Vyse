package fun.Vyse.cloud.model.converter;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MappingContext;
import ma.glasnost.orika.converter.BidirectionalConverter;
import ma.glasnost.orika.metadata.Type;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;

import java.text.ParseException;
import java.util.Date;


@Data
@Slf4j
public class DateConverter extends BidirectionalConverter<java.util.Date,String>{

    private final String format;

    @Override
    public Date convertFrom(String s, Type<Date> type, MappingContext mappingContext) {
        try{
            String[] patterns = {"yyyy-MM-dd","yyyy-MM-dd HH:mm:ss"};
            return DateUtils.parseDate(s,patterns);
        }catch (ParseException e){
            return null;
        }
    }

    @Override
    public String convertTo(Date date, Type<String> type, MappingContext mappingContext) {
        log.debug("date:{}",DateFormatUtils.format(date,format));
        return DateFormatUtils.format(date,format);
    }
}
