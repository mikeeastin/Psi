/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: http://psi.vazkii.us/license.php
 *
 * File Created @ [15/02/2016, 18:11:08 (GMT)]
 */
package vazkii.psi.common.spell.trick.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import vazkii.psi.api.spell.EnumSpellStat;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellCompilationException;
import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.api.spell.SpellMetadata;
import vazkii.psi.api.spell.SpellParam;
import vazkii.psi.api.spell.SpellRuntimeException;
import vazkii.psi.api.spell.param.ParamEntity;
import vazkii.psi.api.spell.piece.PieceTrick;

public class PieceTrickSmeltItem extends PieceTrick {

	SpellParam target;

	public PieceTrickSmeltItem(Spell spell) {
		super(spell);
	}

	@Override
	public void initParams() {
		addParam(target = new ParamEntity(SpellParam.GENERIC_NAME_TARGET, SpellParam.YELLOW, false, false));
	}

	@Override
	public void addToMetadata(SpellMetadata meta) throws SpellCompilationException {
		super.addToMetadata(meta);

		meta.addStat(EnumSpellStat.POTENCY, 80);
		meta.addStat(EnumSpellStat.COST, 240);
	}

	@Override
	public Object execute(SpellContext context) throws SpellRuntimeException {
		Entity targetVal = this.<Entity>getParamValue(context, target);

		if(targetVal instanceof EntityItem && !targetVal.isDead) {
			EntityItem eitem = (EntityItem) targetVal;
			ItemStack stack = eitem.getItem();
			ItemStack result = FurnaceRecipes.instance().getSmeltingResult(stack);

			if(result != null) {
				if(!eitem.getEntityWorld().isRemote) {
					stack.shrink(1);
					if(stack.getCount() == 0)
						eitem.setDead();
				}

				EntityItem item = new EntityItem(context.caster.getEntityWorld(), eitem.posX, eitem.posY, eitem.posZ, result.copy());
				if(!context.caster.getEntityWorld().isRemote)
					context.caster.getEntityWorld().spawnEntity(item);
			}
		} else throw new SpellRuntimeException(SpellRuntimeException.NULL_TARGET);

		return null;
	}

}
