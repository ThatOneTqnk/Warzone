package network.warzone.tgm.modules;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import network.warzone.tgm.TGM;
import network.warzone.tgm.match.Match;
import network.warzone.tgm.match.MatchModule;
import network.warzone.tgm.parser.item.ItemDeserializer;
import network.warzone.tgm.util.Parser;
import network.warzone.tgm.util.Strings;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.event.Listener;
import org.bukkit.inventory.*;

import java.util.*;

/**
 * Created by Jorge on 10/06/2019
 */
public class CraftingModule extends MatchModule implements Listener {

    private List<Recipe> recipes = new ArrayList<>();
    private List<Material> removedRecipes = new ArrayList<>();

    @Override
    public void load(Match match) {
        this.removedRecipes.add(Material.COMPASS);
        boolean removeAll = false;
        JsonElement c = match.getMapContainer().getMapInfo().getJsonObject().get("crafting");
        if (c != null && c.isJsonObject()) {
            JsonObject crafting = c.getAsJsonObject();
            if (crafting.has("remove")) {
                if (crafting.get("remove").isJsonArray()) {
                    for (JsonElement jsonElement : crafting.getAsJsonArray("remove")) {
                        if (!jsonElement.isJsonPrimitive()) continue;
                        Material mat = Material.valueOf(jsonElement.getAsString());
                        if (mat == null) continue;
                        this.removedRecipes.add(mat);
                    }
                } else if (crafting.get("remove").isJsonPrimitive() && crafting.get("remove").getAsString().equals("*")) {
                    removeAll = true;
                }
            }
            if (crafting.has("recipes") && crafting.get("recipes").isJsonArray()) {
                for (JsonElement jsonElement : crafting.getAsJsonArray("recipes")) {
                    if (!jsonElement.isJsonObject()) continue;
                    Recipe recipe = parseRecipe(jsonElement.getAsJsonObject());
                    if (recipe != null) this.recipes.add(recipe);
                }
                this.recipes.forEach(Bukkit::addRecipe);
            }
        }
        if (removeAll) Bukkit.clearRecipes();
        else removeRecipes();
    }

    @Override
    public void unload() {
        Bukkit.resetRecipes();
    }

    private void removeRecipes() {
        List<Recipe> backup = new ArrayList<>();
        for (Material material : this.removedRecipes) {
            ItemStack item = new ItemStack(material);
            Iterator<Recipe> iterator = Bukkit.getServer().recipeIterator();

            while (iterator.hasNext()) {
                Recipe recipe = iterator.next();
                ItemStack result = recipe.getResult();
                if (!result.isSimilar(item)) {
                    backup.add(recipe);
                }
            }
        }

        Bukkit.getServer().clearRecipes();
        for (Recipe r : backup)
            Bukkit.getServer().addRecipe(r);
    }

