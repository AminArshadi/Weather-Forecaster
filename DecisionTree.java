/**
 * This class enables the construction of a decision tree
 * 
 * @author Mehrdad Sabetzadeh, University of Ottawa
 * @author Guy-Vincent Jourdan, University of Ottawa
 *
 */

public class DecisionTree {

	private static class Node<E> {
		E data;
		Node<E>[] children;

		Node(E data) {
			this.data = data;

			//children = (Node<E>[]) new Object[data.attributes.length];
		}
	}

	Node<VirtualDataSet> root;

	/**
	 * @param data is the training set (instance of ActualDataSet) over which a
	 *             decision tree is to be built
	 */
	public DecisionTree(ActualDataSet data) {
		root = new Node<VirtualDataSet>(data.toVirtual());
		build(root);
	}

	/**
	 * The recursive tree building function
	 * 
	 * @param node is the tree node for which a (sub)tree is to be built
	 */
	@SuppressWarnings("unchecked")
	private void build(Node<VirtualDataSet> node) {
		// WRITE YOUR CODE HERE!
		//1. Edge cases:
		if (node == null){
			throw new NullPointerException("Argument --node-- in method --build-- cannot be null.");
		}

		if (node.data.attributes.length < 1){
			throw new IllegalStateException("There should be at least one attribute.");
		}
		
		boolean flag = true;
		for (int i=0; i < node.data.attributes.length; i++){
			if (node.data.attributes[i].getValues().length >= 1){
				flag = false;
			}
		}
		if (flag){
			throw new IllegalStateException("There should be at least one datapoint.");
		}


		//2. Base cases:
		if (node.data.attributes.length == 1){
			toString();
		}

		if (getUniqueAttributeNOMINAL( node.data.attributes[node.data.attributes.length - 1].getValues() ).length == 1){
			toString();
		}

		flag = true;
		for (int i=0; i < node.data.attributes.length - 2; i++){ // -2 becuase non-calss attributes
			if (node.data.attributes[i].getValues().length > 1){
				flag = false;
			}
		}
		if (flag){
			toString();
		}
			

		
		//3. Recursive case:
		else{
			GainInfoItem[] sortedGainList = InformationGainCalculator.calculateAndSortInformationGains(node.data);
			GainInfoItem a_max = sortedGainList[0];

			int i = 0;
			int j = 0;
			
			//for nominal attributes
			if (a_max.getAttributeType() == AttributeType.NOMINAL){

				for (int n=0; n < node.data.attributes.length; n++){

					if (node.data.attributes[n].getName().equals( a_max.getAttributeName() )){
						i = n;
					}
				}

				VirtualDataSet[] tmp = node.data.partitionByNominallAttribute(i);
				node.children = new Node[tmp.length]; //children = (Node<E>[]) new Object[data.attributes.length];

				for (int t=0; t < tmp.length; t ++){
					node.children[t] = new Node<VirtualDataSet>(tmp[t]);
				}

				for (int n=0; n < node.children.length; n++) {
					build(node.children[n]);
				}
			}

			//for numeric attributes
			else{
				for (int n=0; n < node.data.attributes.length; n++){

					if (node.data.attributes[n].getName().equals( a_max.getAttributeName() )){
						i = n;
					}
				}
				for (int k=0; k < node.data.attributes[i].getValues().length; k++){

					String[] targetRow = node.data.attributes[i].getValues();

					if (targetRow[k].equals( a_max.getSplitAt() )){
						j = k;
					}
				}

				VirtualDataSet[] tmp = node.data.partitionByNumericAttribute(i, j);
				node.children = new Node[tmp.length];

				for (int t=0; t < tmp.length; t ++){
					node.children[t] = new Node<VirtualDataSet>(tmp[t]);
				}

				for (int n=1; n < node.children.length - 1; n++) {
					//System.out.println("here");
					build(node.children[n]);
				}
			}
		}
	}

	@Override
	public String toString() {
		return toString(root, 0);
	}

