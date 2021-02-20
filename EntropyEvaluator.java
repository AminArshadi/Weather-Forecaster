/**
 * This class enables calculating (weighted-average) entropy values for a set of
 * datasets
 * 
 * @author Mehrdad Sabetzadeh, University of Ottawa
 * @author Guy-Vincent Jourdan, University of Ottawa
 *
 */
public class EntropyEvaluator {

	/**
	 * A static method that calculates the weighted-average entropy of a given set
	 * (array) of datasets. The assignment description provides a detailed
	 * explanation of this calculation. In particular, note that all logarithms are
	 * to base 2. For your convenience, we provide a log2 method. You can use this
	 * method wherever you need to take logarithms in this assignment.
	 * 
	 * @param partitions is the array of datasets to compute the entropy of
	 * @return Shannon's logarithmic entropy (to base 2) for the partitions
	 */
	public static double evaluate(DataSet[] partitions) {
		// WRITE YOUR CODE HERE!
        String[] lastColumn;
        String[] nDLastColumn;
		double result = 0.0;
		int conditionCounter = 0;
		int[] conditionCounterSaver;


		for (int i=0; i < partitions.length; i++) {

			lastColumn = partitions[i]  . . . ??????? ;
			nDLastColumn = getUniqueAttributeNOMINAL(lastColumn);     //partitions[i].getAttribute( ??? partitions[i].map.length - 1 ??? ).getValues();

			conditionCounterSaver = new int[nDLastColumn.length];

			for (int j=0; j < nDLastColumn.length; j++) {

				for (int n=0; n < lastColumn.length; n++) {

					if (nDLastColumn[j].equals(lastColumn[n])){

						conditionCounter ++;
					}
				}
				conditionCounterSaver[j] = conditionCounter;
				conditionCounter = 0;
			}

			for (int a=0; a < conditionCounterSaver.length; a++) {

				result = result + ((-1) * ((double) firstConditionCounter / lastColumn.length) * log2(((double) firstConditionCounter / lastColumn.length)));
			}
		}

		return result;
	}

	/**
	 * Calculate base-2 logarithm for a given number
	 * 
	 * @param x is the number to take the logarithm of
	 * @return base-2 logarithm for x
	 */
	public static double log2(double x) {
		return (Math.log(x) / Math.log(2));
	}



















	private String[] getUniqueAttributeNOMINAL(String[] oldArray) {
		String[] newArray = new String[oldArray.length];
		for (int i = 0; i < newArray.length; i++) {
			newArray[i] = oldArray[i];
		}
		int end = newArray.length;
        for (int i = 0; i < end; i++) {
            for (int j = i + 1; j < end; j++) {
                if ((newArray[i] != null) && (newArray[j] != null) && (newArray[i].equals(newArray[j]))) {  

                    newArray[j] = newArray[end-1];
                    end --;
                    j --;
                }
            }
        }

        String[] emptyArray = new String[end];

        for(int i = 0; i < end; i++){

            emptyArray[i] = newArray[i];
        }

        // The number of empty or null elements in the chosen array are being counted.
        String[] finalArray = new String[emptyArray.length];
        int finalCounter = 0;
        int ifNull = 0;

        for (int k=0; k < emptyArray.length; k++){

            if (emptyArray[k] == null || emptyArray[k].isEmpty()){

                finalCounter ++;
                ifNull ++;
            }
        }
        if (ifNull > 0){
            finalCounter --;
        }
        // The empty or null elements in the chosen array are being removed.
        String[] finalStringArray = new String[emptyArray.length - finalCounter];
        int n = 0;

        for (int k=0; k < emptyArray.length; k++){
            if (!(emptyArray[k] == null || emptyArray[k].isEmpty())){
                finalStringArray[n] = emptyArray[k];
                n ++;
            }
        }
        // Replacing "null" with "MISSING" in arrays if there is any.
        if (ifNull > 0){
            finalStringArray[finalStringArray.length - 1] = "MISSING";
        }
        return finalStringArray;
    
	}
}









