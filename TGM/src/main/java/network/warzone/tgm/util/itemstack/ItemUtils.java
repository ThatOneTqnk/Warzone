package network.warzone.tgm.util.itemstack;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.HashSet;
import java.util.Set;

import com.google.common.collect.ImmutableSet;

/**
 * Created by luke on 11/15/15.
 */
public class ItemUtils {
    public static final ImmutableSet<Material> woodenMaterials;
    public static final ImmutableSet<Material> stairsMaterials;
    private static Set<Material> bannerMaterials = new HashSet<>();

    static {
        for (Material material : Material.values()) {
            if (material.name().contains("_BANNER") && !material.name().contains("LEGACY"))
                bannerMaterials.add(material);
        }

        ImmutableSet.Builder<Material> woodenMaterialsBuilder = ImmutableSet.builder(); 
        woodenMaterialsBuilder.add(Material.OAK_PLANKS);
        woodenMaterialsBuilder.add(Material.OAK_LOG);
        woodenMaterialsBuilder.add(Material.SPRUCE_PLANKS);
        woodenMaterialsBuilder.add(Material.SPRUCE_LOG);
        woodenMaterialsBuilder.add(Material.ACACIA_PLANKS);
        woodenMaterialsBuilder.add(Material.ACACIA_LOG);
        woodenMaterialsBuilder.add(Material.DARK_OAK_PLANKS);
        woodenMaterialsBuilder.add(Material.DARK_OAK_LOG);
        woodenMaterials = woodenMaterialsBuilder.build();

        ImmutableSet.Builder<Material> stairsMaterialsBuilder = ImmutableSet.builder(); 
        stairsMaterialsBuilder.add(Material.STONE_STAIRS);
        stairsMaterialsBuilder.add(Material.STONE_BRICK_STAIRS);
        stairsMaterialsBuilder.add(Material.OAK_STAIRS);
        stairsMaterialsBuilder.add(Material.DARK_OAK_STAIRS);
        stairsMaterialsBuilder.add(Material.ACACIA_STAIRS);
        stairsMaterialsBuilder.add(Material.SPRUCE_STAIRS);
        stairsMaterials = stairsMaterialsBuilder.build();
    }

    public static boolean compare(ItemStack i1, ItemStack i2) {
        if (i1 != null && i2 != null && i1.getItemMeta() != null && i2.getItemMeta() != null) {
            return i1.getItemMeta().getDisplayName().equals(i2.getItemMeta().getDisplayName());
        }
        return false;
    }
    
    public static boolean isPotion(Material material) {
        return material.equals(Material.POTION) || material.equals(Material.SPLASH_POTION) || material.equals(Material.LINGERING_POTION);
    }

    public static String itemToString(ItemStack item) {
        if (item == null || item.getType().equals(Material.AIR)) {
            return "their hands";
        }
        StringBuilder stringBuilder = new StringBuilder();
        if (item.hasItemMeta() && item.getItemMeta().hasEnchants()) stringBuilder.append("Enchanted ");
        stringBuilder.append(materialToString(item.getType()));
        return stringBuilder.toString().trim();
    }

    public static String materialToString(Material material) {
        StringBuilder stringBuilder = new StringBuilder();

        String materialName = material.toString();
        for (String word : materialName.split("_")) {
            word = word.toLowerCase();
            word = word.substring(0, 1).toUpperCase() + word.substring(1) + " ";
            stringBuilder.append(word);
        }
        return stringBuilder.toString().trim();
    }

    public static Set<Material> allBannerTypes() {
        return bannerMaterials;
    }
}
