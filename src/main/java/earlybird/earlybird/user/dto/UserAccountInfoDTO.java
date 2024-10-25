package earlybird.earlybird.user.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Builder
public class UserAccountInfoDTO {
    private Long id;
    private String accountId;
    private String name;
    private String email;
    private String role;

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof UserAccountInfoDTO))
            return false;
        UserAccountInfoDTO other = (UserAccountInfoDTO) obj;

        return this.id == other.getId()
                && this.accountId.equals(other.getAccountId())
                && this.name.equals(other.getName())
                && this.email.equals(other.getEmail())
                && this.role.equals(other.getRole());
    }
}
