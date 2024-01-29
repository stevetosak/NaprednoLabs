package Labs3.Zadaca2;
import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

class InvalidNameException extends Exception{
    public String name;
    InvalidNameException(String name){
        this.name = name;
    }
}
class InvalidNumberException extends Exception {
    InvalidNumberException(){}
}

class MaximumSizeExceddedException extends Exception{
    MaximumSizeExceddedException(){}
}
class Contact implements Comparable<Contact>{
    private String name;
    private final String[] phoneNumbers = new String[5];
    private int size_phone_numbers = 0;

    private void createContact(String name, String[] numbers) throws InvalidNameException, InvalidNumberException, MaximumSizeExceddedException {
        if(!Checker.checkName(name)){
            throw new InvalidNameException(name);
        }
        for (String number : numbers) {
            addNumber(number);
        }
        this.name = name;
    }
    Contact(String name) throws InvalidNameException, InvalidNumberException, MaximumSizeExceddedException {
            createContact(name,new String[]{});
    }

    Contact(String name, String[] numbers) throws InvalidNameException, InvalidNumberException, MaximumSizeExceddedException {
        createContact(name,numbers);
    }

    Contact(String name,String number) throws InvalidNameException, InvalidNumberException, MaximumSizeExceddedException {
        createContact(name,new String[]{number});
    }

    Contact(String name,String number1,String number2,String number3) throws InvalidNameException, InvalidNumberException, MaximumSizeExceddedException {
        createContact(name,new String[]{number1,number2,number3});

    }
    Contact(){}

    public String getName() {
        return name;
    }

    public String[] getNumbers() {
        if(size_phone_numbers != 0){
            String[] result = Arrays.copyOf(phoneNumbers,size_phone_numbers);
            Arrays.sort(result);
            return result;
        }
        return phoneNumbers;
    }

    public void addNumber(String phoneNumber) throws InvalidNumberException, MaximumSizeExceddedException {
        if(!Checker.checkNumber(phoneNumber)){
            throw new InvalidNumberException();
        }
        if(size_phone_numbers > 4){
            throw new MaximumSizeExceddedException();
        }

        phoneNumbers[size_phone_numbers++] = phoneNumber;

    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(name).append("\n");
        sb.append(size_phone_numbers).append("\n");
        String[] sortedNums = getNumbers();
        for(int i = 0; i < size_phone_numbers; i++){
            sb.append(sortedNums[i]).append("\n");
        }
        return sb.toString();
    }

    public static Contact valueOf(String s) throws InvalidNameException, InvalidNumberException, MaximumSizeExceddedException {
        s = s.replaceAll("[\\[|\\s+]", "");
        String[] parts = s.split("\n");
        String name = parts[0];
        int count = Integer.parseInt(parts[1]);
        String[] numbers = new String[count];
        for(int i = 0; i < count; i++){
            numbers[i] = parts[i + 2];
        }
        return new Contact(name,numbers);
    }

    @Override
    public int compareTo(Contact o) {
        return name.compareTo(o.name);
    }

    private static class Checker{
        static boolean checkName(String name){
            if(name.length() <= 4 || name.length() > 10){
                return false;
            }

            for(int i = 0; i < name.length(); i++){
                if(!Character.isLetterOrDigit(name.charAt(i))){
                    return false;
                }
            }

            return true;
        }
       static boolean checkNumber(String phoneNumber){
            if(phoneNumber.length() != 9) return false;

            for(int i = 0; i < phoneNumber.length(); i++){
                if(!Character.isDigit(phoneNumber.charAt(i))){
                    return false;
                }
            }

            String[] validPrefixes = {"070","071","072","075","076","077","078"};
            for(String prefix : validPrefixes){
                if(phoneNumber.startsWith(prefix)){
                    return true;
                }
            }

            return false;
        }
    }

}

