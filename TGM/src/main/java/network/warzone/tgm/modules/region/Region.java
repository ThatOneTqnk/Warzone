package network.warzone.tgm.modules.region;

import java.util.List;
import org.bukkit.Location;
import org.bukkit.block.Block;

public interface Region {
  boolean contains(Location location);

  boolean contains(Block block);

  Location getCenter();

  List<Block> getBlocks();

  Location getMin();

  Location getMax();
}
