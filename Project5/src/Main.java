import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;


public class Main {
	
	static Scanner scc = new Scanner(System.in);

	public static void main(String[] args) throws FileNotFoundException {
		
		System.out.println("Please enter file name:");
		
		File srcFile = new File(scc.next());
		Scanner sc = new Scanner(srcFile);
		String allAttr = "";
		if(sc.hasNextLine()){
			allAttr = sc.nextLine();
		}
		else{
			System.out.println("Incorrect Format File.");
			sc.close();
			scc.close();
			return;
		}
		String[] allAttrs = allAttr.split(",");
		String tarAttr = allAttrs[allAttrs.length-1].trim();
		ArrayList<String> attrs = new ArrayList<String>();
		for(int i = 0; i < allAttrs.length-1; i++){
			attrs.add(allAttrs[i].trim());
		}
		ArrayList<ArrayList<String>> examples = new ArrayList<ArrayList<String>>();
		while(sc.hasNextLine()){
			String example = sc.nextLine();
			String[] vs = example.split(", ");
			ArrayList<String> values = new ArrayList<String>();
			for(int i = 0; i < vs.length; i++){
				values.add(vs[i].trim());
			}
			examples.add(values);
		}
		DTC tree = new DTC(tarAttr,attrs,examples);
		Node root = tree.construct(0);
		System.out.println(root.toString());
		sc.close();
		
		while(true){
			doTest(root);
		}
	}
	
	public static void doTest(Node root){
		System.out.println("Please a test case:");
		String test = "\n";
		while(!test.contains(",")){
			test = scc.nextLine();
		}
		String[] as = test.split(",");
		ArrayList<String> ast = new ArrayList<String>();
		for(int i = 0; i < as.length; i++){
			ast.add(as[i].trim());
		}
		Node current = root;
		while(current.getResult() == null){
			ArrayList<Node> children = current.getBranches();
			boolean found = false;
			for(int i = 0; i < children.size(); i++){
				for(int j = 0; j < ast.size(); j++){
					if(ast.get(j).equals((children.get(i).getName()))){
//						System.out.printf("go %s\n", children.get(i).getName());
						current = children.get(i);
						found = true;
						break;
					}
				}
				if(found){
					break;
				}
			}
		}
		if(current.getResult().equals("-")){
			System.out.println("no");
		}
		else{
			System.out.println("yes");
		}
	}

}
