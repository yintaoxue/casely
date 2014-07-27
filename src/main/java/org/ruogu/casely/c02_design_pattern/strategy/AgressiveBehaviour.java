package org.ruogu.casely.c02_design_pattern.strategy;

/**
 * @author http://www.oodesign.com/strategy-pattern.html
 */
public class AgressiveBehaviour implements IBehaviour{
	public int moveCommand()
	{
		System.out.println("\tAgressive Behaviour: if find another robot attack it");
		return 1;
	}
}
