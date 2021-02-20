/**
 * This class enables storing the results of information gain calculations
 * 
 * @author Mehrdad Sabetzadeh, University of Ottawa
 * @author Guy-Vincent Jourdan, University of Ottawa
 *
 */
public class GainInfoItem {

	/**
	 * name of the attribute
	 */
	private String attributeName;

	/**
	 * type of the attribute
	 */
	private AttributeType attributeType;

	/**
	 * gain value calculated for splitting over the attribute
	 */
	private double gainValue;

	/**
	 * For numeric attributes, splitAt is the value at which a binary split yields
	 * the optimal information gain. Although splitAt is always a number, we treat
	 * it as a string. Doing so is helpful because we do not want to lose track of
	 * exactly how a numeric value was written in the source data (for example, was
	 * it written as 10 or 10.0?). To not have to bother with distinguishing integer
	 * and decimal-point numbers, the splitAt value is treated as a string.
	 */
	private String splitAt;

	/**
	 * Constructor for GainInfoItem
	 * 
	 * @param attributeName is the name of the attribute
	 * @param attributeType is the type of the attribute
	 * @param gainValue     is the information gain value
	 * @param splitAt       is the number at which splitting a numeric attribute
	 *                      produces the best (highest) information gain
	 */
	public GainInfoItem(String attributeName, AttributeType attributeType, double gainValue, String splitAt) {
		// WRITE YOUR CODE HERE!
	}

	/**
	 * @return attribute name
	 */
	public String getAttributeName() {
		// WRITE YOUR CODE HERE!
		
		//Remove the following line when this method has been implemented
		return null;
	}

	/**
	 * @return attribute type
	 */
	public AttributeType getAttributeType() {
		// WRITE YOUR CODE HERE!
		
		//Remove the following line when this method has been implemented
		return null;
	}

	/**
	 * @return gain value
	 */
	public double getGainValue() {
		// WRITE YOUR CODE HERE!
		
		//Remove the following line when this method has been implemented
		return 0.0;
	}

	/**
	 * @return for numeric attributes, the value at which we the split is optimal
	 */
	public String getSplitAt() {
		// WRITE YOUR CODE HERE!
		
		//Remove the following line when this method has been implemented
		return null;
	}

	/**
	 * String representation of a GainInfoItem instance
	 */
	public String toString() {
		if (this.attributeType == AttributeType.NOMINAL) {
			return "(" + this.attributeName + ", " + String.format("%.6f", this.gainValue) + ")";
		}

		return "(" + this.attributeName + " [split at <= " + this.splitAt + "], "
				+ String.format("%.6f", this.gainValue) + ")";
	}

	/**
	 * Sort function for an array of GainInfoItem instances. This is a reverse sort,
	 * that is, the gain values are sorted in a descending order (highest gain comes
	 * first)
	 * 
	 * @param items is an array of GainInfoItem instances
	 */
	public static void reverseSort(GainInfoItem[] items) {
		// WRITE YOUR CODE HERE!		
	}
}