import java.util.ArrayList;

public class Flight {

    private String name;
    private int admittion;
    private ACC controlCenter;
    private String landing;
    private String departing;
    private ArrayList<Integer> operations;
    private boolean age;
    private int count;
    private int moment;
    private int runtime;

    public Flight(String name, int admittion, ACC controlCenter, String landing, String departing,
            ArrayList<Integer> operations, boolean age, int count, int moment, int runtime) {
        this.name = name;
        this.admittion = admittion;
        this.controlCenter = controlCenter;
        this.landing = landing;
        this.departing = departing;
        this.operations = operations;
        this.age = age;
        this.count = count;
        this.moment = moment;
        this.runtime = runtime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAdmittion() {
        return admittion;
    }

    public void setAdmittion(int admittion) {
        this.admittion = admittion;
    }

    public ACC getControlCenter() {
        return controlCenter;
    }

    public void setControlCenter(ACC controlCenter) {
        this.controlCenter = controlCenter;
    }

    public String getLanding() {
        return landing;
    }

    public void setLanding(String landing) {
        this.landing = landing;
    }

    public String getDeparting() {
        return departing;
    }

    public void setDeparting(String departing) {
        this.departing = departing;
    }

    public ArrayList<Integer> getOperations() {
        return operations;
    }

    public void setOperations(ArrayList<Integer> time) {
        this.operations = time;
    }

    public boolean getAge() {
        return age;
    }

    public void setAge(boolean age) {
        this.age = age;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getMoment() {
        return moment;
    }

    public void setMoment(int moment) {
        this.moment = moment;
    }

    public int getRuntime() {
        return runtime;
    }

    public void setRuntime(int runtime) {
        this.runtime = runtime;
    }

    @Override
    public String toString() {
        return "Flight [name=" + name + ", admittion=" + admittion + ", controlCenter=" + controlCenter + ", landing="
                + landing + ", departing=" + departing + ", operations=" + operations + ", age=" + age + ", count="
                + count + ", moment=" + moment + ", runtime=" + runtime + "]";
    }

    public void run(int duration) {
        age = false;
        operations.set(0, operations.get(0) - duration);
    }

    public boolean isFinished() {
        if (operations.get(0) <= 0) {
            return true;
        } else {
            return false;
        }
    }

}