class PhoneBook{
    private final Map<String,Contact> contacts;
    public PhoneBook() {
        contacts = new HashMap<>();
    }

    public void addContact(Contact contact) throws InvalidNameException, MaximumSizeExceddedException {
        if(contacts.size() >= 250){
            throw new MaximumSizeExceddedException();
        }

        if(contacts.containsKey(contact.getName())){
            throw new InvalidNameException(contact.getName());
        }

        contacts.put(contact.getName(),contact);
    }

    public Contact getContactForName(String name){
        return contacts.get(name);
    }

    public int numberOfContacts(){
        return contacts.size();
    }

    public Contact[] getContacts(){
        Contact[] result = contacts.values().stream().sorted().collect(Collectors.toList()).toArray(new Contact[0]);
        return Arrays.copyOf(result,result.length);
    }

    public boolean removeContact(String name){
        Contact removed = contacts.remove(name);
        return removed != null;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        Arrays.stream(getContacts()).forEach(contact -> sb.append(contact.toString()).append("\n"));
        return sb.toString();
    }

    public static boolean saveAsTextFile(PhoneBook phoneBook, String path) throws IOException {
        PrintWriter pw = new PrintWriter(path);
        pw.println(phoneBook.toString());
        return !pw.checkError();
    }
    public static PhoneBook loadFromTextFile(String path) throws IOException, InvalidNameException, InvalidNumberException, MaximumSizeExceddedException {
        BufferedReader br = new BufferedReader(new FileReader(path));
        PhoneBook phoneBook = new PhoneBook();
        int intCh;
        while((intCh = br.read()) != -1){
            StringBuilder sb = new StringBuilder();
            while (((char)intCh) != ','){
                sb.append((char) intCh);
                intCh = br.read();
            }
            phoneBook.addContact(Contact.valueOf(sb.toString()));
        }

        return phoneBook;
    }

    public Contact[] getContactsForNumber(String number_prefix){
        return Arrays.stream(getContacts())
                .filter(contact -> Arrays.stream(contact.getNumbers()).anyMatch(number -> number.startsWith(number_prefix)))
                .collect(Collectors.toList()).toArray(new Contact[0]);
    }

}

public class PhonebookTester {
    public static void main(String[] args) throws Exception {
        Scanner jin = new Scanner(System.in);
        String line = jin.nextLine();
        switch( line ) {
            case "test_contact":
                testContact(jin);
                break;
            case "test_phonebook_exceptions":
                testPhonebookExceptions(jin);
                break;
            case "test_usage":
                testUsage(jin);
                break;
            case "test_file":
                testFile(jin);
                break;
        }
    }

    private static void testFile(Scanner jin) throws Exception {
        PhoneBook phonebook = new PhoneBook();
        String line = jin.nextLine();
        while (!line.equals("end")){
            String[] line2 = jin.nextLine().split("\\s++");
            phonebook.addContact(new Contact(line,line2));
            line = jin.nextLine();
        }
        String text_file = "phonebook.txt";
        PhoneBook.saveAsTextFile(phonebook,text_file);
        PhoneBook pb = PhoneBook.loadFromTextFile(text_file);
        if ( ! pb.equals(phonebook) ) System.out.println("Your file saving and loading doesn't seem to work right");
        else System.out.println("Your file saving and loading works great. Good job!");
    }

    private static void testUsage(Scanner jin) throws Exception {
        PhoneBook phonebook = new PhoneBook();
        while ( jin.hasNextLine() ) {
            String command = jin.nextLine();
            switch ( command ) {
                case "add":
                    phonebook.addContact(new Contact(jin.nextLine(),jin.nextLine().split("\\s++")));
                    break;
                case "remove":
                    phonebook.removeContact(jin.nextLine());
                    break;
                case "print":
                    System.out.println(phonebook.numberOfContacts());
                    System.out.println(Arrays.toString(phonebook.getContacts()));
                    System.out.println(phonebook.toString());
                    break;
                case "get_name":
                    System.out.println(phonebook.getContactForName(jin.nextLine()));
                    break;
                case "get_number":
                    System.out.println(Arrays.toString(phonebook.getContactsForNumber(jin.nextLine())));
                    break;
            }
        }
    }

