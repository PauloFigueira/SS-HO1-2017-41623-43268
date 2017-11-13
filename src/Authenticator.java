import static java.util.Base64.getEncoder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.security.Key;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import Exceptions.*;

public class Authenticator {

	private Map<String, Account> accounts;
	private String logged;
	private static final String DIR=Authenticator.class.getProtectionDomain().getCodeSource().getLocation().toString().split(":")[1] + "database.txt";
	private static final String ALGO = "AES";
	private static final byte[] keyValue = new byte[]{'S','E','G','U','R','A','C','A','S','O','F',
														'T','W','A','R','E'};

	public Authenticator(){

	}

	public void constFile() throws FileNotFoundException {
		File file = new File(DIR);
		Scanner input = new Scanner(file);
		accounts = new HashMap<String, Account>();
		logged = input.nextLine();
		while (input.hasNextLine()) {
			String[] parts = input.nextLine().split(",");
			accounts.put(parts[0], new Account(parts[0], parts[1],parts[2],parts[3],parts[4]));
		}
		input.close();
	}

	public void createAccount(String name, String pw) throws Exception {
		constFile();
		if (accounts.containsKey(name)) {
			throw new AlreadyExistsException();
		}
		Path path = Paths.get(DIR);
		String newAcc = name + "," + encrypt(pw) + ",false,false,0\n";
		Files.write(path, newAcc.getBytes(), StandardOpenOption.APPEND);
	}

	private void writeToDatabase() throws IOException {
		Path path = Paths.get(DIR);
		String loggedAcc = logged+"\n";
		Files.write(path,loggedAcc.getBytes());
		Files.write(path,() -> accounts.entrySet()
		.stream().<CharSequence>map(e -> e.getKey() + "," + e.getValue().getPassword() + "," + e.getValue().isLogged() + "," + e.getValue().isLocked() + "," + e.getValue().getLockedNum()).iterator()
				,StandardOpenOption.APPEND);

	}

	public void deleteAccount(String name) throws UndefinedAccountException, LoggedInAccountException, LockedAccountException, IOException {
		Account acc;
		if(!accounts.containsKey(name)){
			throw new UndefinedAccountException();
		}

		acc= accounts.get(name);

		if(acc.isLogged()){
			throw new LoggedInAccountException();
		}
		if(!acc.isLocked()){
			throw new LockedAccountException();
		}

		//removes account
		accounts.remove(acc.getName());
		writeToDatabase();
	}

	public Account login(String name, String pwd) throws Exception {
		
		constFile();
		Account acc;
		
		if(!accounts.containsKey(name)){
			throw new UndefinedAccountException();
		}

		acc=accounts.get(name);


		if(!pwd.equals(acc.getPassword())){
			int lockedNum = acc.incLockedNum();
			if(lockedNum>3){
				acc.setLocked(true);
				writeToDatabase();
				throw new LockedAccountException();
			}else{
				writeToDatabase();
			throw new AuthenticationErrorException();}
		}

		if(acc.isLocked()){
			throw new LockedAccountException();
		}

		acc.setLogged(true);
        acc.setLockedNum(0);
		logged = acc.getName();

        writeToDatabase();

		return acc;
	}

	public Account loginSessionParameters(HttpServletRequest request, HttpServletResponse response) throws Exception {
		HttpSession session = request.getSession();
		return login((String)session.getAttribute("USER"),(String)session.getAttribute("PWD"));
		
		//TODO: add error on slides

		
	}
	
	public void logout(Account loggedOut) throws IOException {
		constFile();
		logged = "none";
		accounts.get(loggedOut.getName()).setLogged(false);
		writeToDatabase();
	}

	public void change_pwd(String name, String pw) throws Exception {

		Account acc;

		if(!accounts.containsKey(name)){
			throw new UndefinedAccountException();
		}
		acc = accounts.get(name);
		acc.setPassword(encrypt(pw));

		writeToDatabase();
	}

	public String encrypt(String Data) throws Exception {
		Cipher c = Cipher.getInstance(ALGO);
		Key key = new SecretKeySpec(keyValue,ALGO);
		c.init(Cipher.ENCRYPT_MODE,key);
		byte[] encVal = c.doFinal(Data.getBytes());
		return getEncoder().encodeToString(encVal);
	}

	//Getters & Setters
	public String getLogged() {
		return logged;
	}

	public void setLogged(String logged) {
		this.logged = logged;
	}

	public Map<String, Account> getAccounts() {
		return accounts;
	}

	public void setAccounts(Map<String, Account> accounts) {
		this.accounts = accounts;
	}

}