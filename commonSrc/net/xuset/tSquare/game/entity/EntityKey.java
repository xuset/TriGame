package net.xuset.tSquare.game.entity;

import net.xuset.objectIO.markupMsg.MarkupMsg;

public final class EntityKey {
	final boolean allowUpdates;
	final long id;
	final boolean owned;
	final MarkupMsg initialValues;
	
	private EntityKey(boolean owned, boolean allowUpdates, long id, MarkupMsg initialValues) {
		this.owned = owned;
		this.allowUpdates = allowUpdates;
		this.id = id;
		this.initialValues = initialValues;
	}
	
	static EntityKey createFromExisting(long id, boolean allowUpdates, MarkupMsg initialValues) {
		return new EntityKey(false, allowUpdates, id, initialValues);
	}
}
