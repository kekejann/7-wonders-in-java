﻿package org.tjuscs.sevenwonders.core;

/**
 * The Class SimpleResList.
 */
public class SimpleResList {

	/** The simple resource list. */
	int[] srl;

	/**
	 * Instantiates a new simple res list.
	 */
	SimpleResList() {
		srl = new int[8];
	} // end of SimpleResList constructor

	/**
	 * Instantiates a new simple res list.
	 * 
	 * @param sl
	 *            another simple resource list
	 */
	SimpleResList(SimpleResList sl) {
		srl = (int[]) sl.srl.clone();
	}

	/**
	 * Instantiates a new simple res list.
	 * 
	 * @param thing
	 *            the thing
	 */
	SimpleResList(Buildable thing) {
		srl = new int[8];
		addCard(thing);
	} // end of SimpleResList( buildableThing ) constructor

	/**
	 * Instantiates a new simple res list.
	 * 
	 * @param res
	 *            the res
	 */
	SimpleResList(Resource res) {
		srl = new int[8];
		this.addResource(res);
	}

	/**
	 * Adds the resource.
	 * 
	 * @param res
	 *            the res
	 */
	public void addResource(Resource res) {
		if (res.index() <= 7) {
			srl[res.index()] += 1;
			srl[0] = srl[1] + srl[2] + srl[3] + srl[4] + srl[5] + srl[6]
					+ srl[7];
		}
	} // end of addResource() method

	/**
	 * Builds the cost list.
	 * 
	 * @param thing
	 *            the thing
	 * @return the simple res list
	 */
	public static SimpleResList buildCostList(Buildable thing) {
		SimpleResList temp = new SimpleResList();
		for (Resource r : thing.getCosts()) {
			temp.srl[r.index()] += thing.costCnt(r);
		}
		temp.srl[0] = temp.srl[1] + temp.srl[2] + temp.srl[3] + temp.srl[4]
				+ temp.srl[5] + temp.srl[6] + temp.srl[7];
		return temp;
	} // end of buildCostList method

	/**
	 * Adds the card.
	 * 
	 * @param thing
	 *            the thing
	 */
	public void addCard(Buildable thing) {
		if (thing.hasResources()) {
			if (thing.hasOrResources()) {
				for (Resource r : thing.getGoods()) {
					for (Resource res : r.getOptionalRes()) {
						srl[res.index()] = 1;
					}
				}
			} else {
				for (Resource r : thing.getGoods()) {
					if (r.index() <= 7)
						srl[r.index()] += thing.goodsCnt(r);
				}
			}
		}
		srl[0] = srl[1] + srl[2] + srl[3] + srl[4] + srl[5] + srl[6] + srl[7];
	} // end of addCard method

	/**
	 * Gets the total res.
	 * 
	 * @return the total res
	 */
	public int getTotalRes() {
		return srl[0];
	} // end of getTotalRes() method

	/**
	 * Find raw matches.
	 * 
	 * @param resList
	 *            the res list
	 * @return the int
	 */
	public int findRawMatches(SimpleResList resList) {
		int matches = 0;
		for (int i = 1; i < 5; i++) {
			if (srl[i] > 0 && resList.srl[i] > 0)
				matches++;
		}
		return matches;
	} // end of findRawMatches() method

	/**
	 * Find manf matches.
	 * 
	 * @param resList
	 *            the res list
	 * @return the int
	 */
	public int findManfMatches(SimpleResList resList) {
		int matches = 0;
		for (int i = 4; i < srl.length; i++) {
			if (srl[i] > 0 && resList.srl[i] > 0)
				matches++;
		}
		return matches;
	} // end of findManfMatches() method

	/**
	 * Gets the num manf goods.
	 * 
	 * @return the num manf goods
	 */
	public int getNumManfGoods() {
		int count = 0;
		for (int i = 5; i < srl.length; i++) {
			if (srl[i] > 0)
				count += srl[i];
		}
		return count;
	} // end of getNumManfGoods() method

