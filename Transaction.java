import java.security.*;
import java.util.ArrayList;

public class Transaction {

    public String transactionId;
    public PublicKey sender;
    public PublicKey reciepient;
    public float value;
    public byte[] signature;

    public ArrayList<TransactionInput> inputs = new ArrayList<TransactionInput>();
    public ArrayList<TransactionOutput> outputs = new ArrayList<TransactionOutput>();

    private static int sequence = 0;

    // Constructor:
    public Transaction(PublicKey from, PublicKey to, float value,  ArrayList<TransactionInput> inputs) {
        this.sender = from;
        this.reciepient = to;
        this.value = value;
        this.inputs = inputs;
    }

    public boolean processTransaction() {

        if(verifySignature() == false) {
            System.out.println("#Chữ ký giao dịch xác nhận không đúng!");
            return false;
        }


        for(TransactionInput i : inputs) {
            i.UTXO = BTH_Blockchain.UTXOs.get(i.transactionOutputId);
        }


        if(getInputsValue() < BTH_Blockchain.minimumTransaction) {
            System.out.println("Đầu vào của giao dịch quá bé: " + getInputsValue());
            System.out.println("Vui lòng nhập số số lượng điện thoại trong kho lớn hơn " + BTH_Blockchain.minimumTransaction);
            return false;
        }


        float leftOver = getInputsValue() - value; //Tính giá trị tiền còn lại sau khi giao dịch
        transactionId = calulateHash();
        outputs.add(new TransactionOutput( this.reciepient, value,transactionId));
        outputs.add(new TransactionOutput( this.sender, leftOver,transactionId));

        for(TransactionOutput o : outputs) {
            BTH_Blockchain.UTXOs.put(o.id , o);
        }


        for(TransactionInput i : inputs) {
            if(i.UTXO == null) continue;
            BTH_Blockchain.UTXOs.remove(i.UTXO.id);
        }

        return true;
    }

    public float getInputsValue() {
        float total = 0;
        for(TransactionInput i : inputs) {
            if(i.UTXO == null) continue;
            total += i.UTXO.value;
        }
        return total;
    }

    public void generateSignature(PrivateKey privateKey) {
        String data = StringUtil.getStringFromKey(sender) + StringUtil.getStringFromKey(reciepient) + Float.toString(value)	;
        signature = StringUtil.applyECDSASig(privateKey,data);
    }

    public boolean verifySignature() {
        String data = StringUtil.getStringFromKey(sender) + StringUtil.getStringFromKey(reciepient) + Float.toString(value)	;
        return StringUtil.verifyECDSASig(sender, data, signature);
    }

    public float getOutputsValue() {
        float total = 0;
        for(TransactionOutput o : outputs) {
            total += o.value;
        }
        return total;
    }

    private String calulateHash() {
        sequence++;
        return StringUtil.applySha256(
                StringUtil.getStringFromKey(sender) +
                        StringUtil.getStringFromKey(reciepient) +
                        Float.toString(value) + sequence
        );
    }
}