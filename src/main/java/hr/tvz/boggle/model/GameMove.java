package hr.tvz.boggle.model;

import hr.tvz.boggle.core.Player;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class GameMove implements Serializable {

    private String playerName;
    private String word;
    private LocalDateTime localDateTime;

}
