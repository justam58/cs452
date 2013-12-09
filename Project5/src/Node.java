import java.text.DecimalFormat;
import java.util.ArrayList;


public class Node {
	
	private ArrayList<Node> branches;
	private String result;
	private String split;
	private String splitBy;
	private double gain;
	private String name;
	private int level;
	
	public Node(int level){
		this.level = level;
		branches = new ArrayList<Node>();
	}
	
	public Node(String name, int level){
		this.name = name;
		this.level = level;
		branches = new ArrayList<Node>();
	}
	
	public void addBranch(Node child){
		branches.add(child);
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getSplit() {
		return split;
	}

	public void setSplit(String split) {
		this.split = split;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public double getGain() {
		return gain;
	}

	public void setGain(double gain) {
		this.gain = gain;
	}

	public String getSplitBy() {
		return splitBy;
	}

	public void setSplitBy(String splitBy) {
		this.splitBy = splitBy;
	}

	public ArrayList<Node> getBranches() {
		return branches;
	}

	public void setBranches(ArrayList<Node> branches) {
		this.branches = branches;
	}

	@Override
	public String toString() {
		String result = "";
		DecimalFormat format = new DecimalFormat("0.000");
		for(int i = 0; i < level; i++){
			result += "    ";
		}
		if(name == null){
			result += "Decision Tree Construction ";
		}
		else{
			result += "[" + splitBy + " = " + name +"] ";
		}
		if(split != null){
			result += "split by " + split + " for a gain of " + format.format(gain) + "\n";
		}
		else{
			result += this.result + "\n";
		}
		for(int i = 0; i < branches.size(); i++){
			result += branches.get(i).toString();
		}
		return result;
	}
	
}
