// You are allowed to use LinkedList or other Collection classes in A2 and A3
import java.util.LinkedList;

/**
 * This class is used for representing a virtual dataset, that is, a dataset
 * that is a view over an actual dataset. A virtual dataset has no data matrix
 * of its own.
 * 
 * @author Mehrdad Sabetzadeh, University of Ottawa
 * @author Guy-Vincent Jourdan, University of Ottawa
 *
 */
public class VirtualDataSet extends DataSet {

	/**
	 * reference to the source dataset (instance of ActualDataSet)
	 */
	private ActualDataSet source;

	/**
	 * array of integers mapping the rows of this virtual dataset to the rows of its
	 * source (actual) dataset
	 */
	private int[] map;

	/**
	 * Constructor for VirtualDataSet. There are two important considerations here:
	 * (1) Make sure that you keep COPIES of the "rows" and "attributes" passed as
	 * formal parameters. Do not, for example, say this.map = rows. Instead, create
	 * a copy of rows before assigning that copy to this.map. (2) Prune the value
	 * sets of the attributes. Since a virtual dataset is only a subset of an actual
	 * dataset, it is likely that some or all of its attributes may have smaller
	 * value sets.
	 * 
	 * @param source     is the source dataset (always an instance of ActualDataSet)
	 * @param rows       is the set of rows from the source dataset that belong to
	 *                   this virtual dataset
	 * @param attributes is the set of attributes belonging to this virtual dataset.
	 *                   IMPORTANT: you need to recalculate the unique value sets
	 *                   for these attributes according to the rows. Why? Because
	 *                   this virtual set is only a subset of the source dataset and
	 *                   its attributes potentially have fewer unique values.
	 */
	public VirtualDataSet(ActualDataSet source, int[] rows, Attribute[] attributes) {
		// WRITE YOUR CODE HERE!
		super();
		numRows = rows.length;
		numAttributes = attributes.length;
		Attribute[] copiedAttributes = new Attribute[attributes.length];
		for (int i=0; i < attributes.length; i++){
			copiedAttributes[i] = attributes[i].clone();
		}
		this.attributes = copiedAttributes;

		this.source = source;

		int[] temp = new int[rows.length];
		for (int i=0; i < rows.length; i++){
			temp[i] = rows[i];
		}
		this.map = temp;
	}

	/**
	 * String representation of the virtual dataset.
	 */
	public String toString() {
		// WRITE YOUR CODE HERE!
		StringBuffer buffer = new StringBuffer();

		String separator = System.getProperty("line.separator");

        buffer.append("Virtual dataset with " + numAttributes + " attribute(s) and " + numRows + " row(s)");

        buffer.append(separator);

        buffer.append(" - Dataset is a view over " + this.source.getSourceId());

        buffer.append(separator);

        LinkedList<Integer> mapCollection = new LinkedList<Integer>();
        for (int i=0; i < this.map.length; i++){
        	mapCollection.add(this.map[i]);
        }

        buffer.append(" - Row indices in this dataset (w.r.t. its source dataset): " + mapCollection);

        buffer.append(separator);

        buffer.append(super.toString());

		return buffer.toString();
	}

	/**
	 * Implementation of DataSet's getValueAt abstract method for virtual datasets.
	 * Hint: You need to call source.getValueAt(...). What you need to figure out is
	 * with what parameter values that method needs to be called.
	 */
	public String getValueAt(int row, int attributeIndex) {
		// WRITE YOUR CODE HERE!
		return this.source.getValueAt(row, attributeIndex /*?????????????????????????????????????????????*/);
	}

	/**
	 * @return reference to source dataset
	 */
	public ActualDataSet getSourceDataSet() {
		// WRITE YOUR CODE HERE!
		return this.source;
	}

