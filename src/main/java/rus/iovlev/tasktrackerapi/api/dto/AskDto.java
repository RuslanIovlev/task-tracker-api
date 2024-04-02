package rus.iovlev.tasktrackerapi.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AskDto {

    private Boolean answer;

    public static AskDto makeDefault(Boolean answer){
        return builder()
                .answer(answer)
                .build();
    }
}
