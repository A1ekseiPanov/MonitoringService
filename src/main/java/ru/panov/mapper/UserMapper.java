package ru.panov.mapper;

import org.mapstruct.Mapper;
import ru.panov.domain.model.User;
import ru.panov.domain.requestDTO.UserRequestDTO;
import ru.panov.domain.responseDTO.UserResponseDTO;

/**
 * Интерфейс-маппер для преобразования объектов User между различными представлениями.
 */
@Mapper(componentModel = "spring")
public interface UserMapper {
    /**
     * Метод для преобразования объекта UserRequestDTO в объект User.
     *
     * @param userRequestDTO объект UserRequestDTO
     * @return объект User
     */
    User userDtoToUser(UserRequestDTO userRequestDTO);

    /**
     * Метод для преобразования объекта User в объект UserRequestDTO.
     *
     * @param user объект User
     * @return объект UserRequestDTO
     */
    UserRequestDTO userToUserRequestDto(User user);

    /**
     * Метод для преобразования объекта User в объект UserResponseDTO.
     *
     * @param user объект User
     * @return объект UserResponseDTO
     */
    UserResponseDTO userToUserResponseDto(User user);
}