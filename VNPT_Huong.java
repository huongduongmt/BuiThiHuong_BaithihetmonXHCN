import java.util.ArrayList;
import java.util.Date;
//2.Tạo lớp và phương thức khởi tạo khối
public class VNPT_Huong {
    public String hash;
    public String previousHash;
    public String merkleRoot;
    public ArrayList<Transaction> Transactions = new ArrayList<Transaction>(); //Khai báo mảng chứa các giao dịch.
    public long timeStamp;
    public int nonce;

    public VNPT_Huong(String previousHash, String s) {
        this.previousHash = previousHash;
        this.timeStamp = new Date().getTime();

        this.hash = calculateHash();
    }

    //Calculate new hash based on blocks contents
    public String calculateHash() {
        String calculatedhash = StringUtil.applySha256(
                previousHash +
                        Long.toString(timeStamp) +
                        Integer.toString(nonce) +
                        merkleRoot
        );
        return calculatedhash;
    }

    //Increases nonce value until hash target is reached.
    public void mineVNPT_Huong(int difficulty) {
        merkleRoot = StringUtil.getMerkleRoot(Transactions);
        String target = StringUtil.getDificultyString(difficulty);
        while(!hash.substring( 0, difficulty).equals(target)) {
            nonce ++;
            hash = calculateHash();
        }
        System.out.println("Block Mined!!! : " + hash);
    }

    //Thêm giao dịch vào Block
    public boolean addTransaction(Transaction transaction) {
        //xử lý giao dịch và kiểm tra xem có hợp lệ không; Nếu là khối là khối gốc thì không cần kiểm tra.
        if(transaction == null) return false;
        if((!"0".equals(previousHash))) {
            if((transaction.processTransaction() != true)) {
                System.out.println("hoạt động không xử lý được!.");
                return false;
            }
        }

        Transactions.add(transaction);
        System.out.println("hoạt động đã được thêm thành công vào khối.");
        return true;
    }

}