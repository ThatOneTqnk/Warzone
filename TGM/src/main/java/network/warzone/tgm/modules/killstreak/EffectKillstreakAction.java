package network.warzone.tgm.modules.killstreak;

import java.util.Set;
import lombok.AllArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

@AllArgsConstructor
class EffectKillstreakAction implements KillstreakAction {

  private Set<PotionEffect> potionEffects;

  @Override
  public void apply(Player killer) {
    for (PotionEffect potionEffect : potionEffects) {
      killer.addPotionEffect(
        new PotionEffect(
          potionEffect.getType(),
          potionEffect.getDuration() * 20,
          potionEffect.getAmplifier(),
          potionEffect.isAmbient(),
          potionEffect.hasParticles(),
          false
        ),
        true
      );
    }
  }
}
