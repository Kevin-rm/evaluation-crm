package site.easy.to.build.crm.dto;

import site.easy.to.build.crm.entity.Role;
import site.easy.to.build.crm.entity.User;

import java.util.List;

public record UserDTO(
    Integer id,
    String username,
    String email,
    List<Role> roles
) {
    public static UserDTO createFromUser(final User user) {
        return new UserDTO(user.getId(), user.getUsername(), user.getPassword(), user.getRoles());
    }
}
