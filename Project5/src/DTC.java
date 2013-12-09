import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;


public class DTC {
	
	private String target;
	private ArrayList<String> attributes;
	private ArrayList<ArrayList<String>> examples;
		
	public DTC(String target, ArrayList<String> attributes,
			ArrayList<ArrayList<String>> examples) {
		
		this.target = target;
		this.attributes = attributes;
		this.examples = examples;

	}
	
	public Node construct(int level){
//		System.out.println(examples);
		Node root = new Node(level);
		if(checkAllExamples("Yes")){
//			System.out.println("here1");
			root.setResult("+");
			return root;
		}
		if(checkAllExamples("No")){
//			System.out.println("here2");
			root.setResult("-");
			return root;
		}
		if(attributes.size() == 0){
//			System.out.println("here3");
			root.setResult(getMostCommonResult());
			return root;
		}
		
		int A = getHighestInfoGain();
		root.setSplit(attributes.get(A));
		root.setGain(getGain(A,getExpectedInformationNeeded()));
//		System.out.println("split");
//		System.out.println(attributes.get(A));
//		System.out.println("gain is");
//		System.out.println(getGain(A,getExpectedInformationNeeded()));
//		System.out.println("");
		HashSet<String> sub = getSubCat(A);
		ArrayList<String> subAttributes = new ArrayList<String>();
		for(int i = 0; i < attributes.size(); i++){
			if(A != i){
				subAttributes.add(attributes.get(i));
			}
		}
		for(String cat : sub){
//			System.out.println("sub is");
//			System.out.println(cat);
			Node subNode = new Node(cat,level);
			ArrayList<ArrayList<String>> subExamples = getSubExamples(A, cat);
			if(subExamples.isEmpty()){
				root.setResult(getMostCommonResult());
			}
			else{
//				System.out.println("sub attrs are");
//				System.out.println(subAttributes);
				DTC subTree = new DTC(target,subAttributes,subExamples);
				subNode = subTree.construct(level+1);
				subNode.setName(cat);
				subNode.setSplitBy(attributes.get(A));
			}
			root.addBranch(subNode);
		}
		return root;
	}
	
	private ArrayList<ArrayList<String>> getSubExamples(int index, String cat){
		ArrayList<ArrayList<String>> result = new ArrayList<ArrayList<String>>();
		for(int i = 0; i < examples.size(); i++){
			if(examples.get(i).get(index).equals(cat)){
				ArrayList<String> example = examples.get(i);
				example.remove(index);
				result.add(example);
			}
		}
		return result;
	}
	
	private HashSet<String> getSubCat(int index){
		HashSet<String> result = new HashSet<String>();
		for(int i = 0; i < examples.size(); i++){
			String catogory = examples.get(i).get(index);
			result.add(catogory);
		}
		return result;
	}

	private int getHighestInfoGain() {
		int index = -1;
		double I = getExpectedInformationNeeded();
		for(int i = 0; i < attributes.size(); i++){
			double gain = getGain(i,I);
			if(index == -1){
				index = i;
			}
			else if(gain > getGain(index,I)){
				index = i;
			}
		}
		return index;
	}
	
	private double getExpectedInformationNeeded() {
		double t = 0;
		double f = 0;
		for(int i = 0; i < examples.size(); i++){
			if(examples.get(i).get(attributes.size()).equals("Yes")){
				t++;
			}
			else{
				f++;
			}
		}
		double total = t + f;
		double tp = t/(double)total;
		double fp = f/(double)total;
		if(fp == 0){
			return 0;
		}
		if(tp == 0){
			return 0;
		}
		return -tp*log2(tp)-(fp*log2(fp));
	}

	private double getExpectedInformationNeeded(int index, String catogory) {
		double t = 0;
		double f = 0;
		for(int i = 0; i < examples.size(); i++){
			if(examples.get(i).get(index).equals(catogory)&& examples.get(i).get(attributes.size()).equals("Yes")){
				t++;
			}
			else if(examples.get(i).get(index).equals(catogory)&& examples.get(i).get(attributes.size()).equals("No")){
				f++;
			}
		}
		double total = t + f;
		double tp = t/total;
		double fp = f/total;
		if(fp == 0){
			return 0;
		}
		if(tp == 0){
			return 0;
		}
		return -tp*log2(tp)-(fp*log2(fp));
	}
	
	private double log2(double n){
		double a = Math.log(n);
		double b = Math.log(2);
		return a/b;
	}
	
	private double getEntropy(int index){
		double result = 0;
		HashMap<String, Integer> catMap = new HashMap<String, Integer>();
		for(int i = 0; i < examples.size(); i++){
			String catogory = examples.get(i).get(index);
			if(!catMap.containsKey(catogory)){
				catMap.put(catogory, 0);
			}
			catMap.put(catogory, catMap.get(catogory)+1);
		}
		for (String cat : catMap.keySet()){
			double I = getExpectedInformationNeeded(index,cat);
			double p = catMap.get(cat)/(double)examples.size();
			result += I * p;
		}
		return result;
	}

	private double getGain(int index, double I) {
//		System.out.println("I is");
//		System.out.println(I);
//		System.out.println("E is");
//		System.out.println(getEntropy(index));
		return I - getEntropy(index);
	}

	private boolean checkAllExamples(String tar) {
//		System.out.println(attributes.size());
		for(int i = 0; i < examples.size(); i++){
//			System.out.println(examples.get(i));
			if(!examples.get(i).get(attributes.size()).equals(tar)){
				return false;
			}
		}
		return true;
	}
	
	private String getMostCommonResult() {
		int t = 0;
		int f = 0;
		for(int i = 0; i < examples.size(); i++){
			if(examples.get(i).equals("Yes")){
				t++;
			}
			else{
				f++;
			}
		}
		if(t >= f){
			return "+";
		}
		return "-";
	}
	
}
