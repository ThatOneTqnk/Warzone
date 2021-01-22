package network.warzone.tgm.gametype;

import java.util.ArrayList;
import java.util.List;
import network.warzone.tgm.match.MatchManifest;
import network.warzone.tgm.match.MatchModule;
import network.warzone.tgm.modules.ffa.FFAModule;

/**
 * Created by Jorge on 3/2/2018.
 */
public class FFAManifest extends MatchManifest {

  @Override
  public List<MatchModule> allocateGameModules() {
    List<MatchModule> matchModules = new ArrayList<>();
    matchModules.add(new FFAModule());
    return matchModules;
  }
}
