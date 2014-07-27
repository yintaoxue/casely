package org.ruogu.casely.c02_design_pattern.strategy;

/**
 * @author http://www.oodesign.com/strategy-pattern.html
 */
public class DefensiveBehaviour implements IBehaviour{
	public int moveCommand()
	{
		System.out.println("\tDefensive Behaviour: if find another robot run from it");
		return -1;
	}
}
