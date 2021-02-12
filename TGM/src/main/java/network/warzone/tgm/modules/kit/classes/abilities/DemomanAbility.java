package network.warzone.tgm.modules.kit.classes.abilities;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.util.Vector;

import network.warzone.tgm.util.itemstack.ItemUtils;

public class DemomanAbility extends Ability {

	private List<TNTPrimed> tnts = new ArrayList<>();

    public DemomanAbility() {
        super("Throwing TNT", 20 * 12, Material.TNT, ChatColor.RED.toString() + ChatColor.BOLD + "THROWING TNT");
    }

    @Override
    public void onClick(Player player) {
    	final TNTPrimed tnt = (TNTPrimed) player.getWorld().spawnEntity(new Location(player.getWorld(), player.getEyeLocation().getX(), player.getEyeLocation().getY() + 1, player.getEyeLocation().getZ()), EntityType.PRIMED_TNT);
    	tnt.setVelocity(player.getLocation().getDirection().multiply(1.1));
    	tnts.add(tnt);
    	// TntTracker.setOwner(tnt, player);

		player.playSound(player.getEyeLocation(), Sound.ENTITY_ITEM_PICKUP, 10, 1);
    	
        super.putOnCooldown(player);
    }

    @EventHandler
    public void onExplode(EntityExplodeEvent e){
    	if(e.getEntity() instanceof TNTPrimed){
    		if(tnts.contains(e.getEntity())){
				List<Block> toRemove = new ArrayList<>();

    			for(Block b : e.blockList()) {
                    if (ItemUtils.woodenMaterials.contains(b.getType()) || ItemUtils.stairsMaterials.contains(b.getType())) {

						/*
    					 * Wood (throws 1/4 in air)
    					 */

						Random rn = new Random();
						if((rn.nextInt(4) + 1) <= 3){
							// Material m = b.getType();
                            BlockData blockData = b.getBlockData();
							b.setType(Material.AIR);

							// FallingBlock fb = b.getWorld().spawnFallingBlock(b.getLocation(), m, by);
							FallingBlock fb = b.getWorld().spawnFallingBlock(b.getLocation(), blockData);

							Random r = new Random();

							double xV = -1.1 + (1.5 - -1.1) * r.nextDouble();
							double yV = .4 + (1 - .4) * r.nextDouble();
							double zV = -1.1 + (1.5 - -1.1) * r.nextDouble();

							fb.setVelocity(new Vector(xV,yV,zV));
						}else{
							toRemove.add(b);
						}
    				}else{
						toRemove.add(b);
    				}
    			}

    			/*
    			 * Takes the blocks it shouldn't break out of list.
    			 */
    			
				for (Block remove : toRemove) {
					e.blockList().remove(remove);
				}
    		}
    	}
    }

    @Override
    public void terminate() {
        super.terminate();
        this.tnts.clear();
    }
}
