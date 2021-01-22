package network.warzone.tgm.gametype;

import java.util.ArrayList;
import java.util.List;
import network.warzone.tgm.match.MatchManifest;
import network.warzone.tgm.match.MatchModule;
import network.warzone.tgm.modules.blitz.BlitzModule;

/**
 * Created by Jorge on 10/7/2017.
 */
public class BlitzManifest extends MatchManifest {

  @Override
  public List<MatchModule> allocateGameModules() {
    List<MatchModule> matchModules = new ArrayList<>();
    matchModules.add(new BlitzModule());
    return matchModules;
  }
}
