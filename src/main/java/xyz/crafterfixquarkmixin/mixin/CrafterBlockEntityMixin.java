package xyz.crafterfixquarkmixin.mixin;

//mixin org
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.injection.*;
//mixins
import org.violetmoon.quark.content.automation.block.be.CrafterBlockEntity;
	//field
	import net.minecraft.core.dispenser.DispenseItemBehavior;
	import net.minecraft.server.level.ServerLevel;
	//insert_or_drop
	import net.minecraft.world.item.ItemStack;
	import net.minecraft.core.BlockSource;
	import net.minecraft.core.BlockSourceImpl;
	import net.minecraft.core.Direction;
	import org.violetmoon.quark.content.automation.block.CrafterBlock;
	import net.minecraft.world.Container;
	import net.minecraft.world.level.block.entity.HopperBlockEntity;




@Mixin(value=CrafterBlockEntity.class)
public class CrafterBlockEntityMixin{
	@Shadow @Final
	public static DispenseItemBehavior BEHAVIOR;

	public boolean insert_or_drop(ItemStack outputStack)
	{
		if (outputStack.isEmpty()) {
			return false;
		}

		CrafterBlockEntity thiss = (CrafterBlockEntity)(Object)this;

		BlockSource blockSource = new BlockSourceImpl((ServerLevel)thiss.getLevel(), thiss.getBlockPos());
		Direction direction = thiss.getBlockState().getValue(CrafterBlock.FACING);
		Container inventory = HopperBlockEntity.getContainerAt(thiss.getLevel(), thiss.getBlockPos().relative(direction));
		
		if (inventory == null) {
			BEHAVIOR.dispense(blockSource, outputStack);
			return true;
		}
		if (!thiss.hasSpace(inventory, direction, outputStack)) {
			return false;
		}

		//try to insert
		if (inventory instanceof CrafterBlockEntity) {
			int count = outputStack.getCount();
			for (int i = 0; i < count; i++) {
				ItemStack is = outputStack.copy();
				is.setCount(1);
				HopperBlockEntity.addItem(thiss.result, inventory, is, direction.getOpposite());
			}
		} else {
			HopperBlockEntity.addItem(thiss.result, inventory, outputStack, direction.getOpposite());
		}
		
		return true;
	}

	@Redirect(method = "takeItems()V", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/dispenser/DispenseItemBehavior;dispense(Lnet/minecraft/core/BlockSource;Lnet/minecraft/world/item/ItemStack;)Lnet/minecraft/world/item/ItemStack;"))
	private ItemStack injected(DispenseItemBehavior inputBEHAVIOR,BlockSource bsource, ItemStack is)
	{
		if(!insert_or_drop(is)) {
			inputBEHAVIOR.dispense(bsource,is);
		}
		return is;
	}
}