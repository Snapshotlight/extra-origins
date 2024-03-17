/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.extraorigins.mixin.mobneutrality;

import io.github.apace100.apoli.component.PowerHolderComponent;
import moriyashiine.extraorigins.common.power.MobNeutralityPower;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import net.minecraft.entity.mob.MobEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.function.Predicate;

@Mixin(ActiveTargetGoal.class)
public class ActiveTargetGoalMixin {
	@ModifyVariable(method = "<init>(Lnet/minecraft/entity/mob/MobEntity;Ljava/lang/Class;IZZLjava/util/function/Predicate;)V", at = @At("HEAD"), argsOnly = true)
	private static Predicate<LivingEntity> extraorigins$mobNeutrality(Predicate<LivingEntity> value, MobEntity mob) {
		Predicate<LivingEntity> neutralityPredicate = target -> {
			for (MobNeutralityPower mobNeutralityPower : PowerHolderComponent.getPowers(target, MobNeutralityPower.class)) {
				if (mobNeutralityPower.shouldBeNeutral(mob.getType())) {
					return false;
				}
			}
			return true;
		};
		if (value == null) {
			return neutralityPredicate;
		}
		return value.and(neutralityPredicate);
	}
}
