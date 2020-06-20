package fun.vyse.cloud.test.mapper;


import fun.vyse.cloud.test.domain.User;
import fun.vyse.cloud.test.domain.UserDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {
	UserMapper INSTANCE = Mappers.getMapper( UserMapper.class );

	@Mapping(source = "loginName", target = "username")
	UserDto userToUserDto(User car);
}
