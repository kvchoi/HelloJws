import bean.Kevin;
import bean.Person;

public class TestCast {

    public static void main(String[] args) {
        Person p = new Person("kevin");
        // Kevin k = Kevin.class.cast(p);
        Kevin k = (Kevin) p;
        System.out.println(k.getName());
    }
}
