package org.boisestate.cs;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;

import org.optaplanner.core.api.domain.solution.Solution;
import org.optaplanner.persistence.xstream.impl.domain.solution.XStreamSolutionFileIO;

public class BookExchangingGenerator {

	private String csvFileName;
	private File csvFile;
	private Scanner scanner;
	private HashMap<Integer,User> users;
	private HashMap<Integer,Book> books;
	private Random rand;
	
	/**
	 * Constructor - Assumes that the argument is a CSV file.
	 * @param csvFileName
	 */
	public BookExchangingGenerator(String csvFileName) {
		try {
			this.csvFileName = csvFileName;
			csvFile = new File(csvFileName);
			scanner = new Scanner(csvFile);
			users = new HashMap<Integer,User>();
			books = new HashMap<Integer,Book>();
			rand = new Random();
			// Skip first row in csv file because they are headers.
			// FIXME Possibly should check if first row is actual data or headers somehow instead of always skipping.
			scanner.nextLine(); 
			
			while(scanner.hasNextLine()) {
				String currentLine = scanner.nextLine();
				parseLine(currentLine);
			}
		} catch(IOException e) {
			System.err.println("Error with reading input data from "+csvFileName+".");
			System.err.println(e);
		}
	}
	
	public BookExchange createBookExchange() {
		//PairUsersWithBooksRandomly();
		
		
		/* BookExchange bookExchange = new BookExchange();
		List<User> userList = new ArrayList<User>();
		List<Book> bookList = new ArrayList<Book>();
		
		userList.addAll(usersS);
		bookList.addAll(booksS);
		
		bookExchange.setUserList(userList);
		bookExchange.setBookList(bookList);
		
		return bookExchange;*/
		
		BookExchange bookExchange = new BookExchange();
		
		List<Book> bookList = new ArrayList<Book>();
		List<User> userList = new ArrayList<User>();
		
		bookList.addAll(books.values());
		userList.addAll(users.values());
		
		// TODO Refactor bookExchange's methods to take Collections instead of lists if Optaplanner supports it.
		bookExchange.setBookList(bookList);
		bookExchange.setUserList(userList);
		
		XStreamSolutionFileIO fileIO = new XStreamSolutionFileIO();
		
		String xmlFileName = csvFileName.replace(".csv", ".xml");
		
		File solutionOutput = new File(xmlFileName);
		fileIO.write((Solution)bookExchange, solutionOutput);
		
		return bookExchange;
	}

	private void PairUsersWithBooksRandomly() {
		
		ArrayList<User> userList = new ArrayList<User>();
		userList.addAll(users.values());
		
		ArrayList<Book> bookList = new ArrayList<Book>();
		bookList.addAll(books.values());
		
		// Assign books to users at random for a starting point in the optimization problem
		while(userList.size() > 0) {
			// Get random user
			int index = rand.nextInt(userList.size());
			User currentUser = userList.remove(index);
			
			// Get random book
			index = rand.nextInt(bookList.size());
			Book currentBook = bookList.remove(index);
			
			// Assign Book to User
			currentUser.setBook(currentBook);
		}
		
		if(userList.size() > 0 || bookList.size() > 0) {
			System.err.println("There was an issue randomly pairing users with books in BookExchangingGenerator().");
		}
	}

	private void parseLine(String currentLine) {
		String[] lineArr = currentLine.split(","); //Delimiter
		if(lineArr.length != 3) {
			System.err.println("Too few fields found in input row. Skipping line.");
			return;
		}
		
		try {
			
			int userId=Integer.parseInt(lineArr[0].trim());
			int bookId=Integer.parseInt(lineArr[1].trim());
			double score=Double.parseDouble(lineArr[2].trim());
		
			/*User currentUser = new User();
			currentUser.setUserId(userId);
			Book currentBook = new Book();
			currentBook.setBookId(bookId);
			
			if(! usersS.contains(currentUser)) {
				usersS.add(currentUser);
			}
			if(! booksS.contains(currentBook)) {
				booksS.add(currentBook);
			}
			
			//FIXME - Fix passing in currentUser instead of currentUser id.
			currentBook.addHappinessScore(currentUser.getUserId(), score);*/
			
			Book currentBook;
			User currentUser;
			
  			if(! books.containsKey(bookId)) {
				currentBook = new Book();
				currentBook.setBookId(bookId);
				books.put(bookId, currentBook);
			} else {
				currentBook = books.get(bookId);
			}
			
			if(! users.containsKey(userId)) {
				currentUser = new User();
				currentUser.setUserId(userId);
				users.put(userId, currentUser);
			} else {
				currentUser = users.get(userId);
				
				currentBook.addHappinessScore(currentUser.getUserId(), score);
			}
			
		} catch(Exception e) {
			System.err.println("An exception occured in BookExchangingGenerator().");
			System.err.println(e);;
		}
	}

}
