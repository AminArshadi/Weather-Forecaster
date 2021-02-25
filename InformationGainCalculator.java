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

		double entropyOfLastColumnOfDataset = EntropyEvaluator.evaluate(dataset.partitionByNominallAttribute(dataset.numAttributes - 1));

					//System.out.println(entropyOfLastColumnOfDataset);

		for (int i=0; i < dataset.numAttributes - 1; i++) {       // we don't go through the last column to calculate its gain

			Attribute eachAttribute = dataset.getAttribute(i);

			if (Util.isArrayNumeric(eachAttribute.getValues())) {

				String[] numericColumn = eachAttribute.getValues();
				double bestGain = 0.0;
				double currentGain = (entropyOfLastColumnOfDataset - EntropyEvaluator.evaluate(dataset.partitionByNumericAttribute(i, 1)));

				//System.out.println(currentGain);

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
			System.out.println("Expected a file name as argument!");
			System.out.println("Usage: java InformationGainCalculator <file name>");
			return;
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
}