	/**
	 * Gets the num raw goods.
	 * 
	 * @return the num raw goods
	 */
	public int getNumRawGoods() {
		int count = 0;
		for (int i = 1; i < 5; i++) {
			if (srl[i] > 0)
				count += srl[i];
		}
		return count;
	} // end of getNumRawGoods() method

	/**
	 * Find num matches.
	 * 
	 * @param resList
	 *            the res list
	 * @return the int
	 */
	public int findNumMatches(SimpleResList resList) {
		int matches = 0;
		for (int i = 1; i < srl.length; i++) {
			if (srl[i] > 0 && resList.srl[i] > 0)
				matches++;
		}
		return matches;
	} // end of findNumMatches() method

	// subtracts para from the instance....used to reduce Cost
	// returns remaining resources needed
	/**
	 * Subtract.
	 * 
	 * @param resList
	 *            the res list
	 * @return the int
	 */
	public int subtract(SimpleResList resList) {
		for (int i = 1; i < srl.length; i++) {
			srl[i] -= resList.srl[i];
			if (srl[i] < 0)
				srl[i] = 0;
		}
		srl[0] = srl[1] + srl[2] + srl[3] + srl[4] + srl[5] + srl[6] + srl[7];
		return srl[0];
	} // end of subtract() method

	/**
	 * Resource at.
	 * 
	 * @param index
	 *            the index
	 * @return the resource
	 */
	public static Resource resourceAt(int index) {
		switch (index) {
		case 1:
			return Resource.BRICK;
		case 2:
			return Resource.ORE;
		case 3:
			return Resource.STONE;
		case 4:
			return Resource.WOOD;
		case 5:
			return Resource.CLOTH;
		case 6:
			return Resource.GLASS;
		case 7:
			return Resource.PAPYRUS;
		default:
			return Resource.FREE;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("total: [" + srl[0] + "]");
		if (srl[1] > 0)
			sb.append(" " + srl[1] + " brick ");
		if (srl[2] > 0)
			sb.append(" " + srl[2] + " ore ");
		if (srl[3] > 0)
			sb.append(" " + srl[3] + " stone ");
		if (srl[4] > 0)
			sb.append(" " + srl[4] + " wood ");
		if (srl[5] > 0)
			sb.append(" " + srl[5] + " cloth ");
		if (srl[6] > 0)
			sb.append(" " + srl[6] + " glass ");
		if (srl[7] > 0)
			sb.append(" " + srl[7] + " papyrus ");
		return sb.toString();
	}

	/**
	 * Num at.
	 * 
	 * @param ind
	 *            the ind
	 * @return the int
	 */
	public int numAt(int ind) {
		if (ind >= 0 && ind <= 7)
			return srl[ind];
		else
			return 0;
	}

	/**
	 * Subtract.
	 * 
	 * @param resList1
	 *            the res list1
	 * @param resList2
	 *            the res list2
	 * @return the simple res list
	 */
	public static SimpleResList subtract(SimpleResList resList1,
			SimpleResList resList2) {
		SimpleResList newList = new SimpleResList(resList1);
		for (int i = 1; i < newList.srl.length; i++) {
			newList.srl[i] -= resList2.srl[i];
			if (newList.srl[i] < 0)
				newList.srl[i] = 0;
		}
		newList.srl[0] = newList.srl[1] + newList.srl[2] + newList.srl[3]
				+ newList.srl[4] + newList.srl[5] + newList.srl[6]
				+ newList.srl[7];
		return newList;
	}

	/**
	 * Adds the.
	 * 
	 * @param resList1
	 *            the res list1
	 * @param resList2
	 *            the res list2
	 * @return the simple res list
	 */
	public static SimpleResList add(SimpleResList resList1,
			SimpleResList resList2) {
		SimpleResList newList = new SimpleResList(resList1);
		for (int i = 1; i < newList.srl.length; i++) {
			newList.srl[i] += resList2.srl[i];
		}
		newList.srl[0] = newList.srl[1] + newList.srl[2] + newList.srl[3]
				+ newList.srl[4] + newList.srl[5] + newList.srl[6]
				+ newList.srl[7];
		return newList;
	} // end of add() method

}
