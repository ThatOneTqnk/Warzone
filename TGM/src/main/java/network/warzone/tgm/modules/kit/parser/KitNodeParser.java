package network.warzone.tgm.modules.kit.parser;

import com.google.gson.JsonObject;
import java.util.List;
import network.warzone.tgm.modules.kit.KitNode;

public interface KitNodeParser {
  List<KitNode> parse(JsonObject jsonObject);
}
