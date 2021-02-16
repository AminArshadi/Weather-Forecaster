import java.util.Scanner;
import java.util.Arrays;



/**
 * This class is used for representing an actual dataset, that is, a dataset
 * that holds a data matrix
 * 
 * @author Mehrdad Sabetzadeh, University of Ottawa
 * @author Guy-Vincent Jourdan, University of Ottawa
 *
 */
public class ActualDataSet extends DataSet {
	/**
	 * The data matrix
	 */
	private String[][] matrix;

	/**
	 * The source identifier for the data. When the data source is a file, sourceId
	 * will be the name and location of the source file
	 */
	private String dataSourceId;

	/**
	 * Constructor for ActualDataSet. In addition to initializing dataSourceId,
	 * numAttributes, numRows and matrix, the constructor needs to create an array of
	 * attributes (instance of the Attribute class) and initialize the "attributes"
	 * instance variable of DataSet.
	 * 
	 * 
	 * @param reader is the DataReader instance to read data from.
	 */
	public ActualDataSet(DataReader reader) {
		// WRITE YOUR CODE HERE!
		super();
		numAttributes = reader.getNumberOfColumns();
        numRows = reader.getNumberOfDataRows();

        dataSourceId = reader.getSourceId();
        matrix = reader.getData();

        
        

        //attributes = attributeCreater(numAttributes, attributeNames);

        ActualDataSet reader2 = new ActualDataSet(reader);







        String[][] reverseMatrix = new String[numAttributes][numRows];

        for (int i=0; i < numRows; i++){

            for (int j=0; j < matrix[i].length; j++){

                reverseMatrix[j][i] = matrix[i][j];
            }
        }

        Attribute[] arrayOfAttributes = new Attribute[reverseMatrix.length];
        String[] attributeNames = reader.getAttributeNames();

        for (int i=0; i < reverseMatrix.length; i++){

            if (Util.isArrayNumeric(reverseMatrix[i])){

                arrayOfAttributes[i] = new Attribute(attributeNames[i], i, AttributeType.NUMERIC, reverseMatrix[i]);
            }
            else{
                arrayOfAttributes[i] = new Attribute(attributeNames[i], i, AttributeType.NOMINAL, reverseMatrix[i]);
            }
        }

        attributes = arrayOfAttributes;

	}




/*
	public static void main(String[] args) throws Exception {

        System.out.print("Please enter the name of the CSV file to read: ");

        Scanner scanner = new Scanner(System.in);

        String strFilename = scanner.nextLine();

        CSVReader dataset = new CSVReader(strFilename);

        ActualDataSet actualDataSet = new ActualDataSet(dataset);

        System.out.println(actualDataSet.toString());

        //String[] array1 = {"a", "b", "c"};

        //String[] array2 = {"d", "e", "f"};

        //Attribute amin = new Attribute("amin", 2, AttributeType.NOMINAL, array1);

        //System.out.println(amin.getName());
        //System.out.println(amin.getAbsoluteIndex());
        //System.out.println(amin.getType());
        //System.out.println(Arrays.toString(amin.getValues()));
        //amin.replaceValues(array2);
        //System.out.println(amin.clone());
        //System.out.println(amin.toString());
        //System.out.println(amin.clone() == amin);


    }

*/





	/**
	 * Implementation of DataSet's abstract getValueAt method for an actual dataset
	 */
	public String getValueAt(int row, int attributeIndex) {
		// WRITE YOUR CODE HERE!
		
        return matrix[row][attributeIndex];
	}

	/**
	 * @return the sourceId of the dataset.
	 */
	public String getSourceId() {
		// WRITE YOUR CODE HERE!
		return dataSourceId;
	}

	/**
	 * Returns a virtual dataset over this (actual) dataset
	 * 
	 * @return a virtual dataset spanning the entire data in this (actual) dataset
	 */
	public VirtualDataSet toVirtual() {
		// WRITE YOUR CODE HERE!



        int[] rows = new int[numRows];
        for (int i=0; i < numRows; i++){
            rows[i] = i;
        }

        //ActualDataSet mainSource = new ActualDataSet(reader2);

        
        return new VirtualDataSet(a, rows, attributes);;


	}

	/**
	 * Override of toString() in DataSet
	 * 
	 * @return a string representation of this (actual) dataset.
	 */
	public String toString() {
		// WRITE YOUR CODE HERE!
		StringBuffer buffer = new StringBuffer();

		String separator = System.getProperty("line.separator");

        buffer.append("Actual dataset (" + dataSourceId + ") with " + numAttributes + " attribute(s) and " + numRows + " row(s)");

        buffer.append(separator);

        buffer.append(super.toString());

		return buffer.toString();
	}








	private Attribute[] attributeCreater(int numAttributes, String[] attributeNames){
		Attribute[] arrayOfAttributes = new Attribute[numAttributes];
		AttributeType type1 = AttributeType.NUMERIC;
		AttributeType type2 = AttributeType.NOMINAL;

		for (int i=0; i < numAttributes; i++){

			String[] tempValue = getUniqueAttribute(i);

			if (Util.isArrayNumeric(tempValue)){

                arrayOfAttributes[i] = new Attribute(attributeNames[i], i, type1, tempValue);
            }
            else {

                arrayOfAttributes[i] = new Attribute(attributeNames[i], i, type2, tempValue);
            }     
        } 
        return arrayOfAttributes;
	}

	private String[] getUniqueAttribute(int column) {
        String[][] reverseMatrix = new String[numAttributes][numRows];

        // Changing a (n * m) matrix to a (m * n) one.
        for (int i=0; i < numRows; i++){
            for (int j=0; j < matrix[i].length; j++){
                reverseMatrix[j][i] = matrix[i][j];
            }
        }
        
        // Removing any repeated element in arrays of matrix.
        int end = reverseMatrix[column].length;
        for (int i = 0; i < end; i++) {
            for (int j = i + 1; j < end; j++) {
                if ((reverseMatrix[column][i] != null) && (reverseMatrix[column][j] != null) && reverseMatrix[column][i].equals(reverseMatrix[column][j])) {  

                    reverseMatrix[column][j] = reverseMatrix[column][end-1];
                    end --;
                    j --;
                }
            }
        }

        String[] emptyArray = new String[end];

        for(int i = 0; i < end; i++){

            emptyArray[i] = reverseMatrix[column][i];
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
        String[] a = new String[emptyArray.length - finalCounter];
        int n = 0;

        for (int k=0; k < emptyArray.length; k++){
            if (!(emptyArray[k] == null || emptyArray[k].isEmpty())){
                a[n] = emptyArray[k];
                n ++;
            }
        }
        // Replacing "null" with "MISSING" in arrays if there is any.
        if (ifNull > 0){
            a[a.length - 1] = "MISSING";
        }
        return a;
    }
}




