import java.util.Iterator;

public class ProductLookup {
	private DictionaryADT<String,StockItem> dictionary;

	public ProductLookup(int maxSize){
		dictionary = 
					new Hashtable<String,StockItem>(maxSize);
//					new BalancedTree<String,StockItem>(); 
//					new BinarySearchTree<String,StockItem>();

	}

	public void addItem(String SKU, StockItem item){
		dictionary.add(SKU, item);
	}

	public StockItem getItem(String SKU){
		if (dictionary.contains(SKU)){
			return dictionary.getValue(SKU);
		}
		return null;
	}

	public float getRetail(String SKU) {
		if (dictionary.contains(SKU)){
			return dictionary.getValue(SKU).getRetail();
		}
		return -.01f;
	}

	public float getCost(String SKU)  {
		if (dictionary.contains(SKU)){
			return dictionary.getValue(SKU).getCost();
		}
		return -.01f;
	}

	public String getDescription(String SKU)   {
		if (dictionary.contains(SKU)){
			return dictionary.getValue(SKU).getDescription();
		}
		return null;
	}

	public boolean deleteItem(String SKU){
		if (dictionary.contains(SKU)){
			dictionary.delete(SKU);
			return true;
		}
		return false;
	}

	public void printAll(){
		Iterator<StockItem> itemIter = values();
		while(itemIter.hasNext()){
			System.out.println(itemIter.next());
		}
	}

	public void print(String vendor){
		Iterator<StockItem> itemIter = values();
		while(itemIter.hasNext()){
			StockItem item = itemIter.next();
			if(item.getVendor().equals(vendor))
				System.out.println(item);
		}
	}

	public Iterator<String> keys(){
		return dictionary.keys();
	}
 
	public Iterator<StockItem> values(){
		return dictionary.values();
	}
}