package com.amberpvp.hcfactions.util.command;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Command Framework - Command <br>
 * The command annotation used to designate methods as command. All methods
 * should have a single CommandArgs argument
 * 
 * @author minnymin3
 * 
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Command {

	String name();
	String[] aliases() default {};
	String description() default "";
	String usage() default "";
	boolean inGameOnly() default true;
	boolean inFactionOnly() default false;
	boolean isLeaderOnly() default false;
	boolean isOfficerOnly() default false;
}