    private static Recipe parseRecipe(JsonObject jsonObject) {
        String type = jsonObject.get("type").getAsString();
        ItemStack result = ItemDeserializer.parse(jsonObject.get("result").getAsJsonObject());
        switch (type) {
            case "shapeless":
                ShapelessRecipe shapelessRecipe = new ShapelessRecipe(getKey(result.getType().name() + new Date().getTime()), result);
                for (JsonElement element : jsonObject.getAsJsonArray("ingredients")) {
                    RecipeChoice ingredient = parseRecipeIngredient(element);
                    if (ingredient == null) continue;
                    shapelessRecipe.addIngredient(ingredient);
                }
                return shapelessRecipe;
            case "shaped":
                ShapedRecipe shapedRecipe = new ShapedRecipe(getKey(result.getType().name() + new Date().getTime()), result);
                JsonArray shapeArray = jsonObject.getAsJsonArray("shape");
                String[] shape = new String[shapeArray.size()];
                for (int i = 0; i < shapeArray.size(); i++) {
                    shape[i] = shapeArray.get(i).getAsString();
                }
                shapedRecipe.shape(shape);
                for (Map.Entry<String, JsonElement> entry : jsonObject.getAsJsonObject("ingredients").entrySet()) {
                    try {
                        char key = entry.getKey().charAt(0);
                        RecipeChoice ingredient = parseRecipeIngredient(entry.getValue());
                        if (ingredient == null) continue;
                        shapedRecipe.setIngredient(key, ingredient);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                return shapedRecipe;
            case "furnace":
                RecipeChoice furnaceIngredient = parseRecipeIngredient(jsonObject.get("ingredient"));
                if (furnaceIngredient == null) return null;
                int furnaceExp = 0;
                int furnaceCookingTime = 200;
                if (jsonObject.has("experience")) furnaceExp = jsonObject.get("experience").getAsInt();
                if (jsonObject.has("cookingTime")) furnaceCookingTime = jsonObject.get("cookingTime").getAsInt();
                return new FurnaceRecipe(getKey(result.getType().name() + new Date().getTime()), result, furnaceIngredient, furnaceExp, furnaceCookingTime);
            case "smoking":
                RecipeChoice smokingIngredient = parseRecipeIngredient(jsonObject.get("ingredient"));
                if (smokingIngredient == null) return null;
                int smokingExp = 0;
                int smokingCookingTime = 100;
                if (jsonObject.has("experience")) smokingExp = jsonObject.get("experience").getAsInt();
                if (jsonObject.has("cookingTime")) smokingCookingTime = jsonObject.get("cookingTime").getAsInt();
                return new SmokingRecipe(getKey(result.getType().name() + new Date().getTime()), result, smokingIngredient, smokingExp, smokingCookingTime);
            case "blasting":
                RecipeChoice blastingIngredient = parseRecipeIngredient(jsonObject.get("ingredient"));
                if (blastingIngredient == null) return null;
                int blastingExp = 0;
                int blastingCookingTime = 100;
                if (jsonObject.has("experience")) blastingExp = jsonObject.get("experience").getAsInt();
                if (jsonObject.has("cookingTime")) blastingCookingTime = jsonObject.get("cookingTime").getAsInt();
                return new BlastingRecipe(getKey(result.getType().name() + new Date().getTime()), result, blastingIngredient, blastingExp, blastingCookingTime);
            case "campfire":
                RecipeChoice campfireIngredient = parseRecipeIngredient(jsonObject.get("ingredient"));
                if (campfireIngredient == null) return null;
                int campfireExp = 0;
                int campfireCookingTime = 600;
                if (jsonObject.has("experience")) campfireExp = jsonObject.get("experience").getAsInt();
                if (jsonObject.has("cookingTime")) campfireCookingTime = jsonObject.get("cookingTime").getAsInt();
                return new CampfireRecipe(getKey(result.getType().name() + new Date().getTime()), result, campfireIngredient, campfireExp, campfireCookingTime);
            case "stonecutting":
                RecipeChoice stonecuttingIngredient = parseRecipeIngredient(jsonObject.get("ingredient"));
                if (stonecuttingIngredient == null) return null;
                return new StonecuttingRecipe(getKey(result.getType().name() + new Date().getTime()), result, stonecuttingIngredient);
            default:
                return null;
        }
    }

    private static RecipeChoice parseRecipeIngredient(JsonElement jsonElement) {
        if (jsonElement.isJsonObject()) {
            ItemStack item = ItemDeserializer.parse(jsonElement.getAsJsonObject());
            return new RecipeChoice.ExactChoice(item);
        } else if (jsonElement.isJsonPrimitive()) {
            Material material = Material.valueOf(Strings.getTechnicalName(jsonElement.getAsString()));
            return new RecipeChoice.MaterialChoice(material);
        }
        return null;
    }

    // Will be moved to TGM class
    public static NamespacedKey getKey(String name) {
        return new NamespacedKey(TGM.get(), name);
    }

}