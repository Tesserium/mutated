package com.code2828.mutated;

import java.util.List;

public class Mutations {

	public Mutations() {}
	public static final ExperienceBoostMutation EXPERIENCE_BOOST = new ExperienceBoostMutation();
	public static final FallVulnerabilityMutation FALL_VULNERABILITY = new FallVulnerabilityMutation();
	
	public static final List<Mutation> LIST = List.of(EXPERIENCE_BOOST);

}
