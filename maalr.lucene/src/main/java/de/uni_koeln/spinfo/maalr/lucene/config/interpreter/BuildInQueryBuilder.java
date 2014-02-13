package de.uni_koeln.spinfo.maalr.lucene.config.interpreter;

import de.uni_koeln.spinfo.maalr.lucene.config.interpreter.modifier.DefaultQueryBuilder;
import de.uni_koeln.spinfo.maalr.lucene.config.interpreter.modifier.ExactMatchQueryBuilder;
import de.uni_koeln.spinfo.maalr.lucene.config.interpreter.modifier.InfixQueryBuilder;
import de.uni_koeln.spinfo.maalr.lucene.config.interpreter.modifier.PrefixQueryBuilder;
import de.uni_koeln.spinfo.maalr.lucene.config.interpreter.modifier.SuffixQueryBuilder;

public enum BuildInQueryBuilder {
	
	DEFAULT(DefaultQueryBuilder.class), EXACT(ExactMatchQueryBuilder.class), INFIX(InfixQueryBuilder.class),
	PREFIX(PrefixQueryBuilder.class), SUFFIX(SuffixQueryBuilder.class);
	
	private Class<MaalrQueryBuilder> clazz;

	@SuppressWarnings("unchecked")
	private BuildInQueryBuilder(Class<?> clazz) {
		this.clazz = (Class<MaalrQueryBuilder>) clazz;
	}

	public Class<MaalrQueryBuilder> getClazz() {
		return clazz;
	}

	
}
