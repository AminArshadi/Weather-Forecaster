import java.util.Arrays;


/**
 * This class enables the calculation and sorting of information gain values
 * 
 * @author Mehrdad Sabetzadeh, University of Ottawa
 * @author Guy-Vincent Jourdan, University of Ottawa
 *
 */
public class InformationGainCalculator {

	/**
	 * @param dataset is the dataset whose attributes we want to analyze and sort
	 *                according to information gain
	 * @return an array of GainInfoItem instances sorted in descending order of gain
	 *         value
	 */
	public static GainInfoItem[] calculateAndSortInformationGains(VirtualDataSet dataset) {
		// WRITE YOUR CODE HERE!
		
		GainInfoItem[] gainInfoItem = new GainInfoItem[dataset.numAttributes - 1];

		String[] lastColumn = new String[dataset.numRows];
		for (int i=0; i < dataset.numRows; i++){
			lastColumn[i] = dataset.getValueAt(i, dataset.numAttributes - 1);
		}
		double entropyOfLastColumnOfDataset = 0.0;
		String[] nDLastColumn = getUniqueAttributeNOMINAL(lastColumn);
		int[] conditionCounterSaver = new int[nDLastColumn.length];
		double result = 0.0;
		int conditionCounter = 0;

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

			entropyOfLastColumnOfDataset = entropyOfLastColumnOfDataset + ((-1) * ((double) conditionCounterSaver[a] / lastColumn.length) * EntropyEvaluator.log2(((double) conditionCounterSaver[a] / lastColumn.length)));
		}
		



		for (int i=0; i < dataset.numAttributes - 1; i++) {

			Attribute eachAttribute = dataset.getAttribute(i);

			if (Util.isArrayNumeric(eachAttribute.getValues())) {

				String[] numericColumn = eachAttribute.getValues();
				double bestGain = 0.0;
				double currentGain = 0.0;

				String vlaueAtBestGain = numericColumn[0];

				for (int n=0; n < numericColumn.length; n++){

					currentGain = (entropyOfLastColumnOfDataset - EntropyEvaluator.evaluate(dataset.partitionByNumericAttribute(i, n)));

					if (currentGain > bestGain){

						bestGain = currentGain;
						vlaueAtBestGain = numericColumn[n];
					}
				}
				GainInfoItem gain = new GainInfoItem(dataset.attributes[i].getName(), AttributeType.NUMERIC, bestGain, vlaueAtBestGain);
				gainInfoItem[i] = gain;
			}

			else {

				double gainOfAttribute = (entropyOfLastColumnOfDataset - EntropyEvaluator.evaluate(dataset.partitionByNominallAttribute(i)));

				GainInfoItem gain = new GainInfoItem(dataset.attributes[i].getName(), AttributeType.NOMINAL, gainOfAttribute, null);

				gainInfoItem[i] = gain;

			}
		}
		GainInfoItem.reverseSort(gainInfoItem);
		return gainInfoItem;
	}










	public static void main(String[] args) throws Exception {

		StudentInfo.display();

		if (args == null || args.length == 0) {
			throw new IllegalArgumentException();
		}

		String strFilename = args[0];

		ActualDataSet actual = new ActualDataSet(new CSVReader(strFilename));

		// System.out.println(actual);

		VirtualDataSet virtual = actual.toVirtual();

		// System.out.println(virtual);

		GainInfoItem[] items = calculateAndSortInformationGains(virtual);

		// Print out the output
		System.out.println(
				" *** items represent (attribute name, information gain) in descending order of gain value ***");
		System.out.println();

		for (int i = 0; i < items.length; i++) {
			System.out.println(items[i]);
		}
	}


















	private static String[] getUniqueAttributeNOMINAL(String[] oldArray) {
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