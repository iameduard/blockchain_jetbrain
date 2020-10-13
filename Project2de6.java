//package blockchain;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;
import java.util.Random;
public class Project2de6 {

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss");

    public static void main(String[] args) {
        int numBlocks = 10;
        Timestamp ts;
        long timestamp;
        Block block;
        Scanner scanner = new Scanner(System.in);
        int nzeroes = scanner.nextInt();
        BlockChain blockchain = new BlockChain(nzeroes);
        
        for(int id=1;id<=numBlocks;id++){
            ts = new Timestamp(System.currentTimeMillis());
            timestamp = ts.getTime();
            block = new Block(timestamp);
            blockchain.addBlock(block);
        }
        
        for(int id=0;id<5;id++){
            System.out.println(blockchain.getBlock(id).toString());
        }   
    }
}

class Block{
    private int id;
    private long time;
    private String hashPrevious;
    private String hashBlock;
    private int nonce;

    public Block(long time){
        this.time = time;
    }
    public void setHashPrevious(String hashPrevious){
        this.hashPrevious = hashPrevious;
    }
    public String getHashPrevious(){
        return this.hashPrevious;
    }
    public void setHashBlock(String hashBlock){
        this.hashBlock = hashBlock;
    }
    public String getHashBlock(){
        return this.hashBlock;
    }
    public void setId(int id){
        this.id=id;
    }
    public int getId(){
        return this.id;
    }
    public void setNonce(int nonce){
        this.nonce=nonce;
    }
    public int getNonce(){
        return this.nonce;
    }
    @Override
    public String toString(){
        StringBuilder blockString = new StringBuilder();
        blockString.append("Block:\n");
        blockString.append("Id: "+this.id);
        blockString.append("\nTimestamp: "+this.time);
        blockString.append("\nMagic number: "+this.nonce);
        blockString.append("\nHash of the previous block:\n"+this.hashPrevious);
        blockString.append("\nHash of the block:\n"+this.hashBlock);
        blockString.append("\nBlock was generating for "+" seconds");
        blockString.append("\n");
        return blockString.toString();
    }
}

class BlockChain{
    private String previousBlock;
    private ArrayList<Block> blockchain;
    private int id;
    private int nzeroes;
    
    
    public BlockChain(int nzeroes){
        this.id=0;
        this.previousBlock = "0";
        this.blockchain = new ArrayList<>();
        this.nzeroes = nzeroes;
    } 
    public void addBlock(Block block){
        String[] array;
        Random rand = new Random();
        String zeroes = new String(new char[this.nzeroes]).replace('\0', '0');
        //System.out.println(zeroes);
        block.setHashPrevious(this.previousBlock);
        this.id+=1;
        block.setId(this.id);
        block.setNonce(rand.nextInt());
        do{
            block.setNonce(rand.nextInt());
            array = block.toString().split("\nHash of the block:\n");
            this.previousBlock = StringUtil.applySha256(array[0]);
            //System.out.println(this.previousBlock.substring(0,this.nzeroes).compareTo(zeroes));
        }while(this.previousBlock.substring(0,this.nzeroes).compareTo(zeroes)!=0);
        block.setHashBlock(this.previousBlock);
        blockchain.add(block);
    }
    public boolean isValid(){
        String[] array;
        String previousBlock;
        String previousHash = "0";
        for(Block block: blockchain){
            array = block.toString().split("\nHash of the block:\n"); 
            if(block.getHashPrevious()!=previousHash){
                return false;
            }else{
                //Hash previous block is now hash current block.
                previousHash = StringUtil.applySha256(array[0]);
            }
            if(block.getHashBlock()!=previousHash){
                return false;
            }
        }
        return true;
    }
    
    public int lenghtBlockchain(){
        return id;
    }
    
    public Block getBlock(int id){
        return blockchain.get(id);
    }
}
    
class StringUtil {
    /* Applies Sha256 to a string and returns a hash. */
    public static String applySha256(String input){
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            /* Applies sha256 to our input */
            byte[] hash = digest.digest(input.getBytes("UTF-8"));
            StringBuilder hexString = new StringBuilder();
            for (byte elem: hash) {
                String hex = Integer.toHexString(0xff & elem);
                if(hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        }
        catch(Exception e) {
            throw new RuntimeException(e);
        }
    }
}
