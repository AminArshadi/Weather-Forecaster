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
		
		boolean flag = false;
		for (int i=0; i < node.data.attributes.length; i++){
			if (node.data.attributes[i].getValues().length >= 1){
				flag = true;
			}
		}
		if (flag){
			throw new IllegalStateException("There should be at least one datapoint.");
		}

		//2. Base cases:
		flag = true;

		if (node.data.attributes.length == 1){
			return;
		}

		else if (node.data.attributes[node.data.attributes.length - 1].getValues().length == 1){
			return;
		}

		for (int i=0; i < node.data.attributes.length - 1; i++){
			if (node.data.attributes[i].getValues().length > 1){
				flag = false;
			}
		}
		if (flag){
			return;
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

					if (node.data.attributes[n].getName() == a_max.getAttributeName()){
						i = n;
					}
				}

				VirtualDataSet[] tmp = node.data.partitionByNominallAttribute(i);
				node.children = (Node<VirtualDataSet>[]) new Object[tmp.length];
				for (int t=0; t < tmp.length; t ++){
					node.children[t] = (Node<VirtualDataSet>) (Object) tmp[t];
				}
			}
			//for numeric attributes
			else{
				for (int n=0; n < node.data.attributes.length; n++){

					if (node.data.attributes[n].getName() == a_max.getAttributeName()){
						i = n;
					}
				}
				for (int k=0; k < node.data.attributes[i].getValues().length; k++){

					String[] targetRow = node.data.attributes[i].getValues();

					if (targetRow[k] == a_max.getSplitAt()){
						j = k;
					}
				}

				VirtualDataSet[] tmp = node.data.partitionByNumericAttribute(i, j);
				node.children = (Node<VirtualDataSet>[]) new Object[tmp.length];
				for (int t=0; t < tmp.length; t ++){
					node.children[t] = (Node<VirtualDataSet>) (Object) tmp[t];
				}

				for (int n=0; n < node.children.length; n++) {
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
		
		// Remove the following line once you have implemented the method
		return null;
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
}