	/**
	 * This method splits the virtual dataset over a nominal attribute. This process
	 * has been discussed and exemplified in detail in the assignment description.
	 * 
	 * @param attributeIndex is the index of the nominal attribute over which we
	 *                       want to split.
	 * @return a set (array) of partitions resulting from the split. The partitions
	 *         will no longer contain the attribute over which we performed the
	 *         split.
	 */
	public VirtualDataSet[] partitionByNominallAttribute(int attributeIndex) {
		// WRITE YOUR CODE HERE!
		
		//Attribute[] tempAttributes = new Attribute[this.attributes.length];

		String[] splitAttributeString = this.attributes[attributeIndex].getValues();
		String[] nDSplitAttributeString = getUniqueAttributeNOMINAL(this.attributes[attributeIndex].getValues());

		// here we are creating the map (row numbers)

		int[][] twoDMap = new int[nDSplitAttributeString.length][];
		int[] tempIntArray;

		for (int i = 0; i < nDSplitAttributeString.length; i++) {

			tempIntArray = new int[splitAttributeString.length];

			for (int j = 0; j < splitAttributeString.length; j++) {

				if ((nDSplitAttributeString[i].equals(splitAttributeString[j])) && (j == 0)){

					tempIntArray[j] = -1;
				}
				else if (nDSplitAttributeString[i].equals(splitAttributeString[j])){

					tempIntArray[j] = j;
				}
			}
			if (tempIntArray[0] == -1){
				tempIntArray[0] = 0;
				twoDMap[i] = getUniqueAttributeNUMERS(tempIntArray);
				sort(twoDMap[i]);

			}
			else{
				tempIntArray = getUniqueAttributeNUMERS(tempIntArray);
				int[] tempIntArray2 = new int[tempIntArray.length - 1];
				int counter = 0;
				for (int n=1; n < tempIntArray.length; n++){
					tempIntArray2[counter++] = tempIntArray[n];
				}
				twoDMap[i] = tempIntArray2;
				sort(twoDMap[i]);
			}
		}

		// here we are going thorugh each arrtibute (attributes have duplication) and make a new string array
		//w.r.t the map and then update the String[] of each attribute that we are already.

		VirtualDataSet[] finalVirtualDataSetArray = new VirtualDataSet[twoDMap.length];
		Attribute[] finalAttribute = new Attribute[this.attributes.length - 1];
		int counter = 0;
		String[] tempString;
		String[] oneAttribute;

		for (int i = 0; i < twoDMap.length; i++) {
			
			for (int n = 0; n < this.attributes.length; n++){

				tempString = new String[twoDMap[i].length];
				oneAttribute = this.attributes[n].getValues();

				for (int j = 0; j < twoDMap[i].length; j++) {

					tempString[j] = oneAttribute[twoDMap[i][j]];
				}

				if (n != attributeIndex){
					finalAttribute[counter] = new Attribute(attributes[n].getName(), attributes[n].getAbsoluteIndex(), attributes[n].getType(), getUniqueAttributeNOMINAL(tempString));
					counter++;
				}
			}
			counter = 0;

			this.map = twoDMap[i];

			finalVirtualDataSetArray[i] = new VirtualDataSet(this.source, this.map, finalAttribute);
		}
		return finalVirtualDataSetArray;
	}

	/**
	 * This method splits the virtual dataset over a given numeric attribute at a
	 * specific value from the value set of that attribute. This process has been
	 * discussed and exemplified in detail in the assignment description.
	 * 
	 * @param attributeIndex is the index of the numeric attribute over which we
	 *                       want to split.
	 * @param valueIndex     is the index of the value (in the value set of the
	 *                       attribute of interest) to use for splitting
	 * @return a pair of partitions (VirtualDataSet array of length two) resulting
	 *         from the two-way split. Note that the partitions will retain the
	 *         attribute over which we perform the split. This is in contrast to
	 *         splitting over a nominal, where the split attribute disappears from
	 *         the partitions.
	 */
	public VirtualDataSet[] partitionByNumericAttribute(int attributeIndex, int valueIndex) {
		// WRITE YOUR CODE HERE!

		String[] splitAttributeString = this.attributes[attributeIndex].getValues();
		String target = splitAttributeString[valueIndex];

		// here we are creating the map (row numbers)
		int[][] twoDMap = new int[2][];
		int[] firstArray = new int[splitAttributeString.length];
		int[] secondArray = new int[splitAttributeString.length];

		for (int n = 0; n < splitAttributeString.length; n++){

			try {

				if ((Integer.parseInt(splitAttributeString[n]) <= Integer.parseInt(target)) && (n == 0)){
					firstArray[n] = -1;
				}
				else if (Integer.parseInt(splitAttributeString[n]) <= Integer.parseInt(target)){
					firstArray[n] = n;
				}

				if ((Integer.parseInt(splitAttributeString[n]) > Integer.parseInt(target)) && (n == 0)){
					secondArray[n] = -1;
				}
				else if (Integer.parseInt(splitAttributeString[n]) > Integer.parseInt(target)){
					secondArray[n] = n;
				}

			}

			catch (Exception e){

				if ((Float.parseFloat(splitAttributeString[n]) <= Float.parseFloat(target)) && (n == 0)){
					firstArray[n] = -1;
				}
				else if ((Float.parseFloat(splitAttributeString[n]) <= Float.parseFloat(target))){
					firstArray[n] = n;
				}

				if ((Float.parseFloat(splitAttributeString[n]) > Float.parseFloat(target)) && (n == 0)){
					secondArray[n] = -1;
				}
				else if ((Float.parseFloat(splitAttributeString[n]) > Float.parseFloat(target))){
					secondArray[n] = n;
				}

			}
		}
		twoDMap[0] = firstArray;
		twoDMap[1] = secondArray;


		for (int i=0; i < twoDMap.length; i++){

			if (twoDMap[i][0] == -1){
				twoDMap[i][0] = 0;
				twoDMap[i] = getUniqueAttributeNUMERS(twoDMap[i]);
				sort(twoDMap[i]);
			}

			else{
				twoDMap[i] = getUniqueAttributeNUMERS(twoDMap[i]);
				int[] tempIntArray = new int[twoDMap[i].length - 1];
				int counter = 0;

				for (int n=1; n < twoDMap[i].length; n++){

					tempIntArray[counter++] = twoDMap[i][n];
				}

				twoDMap[i] = tempIntArray;
				sort(twoDMap[i]);
			}
		}

		// here we are going thorugh each arrtibute (attributes have duplication) and make a new string array
		//w.r.t the map and then update the String[] of each attribute that we are already.

		VirtualDataSet[] finalVirtualDataSetArray = new VirtualDataSet[twoDMap.length];
		Attribute[] finalAttribute = new Attribute[this.attributes.length];
		String[] tempString;
		String[] oneAttribute;

		for (int i = 0; i < twoDMap.length; i++) {

			for (int n = 0; n < this.attributes.length; n++){

				tempString = new String[twoDMap[i].length];
				oneAttribute = this.attributes[n].getValues();

				for (int j = 0; j < twoDMap[i].length; j++) {

					tempString[j] = oneAttribute[twoDMap[i][j]];
				}
					
				finalAttribute[n] = new Attribute(attributes[n].getName(), attributes[n].getAbsoluteIndex(), attributes[n].getType(), getUniqueAttributeNOMINAL(tempString));
			}

			this.map = twoDMap[i];

			finalVirtualDataSetArray[i] = new VirtualDataSet(this.source, this.map, finalAttribute);
		}

		return finalVirtualDataSetArray;

	}

