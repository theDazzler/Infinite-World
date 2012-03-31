package com.devon.infiniteworld;

/**
 * Holds data for attacks such as:
 * execution time - amount of frames it takes before attack hitbox is generated
 * active time - amount of frames the attack is active(still able to deal damage)
 * recovery time - amount of frames needed for recovery from attack(player is vulnerable during this time)
 * 
 * Example Attack of 26 Frames
 * 
 * |1|2|3|4|5|6|7|8|9|10|11| 12 | 13 |14|15|16|17|18|19|20|21|22|23|24|25|26|
 * |------Execution--------|--Active-|--------------Recovery----------------|
 * |---------------------------------TOTAL----------------------------------|
 * 
 * @author Devon
 *
 */
public class AttackPhase 
{
	int frameAmount; //total amount of frames for the attack
	int[] execution; //holds execution frames
	int[] active; //holds active frames
	int[] recovery; //holds recovery frames
	
	/**
	 * create default AttackPhase of size 26
	 * execution size = 11
	 * active size = 2
	 * recovery size = 13
	 */
	public AttackPhase()
	{
		this.frameAmount = 26;
		this.execution = new int[11];
		this.active = new int[2];
		this.recovery = new int[13];
	}
	
	/**
	 * Creates an AttackPhase with specified frame sizes
	 * @param executionFrames
	 * @param activeFrames
	 * @param recoveryFrames
	 */
	public AttackPhase(int executionFrames, int activeFrames, int recoveryFrames)
	{
		this.execution = new int[executionFrames];
		this.active = new int[activeFrames];
		this.recovery = new int[recoveryFrames];
		this.frameAmount = executionFrames + activeFrames + recoveryFrames;
	}
	
}
