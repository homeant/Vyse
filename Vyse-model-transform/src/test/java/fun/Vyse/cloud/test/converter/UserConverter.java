package fun.Vyse.cloud.test.converter;

import fun.Vyse.cloud.test.domain.User;
import fun.Vyse.cloud.test.domain.UserDto;
import ma.glasnost.orika.CustomConverter;
import ma.glasnost.orika.MappingContext;
import ma.glasnost.orika.metadata.Type;
import org.apache.commons.lang3.time.DateFormatUtils;

public class UserConverter extends CustomConverter<User, UserDto> {
    @Override
    public UserDto convert(User user, Type<? extends UserDto> type, MappingContext mappingContext) {
        UserDto userDto = new UserDto();
        userDto.setNickName(user.getName());
        userDto.setDate(DateFormatUtils.format(user.getDate(),"yyyy-MM-dd"));
        return userDto;
    }
}
