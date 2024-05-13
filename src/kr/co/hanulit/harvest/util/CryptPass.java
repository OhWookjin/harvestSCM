package kr.co.hanulit.harvest.util;

public class CryptPass {
	
	public static String encPassword = null;
	public static String decPassword = null;

//	public static void main(String[] args) {
//		CryptPass In = new CryptPass();
//		
//		String encResult = "";
//		encResult = In.encrypt("harvest");
//		System.out.println("encrypted passwd = "+encResult);
//		
//		String decResult = "";
//		decResult = In.decrypt("8,-95,0,-59,59,-94,57,96,-95,");
//		System.out.println("decrypted passwd = "+decResult);
//	}
	
	public static String encrypt(String password) {
		encPassword = ScmEncrypt.encrypt(password);
		
		return encPassword;
	}
	public static String decrypt(String password) {
		decPassword = ScmEncrypt.decrypt(password);
		
		return decPassword;
	}

}
