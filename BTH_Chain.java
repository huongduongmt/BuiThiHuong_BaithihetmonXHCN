import java.util.ArrayList;
import com.google.gson.GsonBuilder;
import java.lang.String;
import java.security.Security;
import java.util.HashMap;
import java.util.Map;

public class BTH_Chain {
    public static ArrayList<VNPT_Huong> blockchain = new ArrayList<VNPT_Huong>();
    public static int difficulty = 5;
    public static void main(String[] args) {
          blockchain.add(new VNPT_Huong("Hi im the first block", "0"));
          System.out.println("Trying to Mine block 1... ");
          blockchain.get(0).mineVNPT_Huong(difficulty);
          blockchain.add(new VNPT_Huong("Yo im the second block",blockchain.get(blockchain.size()-1).hash));
          System.out.println("Trying to Mine block 2... ");
          blockchain.get(1).mineVNPT_Huong(difficulty);
          blockchain.add(new VNPT_Huong("Hey im the third block",blockchain.get(blockchain.size()-1).hash));
          System.out.println("Trying to Mine block 3... ");
          blockchain.get(2).mineVNPT_Huong(difficulty);
          System.out.println("\nBlockchain is Valid: " + isChainValid());
          String blockchainJson = new GsonBuilder().setPrettyPrinting().create().toJson(blockchain);
          System.out.println("\nThe block chain: ");
          System.out.println(blockchainJson);
    }
    public static Boolean isChainValid() {
        VNPT_Huong currentBlock;
        VNPT_Huong previousBlock;
        String hashTarget = new String(new char[difficulty]).replace('\0', '0');
        //loop through blockchain to check hashes:
        for (int i = 1; i < blockchain.size(); i++) {
            currentBlock = blockchain.get(i);
            previousBlock = blockchain.get(i - 1);
            //compare registered hash and calculated hash:
            if (!currentBlock.hash.equals(currentBlock.calculateHash())) {
                System.out.println("Current Hashes not equal");
                return false;
            }
            //compare previous hash and registered previous hash
            if (!previousBlock.hash.equals(currentBlock.previousHash)) {
                System.out.println("Previous Hashes not equal");
                return false;
            }
            //check if hash is solved
            if (!currentBlock.hash.substring(0, difficulty).equals(hashTarget)) {
                System.out.println("This block hasn't been mined");
                return false;
            }
        }
        return true;
    }
}