	public static void main(String[] args) throws Exception {

		StudentInfo.display();

		System.out.println("============================================");
		System.out.println("THE WEATHER-NOMINAL DATASET:");
		System.out.println();

		ActualDataSet figure5Actual = new ActualDataSet(new CSVReader("weather-nominal.csv"));

		System.out.println(figure5Actual);

		VirtualDataSet figure5Virtual = figure5Actual.toVirtual();

		System.out.println("JAVA IMPLEMENTATION OF THE SPLIT IN FIGURE 5:");
		System.out.println();

		VirtualDataSet[] figure5Partitions = figure5Virtual
				.partitionByNominallAttribute(figure5Virtual.getAttributeIndex("outlook"));

		for (int i = 0; i < figure5Partitions.length; i++)
			System.out.println("Partition " + i + ": " + figure5Partitions[i]);

		System.out.println("============================================");
		System.out.println("THE WEATHER-NUMERIC DATASET:");
		System.out.println();

		ActualDataSet figure9Actual = new ActualDataSet(new CSVReader("weather-numeric.csv"));

		System.out.println(figure9Actual);

		VirtualDataSet figure9Virtual = figure9Actual.toVirtual();

		// Now let's figure out what is the index for humidity in figure9Virtual and
		// what is the index for "80" in the value set of humidity!

		int indexForHumidity = figure9Virtual.getAttributeIndex("humidity");

		Attribute humidity = figure9Virtual.getAttribute(indexForHumidity);

		String[] values = humidity.getValues();

		int indexFor80 = -1;

		for (int i = 0; i < values.length; i++) {
			if (values[i].equals("80")) {
				indexFor80 = i;
				break;
			}
		}

		if (indexFor80 == -1) {
			System.out.println("Houston, we have a problem!");
			return;
		}

		VirtualDataSet[] figure9Partitions = figure9Virtual.partitionByNumericAttribute(indexForHumidity, indexFor80);

		System.out.println("JAVA IMPLEMENTATION OF THE SPLIT IN FIGURE 9:");
		System.out.println();

		for (int i = 0; i < figure9Partitions.length; i++)
			System.out.println("Partition " + i + ": " + figure9Partitions[i]);

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





	private int[] getUniqueAttributeNUMERS(int[] oldArray) {

		int end = oldArray.length;
        for (int i = 0; i < end; i++) {
            for (int j = i + 1; j < end; j++) {
                if (oldArray[i] == oldArray[j]) {  

                    oldArray[j] = oldArray[end-1];
                    end --;
                    j --;
                }
            }
        }

        int[] emptyArray = new int[end];

        for(int i = 0; i < end; i++){

            emptyArray[i] = oldArray[i];
        }

        return emptyArray;
    
	}

	private void sort(int[] array) {

    boolean condition = true;
    int j = 0;
    int tmp;

    while (condition) {

        condition = false;
        j++;

        for (int i = 0; i < array.length - j; i++) {
            if (array[i] > array[i + 1]) {

                tmp = array[i];
                array[i] = array[i + 1];
                array[i + 1] = tmp;
                condition = true;
            }
        }
    }
	}
}