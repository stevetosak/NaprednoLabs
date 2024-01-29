package Labs2.Zadaca1;

import java.text.DecimalFormat;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

abstract class Contact{
    protected String date; // format:   YYYY-MM-DD
    protected ContactType type;

    Contact(String date){
        this.date = date;
    }

    public int isNewerThan(Contact c){
        DateHandler thisDate = new DateHandler(c.date);
        DateHandler otherDate = new DateHandler(this.date);

        return thisDate.compareTo(otherDate);
    }

    public String getType(){
        if(this.type == ContactType.EMAIL)
            return "Email";
        else if(this.type == ContactType.PHONE)
            return "Phone";
        else
            return "Invalid Type";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Contact contact = (Contact) o;
        return Objects.equals(date, contact.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(date);
    }
}

class EmailContact extends Contact{
    private final String email;
    EmailContact(String date,String email){
        super(date);
        this.email = email;
        this.type = ContactType.EMAIL;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String getType() {
        return super.getType();
    }

    @Override
    public String toString() {
        return  '\"' + email + "\"";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        EmailContact that = (EmailContact) o;
        return Objects.equals(email, that.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), email);
    }
}

class PhoneContact extends Contact{
    private final String number;
    private Operator operator;


    PhoneContact(String date, String phone){
        super(date);
        this.number = phone;
        this.type = ContactType.PHONE;
    }

    public String getPhone() {
        return number;
    }

    public Operator getOperator() {
        if(number.startsWith("070") || number.startsWith("071") || number.startsWith("072")){
            operator = Operator.TMOBILE;
            return operator;

        }
        else if(number.startsWith("075") || number.startsWith("076")){
            operator = Operator.ONE;
            return operator;
        }
        else if(number.startsWith("077") || number.startsWith("078")){
            operator = Operator.VIP;
            return operator;
        }
        return Operator.INVALID;
    }
    @Override
    public String getType() {
        return super.getType();
    }

    @Override
    public String toString() {
        return "\"" + number + '\"';

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        PhoneContact that = (PhoneContact) o;
        return Objects.equals(number, that.number) && operator == that.operator;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), number, operator);
    }
}

class Student {
    private final String firstName;
    private final String lastName;
    private final String city;
    private final int age;
    private final long index;
    private final ArrayList<Contact> contacts = new ArrayList<>();

    public Student(String firstName, String lastName, String city, int age, long index) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.city = city;
        this.age = age;
        this.index = index;
    }

    void addEmailContact(String date, String email) {
        contacts.add(new EmailContact(date, email));
    }

    void addPhoneContact(String date, String phone) {
        contacts.add(new PhoneContact(date, phone));
    }

    Contact[] getEmailContacts() {
//        ArrayList<Contact> emails = new ArrayList<>();
//        for (int i = 0; i < contacts.size(); i++) {
//            if (contacts.get(i).type == ContactType.EMAIL) {
//                emails.add(contacts.get(i));
//            }
//        }
//        return emails.toArray(new Contact[0]);


        return contacts.stream().filter(s -> s.type == ContactType.EMAIL).toArray(Contact[]::new);
    }

    Contact[] getPhoneContacts() {
//        ArrayList<Contact> phoneNumbers = new ArrayList<>();
//        for (int i = 0; i < contacts.size(); i++) {
//            if (contacts.get(i).type == ContactType.PHONE) {
//                phoneNumbers.add(contacts.get(i));
//            }
//        }
//
//        return phoneNumbers.toArray(new Contact[0]);

        return contacts.stream().filter(s -> s.type == ContactType.PHONE).toArray(Contact[]::new);
    }
    String getCity(){
        return city;
    }
    String getFullName(){
        return firstName.toUpperCase() + " " + lastName.toUpperCase();
    }
    long getIndex(){
        return index;
    }
    Contact getLatestContact(){
//        Contact newest = contacts.get(0);
//        for (int i = 1; i < contacts.size(); i++) {
//            if(contacts.get(i).isNewerThan(newest)){
//                newest = contacts.get(i);
//            }
//        }
//
//        return newest;

        return contacts.stream().min(Contact::isNewerThan).get();


    }

    public int getNumContacts() {
        return contacts.size();
    }
    //
    @Override
    public String toString() {
        return "{" +
                "\"ime\":" + "\"" + firstName + "\"" +
                ", \"prezime\":" + "\"" + lastName + "\"" +
                ", \"vozrast\":" + age +
                ", \"grad\":" + "\"" + city + "\"" +
                ", \"indeks\":" + index +
                ", \"telefonskiKontakti\":" + Arrays.toString(getPhoneContacts()) +
                ", \"emailKontakti\":" + Arrays.toString(getEmailContacts()) + "}";
    }

    /*
    {"ime":"Jovan", "prezime":"Jovanov", "vozrast":20, "grad":"Skopje", "indeks":101010, "telefonskiKontakti":["077/777-777", "078/888-888"],
    "emailKontakti":["jovan.jovanov@example.com", "jovanov@jovan.com", "jovan@jovanov.com"]}
     */
}

class Faculty{
    private final String name;
    private final Student[] students;

