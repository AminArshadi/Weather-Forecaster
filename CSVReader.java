import java.io.File;
import java.util.Scanner;
import java.util.Arrays;

/**
 * This class provides an implementation of the DataReader interface for CSV
 * files
 * 
 * @author Mehrdad Sabetzadeh, University of Ottawa
 * @author Guy-Vincent Jourdan, University of Ottawa
 *
 */
public class CSVReader implements DataReader {
    // WRITE YOUR CODE HERE!
    private int numColumns;
    private int numRows;
    private String[] attributeNames;
    private String[][] matrix;
    private String fileName;
    


/*
/////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////

    public static void main(String[] args) throws Exception {

        System.out.print("Please enter the name of the CSV file to read: ");

        Scanner scanner = new Scanner(System.in);

        String strFilename = scanner.nextLine();

        CSVReader dataset = new CSVReader(strFilename);

        System.out.println(Arrays.deepToString(dataset.getData()));
        System.out.println(dataset.getAttributeNames());
        System.out.println(dataset.getNumberOfColumns());
        System.out.println(dataset.getNumberOfDataRows());
        System.out.println(dataset.getSourceId());
    }
/////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////
*/





    /**
     * Constructs a dataset by loading a CSV file
     * 
     * @param strFilename is the name of the file
     */
    public CSVReader(String strFilename) throws Exception {
        // WRITE YOUR CODE HERE!
        fileName = strFilename;
        calculateDimensions(strFilename);
        instantiateFromFile(strFilename);
    }


    public String[] getAttributeNames() {
        // WRITE YOUR CODE HERE!
        return attributeNames;
    }

    public String[][] getData() {
        // WRITE YOUR CODE HERE!
        return matrix;
    }

    public String getSourceId() {
        // WRITE YOUR CODE HERE!
        return fileName;
    }

    public int getNumberOfColumns() {
        // WRITE YOUR CODE HERE!
        return numColumns;
    }

    public int getNumberOfDataRows() {
        // WRITE YOUR CODE HERE!
        return numRows;
    }







    private void calculateDimensions(String strFilename) throws Exception {

        Scanner scanner = new Scanner(new File(strFilename));
        
        // This varaible is for counting the lines.
        int counter = 0;

        // This variable is for attribute names, which are located at the first line.
        String[] firstLine;

        while (scanner.hasNext()) {
        
            String str = scanner.nextLine();
            if (counter == 0){

                // The first line (attribute names) is being splited with respect to having a apostrophe or not and comma.
                firstLine = str.split(",(?=(?:[^\']*\'[^\']*\')*[^\']*$)");
                numColumns = firstLine.length;
            }
            else{
                // Checking if a row is not empty.
                if (!(str != null && (str.trim().equals("") || str.trim().equals("\n")))){
                    numRows++;
                }
            }
            counter++;
        }
        // Initializing the size of matrix. The plus 1
        // is becuase counter does not count the first line.
        matrix = new String[numRows + (counter - numRows) + 1][numColumns];
        scanner.close();
    }
    /**
     * This method should load the attribute names into the attributeNames
     * instance variable and load the datapoints into the matrix instance variable.
     * The method is incomplete; you need to complete it.
     * @param strFilename is the name of the file to read
     */
    private void instantiateFromFile(String strFilename) throws Exception {
        Scanner scanner = new Scanner(new File(strFilename));

        int counter = 0;

        while (scanner.hasNext()) {
        
            String str = scanner.nextLine();
            if (counter == 0){

                attributeNames = str.split(",(?=(?:[^\']*\'[^\']*\')*[^\']*$)");

                for (int i=0; i < attributeNames.length; i++){

                    if (attributeNames[i] != null){
                        attributeNames[i] = attributeNames[i].trim().replace("'", "");
                    }
                }
            }

            else if (counter < numRows + 2){

                matrix[counter] = str.split(",(?=(?:[^\']*\'[^\']*\')*[^\']*$)");

                for (int i=0; i < matrix[counter].length; i++){

                    if (matrix[counter][i] != null){
                        matrix[counter][i] = matrix[counter][i].trim().replace("'", "");
                    }
                }
            }
            counter++;
        }
        String[][] result = noEmptyOrNullArray(matrix);
        matrix = result;
        scanner.close();
    }
    /**
     * Removes all empty or null arrays from matrix.
     * 
     * @return String[][] containing the final matrix elements.
     */
    private String[][] noEmptyOrNullArray (String[][] matrix) {

        String[][] temMatrix2 = new String[numRows][numColumns];

        int nullCounter = 0;
        int tele = 0;
        int f = 0;

        for (int i=0 ; i < numRows + 2 ; i++){
            if (matrix[i].length == 1){
                nullCounter ++;
            }
            else{
                for (int j=0; j < matrix[i].length; j++){
                    if (matrix[i][j] == null){
                        tele ++;
                    }
                }
                if (tele != matrix[i].length){
                    nullCounter ++;
                    temMatrix2[f] = matrix[i];
                    f ++;
                }
                tele = 0;
            }
        }
        return temMatrix2;
    }
}
























