import java.util.Scanner;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
class Node {
    String key,value;
    Node next;
}
public class Hash {
    int n;
    Node[] data;
    public Hash(){
        n = 10;
        data = new Node[10];//建立資料陣列
    }
    public void put(String key,String value){//放入資料
        int slot;//建立插槽
        if(key.hashCode() < 0)//如果是負值
            slot = -key.hashCode() % n;//加負號除之，得到餘數
        else 
            slot = key.hashCode() %n;
        Node tmp;
        for (tmp = data[slot]; tmp != null; tmp = tmp.next)
            if (tmp.key.equals(key))
                break;
        if (tmp != null) {//duplicate key
            tmp.value = value;
            return;
        }
        tmp = new Node();
        tmp.key = key;
        tmp.value = value;
        tmp.next = data[slot];//把資料接上
        data[slot] = tmp;
    }
    public String get(String key){//找資料
        int slot;
		if(key.hashCode() < 0)//如果是負值
            slot = -key.hashCode() % n;//加負號除之，得到餘數
        else 
            slot = key.hashCode() %n;
        Node tmp;//要找的值
        for (tmp = data[slot]; tmp != null; tmp = tmp.next)
            if (tmp.key.equals(key))
                break;
        if (tmp != null)
            return tmp.value;
        return null;
    }
    public void remove(String key) {//移除資料
        int slot;
        if (key.hashCode() < 0)//如果是負值
            slot = -key.hashCode() % n;//加負號除之，得到餘數
        else
            slot = key.hashCode() % n;
        Node tmp, prev;
        tmp = prev = null;
        //用迴圈去找要remove的值
        for (tmp = data[slot]; tmp != null; tmp = tmp.next) { 
            if (tmp.key.equals(key)) {
                break;
            }
            prev = tmp;
        }
        if (tmp == null){
            return;
        }
        if (prev == null) {
            data[slot] = tmp.next;
        } else {
            prev.next = tmp.next;//將前一資料與後面資料相連接
        }
    }
    public static void main(String[] argv) throws IOException{//進行測試
		Hash h = new Hash();
        h.init();
    }
	public void init()throws IOException{
	    String str1;
        //Scanner scanner = new Scanner(System.in);
        //System.out.print("請輸入運算碼：");
        //str1 = scanner.nextLine();
		//Hash h = new Hash();
		FileReader input;
		String s;
		input = new FileReader("c:\\Program1\\OPcode.txt");
		BufferedReader br = new BufferedReader(input);
		while((s=br.readLine())!=null) {  
			String[] arrayA = s.split("\\s+");
		    put(arrayA[0],arrayA[1]);
		}
        //System.out.println("OPcode為:" + get("LDA"));
	}

}            
            