    public Faculty(String name, Student[] students) {
        this.name = name;
        this.students = students;
    }
    int countStudentsFromCity(String cityName){
        int count = 0;
        for(int i = 0; i < students.length; i++){
            if(students[i].getCity().equals(cityName)){
                count++;
            }
        }
        return count;
    }

    Student getStudent(long index){
        for(int i = 0; i < students.length; i++){
            if(students[i].getIndex() == index){
                return students[i];
            }
        }
        return null;
    }

    double getAverageNumberOfContacts(){
        int sumContacts = 0;
        for (int i = 0; i < students.length; i++) {
            sumContacts += students[i].getNumContacts();
        }

        return (double) sumContacts/students.length;
    }
    Student getStudentWithMostContacts(){
        Student max = students[0];
        for (int i = 1; i < students.length; i++) {
            if(students[i].getNumContacts() > max.getNumContacts()){
                max = students[i];
            }
            else if(students[i].getNumContacts() == max.getNumContacts()){
                if(students[i].getIndex() > max.getIndex()){
                    max = students[i];
                }
            }

        }
        return max;
    }

    @Override
    public String toString() {
        return "{" +
                "\"fakultet\":" + "\"" + name + '\"' +
                ", \"studenti\":" + Arrays.toString(students) +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Faculty faculty = (Faculty) o;
        return Objects.equals(name, faculty.name) && Arrays.equals(students, faculty.students);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(name);
        result = 31 * result + Arrays.hashCode(students);
        return result;
    }
}


public class ContactsTester {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        int tests = scanner.nextInt();
        Faculty faculty = null;

        int rvalue = 0;
        long rindex = -1;

        DecimalFormat df = new DecimalFormat("0.00");

        for (int t = 0; t < tests; t++) {

            rvalue++;
            String operation = scanner.next();

            switch (operation) {
                case "CREATE_FACULTY": {
                    String name = scanner.nextLine().trim();
                    int N = scanner.nextInt();

                    Student[] students = new Student[N];

                    for (int i = 0; i < N; i++) {
                        rvalue++;

                        String firstName = scanner.next();
                        String lastName = scanner.next();
                        String city = scanner.next();
                        int age = scanner.nextInt();
                        long index = scanner.nextLong();

                        if ((rindex == -1) || (rvalue % 13 == 0))
                            rindex = index;

                        Student student = new Student(firstName, lastName, city,
                                age, index);
                        students[i] = student;
                    }

                    faculty = new Faculty(name, students);
                    break;
                }

                case "ADD_EMAIL_CONTACT": {
                    long index = scanner.nextInt();
                    String date = scanner.next();
                    String email = scanner.next();

                    rvalue++;

                    if ((rindex == -1) || (rvalue % 3 == 0))
                        rindex = index;

                    faculty.getStudent(index).addEmailContact(date, email);
                    break;
                }

                case "ADD_PHONE_CONTACT": {
                    long index = scanner.nextInt();
                    String date = scanner.next();
                    String phone = scanner.next();

                    rvalue++;

                    if ((rindex == -1) || (rvalue % 3 == 0))
                        rindex = index;

                    faculty.getStudent(index).addPhoneContact(date, phone);
                    break;
                }

                case "CHECK_SIMPLE": {
                    System.out.println("Average number of contacts: "
                            + df.format(faculty.getAverageNumberOfContacts()));

                    rvalue++;

                    String city = faculty.getStudent(rindex).getCity();
                    System.out.println("Number of students from " + city + ": "
                            + faculty.countStudentsFromCity(city));

                    break;
                }

                case "CHECK_DATES": {

                    rvalue++;

                    System.out.print("Latest contact: ");
                    Contact latestContact = faculty.getStudent(rindex)
                            .getLatestContact();
                    if (latestContact.getType().equals("Email"))
                        System.out.println(((EmailContact) latestContact)
                                .getEmail());
                    if (latestContact.getType().equals("Phone"))
                        System.out.println(((PhoneContact) latestContact)
                                .getPhone()
                                + " ("
                                + ((PhoneContact) latestContact).getOperator()
                                .toString() + ")");

                    if (faculty.getStudent(rindex).getEmailContacts().length > 0
                            && faculty.getStudent(rindex).getPhoneContacts().length > 0) {
                        System.out.print("Number of email and phone contacts: ");
                        System.out
                                .println(faculty.getStudent(rindex)
                                        .getEmailContacts().length
                                        + " "
                                        + faculty.getStudent(rindex)
                                        .getPhoneContacts().length);

                        System.out.print("Comparing dates: ");
                        int posEmail = rvalue
                                % faculty.getStudent(rindex).getEmailContacts().length;
                        int posPhone = rvalue
                                % faculty.getStudent(rindex).getPhoneContacts().length;

                        System.out.println(faculty.getStudent(rindex)
                                .getEmailContacts()[posEmail].isNewerThan(faculty
                                .getStudent(rindex).getPhoneContacts()[posPhone]));
                    }

                    break;
                }

                case "PRINT_FACULTY_METHODS": {
                    System.out.println("Faculty: " + faculty.toString());
                    System.out.println("Student with most contacts: "
                            + faculty.getStudentWithMostContacts().toString());
                    break;
                }

            }

        }

        scanner.close();
    }
}
