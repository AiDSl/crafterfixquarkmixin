package xyz.crafterfixquarkmixin;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import com.mojang.logging.LogUtils;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("crafterfixquarkmixin")
public class crafterfixquarkmixin
{
    public static final String MODID = "crafterfixquarkmixin";
    private static final Logger LOGGER = LogUtils.getLogger();


    public crafterfixquarkmixin(FMLJavaModLoadingContext context)
    {
        LOGGER.info("Why quark dev are lazy?");
    }
}