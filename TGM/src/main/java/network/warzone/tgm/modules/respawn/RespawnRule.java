package network.warzone.tgm.modules.respawn;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import network.warzone.tgm.modules.team.MatchTeam;

@AllArgsConstructor
@Getter
public class RespawnRule {

  private List<MatchTeam> teams;
  private int delay;
  private boolean freeze;
  private boolean blindness;
  private boolean confirm;
}