	/**
	 * The recursive toString function
	 * 
	 * @param node        is the tree node for which an if-else representation is to
	 *                    be derived
	 * @param indentDepth is the number of indenting spaces to be added to the
	 *                    representation
	 * @return an if-else representation of node
	 */
	private String toString(Node<VirtualDataSet> node, int indentDepth) {
		// WRITE YOUR CODE HERE!
		StringBuffer buffer = new StringBuffer();

		GainInfoItem[] check = InformationGainCalculator.calculateAndSortInformationGains(node.data); // if (check[0] == 0.0) is true, we know that we should return yes or no becuase we cannot go any further.
		//System.out.println("bbb");
		if(check[0].getGainValue() == 0.0){

			//System.out.println("aaa");

			buffer.append(createIndent(indentDepth + 1));

			String classAttributeName = node.data.attributes[node.data.attributes.length - 1].getName();
			String[] classAttributeValue = getUniqueAttributeNOMINAL(node.data.attributes[node.data.attributes.length - 1].getValues());

			buffer.append(classAttributeName + " = " + classAttributeValue[0]); /// we need to find a way to acess the last column (yes's or no's) of final partition. ////

			buffer.append(System.lineSeparator());

		}
		else if(node.children == null){
			//System.out.println("kkk");
			buffer.append(createIndent(indentDepth + 1));

			String classAttributeName = node.data.attributes[node.data.attributes.length - 1].getName();
			String[] classAttributeValue = getUniqueAttributeNOMINAL(node.data.attributes[node.data.attributes.length - 1].getValues());

			buffer.append(classAttributeName + " = " + classAttributeValue[0]); /// we need to find a way to acess the last column (yes's or no's) of final partition. ////

			buffer.append(System.lineSeparator());
			
		}
		else{

			//build(node);
			//System.out.println("aaa");

			for (int i = 0; i < node.children.length; i++) {

				String condition = node.children[i].data.getCondition();

				if (i == 0){

					buffer.append(createIndent(indentDepth));

					buffer.append("if (" + condition + ") {");

					buffer.append(System.lineSeparator());

					buffer.append(toString(node.children[i], indentDepth + 1));

					//buffer.append(System.lineSeparator());

					buffer.append(createIndent(indentDepth));

					buffer.append("}");

					buffer.append(System.lineSeparator());

				}
				else if ((i != 0) && (i != node.children.length - 1)){

					buffer.append(createIndent(indentDepth));

					buffer.append("else if (" + condition + ") {");

					buffer.append(System.lineSeparator());

					buffer.append(toString(node.children[i], indentDepth + 1));

					//buffer.append(System.lineSeparator());

					buffer.append(createIndent(indentDepth));

					buffer.append("}");

					buffer.append(System.lineSeparator());

				}
				// (i == node.children.length - 1)
				else{

					buffer.append(createIndent(indentDepth));

					buffer.append("else if (" + condition + ") {");

					buffer.append(System.lineSeparator());

					buffer.append(toString(node.children[i], indentDepth + 1));

					//buffer.append(System.lineSeparator());

					buffer.append(createIndent(indentDepth));

					buffer.append("}");

					buffer.append(System.lineSeparator());
				}
			}
		}
		return buffer.toString();
	}

	/**
	 * @param indentDepth is the depth of the indentation
	 * @return a string containing indentDepth spaces; the returned string (composed
	 *         of only spaces) will be used as a prefix by the recursive toString
	 *         method
	 */
	private static String createIndent(int indentDepth) {
		if (indentDepth <= 0) {
			return "";
		}
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < indentDepth; i++) {
			buffer.append(' ');	
		}
		return buffer.toString();
	}

	public static void main(String[] args) throws Exception {
	
		StudentInfo.display();

		if (args == null || args.length == 0) {
			System.out.println("Expected a file name as argument!");
			System.out.println("Usage: java DecisionTree <file name>");
			return;
		}

		String strFilename = args[0];

		ActualDataSet data = new ActualDataSet(new CSVReader(strFilename));

		DecisionTree dtree = new DecisionTree(data);

		System.out.println(dtree);
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