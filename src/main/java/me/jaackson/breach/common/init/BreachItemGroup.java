package me.jaackson.breach.common.init;

import me.jaackson.breach.BreachingContainment;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.ResourceLocation;

/**
 * <p>A standard Battlefields item tab.</p>
 *
 * @author Ocelot
 */
public abstract class BreachItemGroup extends ItemGroup
{
    private static final ResourceLocation ITEMS_TEXTURE = new ResourceLocation(BreachingContainment.MOD_ID, "textures/gui/container/creative_inventory/tab_items.png");
    private static final ResourceLocation ITEM_SEARCH_TEXTURE = new ResourceLocation(BreachingContainment.MOD_ID, "textures/gui/container/creative_inventory/tab_item_search.png");
    private static final ResourceLocation TABS_TEXTURE = new ResourceLocation(BreachingContainment.MOD_ID, "textures/gui/container/creative_inventory/tabs.png");

    private final boolean searchBar;

    public BreachItemGroup(String label, boolean searchBar)
    {
        super(label);
        this.searchBar = searchBar;
    }

    @Override
    public boolean hasSearchBar()
    {
        return searchBar;
    }

    @Override
    public ResourceLocation getBackgroundImage()
    {
        return this.searchBar ? ITEM_SEARCH_TEXTURE : ITEMS_TEXTURE;
    }

    @Override
    public ResourceLocation getTabsImage()
    {
        return TABS_TEXTURE;
    }
}