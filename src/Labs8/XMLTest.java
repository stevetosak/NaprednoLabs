package Labs8;



import java.util.*;
import java.util.List;

interface XMLComponent {
    void addAttribute(String name, String val);

    String xmlString(int depth);
}

class Attribute {
    private final String name;
    private final String val;

    public Attribute(String name, String val) {
        this.name = name;
        this.val = val;
    }

    @Override
    public String toString() {
        return name + "=\"" + val + "\"";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Attribute attribute = (Attribute) o;
        return Objects.equals(name, attribute.name) && Objects.equals(val, attribute.val);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, val);
    }
}

class XMLLeaf implements XMLComponent {
    private final String tag;
    private final Set<Attribute> attributes = new LinkedHashSet<>();
    private final String value;

    public XMLLeaf(String tag, String value) {
        this.tag = tag;
        this.value = value;
    }

    @Override
    public void addAttribute(String name, String val) {
        attributes.add(new Attribute(name, val));
    }

    @Override
    public String xmlString(int depth) {
        StringBuilder sb = new StringBuilder();

        //open tag
        sb.append("\t".repeat(depth)).append("<").append(tag);
        attributes.forEach(atb -> sb.append(" ").append(atb.toString()));
        sb.append(">");

        //val
        sb.append(value);

        //closed tag
        sb.append("</").append(tag).append(">").append("\n");

        return sb.toString();
    }

    @Override
    public String toString() {
        return xmlString(0);
    }
}

class XMLComposite implements XMLComponent {
    private final String tag;
    private final Set<Attribute> attributes = new LinkedHashSet<>();
    private final List<XMLComponent> components = new ArrayList<>();

    public XMLComposite(String tag) {
        this.tag = tag;
    }

    @Override
    public void addAttribute(String name, String val) {
        attributes.add(new Attribute(name, val));
    }

    public void addComponent(XMLComponent xmlComponent) {
        components.add(xmlComponent);
    }


    @Override
    public String xmlString(int depth) {
        StringBuilder sb = new StringBuilder();
        //open tag
        sb.append("\t".repeat(depth));
        sb.append("<").append(tag);
        sb.append(" ");
        attributes.forEach(atb -> sb.append(atb.toString()));
        sb.append(">").append("\n");

        //val
        components.forEach(component -> sb.append(component.xmlString(depth + 1)));

        //closed tag
        sb.append("\t".repeat(depth)).append("</").append(tag).append(">").append("\n");

        return sb.toString();
    }


    @Override
    public String toString() {
        return xmlString(0);
    }
}

public class XMLTest {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int testCase = sc.nextInt();
        XMLComponent component = new XMLLeaf("student", "Trajce Trajkovski");
        component.addAttribute("type", "redoven");
        component.addAttribute("program", "KNI");

        XMLComposite composite = new XMLComposite("name");
        composite.addComponent(new XMLLeaf("first-name", "trajce"));
        composite.addComponent(new XMLLeaf("last-name", "trajkovski"));
        composite.addAttribute("type", "redoven");
        component.addAttribute("program", "KNI");

        if (testCase == 1) {
            //TODO Print the component object
            System.out.println(component);
        } else if (testCase == 2) {
            //TODO print the composite object
            System.out.println(composite);
        } else if (testCase == 3) {
            XMLComposite main = new XMLComposite("level1");
            main.addAttribute("level", "1");
            XMLComposite lvl2 = new XMLComposite("level2");
            lvl2.addAttribute("level", "2");
            XMLComposite lvl3 = new XMLComposite("level3");
            lvl3.addAttribute("level", "3");
            lvl3.addComponent(component);
            lvl2.addComponent(lvl3);
            lvl2.addComponent(composite);
            lvl2.addComponent(new XMLLeaf("something", "blabla"));
            main.addComponent(lvl2);
            main.addComponent(new XMLLeaf("course", "napredno programiranje"));

            System.out.println(main);
        }
    }
}