    private static void testPhonebookExceptions(Scanner jin) {
        PhoneBook phonebook = new PhoneBook();
        boolean exception_thrown = false;
        try {
            while ( jin.hasNextLine() ) {
                phonebook.addContact(new Contact(jin.nextLine()));
            }
        }
        catch ( InvalidNameException e ) {
            System.out.println(e.name);
            exception_thrown = true;
        }
        catch ( Exception e ) {}
        if ( ! exception_thrown ) System.out.println("Your addContact method doesn't throw InvalidNameException");
        /*
		exception_thrown = false;
		try {
		phonebook.addContact(new Contact(jin.nextLine()));
		} catch ( MaximumSizeExceddedException e ) {
			exception_thrown = true;
		}
		catch ( Exception e ) {}
		if ( ! exception_thrown ) System.out.println("Your addContact method doesn't throw MaximumSizeExcededException");
        */
    }

    private static void testContact(Scanner jin) throws Exception {
        boolean exception_thrown = true;
        String names_to_test[] = { "And\nrej","asd","AAAAAAAAAAAAAAAAAAAAAA","Ð�Ð½Ð´Ñ€ÐµÑ˜A123213","Andrej#","Andrej<3"};
        for ( String name : names_to_test ) {
            try {
                new Contact(name);
                exception_thrown = false;
            } catch (InvalidNameException e) {
                exception_thrown = true;
            }
            if ( ! exception_thrown ) System.out.println("Your Contact constructor doesn't throw an InvalidNameException");
        }
        String numbers_to_test[] = { "+071718028","number","078asdasdasd","070asdqwe","070a56798","07045678a","123456789","074456798","073456798","079456798" };
        for ( String number : numbers_to_test ) {
            try {
                new Contact("Andrej",number);
                exception_thrown = false;
            } catch (InvalidNumberException e) {
                exception_thrown = true;
            }
            if ( ! exception_thrown ) System.out.println("Your Contact constructor doesn't throw an InvalidNumberException");
        }
        String nums[] = new String[10];
        for ( int i = 0 ; i < nums.length ; ++i ) nums[i] = getRandomLegitNumber();
        try {
            new Contact("Andrej",nums);
            exception_thrown = false;
        } catch (MaximumSizeExceddedException e) {
            exception_thrown = true;
        }
        if ( ! exception_thrown ) System.out.println("Your Contact constructor doesn't throw a MaximumSizeExceddedException");
        Random rnd = new Random(5);
        Contact contact = new Contact("Andrej",getRandomLegitNumber(rnd),getRandomLegitNumber(rnd),getRandomLegitNumber(rnd));
        System.out.println(contact.getName());
        System.out.println(Arrays.toString(contact.getNumbers()));
        System.out.println(contact.toString());
        contact.addNumber(getRandomLegitNumber(rnd));
        System.out.println(Arrays.toString(contact.getNumbers()));
        System.out.println(contact.toString());
        contact.addNumber(getRandomLegitNumber(rnd));
        System.out.println(Arrays.toString(contact.getNumbers()));
        System.out.println(contact.toString());
    }

    static String[] legit_prefixes = {"070","071","072","075","076","077","078"};
    static Random rnd = new Random();

    private static String getRandomLegitNumber() {
        return getRandomLegitNumber(rnd);
    }

    private static String getRandomLegitNumber(Random rnd) {
        StringBuilder sb = new StringBuilder(legit_prefixes[rnd.nextInt(legit_prefixes.length)]);
        for ( int i = 3 ; i < 9 ; ++i )
            sb.append(rnd.nextInt(10));
        return sb.toString();
    }


}

